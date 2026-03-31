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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dukatrack.ui.theme.DarkNavy
import com.example.dukatrack.ui.theme.PrimaryGreen


@Composable
fun ProductsScreen(navController: NavController){
    var query  by rememberSaveable { mutableStateOf("")}
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
                    modifier = Modifier.clickable { /* Handle filter */ }
                ){
                    Text(
                        text="Filter",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { /* Handle sort */ }
                ){
                    Text(
                        text="Sort",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* Handle more options click */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert, 
                        contentDescription = "More options",
                        tint = Color.White
                    )
                }

            }
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f).semantics {
                    traversalIndex = 1f
                },
            ) {
                items(count = filteredItems.size) {
                    ProductListItem(itemName = filteredItems[it],"Active","Ksh 150")
                }
            }

        }
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
            .padding(top = 8.dp)
            .semantics{isTraversalGroup = true}
    ){
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics{traversalIndex = 0f}
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
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
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            },
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
    modifier: Modifier = Modifier
){
    Surface(modifier = Modifier.clickable{}.fillMaxWidth(),
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
