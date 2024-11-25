package com.swordhealth.catsapp.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Search", fontWeight = FontWeight.Bold) }) }
    ) { padding ->
        // Search bar content
        SearchContent(padding)
    }
}

@Composable
fun SearchContent(padding: PaddingValues) {
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(padding)) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search for items") },
            modifier = Modifier.padding(16.dp)
        )
        // Display search results based on the query
        Text("Search results for: $searchQuery", modifier = Modifier.padding(16.dp))
    }
}