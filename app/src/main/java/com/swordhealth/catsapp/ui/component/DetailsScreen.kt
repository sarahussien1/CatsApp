package com.swordhealth.catsapp.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.swordhealth.catsapp.models.Cat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController: NavController, cat: Cat) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cat Details", fontWeight = FontWeight.Bold) },
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
            if (cat.breeds.isNotEmpty()) {
                val breed = cat.breeds.first()
                Spacer(Modifier.height(16.dp))
                Text(
                    breed.name,
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