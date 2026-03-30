package com.example.dukatrack.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
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
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)){
            ProductSearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { /* Handle search submission */ },
                searchResults = filteredItems,
                onResultClick = { query = it },
                // Customize appearance with optional parameters
                placeholder = { Text("Search desserts") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = "More options") },
                supportingContent = { Text("Android dessert") },
                leadingContent = { Icon(Icons.Filled.Star, contentDescription = "Starred item") }
            )

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
        modifier
            .fillMaxSize()
            .semantics{isTraversalGroup = true}
    ){
        SearchBar(modifier = Modifier
            .align(Alignment.TopCenter)
            .semantics{traversalIndex = 0f},
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
                    trailingIcon = trailingIcon
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            LazyColumn{
                items(count = searchResults.size){index ->
                    val resultText = searchResults[index]
                    ListItem(
                        headlineContent = {Text(resultText)},
                        supportingContent = supportingContent?.let{{it(resultText)}},
                        leadingContent = leadingContent,
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .clickable {
                                onResultClick(resultText)
                                expanded = false
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
