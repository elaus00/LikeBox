package com.example.likebox.ui.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.likebox.R
import com.example.likebox.ui.navigation.Screen
import com.example.likebox.ui.theme.RegisterButton

@Composable
fun SignInScreen(
    onNavigateToSignUp: () -> Unit,
    onSignInSuccess: () -> Unit,        
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xfff7f7f7))
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        BackArrow()
        Spacer(modifier = Modifier.height(40.dp))
        TitleSection(titleText = "Login")
        Spacer(modifier = Modifier.height(20.dp))
        SignInOptions(onSignInSuccess, navController)
        Spacer(modifier = Modifier.height(40.dp))
        DividerWithText("or continue with")
        Spacer(modifier = Modifier.height(20.dp))
        SocialLoginOptions()
        Spacer(modifier = Modifier.height(20.dp))
        TextButton("Create an account", onNavigateToSignUp)
    }
}

@Composable
fun BackArrow() {
    Image(
        painter = painterResource(id = R.drawable.back_arrow),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    )
}

@Composable
fun SignInOptions(onSignInSuccess: () -> Unit, navController: NavController) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        InputGroup()
        InputField("Email Address", "likebox@example.com")
        InputField("Password", "Password")
        RegisterButton(navController = navController, text = "Login", route = Screen.Main.Home.route)
        TextButton("Forgot Password?", {} )
    }
}

@Composable
fun InputGroup() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
        modifier = Modifier
            .width(325.dp)
            .height(54.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .border(width = 1.5.dp, color = Color(0xFF888888))
                .width(162.5.dp)
                .height(54.dp)
                .padding(start = 0.dp, top = 15.dp, end = 0.dp, bottom = 15.dp)
        ) {
            Text(
                text = "Email",
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF888888),
                ),
                modifier = Modifier
                    .width(44.dp)
                    .height(24.dp)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .border(width = 1.5.dp, color = Color(0xFF888888))
                .width(162.5.dp)
                .height(54.dp)
                .padding(start = 0.dp, top = 15.dp, end = 0.dp, bottom = 15.dp)
        ) {
            Text(
                text = "Phone Number",
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFF93C58),
                ),
                modifier = Modifier
                    .width(123.dp)
                    .height(24.dp)
            )
        }
    }
}

@Composable
fun InputField(label: String, placeholder: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xffdcdbea), RoundedCornerShape(8.dp))
            .padding(15.dp)
    ) {
        Text(
            text = label,
            color = Color(0xff8784ac),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = placeholder,
            color = Color(0xff8784ac),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun DividerWithText(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        DividerLine()
        Text(
            text = text,
            color = Color(0xa57d7d7d),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        DividerLine()
    }
}

@Composable
fun DividerLine() {
    Box(
        modifier = Modifier
            .height(1.dp)
            .background(Color(0xff000000))
    )
}

@Composable
fun SocialLoginOptions() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        SocialLoginButton("Continue with Apple", R.drawable.apple_logo)
        SocialLoginButton("Continue with Google", R.drawable.google_logo)
    }
}

@Composable
fun SocialLoginButton(text: String, iconRes: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xffe6e6e6), RoundedCornerShape(30.dp))
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.size(23.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            color = Color(0xff3c3c3c),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TextButton(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        color = Color(0xffff183a),
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
    )
}
