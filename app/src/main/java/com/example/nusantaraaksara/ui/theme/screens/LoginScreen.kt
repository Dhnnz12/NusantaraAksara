package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusantaraaksara.R
import com.example.nusantaraaksara.ui.theme.BrownDusk
import com.example.nusantaraaksara.ui.theme.EarthySand
import com.example.nusantaraaksara.ui.theme.viewmodel.AuthViewModel

// Gunakan FontFamily yang sudah kita buat sebelumnya
// pastikan val Poppins sudah didefinisikan secara global atau di file ini
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {

    val Poppins = FontFamily( // Ini adalah nama Grupnya
        Font(R.font.poppins_bold, FontWeight.Bold),             // Anggota 1
        Font(R.font.poppins_extra_bold, FontWeight.ExtraBold), // Anggota 2
        Font(R.font.poppins_medium, FontWeight.Medium)         // Anggota 3
    )
    val state by viewModel.state
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(state.isLoginSuccess) {
        if (state.isLoginSuccess) {
            onLoginSuccess() // Fungsi ini HARUS memanggil navController.navigate("dashboard")
            viewModel.resetAuthState()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Aksen Atas
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f),
            color = EarthySand.copy(alpha = 0.6f),
            shape = RoundedCornerShape(bottomEnd = 120.dp)
        ) {
            // Padding di dalam Surface agar teks tidak mepet ke pinggir layar
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 35.dp, bottom = 40.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "Selamat\nDatang",
                    fontSize = 42.sp,
                    lineHeight = 46.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraBold,
                    color = BrownDusk
                )
            }
        }

        // Konten Utama
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 35.dp), // Padding utama sisi kiri & kanan
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Spacer fleksibel untuk mendorong konten ke bawah Surface
            Spacer(modifier = Modifier.fillMaxHeight(0.45f))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nama Pengguna", fontFamily = Poppins) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Kata Sandi", fontFamily = Poppins) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { viewModel.login(username, password) },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrownDusk)
            ) {
                Text("Masuk", fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer
            Row(modifier = Modifier.padding(bottom = 30.dp)) {
                Text("Belum punya akun?", color = Color.Gray, fontFamily = Poppins)
                TextButton(onClick = onNavigateToRegister) {
                    Text("Daftar", color = BrownDusk, fontWeight = FontWeight.Bold, fontFamily = Poppins)
                }
            }
        }
    }
}