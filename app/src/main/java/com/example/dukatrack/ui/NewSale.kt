package com.example.dukatrack.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dukatrack.ui.theme.BorderGray
import com.example.dukatrack.ui.theme.DarkNavy
import com.example.dukatrack.ui.theme.LightGrayBg
import com.example.dukatrack.ui.theme.PrimaryGreen
import com.example.dukatrack.ui.theme.RedColor
import com.example.dukatrack.ui.theme.TextDark
import com.example.dukatrack.ui.theme.TextMuted
import com.example.dukatrack.ui.theme.White

data class SaleProduct(
    val name: String,
    val category: String,
    val price: String,
    val stock: String,
    val isLowStock: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSaleScreen(navController: NavController) {
    var query by rememberSaveable { mutableStateOf("") }
    var showCart by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    //-- hardcoded data to be removed and replaced with backend -- //
    val products = remember {
        listOf(
            SaleProduct("Unga wa Ngano 2kg", "Flour", "KSh 180", "24 bags left"),
            SaleProduct("Omo 1kg", "Cleaning", "KSh 220", "3 pcs left", isLowStock = true),
            SaleProduct("Cooking Oil 1L", "Cooking", "KSh 330", "12 litres left"),
            SaleProduct("Sugar 1kg", "Flour", "KSh 160", "8 kg left")
        )
    }
    // -- end of fake data --//

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Search Bar
        ProductSearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = { /* Handle search */ },
            placeholder = { Text("Search product or scan barcode") },
            leadingIcon = { Icon(AppIcons.Search, contentDescription = "Search") }
        )

        // Action Buttons Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionButton(icon = AppIcons.QrCodeScanner, label = "Barcode", onClick = {})
            ActionButton(icon = AppIcons.CameraAlt, label = "Image", onClick = {})
            ActionButton(icon = AppIcons.FilterList, label = "Filter", onClick = {})
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { showCart = true },
                modifier = Modifier.background(White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            ) {
                Icon(AppIcons.ShoppingCart, contentDescription = "Cart", tint = White)
            }
        }

        // Products List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products) { product ->
                SaleProductItem(product = product)
            }
        }
    }

    if (showCart) {
        ModalBottomSheet(
            onDismissRequest = { showCart = false },
            sheetState = sheetState,
            containerColor = White,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            CartModal()
        }
    }
}

@Composable
fun CartModal() {
    var customerName by remember { mutableStateOf("Walk-in customer") }
    var discount by remember { mutableStateOf(0) }
    var selectedPayment by remember { mutableStateOf("Cash") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Current Sale",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = TextDark
            )
            TextButton(onClick = { /* Clear cart */ }) {
                Text("Clear", color = RedColor, fontWeight = FontWeight.Bold)
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderGray.copy(alpha = 0.5f))

        // Cart Items area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text("No items in cart", color = TextMuted)
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderGray.copy(alpha = 0.5f))

        // Summary
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Subtotal", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
                Text("KSh 0", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Discount", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(White, RoundedCornerShape(8.dp))
                        .border(1.dp, BorderGray, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(discount.toString(), color = TextDark, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = { discount++ },
                            modifier = Modifier.size(16.dp)
                        ) {
                            Icon(
                                AppIcons.KeyboardArrowUp,
                                contentDescription = "Increase",
                                tint = TextMuted
                            )
                        }
                        IconButton(
                            onClick = { if (discount > 0) discount-- },
                            modifier = Modifier.size(16.dp)
                        ) {
                            Icon(
                                AppIcons.KeyboardArrowDown,
                                contentDescription = "Decrease",
                                tint = TextMuted
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("%", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = BorderGray.copy(alpha = 0.5f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Total",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = TextDark
                )
                Text(
                    "KSh 0",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = TextDark
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = BorderGray.copy(alpha = 0.5f))

        // Payment Method
        Text(
            "PAYMENT METHOD",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            ),
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PaymentMethodButton(
                icon = AppIcons.CurrencyExchange,
                label = "Cash",
                isSelected = selectedPayment == "Cash",
                modifier = Modifier.weight(1f),
                onClick = { selectedPayment = "Cash" }
            )
            PaymentMethodButton(
                icon = AppIcons.PhoneAndroid,
                label = "M-Pesa",
                isSelected = selectedPayment == "M-Pesa",
                modifier = Modifier.weight(1f),
                onClick = { selectedPayment = "M-Pesa" }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Customer Name
        Text(
            "CUSTOMER NAME (OPTIONAL)",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            ),
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = customerName,
            onValueChange = { customerName = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = LightGrayBg,
                unfocusedContainerColor = LightGrayBg,
                focusedBorderColor = BorderGray,
                unfocusedBorderColor = BorderGray,
                focusedTextColor = TextDark,
                unfocusedTextColor = TextDark
            ),
            textStyle = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Complete Sale Button
        Button(
            onClick = { /* Complete sale */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(AppIcons.CheckCircle, contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Complete Sale",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun PaymentMethodButton(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) PrimaryGreen else BorderGray
        ),
        color = if (isSelected) PrimaryGreen.copy(alpha = 0.05f) else White
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isSelected) PrimaryGreen else TextMuted,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                label,
                color = if (isSelected) PrimaryGreen else TextMuted,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}


@Composable
fun ActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = White.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = label, tint = White, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, color = White, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun SaleProductItem(product: SaleProduct) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextDark
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = product.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = product.stock,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (product.isLowStock) RedColor else TextMuted
                    )
                    if (product.isLowStock) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = AppIcons.Warning,
                            contentDescription = "Low Stock",
                            tint = RedColor,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = " Low",
                            style = MaterialTheme.typography.labelSmall,
                            color = RedColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = product.price,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = PrimaryGreen
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { /* Add to Cart */ },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(AppIcons.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
// add receipt functionality

