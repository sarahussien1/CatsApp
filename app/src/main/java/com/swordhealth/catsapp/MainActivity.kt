package com.swordhealth.catsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swordhealth.catsapp.ui.component.CatsScreen
import com.swordhealth.catsapp.ui.component.MainScreen
import com.swordhealth.catsapp.ui.theme.CatsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatsAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        catViewModel = hiltViewModel(),
                        favoritesViewModel = hiltViewModel(),
                        context = this
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    //TODO: fix the problem here
//    CatsAppTheme {
//        CatsScreen(
//            catViewModel = hiltViewModel(),
//            favoriteViewModel = hiltViewModel(),
//            Modifier.padding(10.dp),
//            getApplicationContext()
//        )
//    }
}