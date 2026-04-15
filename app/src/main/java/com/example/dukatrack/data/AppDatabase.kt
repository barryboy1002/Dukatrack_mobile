package com.example.dukatrack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [
        CategoryEntity::class,
        ProductEntity::class,
        SupplierEntity::class,
        StockEntity::class,
        PurchasesEntity::class,
        SalesEntity::class,
        SaleItemEntity::class,
        CustomerEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    // We will add DAO getters here once we create them
    abstract fun productDao(): ProductDao
    abstract fun salesDao(): SalesDao
    abstract fun customerDao(): CustomerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dukatrack_database"
                )
                .fallbackToDestructiveMigration() // Use this during development to reset DB on schema changes
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
