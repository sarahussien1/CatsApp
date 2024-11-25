package com.swordhealth.catsapp.ui.component

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.swordhealth.catsapp.utils.Resource
import com.swordhealth.catsapp.viewModels.CatsViewModel
import com.swordhealth.catsapp.viewModels.FavoritesViewModel
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.*
@OptIn(ExperimentalMaterialApi::class)
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
    var isRefreshing by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        catViewModel.uiNotifySuccessEvent.collect {
            if (it) {
                isRefreshing = false
            }
        }
    }
    // Pull refresh state
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            // Trigger a refresh action
            isRefreshing = true
            // Simulate a refresh delay
            catViewModel.getCats()
        }
    )

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
                    catViewModel.filterWithFavorites()
                } else {
                    catsUIList
                }
                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier.padding(padding).pullRefresh(pullRefreshState)
                ) {
                    LazyColumn {
                        items(
                            count = data.size,
                        ) { index ->
                            CatItem(
                                catUI = data[index],
                                onFavoriteToggle = { favoritesViewModel.toggleFavorite(data[index]) },
                                gson, navController, favoritesOnly)
                        }
                    }
                    PullRefreshIndicator(
                        refreshing = isRefreshing,
                        state = pullRefreshState,
                    )
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