package com.example.dukatrack.data

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SalesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: StockEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: SalesEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaleItems(saleItems: List<SaleItemEntity>)

    @Query("UPDATE stock SET quantity = quantity - :quantity WHERE productId = :productId")
    suspend fun decreaseStock(productId: Long, quantity: Int)

    @Transaction
    suspend fun processSale(sale: SalesEntity, items: List<SaleItemEntity>): Long {
        val saleId = insertSale(sale)
        val itemsWithSaleId = items.map { it.copy(saleId = saleId) }
        insertSaleItems(itemsWithSaleId)
        items.forEach { item ->
            decreaseStock(item.productId, item.quantity)
        }
        return saleId
    }

    @Query(
    "SELECT p.productId, p.name, sum(si.totalAmount) as totalAmount " +
    "FROM products p " +
    "JOIN sale_items si ON p.productId = si.productId " +
    "GROUP BY p.productId")
    fun getProductSales(): Flow<List<ProductSales>>

    data class ProductSales(
        val productId: Long,
        val name: String,
        val totalAmount: Double
    )
    @Query("SELECT (saleDate / 86400000) * 86400000 as saleDate, SUM(totalAmount) as totalSales FROM sales GROUP BY (saleDate / 86400000) ORDER BY saleDate ASC")
    fun getDailySales(): Flow<List<DailySales>>

    @Query("SELECT COUNT(*) FROM products")
    fun getTotalProductsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM stock WHERE quantity <= lowStockThreshold")
    fun getLowStockCount(): Flow<Int>

    @Query("SELECT SUM(totalAmount) FROM sales WHERE saleDate >= :startOfDay")
    fun getTodaySales(startOfDay: Long): Flow<Double?>

    @Query("SELECT SUM(totalAmount) FROM sales WHERE saleDate >= :startOfMonth")
    fun getMonthlySales(startOfMonth: Long): Flow<Double?>

    @Query("SELECT branchId, SUM(totalAmount) as totalSales FROM sales GROUP BY branchId")
    fun getBranchSales(): Flow<List<BranchSales>>

    data class BranchSales(
        val branchId: Long,
        val totalSales: Double
    )

    data class DailySales(
        val saleDate: Long,
        val totalSales: Double
    )

    @Query("""
        SELECT s.*, (SELECT COUNT(*) FROM sale_items si WHERE si.saleId = s.saleId) as itemsCount 
        FROM sales s 
        ORDER BY s.saleDate DESC
    """)
    fun getAllSalesWithItemCount(): Flow<List<SaleWithItemCount>>

    data class SaleWithItemCount(
        @Embedded val sale: SalesEntity,
        val itemsCount: Int
    )
}