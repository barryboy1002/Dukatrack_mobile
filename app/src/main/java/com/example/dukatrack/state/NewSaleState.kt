package com.example.dukatrack.state

import com.example.dukatrack.data.CustomerEntity
import com.example.dukatrack.data.ProductDao
import com.example.dukatrack.data.SaleItemEntity
import com.example.dukatrack.data.SalesEntity

data class NewSaleState (
    val pSearchQuery : String = "",
    val products: List<ProductDao.ProductWithStock> = emptyList(),
    val cart: List<CartItem> = emptyList(),
    val customerName: String = "Walk-in customer",
    val discount: Int = 0,
    val paymentMethod: String = "Cash",
    val lastCompletedSale: Pair<SalesEntity, List<SaleItemEntity>>? = null,
    val customers: List<CustomerEntity> = emptyList(),
    val showCustomerSearch: Boolean = false
)
data class CartItem(val id: Long, val name: String, val price: Double, val quantity: Int,val total: Double)

