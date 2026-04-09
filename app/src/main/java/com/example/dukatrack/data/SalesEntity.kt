package com.example.dukatrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sales")
data class SalesEntity(
    @PrimaryKey(autoGenerate = true) val saleId: Long = 0,
    val businessId: Long,
    val branchId: Long,
    val userId: Long,
    val customerName: String,
    val totalAmount : Double,
    val paymentMethod: String?,
    val saleDate: Long,
    )