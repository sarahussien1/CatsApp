package com.swordhealth.catsapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.swordhealth.catsapp.R
import com.swordhealth.catsapp.ui.models.CatUI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun CatItem(
    catUI: CatUI,
    onFavoriteToggle: () -> Unit,
    gson: Gson,
    navController: NavController,
    favoriteOnly: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val catJson = gson.toJson(catUI.cat)
                val encodedCatJson = URLEncoder.encode(catJson, StandardCharsets.UTF_8.toString())
                navController.navigate("details/${encodedCatJson}/${catUI
                    .isFavorite.value}/${catUI.favoriteID.value}")
            }
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CardItem(catUI, onFavoriteToggle, favoriteOnly = favoriteOnly)
    }
}

@Composable
fun CardItem(
    catUI: CatUI,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier,
    favoriteOnly: Boolean
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            // Image
            AsyncImage(
                model = catUI.cat.url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )

            // Gradient overlay for text visibility on image
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = 100f
                        )
                    )
            )

            // Title text on top of the image
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = if (catUI.cat.breeds.isNotEmpty()) catUI.cat.breeds.first().name else "",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                if (favoriteOnly) {
                    Text(
                        text = if (catUI.cat.breeds.isNotEmpty()) "${stringResource(R.string.life_span_is)} ${catUI.cat.breeds.first().lifeSpan}" else "",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            }


            // Add to Favorite button
            IconButton(
                onClick = { onFavoriteToggle() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.8f))
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = if (catUI.isFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (catUI.isFavorite.value) stringResource(R.string.remove_from_favorite) else stringResource(R.string.add_to_favorite),
                    tint = if (catUI.isFavorite.value) Color.Red else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}