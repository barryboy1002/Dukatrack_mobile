package com.example.dukatrack.event

import com.example.dukatrack.state.ChartTab
import com.example.dukatrack.state.period

sealed interface DashboardEvent {
    data class SetPeriod(val period: period) : DashboardEvent
    data class SetSelectedTab(val tab: ChartTab) : DashboardEvent
    object ToggleMenu : DashboardEvent

}