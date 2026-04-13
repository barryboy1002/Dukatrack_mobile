package com.example.dukatrack.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dukatrack.data.CategoryEntity
import com.example.dukatrack.data.ProductDao
import com.example.dukatrack.data.ProductEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(private val productDao: ProductDao) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val categories: StateFlow<List<CategoryEntity>> = productDao.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val products = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                productDao.getProductsWithStock()
            } else {
                // Simplified search for now, could be improved in DAO
                productDao.getProductsWithStock() 
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun addProduct(
        name: String,
        categoryId: Long,
        buyingPrice: Double,
        sellingPrice: Double,
        unit: String,
        brand: String,
        lowStockAlert: Int,
        initialStock: Int,
        additionalInfo: String
    ) {
        viewModelScope.launch {
            val product = ProductEntity(
                name = name,
                categoryId = categoryId,
                buyingPrice = buyingPrice,
                sellingPrice = sellingPrice,
                units = unit,
                brand = brand,
                description = additionalInfo,
                additionalInfo = additionalInfo,
                imageUrl = null,
                createdAt = System.currentTimeMillis()
            )
            productDao.insertProductWithStock(product, initialStock, lowStockAlert)
        }
    }

    fun updateProduct(
        productId: Long,
        name: String,
        categoryId: Long,
        buyingPrice: Double,
        sellingPrice: Double,
        unit: String,
        brand: String,
        lowStockAlert: Int,
        additionalInfo: String
    ) {
        viewModelScope.launch {
            val product = ProductEntity(
                productId = productId,
                name = name,
                categoryId = categoryId,
                buyingPrice = buyingPrice,
                sellingPrice = sellingPrice,
                units = unit,
                brand = brand,
                description = additionalInfo,
                additionalInfo = additionalInfo,
                imageUrl = null,
                createdAt = System.currentTimeMillis() // Or keep original
            )
            productDao.updateProduct(product)
            
            // Update stock threshold if needed
            val stock = productDao.getStockByProductId(productId)
            if (stock != null) {
                productDao.updateStock(stock.copy(lowStockThreshold = lowStockAlert))
            }
        }
    }

    fun addCategory(name: String) {
        viewModelScope.launch {
            productDao.insertCategory(CategoryEntity(name = name))
        }
    }
}
