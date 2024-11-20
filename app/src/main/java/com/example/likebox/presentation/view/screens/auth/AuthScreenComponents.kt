package com.example.likebox.presentation.view.screens.auth

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.likebox.R
import com.example.likebox.presentation.state.auth.SignInState
import com.example.likebox.presentation.view.theme.PretendardFontFamily
import com.example.likebox.presentation.view.theme.mainColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


@Composable
fun AuthTitle(
    modifier: Modifier = Modifier,
    title: String,
    titleFontWeight: FontWeight = FontWeight(500), // 기본값 500
    subtitleFontWeight: FontWeight = FontWeight.Normal, // 기본값 Normal
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
            fontWeight = titleFontWeight,
            fontSize = 36.sp,
            textAlign = TextAlign.Center,
        )

        subtitle?.let {
            Text(
                text = it,
                color = Color(0xFF202020),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.pretendard)),
                fontWeight = subtitleFontWeight,
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
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(
                if (enabled) mainColor else mainColor.copy(alpha = 0.5f),
                RoundedCornerShape(24.dp)
            )
            .clickable(enabled = enabled && !isLoading, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.pretendard)),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun AuthTextButton(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 16.sp,
    onClick: () -> Unit,
    textColor: Color = mainColor,
) {
    Text(
        text = text,
        color = textColor,
        fontSize = fontSize,
        lineHeight = 21.sp,
        fontFamily = FontFamily(Font(R.font.pretendard)),
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(start = 4.dp)
    )
}

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    error: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Column {
        Text(
            text = label,
            color = Color(0xFF313131).copy(alpha = 0.86f),
            fontSize = 12.sp,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Bold,
            lineHeight = 40.sp
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color(0xFF8885AC),
                    fontSize = 14.sp,
                    fontFamily = PretendardFontFamily
                )
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = mainColor,
                unfocusedBorderColor = Color(0xFFDCDBEA),
                errorBorderColor = Color.Red,
            ),
            isError = error != null,
            trailingIcon = trailingIcon,
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )
        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                fontSize = 12.sp,
                fontFamily = PretendardFontFamily,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun PhoneNumberTextField(
    modifier: Modifier = Modifier,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    countryCode: String = "+82",
    error: String? = null
) {
    Column(modifier = modifier) {
        Text(
            text = "Phone Number",
            color = Color(0xFF313131).copy(alpha = 0.86f),
            fontSize = 12.sp,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Bold,
            lineHeight = 40.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(
                    width = 1.dp,
                    color = if (error != null) Color.Red else Color(0xFFDCDBEA),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            // Country Code Section
            Row(
                modifier = Modifier
                    .weight(0.3f)
                    .clickable { /* TODO: Country Code Selection */ }
                    .padding(vertical = 14.dp, horizontal = 13.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "KR $countryCode",
                    color = Color(0xFF8885AC),
                    fontSize = 14.sp,
                    fontFamily = PretendardFontFamily
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color(0xFF9593A9)
                )
            }

            // Divider
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(Color.Black.copy(alpha = 0.1f))
            )

            // Phone Number Input
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { value ->
                    // 숫자만 허용
                    if (value.all { it.isDigit() }) {
                        onPhoneNumberChange(value)
                    }
                },
                modifier = Modifier
                    .weight(0.7f)
                    .border(0.dp, Color.Transparent),
                placeholder = {
                    Text(
                        text = "Phone number",
                        color = Color(0xFF8885AC),
                        fontSize = 14.sp,
                        fontFamily = PretendardFontFamily
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                ),
                singleLine = true
            )
        }
        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                fontSize = 12.sp,
                fontFamily = PretendardFontFamily,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun AuthToggleButton(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        options.forEach { option ->
            val isSelected = option == selectedOption
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOptionSelected(option) }
                    .padding(top = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = option,
                    fontSize = 18.sp,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) mainColor else Color(0xFF888888)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .height(1.5.dp)
                        .fillMaxWidth()
                        .background(
                            if (isSelected) mainColor
                            else Color.Black.copy(0.3f)
                        )
                )
            }
        }
    }
}
@Composable
fun SocialLoginButton(
    text: String,
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFE6E6E6)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                color = Color(0xFF3D3D3D),
                fontSize = 14.sp,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikeBoxTopAppBar(
    title: @Composable () -> Unit = {},
    onBackClick: () -> Unit,
    backgroundColor: Color = Color.Transparent,
    startPadding: Dp = 12.dp
) {
    TopAppBar(
        title = title,
        modifier = Modifier
            .padding(start = startPadding),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor
        ),
        windowInsets = WindowInsets(
            top = 8.dp,
            bottom = 0.dp
        )
    )
}


@Composable
fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    showPassword: Boolean,
    onTogglePassword: () -> Unit,
    label: String = "Password",
    error: String? = null
) {
    AuthTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = label,
        placeholder = "Enter your password",
        isPassword = !showPassword,
        error = error,
        trailingIcon = {
            IconButton(onClick = onTogglePassword) {
                Icon(
                    imageVector = if (showPassword) {
                        Icons.Default.VisibilityOff
                    } else {
                        Icons.Default.Visibility
                    },
                    contentDescription = "Toggle password visibility"
                )
            }
        }
    )
}


@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun CustomSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF363636),
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = snackbarData.visuals.message,
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = PretendardFontFamily,
                modifier = Modifier.weight(1f)
            )
            snackbarData.visuals.actionLabel?.let { actionLabel ->
                TextButton(
                    onClick = { snackbarData.performAction() },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFF93C58)
                    )
                ) {
                    Text(
                        text = actionLabel,
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun PlatformSetupDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onNavigateToSetup: () -> Unit,
    onSkipSetup: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Connect Music Platform",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Text(
                    text = "Would you like to continue to connect your music platforms?",
                    fontFamily = PretendardFontFamily
                )
            },
            confirmButton = {
                TextButton(onClick = onNavigateToSetup) {
                    Text(
                        "Yes",
                        color = Color(0xFFF93C58),
                        fontFamily = PretendardFontFamily
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onSkipSetup) {
                    Text(
                        "Later",
                        fontFamily = PretendardFontFamily
                    )
                }
            },
            modifier = modifier
        )
    }
}