package com.swordhealth.catsapp.ui.component

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.swordhealth.catsapp.viewModels.CatsViewModel
import com.swordhealth.catsapp.viewModels.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    catViewModel: CatsViewModel = hiltViewModel(),
    favoritesViewModel: FavoritesViewModel = hiltViewModel(),
    context: Context,
    gson: Gson,
    navController: NavController
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Favorites", fontWeight = FontWeight.Bold) }) }
    ) { padding ->
        CatsList(
            catViewModel,
            favoritesViewModel,
            padding,
            context,
            favoritesOnly = true,
            gson = gson,
            navController = navController
        )
    }
}