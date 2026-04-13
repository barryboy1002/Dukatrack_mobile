package com.example.dukatrack.ui


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.dukatrack.event.DashboardEvent
import com.example.dukatrack.state.ChartTab
import com.example.dukatrack.state.DashboardState
import com.example.dukatrack.state.period
import com.example.dukatrack.ui.theme.BorderGray
import com.example.dukatrack.ui.theme.DarkNavy
import com.example.dukatrack.ui.theme.PrimaryGreen
import com.example.dukatrack.ui.theme.RedColor
import com.example.dukatrack.ui.theme.TextDark
import com.example.dukatrack.ui.theme.TextMuted
import com.example.dukatrack.ui.theme.White
import com.example.dukatrack.ui.theme.pchartcolors

@Composable
fun DashboardScreen(navController: NavController,
                    state : DashboardState,
                    onEvent: (DashboardEvent) -> Unit,
                    modifier: Modifier = Modifier) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            SummaryCardsGrid(state)
        }
        item {
            SalesChartCard(state,onEvent)
        }
        item {
            TopProductsCard(state)
        }
    }
}

@Composable
fun SidebarContent(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(DarkNavy)
    ) {
        // Logo Section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = AppIcons.Storefront,
                contentDescription = "Logo",
                tint = PrimaryGreen,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "DukaTrack",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                color = White
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 0.5.dp,
            color = White.copy(alpha = 0.1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nav Items
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .weight(1f)
        ) {
            SidebarItem(
                label = "Dashboard",
                icon = AppIcons.GridView,
                isSelected = currentRoute == screen_names.Dashboard,
                onClick = {
                    if (currentRoute != screen_names.Dashboard) {
                        navController.navigate(screen_names.Dashboard) {
                            popUpTo(screen_names.Dashboard) { inclusive = true }
                        }
                    }
                }
            )
            SidebarItem(
                label = "Products",
                icon = AppIcons.Inventory,
                isSelected = currentRoute == screen_names.Products,
                onClick = {
                    if (currentRoute != screen_names.Products) {
                        navController.navigate(screen_names.Products)
                    }
                }
            )
            SidebarItem(label = "New Sale",
                icon = AppIcons.AddShoppingCart,
                isSelected = currentRoute == screen_names.NewSale,
                onClick = {
                    if (currentRoute != screen_names.NewSale) {
                        navController.navigate(screen_names.NewSale)
                    }
                }
            )
            SidebarItem(
                label = "Sales History",
                icon = AppIcons.History,
                isSelected = currentRoute == screen_names.SalesHistory,
                onClick = {
                    if (currentRoute != screen_names.SalesHistory) {
                        navController.navigate(screen_names.SalesHistory)
                    }
                }
            )
            SidebarItem("Stock", AppIcons.Layers)
            SidebarItem("Suppliers", AppIcons.LocalShipping)
            SidebarItem("Reports", AppIcons.Assessment)
            SidebarItem("Settings", AppIcons.Settings)

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(thickness = 0.5.dp, color = White.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(8.dp))

            SidebarItem(
                label = "Logout",
                icon = AppIcons.Logout,
                textColor = RedColor,
                iconColor = RedColor
            )
        }

        // Bottom section
        Column(
            modifier = Modifier
                .padding(24.dp)
        ) {
            // Free Plan Badge
            Surface(
                color = PrimaryGreen.copy(alpha = 0.2f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    "FREE PLAN",
                    color = PrimaryGreen,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // User Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = White.copy(alpha = 0.2f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "WK",
                            color = White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Wanjiku Kamau",
                    color = White,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

@Composable
fun SidebarItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean = false,
    textColor: Color = White.copy(alpha = 0.7f),
    iconColor: Color = White.copy(alpha = 0.7f),
    onClick: () -> Unit = {}
) {
    Surface(
        color = if (isSelected) PrimaryGreen else Color.Transparent,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) White else iconColor,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                color = if (isSelected) White else textColor,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
}

@Composable
fun SummaryCardsGrid(state: DashboardState) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SummaryCard(
                modifier = Modifier.weight(1f),
                title = "Products",
                value = state.totalProducts.toString(),
                icon = AppIcons.Inventory,
                iconColor = Color(0xFF0EA5E9),
                iconBg = Color(0xFFE0F2FE)
            )
            SummaryCard(
                modifier = Modifier.weight(1f),
                title = "Low Stock",
                value = state.lowStock.toString(),
                icon = AppIcons.ErrorOutline,
                iconColor = Color(0xFFEF4444),
                iconBg = Color(0xFFFEE2E2),
                valueColor = RedColor
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SummaryCard(
                modifier = Modifier.weight(1f),
                title = "Today",
                value = state.todaySales.toString(),
                icon = AppIcons.AccountBalanceWallet,
                iconColor = Color(0xFF10B981),
                iconBg = Color(0xFFD1FAE5)
            )
            SummaryCard(
                modifier = Modifier.weight(1f),
                title = "This Month",
                value = state.monthlySales.toString(),
                icon = AppIcons.AutoGraph,
                iconColor = Color(0xFF8B5CF6),
                iconBg = Color(0xFFEDE9FE)
            )
        }
    }
}

@Composable
fun SummaryCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    iconBg: Color,
    valueColor: Color = TextDark
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Surface(
                color = iconBg,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                ),
                color = valueColor
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
        }
    }
}

