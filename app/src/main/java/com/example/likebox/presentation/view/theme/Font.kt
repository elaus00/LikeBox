package com.example.likebox.presentation.view.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import com.example.likebox.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val SofiaSans = FontFamily(
    Font(
        googleFont = GoogleFont("Sofia Sans"),
        fontProvider = provider,
        weight = FontWeight.Thin
    ),
    Font(
        googleFont = GoogleFont("Sofia Sans"),
        fontProvider = provider,
        weight = FontWeight.ExtraLight
    ),
    Font(
        googleFont = GoogleFont("Sofia Sans"),
        fontProvider = provider,
        weight = FontWeight.Light
    ),
    Font(
        googleFont = GoogleFont("Sofia Sans"),
        fontProvider = provider,
        weight = FontWeight.Normal
    ),
    Font(
        googleFont = GoogleFont("Sofia Sans"),
        fontProvider = provider,
        weight = FontWeight.Medium
    ),
    Font(
        googleFont = GoogleFont("Sofia Sans"),
        fontProvider = provider,
        weight = FontWeight.SemiBold
    ),
    Font(
        googleFont = GoogleFont("Sofia Sans"),
        fontProvider = provider,
        weight = FontWeight.Bold
    ),
    Font(
        googleFont = GoogleFont("Sofia Sans"),
        fontProvider = provider,
        weight = FontWeight.ExtraBold
    ),
    Font(
        googleFont = GoogleFont("Sofia Sans"),
        fontProvider = provider,
        weight = FontWeight.Black
    )
)