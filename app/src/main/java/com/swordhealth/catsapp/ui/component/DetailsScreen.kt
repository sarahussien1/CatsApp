package com.swordhealth.catsapp.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.swordhealth.catsapp.R
import com.swordhealth.catsapp.models.Cat
import com.swordhealth.catsapp.ui.models.CatUI
import com.swordhealth.catsapp.viewModels.CatsViewModel
import com.swordhealth.catsapp.viewModels.FavoritesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController: NavController, cat: Cat, favoritesViewModel: FavoritesViewModel, catViewModel: CatsViewModel, isFavorite: Boolean, favoriteId: Long?) {
    val isFavoriteState = remember { mutableStateOf(isFavorite) }
    val favoriteIDState = remember { mutableStateOf(favoriteId) }
    val catUI = remember {CatUI(cat,isFavoriteState, favoriteIDState)}
    LaunchedEffect(Unit) {
        favoritesViewModel.uiNotifySuccessEvent.collect {
            if (it) {
                catViewModel.getCats()
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.cat_details), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberImagePainter(data = cat.url),
                contentDescription = "Cat Image",
                modifier = Modifier
                    .height(400.dp)
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                Alignment.TopEnd
            ) {
                IconButton(
                    onClick = {
                        favoritesViewModel.toggleFavorite(catUI, notifyUiOnSuccess = true)
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.8f))
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = if (isFavoriteState.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavoriteState.value) stringResource(R.string.remove_from_favorite) else stringResource(R.string.add_to_favorite),
                        tint = if (isFavoriteState.value) Color.Red else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            if (cat.breeds.isNotEmpty()) {
                val breed = cat.breeds.first()
                Text(
                    breed.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h5
                )
                Text(
                    "Origin: ${breed.origin}",
                    style = MaterialTheme.typography.body2
                )
                Text("Temperament: ${breed.temperament}", style = MaterialTheme.typography.body2)
                Text(breed.description, style = MaterialTheme.typography.body1)
            }
        }
    }
}