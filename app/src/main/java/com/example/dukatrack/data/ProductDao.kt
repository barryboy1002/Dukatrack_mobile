package com.example.dukatrack.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE categoryId = :categoryId ORDER BY name ASC")
    fun getProductsByCategory(categoryId: Long): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity): Long

    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM stock WHERE productId = :productId LIMIT 1")
    suspend fun getStockByProductId(productId: Long): StockEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: StockEntity)

    @Update
    suspend fun updateStock(stock: StockEntity)

    @Transaction
    suspend fun insertProductWithStock(product: ProductEntity, quantity: Int, lowStockThreshold: Int) {
        val productId = insertProduct(product)
        insertStock(StockEntity(productId = productId, branchId = 1, quantity = quantity, lowStockThreshold = lowStockThreshold))
    }

    @Query("""
        SELECT p.*, s.quantity as stockQuantity, s.lowStockThreshold as lowStockThreshold, c.name as categoryName 
        FROM products p 
        LEFT JOIN stock s ON p.productId = s.productId 
        LEFT JOIN categories c ON p.categoryId = c.id
        WHERE p.name LIKE '%' || :query || '%'
        ORDER BY p.name ASC
    """)
    fun searchProductsWithStock(query: String): Flow<List<ProductWithStock>>

    data class ProductWithStock(
        val productId: Long,
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
        val stockQuantity: Int?,
        val lowStockThreshold: Int?,
        val categoryName: String?
    )
}
