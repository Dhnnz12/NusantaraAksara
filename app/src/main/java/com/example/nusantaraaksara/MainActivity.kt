package com.example.nusantaraaksara

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.nusantaraaksara.data.RetrofitClient
import com.example.nusantaraaksara.ui.theme.NusantaraAksaraTheme
import com.example.nusantaraaksara.ui.theme.viewmodel.AuthViewModel


class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NusantaraAksaraTheme {
                val navController = rememberNavController()

                // Untuk sementara inisialisasi manual (nanti bisa pakai Dependency Injection)
                val apiService = RetrofitClient.instance
                val authViewModel = AuthViewModel(application, apiService)

                Surface(color = MaterialTheme.colorScheme.background) {
                    NavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        apiService = apiService
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
    NusantaraAksaraTheme {
        Greeting("Android")
    }
}