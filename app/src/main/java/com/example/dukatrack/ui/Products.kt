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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dukatrack.ui.theme.DarkNavy


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
                searchResults = filteredItems,
                onResultClick = { query = it },
                placeholder = { Text("Search products...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = "More options") },
                supportingContent = { Text("Android dessert") },
                leadingContent = { Icon(Icons.Filled.Star, contentDescription = "Starred item") }
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
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 72.dp, // Provides space for the search bar
                    end = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.semantics {
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
    searchResults: List<String>,
    onResultClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder : @Composable () -> Unit = {Text("Search Products")},
    leadingIcon : @Composable (()-> Unit)? = {Icon(Icons.Default.Search, contentDescription = "Search bar")},
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingContent: (@Composable (String) -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    ){
    var expanded by remember {mutableStateOf(false)}
    Box(
        modifier = modifier
            .fillMaxWidth() // Changed from fillMaxSize() to show content below
            .semantics{isTraversalGroup = true}
    ){
        SearchBar(modifier = Modifier
            .align(Alignment.TopCenter)
            .semantics{traversalIndex = 0f},
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 6.dp,
                    inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                   onQueryChange = onQueryChange,
                   onSearch = {
                       onSearch(query)
                       expanded = false
                   },
                    expanded = expanded,
                    onExpandedChange = {expanded = it},
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    colors = SearchBarDefaults.inputFieldColors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            LazyColumn{
                items(count = searchResults.size){index ->
                    val resultText = searchResults[index]
                    ProductListItem(resultText, "Active","ksh 150")
                }
            }
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
    Surface(modifier = Modifier.clickable{}.padding(horizontal = 16.dp, vertical = 8.dp),
        color = Color.White.copy(alpha = 1f),
        shape = RoundedCornerShape(8.dp)){
        Row (){
            Column(modifier = modifier.padding(8.dp)){
                Text(text = itemName)
                Text(text = itemPrice)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = itemDescription, modifier = Modifier.padding(8.dp))
        }
    }
}
