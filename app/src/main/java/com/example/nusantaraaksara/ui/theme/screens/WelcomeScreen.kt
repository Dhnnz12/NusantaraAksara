package com.example.nusantaraaksara.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusantaraaksara.R
import com.example.nusantaraaksara.ui.theme.BrownDusk
import com.example.nusantaraaksara.ui.theme.EarthySand
@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val Poppins = FontFamily( // Ini adalah nama Grupnya
        Font(R.font.poppins_bold, FontWeight.Bold),             // Anggota 1
        Font(R.font.poppins_extra_bold, FontWeight.ExtraBold), // Anggota 2
        Font(R.font.poppins_medium, FontWeight.Medium)         // Anggota 3
    )
    // State untuk memicu animasi
    val visible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible.value = true }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Latar belakang tetap sama
        Surface(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.48f),
            color = EarthySand.copy(alpha = 0.6f),
            shape = RoundedCornerShape(bottomStart = 120.dp)
        ) {}

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 35.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.15f))

            // ANIMASI 1: Logo muncul duluan
            androidx.compose.animation.AnimatedVisibility(
                visible = visible.value,
                enter = androidx.compose.animation.fadeIn() + androidx.compose.animation.slideInVertically()
            ) {
                Surface(
                    modifier = Modifier.size(75.dp),
                    shape = RoundedCornerShape(22.dp),
                    color = Color.White,
                    shadowElevation = 10.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(45.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(35.dp))

            // ANIMASI 2: Judul muncul belakangan
            androidx.compose.animation.AnimatedVisibility(
                visible = visible.value,
                enter = androidx.compose.animation.fadeIn(
                    animationSpec = androidx.compose.animation.core.tween(durationMillis = 1000)
                ) + androidx.compose.animation.slideInVertically(
                    initialOffsetY = { 50 }
                )
            ) {
                Column {
                    Text(
                        text = "Nusantara\nAksara",
                        fontSize = 52.sp,
                        lineHeight = 56.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.ExtraBold,
                        color = BrownDusk,
                        letterSpacing = (-2).sp
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.width(4.dp).height(45.dp).background(BrownDusk))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Pelajari ribuan goresan sejarah Nusantara dalam satu genggaman.",
                            fontSize = 17.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray,
                            lineHeight = 24.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // ANIMASI 3: Tombol muncul paling akhir
            androidx.compose.animation.AnimatedVisibility(
                visible = visible.value,
                enter = androidx.compose.animation.fadeIn(
                    animationSpec = androidx.compose.animation.core.tween(delayMillis = 500)
                )
            ) {
                Column {
                    Button(
                        onClick = onNavigateToLogin,
                        modifier = Modifier.fillMaxWidth().height(65.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrownDusk)
                    ) {
                        Text("Mulai Sekarang", fontSize = 18.sp, fontFamily = Poppins, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = onNavigateToRegister,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            "Belum punya akun? Daftar",
                            color = BrownDusk.copy(alpha = 0.8f),
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}