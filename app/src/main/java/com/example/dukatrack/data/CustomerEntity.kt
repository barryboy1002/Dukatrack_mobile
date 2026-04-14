package com.example.dukatrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true) val customerId: Long = 0,
    val name: String,
    val phoneNumber: String? = null,
    val email: String? = null,
    val address: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)