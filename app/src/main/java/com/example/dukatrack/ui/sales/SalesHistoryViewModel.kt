package com.example.dukatrack.ui.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dukatrack.data.SalesDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class SalesHistoryState(
    val searchQuery: String = "",
    val selectedPayment: String = "All Payments",
    val sales: List<SalesDao.SaleWithItemCount> = emptyList()
)

class SalesHistoryViewModel(private val salesDao: SalesDao) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _selectedPayment = MutableStateFlow("All Payments")

    val state: StateFlow<SalesHistoryState> = combine(
        salesDao.getAllSalesWithItemCount(),
        _searchQuery,
        _selectedPayment
    ) { sales, query, payment ->
        val filteredSales = sales.filter { item ->
            val matchesQuery = item.sale.customerName.contains(query, ignoreCase = true) ||
                    "RCP-${item.sale.saleId}".contains(query, ignoreCase = true)
            val matchesPayment = payment == "All Payments" || item.sale.paymentMethod == payment
            matchesQuery && matchesPayment
        }
        SalesHistoryState(
            searchQuery = query,
            selectedPayment = payment,
            sales = filteredSales
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SalesHistoryState()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onPaymentMethodChange(payment: String) {
        _selectedPayment.value = payment
    }
}