package com.example.dukatrack.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dukatrack.ui.theme.*

data class Product(
    val name: String,
    val category: String,
    val buyingPrice: String,
    val sellingPrice: String,
    val stock: String,
    val isLowStock: Boolean,
    val status: String
)

@Composable
fun ProductsScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var lowStockOnly by remember { mutableStateOf(false) }

    val products = listOf(
        Product("Unga wa Ngano 2kg", "Flour", "KSh 150", "KSh 180", "24 bags", false, "Active"),
        Product("Omo 1kg", "Cleaning", "KSh 180", "KSh 220", "3 pcs", true, "Active"),
        Product("Cooking Oil 1L", "Cooking", "KSh 280", "KSh 330", "12 litres", false, "Active"),
        Product("Sugar 1kg", "Flour", "KSh 130", "KSh 160", "8 kg", true, "Active")
    )

    MainLayout(navController = navController, title = "Products") { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF0F172A)) // Dark background from the image
                .padding(24.dp)
        ) {
            // Header Action Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Search Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search products...", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF94A3B8)) },
                    modifier = Modifier
                        .width(320.dp)
                        .height(44.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = White,
                        unfocusedContainerColor = White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = TextDark
                    ),
                    singleLine = true
                )

                // Category Selector
                Surface(
                    modifier = Modifier.height(44.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = White
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("All Categories", color = TextDark, fontSize = 14.sp)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TextDark)
                    }
                }

                // Low Stock Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Checkbox(
                        checked = lowStockOnly,
                        onCheckedChange = { lowStockOnly = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = PrimaryGreen,
                            uncheckedColor = Color.Gray,
                            checkmarkColor = White
                        )
                    )
                    Text("Low stock only", color = White, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.weight(1f))

                // Action Buttons
                Surface(
                    modifier = Modifier
                        .height(40.dp)
                        .clickable { },
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Transparent,
                    border = BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Tune, contentDescription = null, tint = White, modifier = Modifier.size(18.dp))
                        Text("Categories", color = White, fontSize = 14.sp)
                    }
                }

                Button(
                    onClick = { },
                    modifier = Modifier.height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Product", color = White, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Products Table Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    // Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("PRODUCT", modifier = Modifier.weight(2f), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp), color = Color(0xFF94A3B8))
                        Text("CATEGORY", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp), color = Color(0xFF94A3B8))
                        Text("BUYING PRICE", modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp), color = Color(0xFF94A3B8))
                        Text("SELLING PRICE", modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp), color = Color(0xFF94A3B8))
                        Text("STOCK", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp), color = Color(0xFF94A3B8))
                        Text("STATUS", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp), color = Color(0xFF94A3B8))
                        Box(modifier = Modifier.width(120.dp))
                    }

                    HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)

                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(products) { product ->
                            ProductRow(product)
                            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductRow(product: Product) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(product.name, modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold, color = Color(0xFF1E293B), fontSize = 14.sp)
        Text(product.category, modifier = Modifier.weight(1f), color = Color(0xFF64748B), fontSize = 14.sp)
        Text(product.buyingPrice, modifier = Modifier.weight(1.2f), color = Color(0xFF1E293B), fontSize = 14.sp)
        Text(product.sellingPrice, modifier = Modifier.weight(1.2f), color = Color(0xFF1E293B), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        
        Text(
            product.stock,
            modifier = Modifier.weight(1f),
            color = if (product.isLowStock) Color(0xFFEF4444) else Color(0xFF22C55E),
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )

        // Status badge
        Box(modifier = Modifier.weight(1f)) {
            Surface(
                color = if (product.status == "Active") Color(0xFFF0FDF4) else Color(0xFFF1F5F9),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    product.status,
                    color = if (product.status == "Active") Color(0xFF166534) else Color(0xFF64748B),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        // Action Icons
        Row(
            modifier = Modifier.width(120.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(32.dp).clickable { },
                shape = RoundedCornerShape(6.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(14.dp))
                }
            }
            Surface(
                modifier = Modifier.size(32.dp).clickable { },
                shape = RoundedCornerShape(6.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Percent, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(14.dp))
                }
            }
            Surface(
                modifier = Modifier.size(32.dp).clickable { },
                shape = RoundedCornerShape(6.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}
