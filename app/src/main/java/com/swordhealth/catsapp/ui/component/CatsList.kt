package com.swordhealth.catsapp.ui.component

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.swordhealth.catsapp.utils.Resource
import com.swordhealth.catsapp.viewModels.CatsViewModel
import com.swordhealth.catsapp.viewModels.FavoritesViewModel

@Composable
fun CatsList(
    catViewModel: CatsViewModel = hiltViewModel(),
    favoritesViewModel: FavoritesViewModel,
    padding: PaddingValues = PaddingValues(16.dp),
    context: Context,
    favoritesOnly: Boolean,
    gson: Gson,
    navController: NavController
) {
    val catsUI by catViewModel.cats.collectAsState()
    when (catsUI) {
        is Resource.Loading -> {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is Resource.Success -> {
            catsUI.data?.let { catsUIList ->
                val data = if (favoritesOnly) {
                    catViewModel.filterWithFavorites(catsUIList)
                } else {
                    catsUIList
                }
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(
                        count = data.size,
                    ) { index ->
                        CatItem(
                            catUI = data[index],
                            onFavoriteToggle = { favoritesViewModel.toggleFavorite(data[index]) },
                            gson, navController)
                    }
                }
            }
        }

        is Resource.DataError -> {
            Toast.makeText(
                context,
                catsUI.errorMessage,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    LaunchedEffect(Unit) {
        favoritesViewModel.toggleFavoriteFailure.collect { favFailure ->
            if (!favFailure.isNullOrEmpty()) {
                Toast.makeText(
                    context,
                    favFailure,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    val notifyDataSetChanged by favoritesViewModel.notifyDataSetChanged.collectAsState()
    if (notifyDataSetChanged) {
        catViewModel.notifyDataSetChanged()
    }
}