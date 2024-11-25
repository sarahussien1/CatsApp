package com.swordhealth.catsapp.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.swordhealth.catsapp.R
import com.swordhealth.catsapp.ui.models.CatUI
import com.swordhealth.catsapp.viewModels.CatsViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, catsViewModel: CatsViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.search), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                })
        }
    ) { padding ->
        // Search bar content
        SearchContent(padding, catsViewModel, navController)
    }
}

@Composable
fun SearchContent(padding: PaddingValues, catsViewModel: CatsViewModel, navController: NavController) {
    val gson = remember { Gson() }
    var searchQuery by remember { mutableStateOf("") }
    var filteredCats: List<CatUI> by remember { mutableStateOf(listOf()) }
    Column(modifier = Modifier.padding(padding)) {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                filteredCats = catsViewModel.searchByPrefix(searchQuery)
            },
            label = { Text("Search for breeds") },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
        // Display search results based on the query
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                count = filteredCats.size,
            ) { index ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val catJson = gson.toJson(filteredCats[index].cat)
                        val encodedCatJson =
                            URLEncoder.encode(catJson, StandardCharsets.UTF_8.toString())
                        navController.navigate("details/${encodedCatJson}")
                    }
                ) {
                    Text(filteredCats[index].cat.breeds.first().name, Modifier.padding(16.dp))

                }
            }
        }
    }
}