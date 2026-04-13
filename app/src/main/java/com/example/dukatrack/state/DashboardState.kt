package com.example.dukatrack.state

import com.example.dukatrack.data.SalesDao


data class DashboardState(
    //State for Summarycards
    val totalProducts: Int = 0,
    val lowStock: Int = 0,
    val todaySales: Double = 0.0,
    val monthlySales: Double = 0.0,

    //for the sales chartcard
    var showMenu: Boolean = false,
    var period: period = com.example.dukatrack.state.period.Weekly,
    val chartTab: ChartTab = ChartTab.sales,
    val productSales: List<SalesDao.ProductSales> = emptyList(),
    val dailySales: List<SalesDao.DailySales> = emptyList()
)
