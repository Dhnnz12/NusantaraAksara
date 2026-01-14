package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusantaraaksara.R
import com.example.nusantaraaksara.ui.theme.BrownDusk
import com.example.nusantaraaksara.ui.theme.GoldenHeritage
import com.example.nusantaraaksara.ui.theme.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    settingsViewModel: SettingsViewModel, // Tambahkan parameter ini
    onBack: () -> Unit
) {
    val Poppins = FontFamily(
        Font(com.example.nusantaraaksara.R.font.poppins_bold, FontWeight.Bold),
        Font(com.example.nusantaraaksara.R.font.poppins_extra_bold, FontWeight.ExtraBold),
        Font(R.font.poppins_medium, FontWeight.Medium)
    )
    // --- STATE TEMA & BAHASA ---
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val currentLang by settingsViewModel.language.collectAsState()

    // Kamus Bahasa Lokal
    val strings = object {
        val title = if (currentLang == "id") "Ganti Kata Sandi" else "Change Password"
        val subtitle = if (currentLang == "id")
            "Buat kata sandi baru yang kuat untuk melindungi akun Anda."
        else "Create a strong new password to protect your account."
        val oldPass = if (currentLang == "id") "Kata Sandi Lama" else "Old Password"
        val newPass = if (currentLang == "id") "Kata Sandi Baru" else "New Password"
        val confirmPass = if (currentLang == "id") "Konfirmasi Kata Sandi Baru" else "Confirm New Password"
        val saveBtn = if (currentLang == "id") "SIMPAN PERUBAHAN" else "SAVE CHANGES"
    }

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(strings.title, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = if (isDark) Color(0xFF1A1614) else BrownDusk
                )
            )
        },
        containerColor = if (isDark) Color(0xFF121212) else Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = strings.subtitle,
                fontSize = 14.sp,
                color = if (isDark) Color.LightGray else Color.Gray,
                fontFamily = Poppins // Pastikan variabel Poppins sudah didefinisikan global
            )

            // Password Lama
            PasswordField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                label = strings.oldPass,
                visible = passwordVisible,
                onToggleVisibility = { passwordVisible = !passwordVisible },
                isDark = isDark
            )

            // Password Baru
            PasswordField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = strings.newPass,
                visible = passwordVisible,
                onToggleVisibility = { passwordVisible = !passwordVisible },
                isDark = isDark
            )

            // Konfirmasi Password Baru
            PasswordField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = strings.confirmPass,
                visible = passwordVisible,
                onToggleVisibility = { passwordVisible = !passwordVisible },
                isDark = isDark
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* Implementasi logic API Update Password */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) GoldenHeritage else BrownDusk,
                    contentColor = if (isDark) BrownDusk else Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = strings.saveBtn,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
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
    onToggleVisibility: () -> Unit,
    isDark: Boolean
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) GoldenHeritage else BrownDusk,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(label, color = Color.Gray) },
            leadingIcon = {
                Icon(Icons.Default.Lock, null, tint = if (isDark) GoldenHeritage else BrownDusk)
            },
            trailingIcon = {
                IconButton(onClick = onToggleVisibility) {
                    val icon = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    Icon(icon, null, tint = Color.Gray)
                }
            },
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(16.dp),
            textStyle = TextStyle(color = if (isDark) Color.White else Color.Black),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isDark) GoldenHeritage else BrownDusk,
                unfocusedBorderColor = if (isDark) Color.DarkGray else Color(0xFFEEEEEE),
                focusedContainerColor = if (isDark) Color(0xFF1E1E1E) else Color(0xFFFBFBFB),
                unfocusedContainerColor = if (isDark) Color(0xFF1A1A1A) else Color.White
            )
        )
    }
}