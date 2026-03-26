package com.example.dukatrack.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dukatrack.ui.theme.DarkNavy
import com.example.dukatrack.ui.theme.LightGrayBg
import com.example.dukatrack.ui.theme.TextDark
import com.example.dukatrack.ui.theme.TextMuted
import com.example.dukatrack.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(
    navController: NavController,
    title: String,
    content: @Composable (PaddingValues) -> Unit
) {
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
                SidebarContent(navController)
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
                                title,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = TextDark
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = TextDark
                                )
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
            content(paddingValues)
        }
    }
}
