package com.example.likebox.presentation.view.screens.auth.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.likebox.presentation.view.theme.PretendardFontFamily
import com.example.likebox.presentation.view.theme.mainColor

@Composable
fun VerificationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.length <= 6 && it.all { char -> char.isDigit() }) onValueChange(it) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = mainColor,
            unfocusedBorderColor = Color(0xFFDCDBEA)
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(8.dp)
    )
}
@Composable
fun VerificationResendButton(
    onClick: () -> Unit,
    remainingTime: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.clickable(enabled = remainingTime == 0) { onClick() },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Resend code",
            color = if (remainingTime == 0) mainColor else Color.Gray,
            fontSize = 14.sp,
            fontFamily = PretendardFontFamily
        )
        if (remainingTime > 0) {
            Text(
                text = "($remainingTime)",
                color = Color.Gray,
                fontSize = 14.sp,
                fontFamily = PretendardFontFamily
            )
        }
    }
}