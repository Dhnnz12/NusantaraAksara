package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusantaraaksara.data.SessionManager
import com.example.nusantaraaksara.ui.theme.BrownDusk
import com.example.nusantaraaksara.ui.theme.GoldenHeritage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(onBack: () -> Unit) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val sessionManager = SessionManager(LocalContext.current)
    val currentUserId = sessionManager.getUserId()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Ganti Kata Sandi", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BrownDusk)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Buat kata sandi baru yang kuat untuk melindungi akun Anda.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            // Password Lama
            PasswordField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                label = "Kata Sandi Lama",
                visible = passwordVisible,
                onToggleVisibility = { passwordVisible = !passwordVisible }
            )

            // Password Baru
            PasswordField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = "Kata Sandi Baru",
                visible = passwordVisible,
                onToggleVisibility = { passwordVisible = !passwordVisible }
            )

            // Konfirmasi Password Baru
            PasswordField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Konfirmasi Kata Sandi Baru",
                visible = passwordVisible,
                onToggleVisibility = { passwordVisible = !passwordVisible }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* Implementasi logic API Update Password di sini */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrownDusk),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Simpan Perubahan", fontWeight = FontWeight.Bold, color = GoldenHeritage)
            }
        }
    }
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visible: Boolean,
    onToggleVisibility: () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = BrownDusk,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            // Hilangkan label di dalam untuk rupa yang lebih minimalis
            placeholder = { Text("Masukkan $label", color = Color.LightGray) },
            leadingIcon = { Icon(Icons.Default.Lock, null, tint = BrownDusk) },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldenHeritage,
                unfocusedBorderColor = Color(0xFFEEEEEE),
                focusedContainerColor = Color(0xFFFBFBFB),
                unfocusedContainerColor = Color.White
            )
        )
    }
}