package com.example.dukatrack.ui.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dukatrack.data.CustomerDao
import com.example.dukatrack.data.ProductDao
import com.example.dukatrack.data.SalesDao
import com.example.dukatrack.data.SalesEntity
import com.example.dukatrack.data.SaleItemEntity
import com.example.dukatrack.event.NewSaleEvent
import com.example.dukatrack.state.CartItem
import com.example.dukatrack.state.NewSaleState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NewSaleViewModel(
    private val productDao: ProductDao,
    private val salesDao: SalesDao,
    private val customerDao: CustomerDao
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    private val _customerName = MutableStateFlow("Walk-in customer")
    private val _discount = MutableStateFlow(0)
    private val _paymentMethod = MutableStateFlow("Cash")
    private val _lastCompletedSale = MutableStateFlow<Pair<SalesEntity, List<SaleItemEntity>>?>(null)
    private val _showCustomerSearch = MutableStateFlow(false)

    private val _products = _searchQuery.flatMapLatest { query ->
        productDao.searchProductsWithStock(query)
    }

    private val _customers = _customerName.flatMapLatest { query ->
        if (query.length >= 2) {
            customerDao.searchCustomers(query)
        } else {
            customerDao.getAllCustomers()
        }
    }

    val state: StateFlow<NewSaleState> = combine(
        _searchQuery,
        _products,
        _cart,
        _customerName,
        _discount,
        _paymentMethod,
        _lastCompletedSale,
        _customers,
        _showCustomerSearch
    ) { args: Array<Any?> ->
        NewSaleState(
            pSearchQuery = args[0] as String,
            products = args[1] as List<ProductDao.ProductWithStock>,
            cart = args[2] as List<CartItem>,
            customerName = args[3] as String,
            discount = args[4] as Int,
            paymentMethod = args[5] as String,
            lastCompletedSale = args[6] as Pair<SalesEntity, List<SaleItemEntity>>?,
            customers = args[7] as List<com.example.dukatrack.data.CustomerEntity>,
            showCustomerSearch = args[8] as Boolean
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NewSaleState())

    fun onEvent(event: NewSaleEvent) {
        when (event) {
            is NewSaleEvent.SearchQueryChanged -> {
                _searchQuery.value = event.query
            }
            is NewSaleEvent.AddToCart -> {
                val currentCart = _cart.value.toMutableList()
                val existingItem = currentCart.find { it.id == event.product.productId }
                if (existingItem != null) {
                    val index = currentCart.indexOf(existingItem)
                    val updatedItem = existingItem.copy(
                        quantity = existingItem.quantity + 1,
                        total = (existingItem.quantity + 1) * (event.product.sellingPrice ?: 0.0)
                    )
                    currentCart[index] = updatedItem
                } else {
                    currentCart.add(
                        CartItem(
                            id = event.product.productId,
                            name = event.product.name,
                            price = event.product.sellingPrice ?: 0.0,
                            quantity = 1,
                            total = event.product.sellingPrice ?: 0.0
                        )
                    )
                }
                _cart.value = currentCart
            }
            is NewSaleEvent.RemoveFromCart -> {
                _cart.value = _cart.value.filter { it.id != event.productId }
            }
            is NewSaleEvent.UpdateCartQuantity -> {
                val currentCart = _cart.value.toMutableList()
                val index = currentCart.indexOfFirst { it.id == event.productId }
                if (index != -1) {
                    if (event.quantity > 0) {
                        val item = currentCart[index]
                        currentCart[index] = item.copy(
                            quantity = event.quantity,
                            total = event.quantity * item.price
                        )
                    } else {
                        currentCart.removeAt(index)
                    }
                    _cart.value = currentCart
                }
            }
            is NewSaleEvent.CustomerNameChanged -> {
                _customerName.value = event.name
                _showCustomerSearch.value = event.name.isNotEmpty() && event.name != "Walk-in customer"
            }
            is NewSaleEvent.SetCustomerSearchVisibility -> {
                _showCustomerSearch.value = event.visible
            }
            is NewSaleEvent.DiscountChanged -> {
                _discount.value = event.discount
            }
            is NewSaleEvent.PaymentMethodChanged -> {
                _paymentMethod.value = event.method
            }
            NewSaleEvent.ClearCart -> {
                _cart.value = emptyList()
                _customerName.value = "Walk-in customer"
                _discount.value = 0
                _paymentMethod.value = "Cash"
            }
            NewSaleEvent.DismissReceipt -> {
                _lastCompletedSale.value = null
            }
            NewSaleEvent.Checkout -> {
                viewModelScope.launch {
                    val currentState = state.value
                    val cartItems = currentState.cart
                    if (cartItems.isEmpty()) return@launch

                    val subtotal = cartItems.sumOf { it.total }
                    val totalAmount = subtotal * (1 - currentState.discount / 100.0)
                    
                    val sale = SalesEntity(
                        businessId = 1, // Default IDs for now
                        branchId = 1,
                        userId = 1,
                        customerName = currentState.customerName,
                        totalAmount = totalAmount,
                        paymentMethod = currentState.paymentMethod,
                        saleDate = System.currentTimeMillis()
                    )

                    val saleItems = cartItems.map {
                        SaleItemEntity(
                            saleId = 0, // Will be set by DAO
                            productId = it.id,
                            quantity = it.quantity,
                            unitPrice = it.price,
                            totalAmount = it.total
                        )
                    }

                    val saleId = salesDao.processSale(sale, saleItems)
                    _lastCompletedSale.value = sale.copy(saleId = saleId) to saleItems

                    _cart.value = emptyList()
                    _customerName.value = "Walk-in customer"
                    _discount.value = 0
                    _paymentMethod.value = "Cash"
                    _searchQuery.value = ""
                }
            }
        }
    }
}
