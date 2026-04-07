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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dukatrack.ui.theme.DarkNavy
import com.example.dukatrack.ui.theme.PrimaryGreen
import com.example.dukatrack.ui.theme.TextDark
import com.example.dukatrack.ui.theme.TextMuted
import com.example.dukatrack.ui.theme.White

data class SaleRecord(
    val receiptId: String,
    val customer: String,
    val itemsCount: Int,
    val amount: Int,
    val paymentMethod: String,
    val time: String
)

@Composable
fun SalesHistoryScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedPayment by remember { mutableStateOf("All Payments") }
    var expanded by remember { mutableStateOf(false) }
    
    val sales = remember {
        listOf(
            SaleRecord("RCP-A1B2C3", "Walk-In", 3, 450, "Cash", "9:00 AM"),
            SaleRecord("RCP-D4E5F6", "John Kamau", 1, 220, "M-Pesa", "10:15 AM"),
            SaleRecord("RCP-G7H8I9", "Walk-In", 2, 594, "Cash", "11:30 AM"),
            SaleRecord("RCP-J1K2L3", "Mary Wanjiku", 5, 780, "M-Pesa", "1:45 PM")
        )
    }

    val filteredSales by remember(searchQuery, selectedPayment) {
        derivedStateOf {
            sales.filter {
                (it.receiptId.contains(searchQuery, ignoreCase = true) || it.customer.contains(searchQuery, ignoreCase = true)) &&
                (selectedPayment == "All Payments" || it.paymentMethod == selectedPayment)
            }
        }
    }

    val totalAmount by remember {
        derivedStateOf { filteredSales.sumOf { it.amount } }
    }
    val salesCount by remember {
        derivedStateOf { filteredSales.size }
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
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
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
                                Text(selectedPayment, color = White, fontSize = 14.sp)
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
                                        selectedPayment = method
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
                    Text("KSh ${"%,d".format(totalAmount)}", color = White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                items(filteredSales) { sale ->
                    SaleHistoryItem(sale)
                }
            }
        }
}

@Composable
fun SaleHistoryItem(sale: SaleRecord) {
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
                    Text(sale.receiptId, fontWeight = FontWeight.Bold, color = TextDark)
                    Text(sale.customer, color = TextMuted, fontSize = 14.sp)
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
                    Text("${sale.itemsCount} ${if(sale.itemsCount > 1) "items" else "item"}", color = TextMuted, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("KSh ${sale.amount}", fontWeight = FontWeight.Bold, color = TextDark)
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = if (sale.paymentMethod == "Cash") PrimaryGreen.copy(alpha = 0.1f) else Color(0xFF3B82F6).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = sale.paymentMethod,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = if (sale.paymentMethod == "Cash") PrimaryGreen else Color(0xFF3B82F6),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(sale.time, color = TextMuted, fontSize = 12.sp)
                }
            }
        }
    }
}
