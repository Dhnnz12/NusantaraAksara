package com.example.nusantaraaksara.ui.theme.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
fun RegisterScreen(
    viewModel: AuthViewModel,
    onBackToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val Poppins = FontFamily( // Ini adalah nama Grupnya
        Font(com.example.nusantaraaksara.R.font.poppins_bold, FontWeight.Bold),             // Anggota 1
        Font(com.example.nusantaraaksara.R.font.poppins_extra_bold, FontWeight.ExtraBold), // Anggota 2
        Font(R.font.poppins_medium, FontWeight.Medium)         // Anggota 3
    )
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val state by viewModel.state

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // --- 1. AKSEN GEOMETRIS ATAS (Inversi dari Login Screen) ---
        // --- 1. AKSEN GEOMETRIS ATAS ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.32f), // Sedikit lebih tinggi agar teks punya ruang bernapas
            color = BrownDusk,
            shape = RoundedCornerShape(bottomStart = 100.dp)
        ) {
            // Gunakan Box dengan padding untuk kontrol posisi yang presisi
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 35.dp, bottom = 45.dp), // Bottom padding ditambah agar tidak mepet lengkungan
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = "Daftar\nAkun", // Tambahkan titik untuk kesan desain modern
                    fontSize = 44.sp,
                    lineHeight = 48.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = (-1.5).sp // Spasi antar huruf dirapatkan sedikit agar lebih "Editorial"
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 35.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Spacer untuk memberi ruang bagi Surface di atas
            Spacer(modifier = Modifier.fillMaxHeight(0.35f))

            // --- 2. INPUT FIELDS ---
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Lengkapi data untuk bergabung",
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Nama Pengguna", fontFamily = Poppins) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BrownDusk) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrownDusk,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = BrownDusk
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Alamat Email", fontFamily = Poppins) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = BrownDusk) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrownDusk,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = BrownDusk
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Kata Sandi", fontFamily = Poppins) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = BrownDusk) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrownDusk,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = BrownDusk
                    ),
                    singleLine = true
                )
            }

            // Pesan Error
            state.errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontFamily = Poppins,
                    modifier = Modifier.padding(top = 10.dp).align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // --- 3. REGISTER BUTTON ---
            Button(
                onClick = { viewModel.register(username, email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrownDusk),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        "Daftar Sekarang",
                        fontFamily = Poppins,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- 4. FOOTER ---
            Row(
                modifier = Modifier.padding(bottom = 30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Sudah punya akun?",
                    fontFamily = Poppins,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                TextButton(onClick = onBackToLogin) {
                    Text(
                        "Masuk",
                        fontFamily = Poppins,
                        color = BrownDusk,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }

    LaunchedEffect(state.isRegisterSuccess) {
        if (state.isRegisterSuccess) {
            onBackToLogin()
        }
    }
}