@Composable
fun SalesChartCard(state: DashboardState, onEvent: (DashboardEvent) -> Unit) {

    val chartData = state.dailySales.associate { it.saleDate.toString() to it.totalSales.toFloat() }
    val productData = state.productSales.associate {  it.name to it.totalAmount.toFloat() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sales Performance",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = TextDark
                    )
                    
                    Box {
                        Surface(
                            modifier = Modifier
                                .clickable { onEvent(DashboardEvent.ToggleMenu) },
                            shape = RoundedCornerShape(16.dp),
                            color = PrimaryGreen
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = state.period.name,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                    color = White,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = AppIcons.ArrowDropDown,
                                    contentDescription = null,
                                    tint = White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = state.showMenu,
                            onDismissRequest = { state.showMenu = false },
                            modifier = Modifier
                                .widthIn(min = 100.dp)
                                .background(White)
                        ) {
                            period.entries.forEach { label ->
                                DropdownMenuItem(
                                    text = { Text(text = label.name, color = TextDark) },
                                    onClick = {
                                        onEvent(DashboardEvent.SetPeriod(label))
                                    }
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    chartTab(
                        isSelected =  state.chartTab == ChartTab.sales,
                        label = "Sales",
                        onClick = { onEvent(DashboardEvent.SetSelectedTab(ChartTab.sales)) },
                        modifier = Modifier.weight(1f)
                    )
                    chartTab(
                        isSelected = state.chartTab == ChartTab.product,
                        label = "Products",
                        onClick = { onEvent(DashboardEvent.SetSelectedTab(ChartTab.product)) },
                        modifier = Modifier.weight(1f)
                    )
                    chartTab(
                        isSelected = state.chartTab == ChartTab.branches,
                        label = "Branch Sales",
                        isLocked = true,
                        onClick = { onEvent(DashboardEvent.SetSelectedTab(ChartTab.branches)) },
                        modifier = Modifier.weight(1.2f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                if (state.chartTab == ChartTab.sales) {
                    Areachart(chartData, modifier = Modifier.fillMaxSize())
                } else if (state.chartTab == ChartTab.product) {
                    ProductsChart(productData, modifier = Modifier.fillMaxSize())
                } else {
                    Text("Upgrade")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (state.chartTab == ChartTab.sales) {
                    val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
                    chartData.keys.forEach { timestampStr ->
                        val timestamp = timestampStr.toLongOrNull() ?: 0L
                        val date = formatter.format(Date(timestamp))
                        Text(
                            date,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                } else if (state.chartTab == ChartTab.product) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        productData.entries.forEachIndexed { index, product ->
                            Row(
                                modifier = Modifier,
                                horizontalArrangement = Arrangement.spacedBy(1.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(pchartcolors[index % pchartcolors.size])
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    product.key,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Areachart(data: Map<String, Float>, modifier: Modifier = Modifier) {
    val values = data.values.toList()
    val graphColor = PrimaryGreen
    val transparentGraphColor = PrimaryGreen.copy(alpha = 0.2f)

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val spacePerDay = if (data.size > 1) width / (data.size - 1) else 0f

        val maxData = values.maxOrNull() ?: 0f
        val minData = values.minOrNull() ?: 0f
        val range = maxData - minData
        val heightFactor = height / (if (range == 0f) 1f else range)

        val path = Path().apply {
            values.forEachIndexed { index, value ->
                val x = index * spacePerDay
                val y = height - (value - minData) * heightFactor
                if (index == 0) moveTo(x, y) else lineTo(x, y)
            }
        }
        val fillpath = Path().apply {
            addPath(path)
            lineTo((data.size - 1) * spacePerDay, height)
            lineTo(0f, height)
            close()
        }

        drawPath(
            path = fillpath,
            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                colors = listOf(transparentGraphColor, Color.Transparent),
                startY = 0f,
                endY = height
            )
        )
        drawPath(
            path = path,
            color = graphColor,
            style = Stroke(width = 3.dp.toPx())
        )

        for (i in values.indices) {
            val x = i * spacePerDay
            val y = height - (values[i] - minData) * heightFactor
            drawCircle(
                color = graphColor,
                radius = 4.dp.toPx(),
                center = Offset(x, y)
            )
            drawCircle(
                color = White,
                radius = 2.dp.toPx(),
                center = Offset(x, y)
            )
        }
    }
}

@Composable
fun chartTab(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    label: String,
    isLocked: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) PrimaryGreen.copy(alpha = 0.1f) else Color.Transparent,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = if (isSelected) PrimaryGreen else TextMuted,
                maxLines = 1,
                softWrap = false
            )
            if (isLocked) {
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = AppIcons.Lock,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(10.dp)
                )
            }
        }
    }
}

@Composable
fun ProductsChart(data : Map<String,Float>, modifier: Modifier = Modifier) {
    val values = data.values.toList()
    val totalvalues =  values.sum()

    Canvas(modifier = modifier) {
        val canvasSize = size.minDimension
        var currentStartAngle = -90f

        values.forEachIndexed { index, value ->
            val sweepAngle = (value / totalvalues) * 360f
            drawArc(
                color = pchartcolors[index % pchartcolors.size],
                startAngle = currentStartAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 40.dp.toPx(), cap = StrokeCap.Butt),
                size = Size(canvasSize, canvasSize),
                topLeft = Offset(
                    (size.width - canvasSize) / 2f,
                    (size.height - canvasSize) / 2f
                )
            )
            currentStartAngle += sweepAngle
        }
    }
}

@Composable
fun TopProductsCard(state: DashboardState,modifier: Modifier = Modifier){
    val productData = state.productSales.associate { it.name to it.totalAmount.toFloat() }
    val productsList = productData.toList()
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BorderGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ){
        Column(modifier = Modifier.padding(20.dp)){
            Text(text = "Top Products",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            productsList.forEachIndexed { index, (product, value) ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = " #${index+1}",
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.width(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(2f)){
                        Text(text = product,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextDark
                        )
                        Text(text = "$value%",
                            style = MaterialTheme.typography.bodySmall,
                            color = PrimaryGreen
                        )
                    }
                    Icon(
                        imageVector = AppIcons.ChevronRight,
                        contentDescription = null,
                        tint = BorderGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
                if (index < productsList.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        thickness = 1.dp,
                        color = BorderGray.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}
