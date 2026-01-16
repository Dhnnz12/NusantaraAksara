package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val Poppins = FontFamily(
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_extra_bold, FontWeight.ExtraBold),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_regular, FontWeight.Normal)
    )

    val state by viewModel.state
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    // Effect saat login berhasil
    LaunchedEffect(state.isLoginSuccess) {
        if (state.isLoginSuccess) {
            onLoginSuccess()
            viewModel.resetAuthState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Aksen Atas (Header)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f),
            color = EarthySand.copy(alpha = 0.6f),
            shape = RoundedCornerShape(bottomEnd = 120.dp)
        ) {
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
                .padding(horizontal = 35.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.45f))

            // Input Nama Pengguna
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    localError = null // Reset error saat mengetik
                },
                label = { Text("Nama Pengguna", fontFamily = Poppins) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                singleLine = true,
                isError = localError != null || state.error != null
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input Kata Sandi
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    localError = null // Reset error saat mengetik
                },
                label = { Text("Kata Sandi", fontFamily = Poppins) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                singleLine = true,
                isError = localError != null || state.error != null
            )

            // Area Pesan Error
            val displayError = localError ?: state.error
            AnimatedVisibility(visible = displayError != null) {
                Text(
                    text = displayError ?: "",
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontFamily = Poppins,
                    modifier = Modifier.padding(top = 8.dp, start = 4.dp).fillMaxWidth(),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Tombol Login
            Button(
                onClick = {
                    when {
                        username.isBlank() -> localError = "Username tidak boleh kosong"
                        password.isBlank() -> localError = "Password tidak boleh kosong"
                        else -> viewModel.login(username, password)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrownDusk),
                enabled = !state.isLoading // Disable saat loading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Masuk",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer (Daftar Akun)
            Row(
                modifier = Modifier.padding(bottom = 30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Belum punya akun?",
                    color = Color.Gray,
                    fontFamily = Poppins,
                    fontSize = 14.sp
                )
                TextButton(onClick = onNavigateToRegister) {
                    Text(
                        "Daftar",
                        color = BrownDusk,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}