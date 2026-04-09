package com.example.dukatrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suppliers")
data class SupplierEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val contactPerson: String,
    val phoneNumber: String,
    val email: String,
    val paymentTerms: String,
    val address: String,
    val createdAt: Long
)
