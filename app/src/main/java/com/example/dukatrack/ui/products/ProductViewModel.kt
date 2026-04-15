package com.example.dukatrack.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dukatrack.data.CategoryEntity
import com.example.dukatrack.data.ProductDao
import com.example.dukatrack.data.ProductEntity
import com.example.dukatrack.event.ProductEvent
import com.example.dukatrack.state.ProductState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProductViewModel(private val productDao: ProductDao) : ViewModel() {

    init {
        ensureDefaultCategory()
    }

    private fun ensureDefaultCategory() {
        viewModelScope.launch {
            val categories = productDao.getAllCategories().first()
            if (categories.isEmpty()) {
                productDao.insertCategory(CategoryEntity(name = "General"))
            }
        }
    }

    private val _searchQuery = MutableStateFlow("")
    
    private val _categories = productDao.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _products = _searchQuery
        .flatMapLatest { query ->
            productDao.searchProductsWithStock(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val state: StateFlow<ProductState> = combine(
        _searchQuery,
        _products,
        _categories
    ) { query, products, categories ->
        ProductState(
            searchQuery = query,
            products = products,
            categories = categories
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProductState())

    fun onEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.SearchQueryChanged -> {
                _searchQuery.value = event.query
            }
            is ProductEvent.AddProduct -> {
                addProduct(event)
            }
            is ProductEvent.UpdateProduct -> {
                updateProduct(event)
            }
            is ProductEvent.AddCategory -> {
                addCategory(event.name)
            }
            is ProductEvent.FilterCategory ->{
                filterCategory(event.categoryId)

            }
        }
    }

    private fun addProduct(event: ProductEvent.AddProduct) {
        viewModelScope.launch {
            val product = ProductEntity(
                name = event.name,
                categoryId = event.categoryId,
                buyingPrice = event.buyingPrice,
                sellingPrice = event.sellingPrice,
                units = event.unit,
                brand = event.brand,
                description = event.additionalInfo,
                additionalInfo = event.additionalInfo,
                imageUrl = null,
                createdAt = System.currentTimeMillis()
            )
            productDao.insertProductWithStock(product, event.initialStock, event.lowStockAlert)
        }
    }

    private fun updateProduct(event: ProductEvent.UpdateProduct) {
        viewModelScope.launch {
            val product = ProductEntity(
                productId = event.productId,
                name = event.name,
                categoryId = event.categoryId,
                buyingPrice = event.buyingPrice,
                sellingPrice = event.sellingPrice,
                units = event.unit,
                brand = event.brand,
                description = event.additionalInfo,
                additionalInfo = event.additionalInfo,
                imageUrl = null,
                createdAt = System.currentTimeMillis() // Or keep original
            )
            productDao.updateProduct(product)
            
            val stock = productDao.getStockByProductId(event.productId)
            if (stock != null) {
                productDao.updateStock(stock.copy(lowStockThreshold = event.lowStockAlert))
            }
        }
    }

    private fun addCategory(name: String) {
        viewModelScope.launch {
            productDao.insertCategory(CategoryEntity(name = name))
        }
    }
    private fun filterCategory(categoryId: Long) {
        viewModelScope.launch {
            productDao.getProductsByCategory(categoryId)
        }
    }

}
