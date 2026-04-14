package com.example.dukatrack.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dukatrack.data.SalesDao
import com.example.dukatrack.ui.sales.SalesHistoryViewModel
import com.example.dukatrack.ui.theme.DarkNavy
import com.example.dukatrack.ui.theme.PrimaryGreen
import com.example.dukatrack.ui.theme.TextDark
import com.example.dukatrack.ui.theme.TextMuted
import com.example.dukatrack.ui.theme.White
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SalesHistoryScreen(
    navController: NavController,
    viewModel: SalesHistoryViewModel
) {
    val state by viewModel.state.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    val totalAmount by remember(state.sales) {
        derivedStateOf { state.sales.sumOf { it.sale.totalAmount } }
    }
    val salesCount by remember(state.sales) {
        derivedStateOf { state.sales.size }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
            // Filters Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Search Bar
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search receipt or customer...", color = White.copy(alpha = 0.6f)) },
                    leadingIcon = { Icon(AppIcons.Search, contentDescription = null, tint = White.copy(alpha = 0.6f)) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = White.copy(alpha = 0.1f),
                        unfocusedContainerColor = White.copy(alpha = 0.1f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = White,
                        unfocusedTextColor = White
                    ),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Date Picker Button
                    Surface(
                        onClick = { /* Open Date Picker */ },
                        color = White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = AppIcons.CalendarToday,
                                contentDescription = "Pick Date",
                                tint = White,
                                modifier = Modifier.size(20.dp)
                            )
                            Text("Select Date", color = White, fontSize = 14.sp)
                        }
                    }

                    // Payment Dropdown
                    val paymentMethods = listOf("All Payments", "Cash", "M-Pesa")

                    Box(modifier = Modifier.weight(1f)) {
                        Surface(
                            onClick = { expanded = true },
                            color = White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(state.selectedPayment, color = White, fontSize = 14.sp)
                                Icon(AppIcons.ArrowDropDown, contentDescription = null, tint = White)
                            }
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(White)
                        ) {
                            paymentMethods.forEach { method ->
                                DropdownMenuItem(
                                    text = { Text(method, color = TextDark) },
                                    onClick = {
                                        viewModel.onPaymentMethodChange(method)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Summary Stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text("TOTAL", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("KSh ${"%,.0f".format(totalAmount)}", color = White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(24.dp))
                Column(horizontalAlignment = Alignment.End) {
                    Text("SALES", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("$salesCount", color = White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Sales List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.sales) { sale ->
                    SaleHistoryItem(sale)
                }
            }
        }
}

@Composable
fun SaleHistoryItem(saleWithCount: SalesDao.SaleWithItemCount) {
    val sale = saleWithCount.sale
    val dateFormat = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }
    val timeStr = remember(sale.saleDate) { dateFormat.format(Date(sale.saleDate)) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("RCP-${sale.saleId}", fontWeight = FontWeight.Bold, color = TextDark)
                    Text(sale.customerName, color = TextMuted, fontSize = 14.sp)
                }
                IconButton(onClick = { /* View details */ }) {
                    Icon(AppIcons.Visibility, contentDescription = "View", tint = TextMuted)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val count = saleWithCount.itemsCount
                    Text("$count ${if(count > 1) "items" else "item"}", color = TextMuted, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("KSh ${"%,.0f".format(sale.totalAmount)}", fontWeight = FontWeight.Bold, color = TextDark)
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val paymentMethod = sale.paymentMethod ?: "N/A"
                    Surface(
                        color = if (paymentMethod == "Cash") PrimaryGreen.copy(alpha = 0.1f) else Color(0xFF3B82F6).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = paymentMethod,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = if (paymentMethod == "Cash") PrimaryGreen else Color(0xFF3B82F6),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(timeStr, color = TextMuted, fontSize = 12.sp)
                }
            }
        }
    }
}
