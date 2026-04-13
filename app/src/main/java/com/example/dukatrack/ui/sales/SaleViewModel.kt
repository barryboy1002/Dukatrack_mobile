package com.example.dukatrack.ui.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dukatrack.data.CategoryEntity
import com.example.dukatrack.data.ProductEntity
import com.example.dukatrack.data.SaleItemEntity
import com.example.dukatrack.data.SalesDao
import com.example.dukatrack.data.SalesEntity
import com.example.dukatrack.data.StockEntity
import com.example.dukatrack.event.DashboardEvent
import com.example.dukatrack.state.ChartTab
import com.example.dukatrack.state.DashboardState
import com.example.dukatrack.state.period
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class SaleViewModel(private val salesDao: SalesDao): ViewModel() {
     private val _state= MutableStateFlow(DashboardState())
     private val  _period = MutableStateFlow(period.Weekly)
     private val  _chartTab = MutableStateFlow(ChartTab.sales)

     val productSales = salesDao.getProductSales()
          .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
     val dailySales = salesDao.getDailySales()
          .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
     val totalProducts = salesDao.getTotalProductsCount()
          .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)
     val lowStock = salesDao.getLowStockCount()
          .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

     private val startOfDay: Long
          get() {
               val calendar = Calendar.getInstance()
               calendar.set(Calendar.HOUR_OF_DAY, 0)
               calendar.set(Calendar.MINUTE, 0)
               calendar.set(Calendar.SECOND, 0)
               calendar.set(Calendar.MILLISECOND, 0)
               return calendar.timeInMillis
          }

     private val startOfMonth: Long
          get() {
               val calendar = Calendar.getInstance()
               calendar.set(Calendar.DAY_OF_MONTH, 1)
               calendar.set(Calendar.HOUR_OF_DAY, 0)
               calendar.set(Calendar.MINUTE, 0)
               calendar.set(Calendar.SECOND, 0)
               calendar.set(Calendar.MILLISECOND, 0)
               return calendar.timeInMillis
          }

     val todaySales = salesDao.getTodaySales(startOfDay)
          .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)
     val monthlySales = salesDao.getMonthlySales(startOfMonth)
          .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

     val state = combine(
          _state, _period, _chartTab, productSales, dailySales,
          totalProducts, lowStock, todaySales, monthlySales
     ) { flows ->
          val currentState = flows[0] as DashboardState
          val period = flows[1] as period
          val chartTab = flows[2] as ChartTab
          val productSales = flows[3] as List<SalesDao.ProductSales>
          val dailySales = flows[4] as List<SalesDao.DailySales>
          val totalProductsCount = flows[5] as Int
          val lowStockCount = flows[6] as Int
          val todaySalesAmount = flows[7] as Double? ?: 0.0
          val monthlySalesAmount = flows[8] as Double? ?: 0.0

          currentState.copy(
               period = period,
               chartTab = chartTab,
               productSales = productSales,
               dailySales = dailySales,
               totalProducts = totalProductsCount,
               lowStock = lowStockCount,
               todaySales = todaySalesAmount,
               monthlySales = monthlySalesAmount
          )
     }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardState())

     fun onEvent(event: DashboardEvent){
          when(event){
               is DashboardEvent.SetPeriod -> {
                    _state.update {it.copy(showMenu =  false)}
                    _period.value = event.period
               }
               is DashboardEvent.SetSelectedTab -> {
                    _state.update { it.copy(showMenu = false)}
                    _chartTab.value = event.tab

                }

               is DashboardEvent.ToggleMenu -> {
                    _state.update { it.copy(showMenu =!it.showMenu) }
               }
               is DashboardEvent.SeedData -> {
                    seedFakeData()
               }

          }
     }

     private fun seedFakeData() {
          viewModelScope.launch {
               val categoryId = salesDao.insertCategory(CategoryEntity(name = "Electronics"))
               val productId = salesDao.insertProduct(
                    ProductEntity(
                         categoryId = categoryId,
                         name = "Smartphone",
                         description = "Latest model",
                         sellingPrice = 500.0,
                         buyingPrice = 300.0,
                         units = "pcs",
                         brand = "Samsung",
                         imageUrl = null,
                         additionalInfo = null,
                         createdAt = System.currentTimeMillis()
                    )
               )
               salesDao.insertStock(
                    StockEntity(
                         productId = productId,
                         branchId = 1,
                         quantity = 5,
                         lowStockThreshold = 10
                    )
               )

               val now = System.currentTimeMillis()
               val saleId = salesDao.insertSale(
                    SalesEntity(
                         businessId = 1,
                         branchId = 1,
                         userId = 1,
                         customerName = "John Doe",
                         totalAmount = 500.0,
                         paymentMethod = "Cash",
                         saleDate = now
                    )
               )
               salesDao.insertSaleItem(
                    SaleItemEntity(
                         saleId = saleId,
                         productId = productId,
                         quantity = 1,
                         unitPrice = 500.0,
                         totalAmount = 500.0
                    )
               )
          }
     }
}

