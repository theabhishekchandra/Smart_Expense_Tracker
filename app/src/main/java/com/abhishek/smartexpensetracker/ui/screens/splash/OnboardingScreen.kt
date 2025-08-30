@file:OptIn(ExperimentalPagerApi::class)

package com.abhishek.smartexpensetracker.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.abhishek.smartexpensetracker.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val pages = listOf(
        OnboardingPage(
            title = "Track Expenses Easily",
            description = "Keep your expenses organized with ease.",
            image = R.drawable.ic_apple, // replace with your illustration
        ),
        OnboardingPage(
            title = "Smart AI Insights",
            description = "Get AI powered insights and suggestions.",
            image = R.drawable.ic_facebook // replace with your illustration
        ),
        OnboardingPage(
            title = "Switch Personal & Business Mode",
            description = "Use one app for both personal and business.",
            image = R.drawable.ic_google // replace with your illustration
        ),
        OnboardingPage(
            title = "Stay in Control",
            description = "Never miss a due date with reminders.",
            image = R.drawable.ic_upi // replace with your illustration
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Skip Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onFinish) {
                Text("Skip", color = Color.Gray)
            }
        }

        // Pager
        HorizontalPager(
            count = pages.size,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageUI(page = pages[page])
        }

        // Dots + Next/Finish
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pagerState.currentPage != 0) {
                Button(
                    onClick = {
                        if (pagerState.currentPage > 0) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    },
                    shape = CircleShape
                ) {
                    Text(
                        text = "Back",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                activeColor = MaterialTheme.colorScheme.primary,
                inactiveColor = Color.Gray,
                indicatorWidth = 10.dp,
                spacing = 6.dp
            )

            Button(
                onClick = {
                    if (pagerState.currentPage + 1 < pages.size) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        onFinish()
                    }
                },
                shape = CircleShape
            ) {
                Text(
                    text = if (pagerState.currentPage == pages.lastIndex) "Finish" else "Next",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun OnboardingPageUI(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = page.image),
            contentDescription = page.title,
            modifier = Modifier.size(220.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = page.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = page.description,
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp),
            lineHeight = 20.sp
        )
    }
}

data class OnboardingPage(
    val title: String,
    val description: String,
    val image: Int
)

@Preview(showBackground = true)
@Composable
private fun PreviewOnboardingScreen() {
    OnboardingScreen(onFinish = {})
}