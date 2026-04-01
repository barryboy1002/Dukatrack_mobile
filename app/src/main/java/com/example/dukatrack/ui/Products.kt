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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
    var showEditSheet by remember { mutableStateOf(false) }
    var selectedProductName by remember { mutableStateOf("") }
    var showAddDrop by remember{mutableStateOf(false)}
    var showCategory by remember{mutableStateOf(false)}
    var showSort by remember{mutableStateOf(false)}
    
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
    MainLayout(navController = navController, title = "Products") { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(DarkNavy)
        ){
            ProductSearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { /* Handle search submission */ },
                placeholder = { Text("Search products...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = "More options") }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Surface(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { showCategory  = true }
                ){
                    Text(
                        text="Filter",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    DropdownMenu(
                            expanded = showCategory,
                            onDismissRequest = { showCategory = false }
                    ) {
                        //figure out loop for categories
                        DropdownMenuItem(text = { Text("All categories") }, onClick = { /* Handle add click */ })
                        DropdownMenuItem(text = { Text("Flour") }, onClick = { /* Handle edit click */ })
                    }

                }
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { showSort = true }
                ){
                    Text(
                        text="Sort",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    DropdownMenu(
                        expanded = showSort,
                        onDismissRequest = { showSort = false }
                    ) {
                        //figure out loop for categories
                        DropdownMenuItem(text = { Text("Ascending") }, onClick = { /* Handle add click */ })
                        DropdownMenuItem(text = { Text("Descending") }, onClick = { /* Handle edit click */ })
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { showAddDrop = true}) {
                    Icon(
                        imageVector = Icons.Default.MoreVert, 
                        contentDescription = "More options",
                        tint = Color.White
                    )
                    DropdownMenu(
                        expanded = showAddDrop,
                        onDismissRequest = { showAddDrop = false }
                    ) {
                        DropdownMenuItem(text = { Text("Add Product") }, onClick = { /* Handle add click */ })
                        DropdownMenuItem(text = { Text("Add New Category") }, onClick = { /* Handle edit click */ })
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
                            showEditSheet = true
                        }
                    )
                }
            }
        }

        if (showEditSheet) {
            ModalBottomSheet(
                onDismissRequest = { showEditSheet = false },
                sheetState = sheetState,
                containerColor = White,
                dragHandle = null
            ) {
                EditProductSheet(
                    productName = selectedProductName,
                    onDismiss = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) showEditSheet = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun EditProductSheet(
    productName: String,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(productName) }
    var category by remember { mutableStateOf("Flour") }
    var barcode by remember { mutableStateOf("") }
    var buyingPrice by remember { mutableStateOf("150") }
    var sellingPrice by remember { mutableStateOf("180") }
    var unit by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var lowStockAlert by remember { mutableStateOf("10") }
    var initialStock by remember { mutableStateOf("0") }
    var additionalInfo by remember { mutableStateOf("") }

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
                "Edit Product",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        EditField(label = "PRODUCT NAME *", value = name, onValueChange = { name = it })
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            EditField(modifier = Modifier.weight(1f), label = "CATEGORY", value = category, onValueChange = { category = it })
            EditField(modifier = Modifier.weight(1f), label = "BARCODE", value = barcode, onValueChange = { barcode = it }, placeholder = "Optional")
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            EditField(modifier = Modifier.weight(1f), label = "BUYING PRICE (KSH) *", value = buyingPrice, onValueChange = { buyingPrice = it })
            EditField(modifier = Modifier.weight(1f), label = "SELLING PRICE (KSH) *", value = sellingPrice, onValueChange = { sellingPrice = it })
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            EditField(modifier = Modifier.weight(1f), label = "UNIT", value = unit, onValueChange = { unit = it })
            EditField(modifier = Modifier.weight(1f), label = "BRAND", value = brand, onValueChange = { brand = it }, placeholder = "Optional")
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            EditField(modifier = Modifier.weight(1f), label = "LOW STOCK ALERT AT", value = lowStockAlert, onValueChange = { lowStockAlert = it })
            EditField(modifier = Modifier.weight(1f), label = "INITIAL STOCK QUANTITY", value = initialStock, onValueChange = { initialStock = it })
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
    minLines: Int = 1
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
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = TextMuted.copy(alpha = 0.5f)) },
            singleLine = singleLine,
            minLines = minLines,
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
    leadingIcon : @Composable (()-> Unit)? = {Icon(Icons.Default.Search, contentDescription = "Search bar")},
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
                                Icon(Icons.Default.Close, contentDescription = "Clear search")
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
