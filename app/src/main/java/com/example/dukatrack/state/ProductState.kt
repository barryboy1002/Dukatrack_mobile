package com.example.dukatrack.state

import com.example.dukatrack.data.CategoryEntity
import com.example.dukatrack.data.ProductDao

data class ProductState(
    val searchQuery: String = "",
    val products: List<ProductDao.ProductWithStock> = emptyList(),
    val categories: List<CategoryEntity> = emptyList()
)
