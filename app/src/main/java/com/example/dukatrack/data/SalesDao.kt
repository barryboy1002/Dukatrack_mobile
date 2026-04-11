package com.example.dukatrack.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SalesDao {
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
    @Query("SELECT saleDate,SUM(totalAmount) as totalSales FROM sales GROUP BY saleDate")
    fun getDailySales(): Flow<List<DailySales>>

    @Query("SELECT COUNT(*) FROM products")
    fun getTotalProductsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM stock WHERE quantity <= lowStockThreshold")
    fun getLowStockCount(): Flow<Int>

    @Query("SELECT SUM(totalAmount) FROM sales WHERE saleDate >= :startOfDay")
    fun getTodaySales(startOfDay: Long): Flow<Double?>

    @Query("SELECT SUM(totalAmount) FROM sales WHERE saleDate >= :startOfMonth")
    fun getMonthlySales(startOfMonth: Long): Flow<Double?>

    data class DailySales(
        val saleDate: Long,
        val totalSales: Double
    )
}