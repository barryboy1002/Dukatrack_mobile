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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.AddShoppingCart
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.dukatrack.ui.theme.BorderGray
import com.example.dukatrack.ui.theme.DarkNavy
import com.example.dukatrack.ui.theme.PrimaryGreen
import com.example.dukatrack.ui.theme.RedColor
import com.example.dukatrack.ui.theme.TextDark
import com.example.dukatrack.ui.theme.TextMuted
import com.example.dukatrack.ui.theme.White
import com.example.dukatrack.ui.theme.pchartcolors

@Composable
fun DashboardScreen(navController: NavController) {
    MainLayout(navController = navController, title = "Dashboard") { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkNavy),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                SummaryCardsGrid()
            }
            item {
                SalesChartCard()
            }
            item {
                TopProductsCard()
            }
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
                imageVector = Icons.Default.Storefront,
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
                icon = Icons.Outlined.GridView,
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
                icon = Icons.Outlined.Inventory2,
                isSelected = currentRoute == screen_names.Products,
                onClick = {
                    if (currentRoute != screen_names.Products) {
                        navController.navigate(screen_names.Products)
                    }
                }
            )
            SidebarItem(label = "New Sale",
                icon = Icons.Outlined.AddShoppingCart,
                isSelected = currentRoute == screen_names.NewSale,
                onClick = {
                    if (currentRoute != screen_names.NewSale) {
                        navController.navigate(screen_names.NewSale)
                    }
                }
            )
            SidebarItem(
                label = "Sales History",
                icon = Icons.Outlined.History,
                isSelected = currentRoute == screen_names.SalesHistory,
                onClick = {
                    if (currentRoute != screen_names.SalesHistory) {
                        navController.navigate(screen_names.SalesHistory)
                    }
                }
            )
            SidebarItem("Stock", Icons.Outlined.Layers)
            SidebarItem("Suppliers", Icons.Outlined.LocalShipping)
            SidebarItem("Reports", Icons.Outlined.Assessment)
            SidebarItem("Settings", Icons.Outlined.Settings)

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(thickness = 0.5.dp, color = White.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(8.dp))

            SidebarItem(
                label = "Logout",
                icon = Icons.AutoMirrored.Filled.Logout,
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
fun SummaryCardsGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SummaryCard(
                modifier = Modifier.weight(1f),
                title = "Products",
                value = "248",
                icon = Icons.Default.Inventory,
                iconColor = Color(0xFF0EA5E9),
                iconBg = Color(0xFFE0F2FE)
            )
            SummaryCard(
                modifier = Modifier.weight(1f),
                title = "Low Stock",
                value = "5",
                icon = Icons.Default.ErrorOutline,
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
                value = "KSh 4,200",
                icon = Icons.Default.AccountBalanceWallet,
                iconColor = Color(0xFF10B981),
                iconBg = Color(0xFFD1FAE5)
            )
            SummaryCard(
                modifier = Modifier.weight(1f),
                title = "This Month",
                value = "KSh 31k",
                icon = Icons.Default.AutoGraph,
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
fun SalesChartCard() {
    var selectedTab by remember { mutableStateOf("Sales") }
    var showMenu by remember { mutableStateOf(false) }
    val mperiods = listOf("Weekly", "Monthly", "Yearly")
    var selectedperiod by remember { mutableStateOf("Weekly") }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    val chartData = remember(selectedperiod) {
        when (selectedperiod) {
            "Weekly" -> mapOf(
                "Mon" to 3500f,
                "Tue" to 4200f,
                "Wed" to 3800f,
                "Thu" to 5100f,
                "Fri" to 4800f,
                "Sat" to 6200f,
                "Sun" to 5800f
            )
            "Monthly" -> mapOf(
                "Week 1" to 12000f, "Week 2" to 15500f,
                "Week 3" to 9000f, "Week 4" to 18200f
            )
            "Yearly" -> mapOf(
                "Jan" to 45000f,
                "Mar" to 52000f,
                "May" to 48000f,
                "Jul" to 61000f,
                "Sep" to 55000f,
                "Nov" to 72000f
            )
            else -> emptyMap()
        }
    }
    val productData = mapOf("Electronics" to 40f, "Grocery" to 30f, "Clothing" to 30f)
    val topProductData = productData.toList().sortedByDescending { (_, value) -> value }.take(3)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ChartTab(
                        label = "Sales",
                        isSelected = selectedTab == "Sales",
                        onClick = { selectedTab = "Sales" }
                    )
                    ChartTab(
                        label = "Products",
                        isSelected = selectedTab == "Products",
                        onClick = { selectedTab = "Products" }
                    )
                    ChartTab(
                        label = "Branch Sales",
                        isSelected = selectedTab == "Branch",
                        isLocked = true,
                        onClick = { selectedTab = "Branch" }
                    )
                }

                Surface(
                    modifier = Modifier
                        .clickable { showMenu = true }
                        .onGloballyPositioned { coordinates ->
                            mTextFieldSize = coordinates.size.toSize()
                        },
                    shape = RoundedCornerShape(16.dp),
                    color = PrimaryGreen
                ) {
                    Text(
                        text = selectedperiod,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.width(mTextFieldSize.width.dp)
                    ) {
                        mperiods.forEach { label ->
                            DropdownMenuItem(
                                text = { Text(text = label) },
                                onClick = {
                                    selectedperiod = label
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                if (selectedTab == "Sales") {
                    Areachart(data = chartData, modifier = Modifier.fillMaxSize())
                } else if (selectedTab == "Products") {
                    ProductsChart(data = productData, modifier = Modifier.fillMaxSize())
                } else {
                    Text("Upgrade")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (selectedTab == "Sales") {
                    chartData.keys.forEach { day ->
                        Text(
                            day,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                } else if (selectedTab == "Products") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        topProductData.forEachIndexed { index, product ->
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
                                    product.first,
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
fun ChartTab(
    label: String,
    isSelected: Boolean,
    isLocked: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) PrimaryGreen.copy(alpha = 0.1f) else Color.Transparent,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = if (isSelected) PrimaryGreen else TextMuted
            )
            if (isLocked) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
fun ProductsChart(data: Map<String, Float>, modifier: Modifier = Modifier) {
    val values = data.values.toList()
    val totalvalues = values.sum()

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
fun TopProductsCard(modifier: Modifier = Modifier){
    val productData = mapOf("Electronics" to 40f, "Grocery" to 30f, "Clothing" to 30f)
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
                        imageVector = Icons.Filled.ChevronRight,
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
