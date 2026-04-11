package com.example.dukatrack.event

sealed interface DashboardEvent {
    data class SetPeriod(val period: String) : DashboardEvent
    data class SetSelectedTab(val tab: String) : DashboardEvent
    object ToggleMenu : DashboardEvent
}