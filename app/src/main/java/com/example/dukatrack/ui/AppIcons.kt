package com.example.dukatrack.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*

/**
 * Centralized Icons for the app to facilitate moving away from material-icons-extended.
 * Core icons are used directly from the Material Icons Core library.
 * Extended icons that are required are manually defined or mapped here.
 */
object AppIcons {
    // Core Icons (already in material-icons-core)
    val Menu = Icons.Default.Menu
    val Person = Icons.Default.Person
    val Notifications = Icons.Outlined.Notifications
    val Search = Icons.Default.Search
    val Add = Icons.Default.Add
    val Close = Icons.Default.Close
    val ArrowDropDown = Icons.Default.ArrowDropDown
    val ShoppingCart = Icons.Default.ShoppingCart
    val KeyboardArrowDown = Icons.Default.KeyboardArrowDown
    val KeyboardArrowUp = Icons.Default.KeyboardArrowUp
    val Settings = Icons.Outlined.Settings
    val Lock = Icons.Default.Lock
    val Warning = Icons.Default.Warning
    val CheckCircle = Icons.Default.CheckCircle

    // Mapping Extended icons to Core icons for build compatibility without the extended library.
    // Replace these with manual ImageVector paths or SVG resources for accurate visuals.
    val Visibility = Icons.Default.Info
    val FilterList = Icons.AutoMirrored.Filled.List
    val CameraAlt = Icons.Default.Add
    val PhoneAndroid = Icons.Default.Person
    val ChevronRight = Icons.AutoMirrored.Filled.KeyboardArrowRight
    val ErrorOutline = Icons.Default.Warning
    val CalendarToday = Icons.Default.Create
    val Logout = Icons.AutoMirrored.Filled.ExitToApp
    val Storefront = Icons.Default.Home
    val Inventory = Icons.AutoMirrored.Filled.List
    val Inventory2 = Icons.AutoMirrored.Filled.List
    val AutoGraph = Icons.Default.Star
    val AccountBalanceWallet = Icons.Default.ShoppingCart
    val AddShoppingCart = Icons.Default.ShoppingCart
    val History = Icons.Default.Refresh
    val Layers = Icons.AutoMirrored.Filled.List
    val LocalShipping = Icons.Default.Place
    val Assessment = Icons.Default.Create
    val GridView = Icons.Default.Menu
    val QrCodeScanner = Icons.Default.Search
    val CurrencyExchange = Icons.Default.ShoppingCart
    val MoreVert = Icons.Default.MoreVert
}
