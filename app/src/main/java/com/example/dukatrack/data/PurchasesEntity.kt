package com.example.dukatrack.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "purchases",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["productId"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SupplierEntity::class,
            parentColumns = ["id"],
            childColumns = ["supplierId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("productId"), Index("supplierId")]
)
data class PurchasesEntity(
    @PrimaryKey(autoGenerate = true) val purchaseId: Long = 0,
    val productId: Long,
    val supplierId: Long,
    val branchId: Long,
    val quantityReceived: Int,
    val totalAmount: Double,
    val paymentMethod: String?,
    val purchaseDate: Long,
    val dateReceived: Long,
    val expiryDate: Long
)
