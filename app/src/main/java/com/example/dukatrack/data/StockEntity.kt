package com.example.dukatrack.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "stock",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["productId"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("productId")]
)
data class StockEntity(
    @PrimaryKey(autoGenerate = true) val stockId: Long = 0,
    val productId: Long,
    val branchId: Long,
    val quantity: Int,
    val lowStockThreshold: Int,
)
