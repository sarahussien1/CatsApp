package com.swordhealth.catsapp.ui.component

import android.content.Context

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.swordhealth.catsapp.R
import com.swordhealth.catsapp.viewModels.CatsViewModel
import com.swordhealth.catsapp.viewModels.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatsScreen(
    catViewModel: CatsViewModel = hiltViewModel(),
    favoritesViewModel: FavoritesViewModel = hiltViewModel(),
    context: Context,
    navController: NavController,
    gson: Gson
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home), fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { navController.navigate("search") }) {
                        Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search))
                    }
                }
            )
        }
    ) { padding ->
        CatsList(
            catViewModel,
            favoritesViewModel,
            padding,
            context,
            favoritesOnly = false,
            gson,
            navController
        )
    }
}
