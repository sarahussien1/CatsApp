package com.swordhealth.catsapp.ui.component

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.swordhealth.catsapp.viewModels.CatsViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.swordhealth.catsapp.models.Cat
import com.swordhealth.catsapp.ui.models.CatUI
import com.swordhealth.catsapp.viewModels.FavoritesViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


@Composable
fun MainScreen(catViewModel: CatsViewModel,
               favoritesViewModel: FavoritesViewModel,context: Context) {
    val navController = rememberNavController()
    val gson = remember { Gson() }
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(0.dp)
        ) {
            composable("home") {
                CatsScreen(
                    catViewModel,
                    favoritesViewModel,
                    context = context,
                    gson = gson,
                    navController = navController
                )
            }
            composable("favorites") { FavoritesScreen( catViewModel,
                favoritesViewModel,
                context = context,
                gson = gson,
                navController = navController) }
            composable("search") { SearchScreen() }

            composable(
                "details/{catJson}",
                arguments = listOf(navArgument("catJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val encodedCatJson = backStackEntry.arguments?.getString("catJson")
                val catJson = URLDecoder.decode(encodedCatJson, StandardCharsets.UTF_8.toString())
                val cat = gson.fromJson(catJson, Cat::class.java)
                DetailsScreen(navController, cat)
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("home", "Home", Icons.Default.Home),
        BottomNavItem("favorites", "Favorites", Icons.Default.Favorite)
    )
    BottomNavigation(backgroundColor = Color.White, contentColor = Color.Black) {
        val currentRoute = navController.currentBackStackEntryAsState()?.value?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Avoid building a new instance of the destination if already selected
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

