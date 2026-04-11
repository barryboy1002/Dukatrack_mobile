package com.example.dukatrack.state

import com.example.dukatrack.data.SalesDao


data class DashboardState(
    //State for Summarycards
    var totalProducts: Int = 0,
    var lowStock: Int = 0,
    var todaySales: Double = 0.0,
    var monthlySales: Double = 0.0,

    //for the sales chartcard
    var showMenu: Boolean = false,
    var period: period = com.example.dukatrack.state.period.Weekly,
    var chartTab: ChartTab = ChartTab.sales,
    val productSales: List<SalesDao.ProductSales> = emptyList(),
    val dailySales: List<SalesDao.DailySales> = emptyList()
)
