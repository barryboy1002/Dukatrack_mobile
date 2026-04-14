package com.example.dukatrack.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Centralized Icons for the app to facilitate moving away from material-icons-extended.
 * Missing icons are manually defined to keep APK size small.
 */
object AppIcons {
    // Core Icons
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
    val MoreVert = Icons.Default.MoreVert
    val Star = Icons.Default.Star
    val AddCircleOutline = Icons.Default.AddCircle
    
    val RemoveCircleOutline = materialIcon("RemoveCircleOutline") {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12.0f, 2.0f)
            curveTo(6.48f, 2.0f, 2.0f, 6.48f, 2.0f, 12.0f)
            reflectiveCurveTo(6.48f, 22.0f, 12.0f, 22.0f)
            reflectiveCurveTo(22.0f, 17.52f, 22.0f, 12.0f)
            reflectiveCurveTo(17.52f, 2.0f, 12.0f, 2.0f)
            close()
            moveTo(17.0f, 13.0f)
            horizontalLineTo(7.0f)
            verticalLineTo(11.0f)
            horizontalLineTo(17.0f)
            verticalLineTo(13.0f)
            close()
        }
    }
    
    val Remove = materialIcon("Remove") {
        path(fill = SolidColor(Color.Black)) {
            moveTo(19.0f, 13.0f)
            horizontalLineTo(5.0f)
            verticalLineTo(11.0f)
            horizontalLineTo(19.0f)
            verticalLineTo(13.0f)
            close()
        }
    }

    // Custom Defined Icons (Professional replacements for Extended Library)
    val Storefront = materialIcon("Storefront") {
        path(fill = SolidColor(Color.Black)) {
            moveTo(21.9f, 9.0f)
            lineToRelative(-1.1f, -4.3f)
            curveTo(20.6f, 3.9f, 19.8f, 3.0f, 18.8f, 3.0f)
            horizontalLineTo(5.2f)
            curveTo(4.2f, 3.0f, 3.4f, 3.9f, 3.2f, 4.7f)
            lineTo(2.1f, 9.0f)
            curveTo(2.0f, 9.3f, 2.0f, 9.7f, 2.0f, 10.0f)
            curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
            verticalLineToRelative(7.0f)
            curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
            horizontalLineToRelative(12.0f)
            curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
            verticalLineToRelative(-7.0f)
            curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
            curveTo(22.0f, 9.7f, 22.0f, 9.3f, 21.9f, 9.0f)
            close()
            moveTo(18.0f, 19.0f)
            horizontalLineTo(6.0f)
            verticalLineToRelative(-7.0f)
            horizontalLineToRelative(12.0f)
            verticalLineTo(19.0f)
            close()
            moveTo(20.0f, 10.0f)
            curveToRelative(0.0f, 0.55f, -0.45f, 1.0f, -1.0f, 1.0f)
            reflectiveCurveToRelative(-1.0f, -0.45f, -1.0f, -1.0f)
            verticalLineTo(9.0f)
            horizontalLineTo(6.0f)
            verticalLineToRelative(1.0f)
            curveToRelative(0.0f, 0.55f, -0.45f, 1.0f, -1.0f, 1.0f)
            reflectiveCurveToRelative(-1.0f, -0.45f, -1.0f, -1.0f)
            verticalLineTo(9.0f)
            lineTo(5.2f, 4.0f)
            horizontalLineToRelative(13.6f)
            lineTo(20.0f, 9.0f)
            verticalLineTo(10.0f)
            close()
        }
    }

    val Inventory = materialIcon("Inventory") {
        path(fill = SolidColor(Color.Black)) {
            moveTo(20.0f, 2.0f)
            horizontalLineTo(4.0f)
            curveTo(2.9f, 2.0f, 2.0f, 2.9f, 2.0f, 4.0f)
            verticalLineToRelative(16.0f)
            curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
            horizontalLineToRelative(16.0f)
            curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
            verticalLineTo(4.0f)
            curveTo(22.0f, 2.9f, 21.1f, 2.0f, 20.0f, 2.0f)
            close()
            moveTo(20.0f, 20.0f)
            horizontalLineTo(4.0f)
            verticalLineToRelative(-10.0f)
            horizontalLineToRelative(16.0f)
            verticalLineTo(20.0f)
            close()
            moveTo(20.0f, 8.0f)
            horizontalLineTo(4.0f)
            verticalLineTo(4.0f)
            horizontalLineToRelative(16.0f)
            verticalLineTo(8.0f)
            close()
            moveTo(15.0f, 12.0f)
            horizontalLineTo(9.0f)
            verticalLineToRelative(2.0f)
            horizontalLineToRelative(6.0f)
            verticalLineTo(12.0f)
            close()
        }
    }

    val History = materialIcon("History") {
        path(fill = SolidColor(Color.Black)) {
            moveTo(13.0f, 3.0f)
            curveToRelative(-4.97f, 0.0f, -9.0f, 4.03f, -9.0f, 9.0f)
            horizontalLineTo(1.0f)
            lineToRelative(3.89f, 3.89f)
            lineToRelative(0.07f, 0.14f)
            lineTo(9.0f, 12.0f)
            horizontalLineTo(6.0f)
            curveToRelative(0.0f, -3.87f, 3.13f, -7.0f, 7.0f, -7.0f)
            reflectiveCurveToRelative(7.0f, 3.13f, 7.0f, 7.0f)
            reflectiveCurveToRelative(-3.13f, 7.0f, -7.0f, 7.0f)
            curveToRelative(-1.93f, 0.0f, -3.68f, -0.79f, -4.94f, -2.06f)
            lineToRelative(-1.42f, 1.42f)
            curveTo(8.27f, 19.99f, 10.51f, 21.0f, 13.0f, 21.0f)
            curveToRelative(4.97f, 0.0f, 9.0f, -4.03f, 9.0f, -9.0f)
            reflectiveCurveToRelative(-4.03f, -9.0f, -9.0f, -9.0f)
            close()
            moveTo(12.0f, 8.0f)
            verticalLineToRelative(5.0f)
            lineToRelative(4.28f, 2.54f)
            lineToRelative(0.72f, -1.21f)
            lineToRelative(-3.5f, -2.08f)
            verticalLineTo(8.0f)
            horizontalLineTo(12.0f)
            close()
        }
    }

    val Assessment = materialIcon("Assessment") {
        path(fill = SolidColor(Color.Black)) {
            moveTo(19.0f, 3.0f)
            horizontalLineTo(5.0f)
            curveToRelative(-1.1f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
            verticalLineToRelative(14.0f)
            curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
            horizontalLineToRelative(14.0f)
            curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
            verticalLineTo(5.0f)
            curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
            close()
            moveTo(9.0f, 17.0f)
            horizontalLineTo(7.0f)
            verticalLineTo(-7.0f)
            horizontalLineToRelative(2.0f)
            verticalLineTo(17.0f)
            close()
            moveTo(13.0f, 17.0f)
            horizontalLineToRelative(-2.0f)
            verticalLineTo(7.0f)
            horizontalLineToRelative(2.0f)
            verticalLineTo(17.0f)
            close()
            moveTo(17.0f, 17.0f)
            horizontalLineToRelative(-2.0f)
            verticalLineToRelative(-4.0f)
            horizontalLineToRelative(2.0f)
            verticalLineTo(17.0f)
            close()
        }
    }

    // Mapping remaining common ones to Core
    val Visibility = Icons.Default.Info
    val FilterList = Icons.AutoMirrored.Filled.List
    val CameraAlt = Icons.Default.Add
    val PhoneAndroid = Icons.Default.Person
    val ChevronRight = Icons.AutoMirrored.Filled.KeyboardArrowRight
    val ErrorOutline = Icons.Default.Warning
    val CalendarToday = Icons.Default.DateRange
    val Logout = Icons.AutoMirrored.Filled.ExitToApp
    val AutoGraph = Star
    val AccountBalanceWallet = ShoppingCart
    val AddShoppingCart = ShoppingCart
    val Layers = Icons.AutoMirrored.Filled.List
    val LocalShipping = Icons.Default.Place
    val GridView = Icons.Default.Menu
    val QrCodeScanner = Icons.Default.Search
    val CurrencyExchange = ShoppingCart

    private inline fun materialIcon(
        name: String,
        block: ImageVector.Builder.() -> ImageVector.Builder
    ): ImageVector = ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).block().build()
}
