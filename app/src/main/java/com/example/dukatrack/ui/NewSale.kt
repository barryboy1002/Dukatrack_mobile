package com.example.dukatrack.ui

import android.content.Intent
import android.net.Uri
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dukatrack.data.ProductDao
import com.example.dukatrack.data.SaleItemEntity
import com.example.dukatrack.data.SalesEntity
import com.example.dukatrack.event.NewSaleEvent
import com.example.dukatrack.state.CartItem
import com.example.dukatrack.state.NewSaleState
import com.example.dukatrack.ui.sales.NewSaleViewModel
import com.example.dukatrack.ui.theme.BorderGray
import com.example.dukatrack.ui.theme.DarkNavy
import com.example.dukatrack.ui.theme.LightGrayBg
import com.example.dukatrack.ui.theme.PrimaryGreen
import com.example.dukatrack.ui.theme.RedColor
import com.example.dukatrack.ui.theme.TextDark
import com.example.dukatrack.ui.theme.TextMuted
import com.example.dukatrack.ui.theme.White
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SaleProduct(
    val name: String,
    val category: String,
    val price: String,
    val stock: String,
    val isLowStock: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSaleScreen(
    navController: NavController,
    viewModel: NewSaleViewModel
) {
    val state by viewModel.state.collectAsState()
    var showCart by remember { mutableStateOf(false) }
    var showProDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showProDialog) {
        ProFeatureDialog(onDismiss = { showProDialog = false })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryGreen)
    ) {
        // Search Bar
        ProductSearchBar(
            query = state.pSearchQuery,
            onQueryChange = { viewModel.onEvent(NewSaleEvent.SearchQueryChanged(it)) },
            onSearch = { /* Handle search */ },
            placeholder = { Text("Search product or scan barcode", color = Color.Gray) },
            leadingIcon = { Icon(AppIcons.Search, contentDescription = "Search", tint = Color.Gray) }
        )

        // Action Buttons Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionButton(
                icon = AppIcons.QrCodeScanner,
                label = "Barcode",
                onClick = { showProDialog = true }
            )
            ActionButton(
                icon = AppIcons.CameraAlt,
                label = "Image",
                onClick = { showProDialog = true }
            )
            ActionButton(icon = AppIcons.FilterList, label = "Filter", onClick = {})
            Spacer(modifier = Modifier.weight(1f))
            BadgedBox(
                badge = {
                    if (state.cart.isNotEmpty()) {
                        Badge { Text(state.cart.size.toString()) }
                    }
                }
            ) {
                IconButton(
                    onClick = { showCart = true },
                    modifier = Modifier.background(White.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                ) {
                    Icon(AppIcons.ShoppingCart, contentDescription = "Cart", tint = White)
                }
            }
        }

        // Products List (With white background)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGrayBg, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.products) { product ->
                    SaleProductItem(
                        product = product,
                        onAdd = { viewModel.onEvent(NewSaleEvent.AddToCart(product)) }
                    )
                }
            }
        }
    }

    state.lastCompletedSale?.let { (sale, items) ->
        ReceiptDialog(
            sale = sale,
            items = items,
            products = state.products,
            onDismiss = { viewModel.onEvent(NewSaleEvent.DismissReceipt) }
        )
    }

    if (showCart) {
        ModalBottomSheet(
            onDismissRequest = { showCart = false },
            sheetState = sheetState,
            containerColor = White,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            CartModal(
                state = state,
                onEvent = viewModel::onEvent,
                onClose = { showCart = false }
            )
        }
    }
}

@Composable
fun ReceiptDialog(
    sale: SalesEntity,
    items: List<SaleItemEntity>,
    products: List<ProductDao.ProductWithStock> = emptyList(),
    onDismiss: () -> Unit
) {
    var showProDialog by remember { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()) }

    if (showProDialog) {
        ProFeatureDialog(onDismiss = { showProDialog = false })
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { showProDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Icon(AppIcons.Print, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Print Receipt (Pro)")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = TextMuted)
            }
        },
        containerColor = White,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "DUKATRACK",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = PrimaryGreen
                )
                Text(
                    "Sales Receipt",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Receipt ID:", color = TextMuted)
                    Text("#${sale.saleId}", fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Date:", color = TextMuted)
                    Text(dateFormatter.format(Date(sale.saleDate)))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Customer:", color = TextMuted)
                    Text(sale.customerName)
                }
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderGray.copy(alpha = 0.5f))

                items.forEach { item ->
                    val productName = remember(item.productId) {
                        products.find { it.productId == item.productId }?.name ?: "Product #${item.productId}"
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(productName, fontWeight = FontWeight.Medium)
                            Text("${item.quantity} x KSh ${item.unitPrice}", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                        }
                        Text("KSh ${item.totalAmount}", fontWeight = FontWeight.Bold)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderGray.copy(alpha = 0.5f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Amount", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("KSh ${sale.totalAmount}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Payment Method", color = TextMuted)
                    Text(sale.paymentMethod ?: "Cash")
                }
            }
        }
    )
}

