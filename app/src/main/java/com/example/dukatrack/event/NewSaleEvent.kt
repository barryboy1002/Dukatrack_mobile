package com.example.dukatrack.event

import com.example.dukatrack.data.ProductDao

sealed interface NewSaleEvent {
    data class SearchQueryChanged(val query: String) : NewSaleEvent
    data class AddToCart(val product: ProductDao.ProductWithStock) : NewSaleEvent
    data class RemoveFromCart(val productId: Long) : NewSaleEvent
    data class UpdateCartQuantity(val productId: Long, val quantity: Int) : NewSaleEvent
    data class CustomerNameChanged(val name: String) : NewSaleEvent
    data class DiscountChanged(val discount: Int) : NewSaleEvent
    data class PaymentMethodChanged(val method: String) : NewSaleEvent
    object Checkout : NewSaleEvent
    object ClearCart : NewSaleEvent
    object DismissReceipt : NewSaleEvent
    data class SetCustomerSearchVisibility(val visible: Boolean) : NewSaleEvent
}
