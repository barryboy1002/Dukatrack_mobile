package com.example.dukatrack.event

import com.example.dukatrack.data.ProductDao

sealed interface ProductEvent {
    data class SearchQueryChanged(val query: String) : ProductEvent
    data class AddProduct(
        val name: String,
        val categoryId: Long,
        val buyingPrice: Double,
        val sellingPrice: Double,
        val unit: String,
        val brand: String,
        val lowStockAlert: Int,
        val initialStock: Int,
        val additionalInfo: String
    ) : ProductEvent
    data class UpdateProduct(
        val productId: Long,
        val name: String,
        val categoryId: Long,
        val buyingPrice: Double,
        val sellingPrice: Double,
        val unit: String,
        val brand: String,
        val lowStockAlert: Int,
        val additionalInfo: String
    ) : ProductEvent
    data class AddCategory(val name: String) : ProductEvent
}