@Composable
fun CartModal(
    state: NewSaleState,
    onEvent: (NewSaleEvent) -> Unit,
    onClose: () -> Unit
) {
    val subtotal = state.cart.sumOf { it.total }
    val total = subtotal * (1 - state.discount / 100.0)

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
            TextButton(onClick = { onEvent(NewSaleEvent.ClearCart) }) {
                Text("Clear", color = RedColor, fontWeight = FontWeight.Bold)
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderGray.copy(alpha = 0.5f))

        // Cart Items area
        if (state.cart.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("No items in cart", color = TextMuted)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.cart) { item ->
                    CartItemRow(item = item, onEvent = onEvent)
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderGray.copy(alpha = 0.5f))

        // Summary
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Subtotal", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
                Text("KSh ${"%.2f".format(subtotal)}", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
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
                    Text(state.discount.toString(), color = TextDark, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = { if (state.discount < 100) onEvent(NewSaleEvent.DiscountChanged(state.discount + 1)) },
                            modifier = Modifier.size(16.dp)
                        ) {
                            Icon(
                                AppIcons.KeyboardArrowUp,
                                contentDescription = "Increase",
                                tint = TextMuted
                            )
                        }
                        IconButton(
                            onClick = { if (state.discount > 0) onEvent(NewSaleEvent.DiscountChanged(state.discount - 1)) },
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
                    "KSh ${"%.2f".format(total)}",
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
                isSelected = state.paymentMethod == "Cash",
                modifier = Modifier.weight(1f),
                onClick = { onEvent(NewSaleEvent.PaymentMethodChanged("Cash")) }
            )
            PaymentMethodButton(
                icon = AppIcons.PhoneAndroid,
                label = "M-Pesa",
                isSelected = state.paymentMethod == "M-Pesa",
                modifier = Modifier.weight(1f),
                onClick = { onEvent(NewSaleEvent.PaymentMethodChanged("M-Pesa")) }
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
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = state.customerName,
                onValueChange = { onEvent(NewSaleEvent.CustomerNameChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search or add customer") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = LightGrayBg,
                    unfocusedContainerColor = LightGrayBg,
                    focusedBorderColor = BorderGray,
                    unfocusedBorderColor = BorderGray,
                    focusedTextColor = TextDark,
                    unfocusedTextColor = TextDark
                ),
                textStyle = MaterialTheme.typography.bodyLarge,
                trailingIcon = {
                    if (state.customerName.isNotEmpty() && state.customerName != "Walk-in customer") {
                        IconButton(onClick = { onEvent(NewSaleEvent.CustomerNameChanged("Walk-in customer")) }) {
                            Icon(AppIcons.Close, contentDescription = "Clear")
                        }
                    }
                }
            )

            if (state.showCustomerSearch && state.customers.isNotEmpty()) {
                DropdownMenu(
                    expanded = true,
                    onDismissRequest = { onEvent(NewSaleEvent.SetCustomerSearchVisibility(false)) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(White)
                ) {
                    state.customers.forEach { customer ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(customer.name, fontWeight = FontWeight.Bold)
                                    customer.phoneNumber?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = TextMuted) }
                                }
                            },
                            onClick = {
                                onEvent(NewSaleEvent.CustomerNameChanged(customer.name))
                                onEvent(NewSaleEvent.SetCustomerSearchVisibility(false))
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Complete Sale Button
        Button(
            onClick = { 
                onEvent(NewSaleEvent.Checkout)
                onClose()
            },
            enabled = state.cart.isNotEmpty(),
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
fun CartItemRow(item: CartItem, onEvent: (NewSaleEvent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, fontWeight = FontWeight.Bold, color = TextDark)
            Text("KSh ${item.price}", color = TextMuted, style = MaterialTheme.typography.bodySmall)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onEvent(NewSaleEvent.UpdateCartQuantity(item.id, item.quantity - 1)) }) {
                Icon(AppIcons.Remove, contentDescription = null, tint = PrimaryGreen)
            }
            Text(item.quantity.toString(), fontWeight = FontWeight.Bold)
            IconButton(onClick = { onEvent(NewSaleEvent.UpdateCartQuantity(item.id, item.quantity + 1)) }) {
                Icon(AppIcons.Add, contentDescription = null, tint = PrimaryGreen)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("KSh ${"%.2f".format(item.total)}", fontWeight = FontWeight.Bold, color = TextDark)
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
fun SaleProductItem(
    product: ProductDao.ProductWithStock,
    onAdd: () -> Unit
) {
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
                        text = product.categoryName ?: "Uncategorized",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    val isLowStock = (product.stockQuantity ?: 0) <= (product.lowStockThreshold ?: 5)
                    Text(
                        text = "${product.stockQuantity ?: 0} in stock",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isLowStock) RedColor else TextMuted
                    )
                    if (isLowStock) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = AppIcons.Warning,
                            contentDescription = "Low Stock",
                            tint = RedColor,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "KSh ${product.sellingPrice ?: 0.0}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = PrimaryGreen
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onAdd,
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
