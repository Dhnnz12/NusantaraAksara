package com.example.nusantaraaksara.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusantaraaksara.R
import com.example.nusantaraaksara.model.Aksara
import com.example.nusantaraaksara.ui.theme.BrownDusk
import com.example.nusantaraaksara.ui.theme.components.AksaraDropdown
import com.example.nusantaraaksara.ui.theme.viewmodel.AksaraViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransliterasiScreen(viewModel: AksaraViewModel, onBack: () -> Unit) {
    var inputText by remember { mutableStateOf("") }
    val listAksara by viewModel.aksaraList
    var selectedAksara by remember { mutableStateOf<Aksara?>(null) }
    val hasil by viewModel.hasilTransliterasi

    val Poppins = FontFamily(
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_medium, FontWeight.Medium)
    )

    Scaffold(
        // Menggunakan topBar agar header benar-benar di atas dan rapi
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                color = BrownDusk,
                shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp),
                shadowElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Transliterasi",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFBFBFB))
                .padding(innerPadding) // Menghilangkan space putih liar
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Pilih Aksara Target", fontFamily = Poppins, fontWeight = FontWeight.Bold, color = BrownDusk)
            Spacer(modifier = Modifier.height(8.dp))

            AksaraDropdown(
                options = listAksara,
                onSelected = { aksara ->
                    selectedAksara = aksara
                    inputText = ""
                    viewModel.loadAturan(aksara.id)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Teks Latin", fontFamily = Poppins, fontWeight = FontWeight.Bold, color = BrownDusk)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = inputText,
                onValueChange = {
                    inputText = it
                    viewModel.konversiTeks(it, selectedAksara?.id ?: 1)
                },
                placeholder = { Text("Ketik di sini...", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                // FIX GARIS MERAH: Gunakan OutlinedTextFieldDefaults.colors
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrownDusk,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Hasil (${selectedAksara?.nama ?: "Aksara"})",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                color = BrownDusk
            )
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth().height(180.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(20.dp)) {
                    Text(
                        text = hasil.ifEmpty { "Hasil muncul di sini" },
                        fontSize = if (hasil.isEmpty()) 14.sp else 32.sp,
                        color = if (hasil.isEmpty()) Color.LightGray else BrownDusk,
                        fontFamily = Poppins,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}