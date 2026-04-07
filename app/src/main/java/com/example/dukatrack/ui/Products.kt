package com.example.dukatrack.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dukatrack.ui.theme.DarkNavy
import com.example.dukatrack.ui.theme.PrimaryGreen
import com.example.dukatrack.ui.theme.TextMuted
import com.example.dukatrack.ui.theme.White
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(navController: NavController){
    var query  by rememberSaveable { mutableStateOf("")}
    var showProductSheet by remember { mutableStateOf(false) }
    var selectedProductName by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    
    // Manage categories list
    val categories = remember { mutableStateListOf("Flour", "Sugar", "Cooking Oil", "Electronics", "Groceries") }
    var showCategoryDialog by remember { mutableStateOf(false) }

    // State for the dropdowns
    var showMoreMenu by remember{mutableStateOf(false)}
    var showFilterMenu by remember{mutableStateOf(false)}
    var showSortMenu by remember{mutableStateOf(false)}
    
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val items = listOf("Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb",
        "Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lollipop", "Marshmallow",
        "Nougat", "Oreo", "Pie")
    
    val filteredItems by remember{
        derivedStateOf { if(query.isEmpty()){
            items
        }else{
            items.filter{it.contains(query,ignoreCase = true) }
        }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
    ){
        ProductSearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = { /* Handle search submission */ },
            placeholder = { Text("Search products...", color = Color.White.copy(alpha = 0.6f)) },
            leadingIcon = { Icon(AppIcons.Search, contentDescription = "Search", tint = Color.White.copy(alpha = 0.6f)) },
            trailingIcon = { Icon(AppIcons.MoreVert, contentDescription = "More options", tint = Color.White.copy(alpha = 0.6f)) }
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            // Filter Dropdown
            Box {
                Surface(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { showFilterMenu = true }
                ){
                    Text(
                        text="Filter",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                DropdownMenu(
                    expanded = showFilterMenu,
                    onDismissRequest = { showFilterMenu = false },
                    modifier = Modifier.background(White)
                ) {
                    DropdownMenuItem(
                        text = { Text("All Categories", color = Color.Black) }, 
                        onClick = { showFilterMenu = false }
                    )
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category, color = Color.Black) }, 
                            onClick = { showFilterMenu = false }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Sort Dropdown
            Box {
                Surface(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { showSortMenu = true }
                ){
                    Text(
                        text="Sort",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false },
                    modifier = Modifier.background(White)
                ) {
                    DropdownMenuItem(text = { Text("Ascending", color = Color.Black) }, onClick = { showSortMenu = false })
                    DropdownMenuItem(text = { Text("Descending", color = Color.Black) }, onClick = { showSortMenu = false })
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
                            selectedProductName = ""
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
            modifier = Modifier.weight(1f).semantics {
                traversalIndex = 1f
            },
        ) {
            items(count = filteredItems.size) { index ->
                ProductListItem(
                    itemName = filteredItems[index],
                    itemDescription = "Active",
                    itemPrice = "Ksh 150",
                    onClick = {
                        selectedProductName = filteredItems[index]
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
                productName = selectedProductName,
                categories = categories,
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
                if (newCategory.isNotBlank()) categories.add(newCategory)
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
                Text("Enter category name", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                Spacer(modifier = Modifier.height(16.dp))
                EditField(
                    label = "NAME", 
                    value = categoryName, 
                    onValueChange = { categoryName = it },
                    placeholder = "e.g. Beverages"
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onAdd(categoryName) },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted)
            }
        },
        containerColor = White,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun ProductFormSheet(
    title: String,
    productName: String,
    categories: List<String>,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(productName) }
    var category by remember { mutableStateOf(if (categories.isNotEmpty()) categories[0] else "Uncategorized") }
    var barcode by remember { mutableStateOf("") }
    var buyingPrice by remember { mutableStateOf("") }
    var sellingPrice by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var lowStockAlert by remember { mutableStateOf("10") }
    var initialStock by remember { mutableStateOf("0") }
    var additionalInfo by remember { mutableStateOf("") }

    var categoryExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            IconButton(onClick = onDismiss) {
                Icon(AppIcons.Close, contentDescription = "Close", tint = TextMuted)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        EditField(label = "PRODUCT NAME *", value = name, onValueChange = { name = it })
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Category Dropdown
            Column(modifier = Modifier.weight(1f).padding(vertical = 8.dp)) {
                Text(
                    text = "CATEGORY",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = { Icon(AppIcons.ArrowDropDown, contentDescription = null) },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryGreen,
                            unfocusedBorderColor = Color(0xFFE5E7EB),
                            focusedContainerColor = White,
                            unfocusedContainerColor = White
                        )
                    )
                    // Transparent layer to capture click for dropdown
                    Box(modifier = Modifier.matchParentSize().clickable { categoryExpanded = true })
                    
                    DropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false },
                        modifier = Modifier.fillMaxWidth(0.45f)
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            EditField(modifier = Modifier.weight(1f), label = "BARCODE", value = barcode, onValueChange = { barcode = it }, placeholder = "Optional")
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            EditField(
                modifier = Modifier.weight(1f), 
                label = "BUYING PRICE (KSH) *", 
                value = buyingPrice, 
                onValueChange = { buyingPrice = it },
                keyboardType = KeyboardType.Number
            )
            EditField(
                modifier = Modifier.weight(1f), 
                label = "SELLING PRICE (KSH) *", 
                value = sellingPrice, 
                onValueChange = { sellingPrice = it },
                keyboardType = KeyboardType.Number
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            EditField(modifier = Modifier.weight(1f), label = "UNIT", value = unit, onValueChange = { unit = it })
            EditField(modifier = Modifier.weight(1f), label = "BRAND", value = brand, onValueChange = { brand = it }, placeholder = "Optional")
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            EditField(modifier = Modifier.weight(1f), label = "LOW STOCK ALERT AT", value = lowStockAlert, onValueChange = { lowStockAlert = it }, keyboardType = KeyboardType.Number)
            EditField(modifier = Modifier.weight(1f), label = "INITIAL STOCK QUANTITY", value = initialStock, onValueChange = { initialStock = it }, keyboardType = KeyboardType.Number)
        }

        EditField(label = "ADDITIONAL INFO", value = additionalInfo, onValueChange = { additionalInfo = it }, placeholder = "Purpose, usage notes, etc.", singleLine = false, minLines = 3)

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Save Product", color = White)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun EditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            ),
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = { input ->
                // Basic numeric validation if needed
                if (keyboardType == KeyboardType.Number) {
                    if (input.isEmpty() || input.all { it.isDigit() || it == '.' }) {
                        onValueChange(input)
                    }
                } else {
                    onValueChange(input)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = TextMuted.copy(alpha = 0.5f)) },
            singleLine = singleLine,
            minLines = minLines,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = TextStyle(fontSize = 14.sp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryGreen,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedContainerColor = White,
                unfocusedContainerColor = White
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSearchBar(
    query : String,
    onQueryChange : (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder : @Composable () -> Unit = {Text("Search Products")},
    leadingIcon : @Composable (()-> Unit)? = {Icon(AppIcons.Search, contentDescription = "Search bar")},
    trailingIcon: @Composable (() -> Unit)? = null,
    ){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp,vertical = 8.dp)
            .semantics{isTraversalGroup = true},
    ){
        val containerColor = Color.White.copy(alpha = 0.15f)
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics{traversalIndex = 0f}
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = SearchBarDefaults.colors(
                containerColor = containerColor,
            ),
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = {
                        onSearch(query)
                    },
                    expanded = false,
                    onExpandedChange = { },
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { onQueryChange("") }) {
                                Icon(AppIcons.Close, contentDescription = "Clear search")
                            }
                        } else {
                            trailingIcon?.invoke()
                        }
                    },
                    colors = SearchBarDefaults.inputFieldColors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedPlaceholderColor = Color.LightGray,
                        unfocusedPlaceholderColor = Color.LightGray,
                        focusedLeadingIconColor = Color.White,
                        unfocusedLeadingIconColor = Color.White,
                        focusedTrailingIconColor = Color.White,
                        unfocusedTrailingIconColor = Color.White
                    )

                )
            },
            shape = RoundedCornerShape(8.dp),
            expanded = false,
            onExpandedChange = { },
        ) {
            // Content is empty to avoid showing a separate suggestion list
        }
    }
}


@Composable
fun ProductListItem(
    itemName:String,
    itemDescription:String,
    itemPrice:String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
){
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
                color = PrimaryGreen.copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp)){
                    Text(text = itemDescription,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = PrimaryGreen
                    )
            }
        }
    }
}
