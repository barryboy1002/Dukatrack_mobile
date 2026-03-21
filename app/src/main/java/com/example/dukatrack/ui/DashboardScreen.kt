package com.example.dukatrack.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.dukatrack.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(240.dp),
                drawerContainerColor = DarkNavy,
                drawerShape = RoundedCornerShape(0.dp)
            ) {
                SidebarContent()
            }
        }
    ) {
        Scaffold(
            topBar = {
                Surface(
                    shadowElevation = 1.dp,
                    color = White
                ) {
                    TopAppBar(
                        title = {
                            Text(
                                "Dashboard",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = TextDark
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextDark)
                            }
                        },
                        actions = {
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.Outlined.Notifications,
                                    contentDescription = "Notifications",
                                    tint = TextMuted
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            IconButton(onClick = { }) {
                                Surface(
                                    shape = CircleShape,
                                    color = LightGrayBg,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Profile",
                                            tint = TextMuted,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = White
                        ),
                        modifier = Modifier.height(64.dp)
                    )
                }
            },
            containerColor = LightGrayBg
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(LightGrayBg) ,// 24px padding as requested
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    SummaryCardsGrid()
                }
                item {
                    SalesChartCard()
                }
            }
        }
    }
}

@Composable
fun SidebarContent() {
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
            // Inside SidebarContent()
            SidebarItem("Dashboard", Icons.Outlined.GridView, isSelected = true) // GridView looks more like a dashboard
            SidebarItem("Products", Icons.Outlined.Inventory2)
            SidebarItem("New Sale", Icons.Outlined.AddShoppingCart) // More action-oriented
            SidebarItem("Sales History", Icons.Outlined.History)
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
    iconColor: Color = White.copy(alpha = 0.7f)
) {
    Surface(
        color = if (isSelected) PrimaryGreen else Color.Transparent,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
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
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SummaryCard(
                modifier = Modifier.weight(1f),
                title = "Products",
                value = "248",
                icon = Icons.Default.Inventory,
                iconColor =  Color(0xFF0EA5E9),
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
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
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
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
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
    var showMenu by remember { mutableStateOf(false) }
    val mperiods = listOf("Weekly", "Monthly", "Yearly")
    var selectedperiod by remember { mutableStateOf("Weekly") }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}
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
                Text(
                    "Sales Last 7 Days",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
                    color = TextDark
                )
                Surface(modifier = Modifier.clickable { showMenu = true }.
                    onGloballyPositioned { coordinates -> mTextFieldSize = coordinates.size.toSize() },
                    shape = RoundedCornerShape(16.dp),
                    color = PrimaryGreen) {
                    Text(text = selectedperiod,
                        style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.width(mTextFieldSize.width.dp)
                    ) {mperiods.forEach{label ->
                        DropdownMenuItem(
                            text = { Text(text = label) },
                            onClick = { selectedperiod = label
                                showMenu = false }
                        ) }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Chart visualization will go here",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextMuted
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun DashboardPreview() {
    DukatrackTheme {
        DashboardScreen()
    }
}
