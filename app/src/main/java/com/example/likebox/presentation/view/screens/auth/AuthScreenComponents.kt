package com.example.likebox.presentation.view.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.likebox.R
import com.example.likebox.presentation.view.theme.mainColor

@Composable
fun AuthTitle(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        Text(
            text = title,
            color = Color(0xff1f1f1f),
            fontFamily = FontFamily(Font(R.font.pretendard)),
            fontWeight = FontWeight.SemiBold,
            fontSize = 36.sp,
            textAlign = TextAlign.Center,
        )

        subtitle?.let {
            Text(
                text = it,
                color = Color(0xFF202020),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.pretendard)),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun AuthButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(mainColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.pretendard)),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun AuthTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    textColor: Color = mainColor,
) {
    Text(
        text = text,
        color = textColor,
        fontSize = 16.sp,
        lineHeight = 21.sp,
        fontFamily = FontFamily(Font(R.font.pretendard)),
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.clickable(onClick = onClick)
    )
}