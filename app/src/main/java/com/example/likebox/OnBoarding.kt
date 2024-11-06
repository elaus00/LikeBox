package com.example.likebox

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.likebox.ui.theme.LikeBoxTheme
import com.example.likebox.ui.theme.RegisterButton

object Variables {
    val TextPrimary: Color = Color(0xFF202020)
}

@Composable
fun OnBoarding(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color(0xfff7f7f7))
            .padding(30.dp)
    ) {
        ImageSection()
        TitleSection()
        StartOptionsSection(navController = navController)
    }
}

@Composable
fun ImageSection() {
    Box(
        modifier = Modifier
            .padding(0.dp, 160.dp,0.dp,0.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.gallery),
            contentDescription = null,
            modifier = Modifier
                .requiredWidth(575.dp)
        )
    }
}

@Composable
fun TitleSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 115.dp)
    ) {
        Text(
            text = "Welcome to LikeBox",
            color = Color(0xff1f1f1f),
            fontFamily = FontFamily(Font(R.font.pretendard)),
            fontWeight = FontWeight(700),
            fontSize = 36.sp,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "All your favorite songs and playlists in one place",
            color = Variables.TextPrimary,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.pretendard)),
            fontWeight = FontWeight(400),
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TitleSection(
    titleText: String,
    showSubtitle: Boolean = false, // 서브 텍스트 출력 여부
    subtitleText: String = "" // 서브 텍스트 내용
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 20.dp, 0.dp, 40.dp)
    ) {
        Text(
            text = titleText,
            color = Color(0xff1f1f1f),
            fontFamily = FontFamily(Font(R.font.pretendard)),
            fontWeight = FontWeight(500),
            fontSize = 36.sp,
            textAlign = TextAlign.Center,
        )

        if (showSubtitle) {
            Text(
                text = subtitleText,
                color = Variables.TextPrimary,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.pretendard)),
                fontWeight = FontWeight(400),
                textAlign = TextAlign.Center,
                lineHeight = 30.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun StartOptionsSection(
    navController: NavController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 20.dp)
    ) {
        LoginButton(navController = navController)
        CreateAccountLink(navController = navController)
    }
}

@Composable
fun LoginButton(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    RegisterButton(
        navController = navController,
        text = "Login",
        route = Screen.Auth.SignIn.route
    )
}

@Composable
fun CreateAccountLink(
    onClick: @Composable () -> Unit = {}, // 클릭 시 호출할 이벤트를 받음
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Create an account",
        color = Color(0xffff183a),
        fontSize = 16.sp,
        lineHeight = 21.sp,
        fontFamily = FontFamily(Font(R.font.pretendard)),
        fontWeight = FontWeight(200),
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.clickable(onClick = { navController.navigate(Screen.Auth.SignUp.route) }) // 클릭 이벤트 설정
    )
}

@Preview(showBackground = true)
@Composable
fun OnBoardingPreview() {
    LikeBoxTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {}
        }
    }
}
