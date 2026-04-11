package com.example.dukatrack.ui.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dukatrack.data.SalesDao

class SaleViewModelFactory(private val salesDao: SalesDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SaleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SaleViewModel(salesDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}