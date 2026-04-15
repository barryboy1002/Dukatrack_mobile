package com.example.dukatrack.ui.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dukatrack.data.SalesDao

class DashboardViewModelFactory(private val salesDao: SalesDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(salesDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}