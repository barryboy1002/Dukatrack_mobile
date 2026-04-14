package com.example.dukatrack.ui.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dukatrack.data.ProductDao
import com.example.dukatrack.data.SalesDao
import com.example.dukatrack.data.CustomerDao

class NewSaleViewModelFactory(
    private val productDao: ProductDao,
    private val salesDao: SalesDao,
    private val customerDao: CustomerDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewSaleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewSaleViewModel(productDao, salesDao, customerDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
