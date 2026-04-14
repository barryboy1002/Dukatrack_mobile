package com.example.dukatrack.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dukatrack.data.CategoryEntity
import com.example.dukatrack.data.ProductDao
import com.example.dukatrack.event.ProductEvent
import com.example.dukatrack.ui.theme.PrimaryGreen
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    navController: NavController,
    viewModel: com.example.dukatrack.ui.products.ProductViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showProductSheet by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<ProductDao.ProductWithStock?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    var showCategoryDialog by remember { mutableStateOf(false) }

    // State for the dropdowns
    var showMoreMenu by remember { mutableStateOf(false) }
    var showFilterMenu by remember { mutableStateOf(false) }

    var currentCategoryId by remember { mutableStateOf<Long?>(null) }


    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val filteredItems by remember(state.searchQuery, state.products, currentCategoryId) {
        derivedStateOf {
            val baseList = if (currentCategoryId == null) {
                state.products
            } else {
                state.products.filter { it.categoryId == currentCategoryId }
            }

            if (state.searchQuery.isEmpty()) {
                baseList
            } else {
                baseList.filter { it.name.contains(state.searchQuery, ignoreCase = true) }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ProductSearchBar(
            query = state.searchQuery,
            onQueryChange = { viewModel.onEvent(ProductEvent.SearchQueryChanged(it)) },
            onSearch = { /* Handle search submission */ },
            placeholder = { Text("Search products...", color = Color.Black.copy(alpha = 0.4f)) },
            leadingIcon = {
                Icon(
                    AppIcons.Search,
                    contentDescription = "Search",
                    tint = Color.Black.copy(alpha = 0.4f)
                )
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Filter Dropdown
            Box {
                Surface(
                    color = Color.White.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { showFilterMenu = true }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = AppIcons.FilterList,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (currentCategoryId == null) "Filter" else state.categories.find { it.id == currentCategoryId }?.name ?: "Filter",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                DropdownMenu(
                    expanded = showFilterMenu,
                    onDismissRequest = { showFilterMenu = false },
                    modifier = Modifier.background(White)
                ) {
                    DropdownMenuItem(
                        text = { Text("All Categories", color = Color.Black) },
                        onClick = { 
                            currentCategoryId = null
                            showFilterMenu = false 
                        }
                    )
                    state.categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name, color = Color.Black) },
                            onClick = { 
                                currentCategoryId = category.id
                                showFilterMenu = false
                            }
                        )
                    }
                }
            }



            Spacer(modifier = Modifier.weight(1f))

            // More Menu
            Box {
                IconButton(onClick = { showMoreMenu = true }) {
                    Icon(
                        imageVector = AppIcons.MoreVert,
                        contentDescription = "More options",
                        tint = Color.White
                    )
                }
                DropdownMenu(
                    expanded = showMoreMenu,
                    onDismissRequest = { showMoreMenu = false },
                    modifier = Modifier.background(White)
                ) {
                    DropdownMenuItem(
                        text = { Text("Add Product", color = Color.Black) },
                        onClick = {
                            showMoreMenu = false
                            isEditing = false
                            selectedProduct = null
                            showProductSheet = true
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Add New Category", color = Color.Black) },
                        onClick = {
                            showMoreMenu = false
                            showCategoryDialog = true
                        }
                    )
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .weight(1f)
                .semantics {
                    traversalIndex = 1f
                },
        ) {
            items(count = filteredItems.size) { index ->
                val product = filteredItems[index]
                val isLowStock = (product.stockQuantity ?: 0) <= (product.lowStockThreshold ?: 5)
                ProductListItem(
                    itemName = product.name,
                    itemDescription = if ((product.stockQuantity ?: 0) <= 0) "Out of Stock" else "Stock: ${product.stockQuantity}",
                    itemPrice = "Ksh ${product.sellingPrice ?: 0}",
                    isLowStock = isLowStock,
                    onClick = {
                        selectedProduct = product
                        isEditing = true
                        showProductSheet = true
                    }
                )
            }
        }
    }

    // Reusable Product Form Sheet (Add/Edit)
    if (showProductSheet) {
        ModalBottomSheet(
            onDismissRequest = { showProductSheet = false },
            sheetState = sheetState,
            containerColor = White,
            dragHandle = null
        ) {
            ProductFormSheet(
                title = if (isEditing) "Edit Product" else "Add Product",
                product = selectedProduct,
                categories = state.categories,
                onSave = { name, categoryId, buyingPrice, sellingPrice, unit, brand, lowStockAlert, initialStock, additionalInfo ->
                    if (isEditing && selectedProduct != null) {
                        viewModel.onEvent(ProductEvent.UpdateProduct(
                            productId = selectedProduct!!.productId,
                            name = name,
                            categoryId = categoryId,
                            buyingPrice = buyingPrice,
                            sellingPrice = sellingPrice,
                            unit = unit,
                            brand = brand,
                            lowStockAlert = lowStockAlert,
                            additionalInfo = additionalInfo
                        ))
                    } else {
                        viewModel.onEvent(ProductEvent.AddProduct(
                            name = name,
                            categoryId = categoryId,
                            buyingPrice = buyingPrice,
                            sellingPrice = sellingPrice,
                            unit = unit,
                            brand = brand,
                            lowStockAlert = lowStockAlert,
                            initialStock = initialStock,
                            additionalInfo = additionalInfo
                        ))
                    }
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) showProductSheet = false
                    }
                },
                onDismiss = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) showProductSheet = false
                    }
                }
            )
        }
    }

    // Category Dialog
    if (showCategoryDialog) {
        CategoryDialog(
            onDismiss = { showCategoryDialog = false },
            onAdd = { newCategory ->
                if (newCategory.isNotBlank()) {
                    viewModel.onEvent(ProductEvent.AddCategory(newCategory))
                }
                showCategoryDialog = false
            }
        )
    }
}

