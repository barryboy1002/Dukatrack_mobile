package com.example.dukatrack.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("categoryId")]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val productId: Long = 0,
    val categoryId: Long,
    val name: String,
    val description: String?,
    val sellingPrice: Double?,
    val buyingPrice: Double?,
    val imageUrl: String?,
    val units: String,
    val brand: String?,
    val additionalInfo: String?,
    val createdAt: Long,
)