@Composable
fun CategoryDialog(
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {
    var categoryName by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Manage Category", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Category Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onAdd(categoryName) },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        }
    )
}

@Composable
fun ProductFormSheet(
    title: String,
    product: ProductDao.ProductWithStock? = null,
    categories: List<CategoryEntity>,
    onSave: (String, Long, Double, Double, String, String, Int, Int, String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var categoryId by remember { mutableStateOf(product?.categoryId ?: (categories.firstOrNull()?.id ?: 0L)) }
    var buyingPrice by remember { mutableStateOf(product?.buyingPrice?.toString() ?: "") }
    var sellingPrice by remember { mutableStateOf(product?.sellingPrice?.toString() ?: "") }
    var unit by remember { mutableStateOf(product?.units ?: "") }
    var brand by remember { mutableStateOf(product?.brand ?: "") }
    var lowStockAlert by remember { mutableStateOf(product?.lowStockThreshold?.toString() ?: "5") }
    var initialStock by remember { mutableStateOf(product?.stockQuantity?.toString() ?: "0") }
    var additionalInfo by remember { mutableStateOf(product?.description ?: "") }

    var expandedCategory by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(bottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            IconButton(onClick = onDismiss) {
                Icon(AppIcons.Close, contentDescription = "Close")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                EditField("Product Name", name, { name = it })
            }
            item {
                Text("Category", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedCard(
                        onClick = { expandedCategory = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = categories.find { it.id == categoryId }?.name ?: "Select Category",
                                modifier = Modifier.weight(1f)
                            )
                            Icon(AppIcons.ArrowDropDown, contentDescription = null)
                        }
                    }
                    DropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false },
                        modifier = Modifier.fillMaxWidth(0.9f).background(White)
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name, color = Color.Black) },
                                onClick = {
                                    categoryId = category.id
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    EditField("Buying Price", buyingPrice, { buyingPrice = it }, Modifier.weight(1f), keyboardType = KeyboardType.Decimal)
                    EditField("Selling Price", sellingPrice, { sellingPrice = it }, Modifier.weight(1f), keyboardType = KeyboardType.Decimal)
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    EditField("Unit (e.g., kg, pcs)", unit, { unit = it }, Modifier.weight(1f))
                    EditField("Brand", brand, { brand = it }, Modifier.weight(1f))
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    EditField("Low Stock Alert", lowStockAlert, { lowStockAlert = it }, Modifier.weight(1f), keyboardType = KeyboardType.Number)
                    if (product == null) {
                        EditField("Initial Stock", initialStock, { initialStock = it }, Modifier.weight(1f), keyboardType = KeyboardType.Number)
                    }
                }
            }
            item {
                EditField("Additional Info", additionalInfo, { additionalInfo = it }, isSingleLine = false)
            }
            item {
                Button(
                    onClick = {
                        onSave(
                            name,
                            categoryId,
                            buyingPrice.toDoubleOrNull() ?: 0.0,
                            sellingPrice.toDoubleOrNull() ?: 0.0,
                            unit,
                            brand,
                            lowStockAlert.toIntOrNull() ?: 5,
                            initialStock.toIntOrNull() ?: 0,
                            additionalInfo
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("Save Product", modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Composable
fun EditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isSingleLine: Boolean = true,
    maxLines: Int = 3,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            singleLine = isSingleLine,
            maxLines = if (isSingleLine) 1 else maxLines,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
fun ProductSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(88.dp),
        shadowElevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = placeholder,
                leadingIcon = leadingIcon,
                trailingIcon = if (query.isNotEmpty()) {
                    {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(
                                imageVector = AppIcons.Close,
                                contentDescription = "Clear search",
                                tint = Color.Black.copy(alpha = 0.6f)
                            )
                        }
                    }
                } else {
                    trailingIcon
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Black.copy(alpha = 0.05f),
                    unfocusedContainerColor = Color.Black.copy(alpha = 0.05f),
                    disabledContainerColor = Color.Transparent,
                    cursorColor = PrimaryGreen,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedPlaceholderColor = Color.Black.copy(alpha = 0.4f),
                    unfocusedPlaceholderColor = Color.Black.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}


@Composable
fun ProductListItem(
    itemName:String,
    itemDescription:String,
    itemPrice:String,
    modifier: Modifier = Modifier,
    isLowStock: Boolean = false,
    onClick: () -> Unit = {}
){
    val statusColor = if (isLowStock) Color.Red else PrimaryGreen
    Surface(modifier = Modifier.clickable(onClick = onClick).fillMaxWidth(),
        color = Color.White.copy(alpha = 1f),
        shape = RoundedCornerShape(8.dp)){
        Row (modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically){
            Column(modifier = modifier.weight(1f)){
                Text(text = itemName,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge)
                Text(text = itemPrice,
                    style = MaterialTheme.typography.bodyMedium,
                    color = PrimaryGreen)
            }
            Surface(
                color = statusColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp)){
                    Text(text = itemDescription,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = statusColor
                    )
            }
        }
    }
}
