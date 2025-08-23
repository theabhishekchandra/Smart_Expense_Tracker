package com.abhishek.smartexpensetracker.ui.screens.report

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// ----------------- PIE CHART -----------------

@Composable
fun PieChart(
    data: Map<String, Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val total = data.values.sum()
    var startAngle = -90f

    Canvas(modifier = modifier) {
        data.values.forEachIndexed { index, value ->
            val sweep = (value / total) * 360f
            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = true,
                size = size
            )
            startAngle += sweep
        }
    }
}

// ----------------- BAR CHART -----------------

@Composable
fun BarChart(
    data: Map<String, Float>,
    barColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    val maxValue = data.values.maxOrNull() ?: 1f
    val barSpacing = 16.dp

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        data.forEach { (label, value) ->
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    val barHeight = (value / maxValue) * size.height
                    drawRoundRect(
                        color = barColor,
                        topLeft = Offset(0f, size.height - barHeight),
                        size = Size(size.width, barHeight),
                        cornerRadius = CornerRadius(12f, 12f)
                    )
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.width(barSpacing))
        }
    }
}

// ----------------- LINE CHART -----------------

@Composable
fun LineChart(
    data: List<Float>,
    lineColor: Color = MaterialTheme.colorScheme.secondary,
    modifier: Modifier = Modifier
) {
    val maxValue = data.maxOrNull() ?: 1f

    Canvas(modifier = modifier) {
        val xStep = size.width / (data.size - 1)
        val yScale = size.height / maxValue

        val path = Path()
        data.forEachIndexed { index, value ->
            val x = index * xStep
            val y = size.height - (value * yScale)
            if (index == 0) path.moveTo(x, y)
            else path.lineTo(x, y)
        }
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 6f)
        )
    }
}

// ----------------- REPORTS SCREEN -----------------

@Composable
fun ReportsScreenA(
    isBusinessUser: Boolean = false,
    aiInsights: String = "Your travel expenses are 20% higher this month 🚀"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Reports & Analytics", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(16.dp))

        // Pie Chart - Category Breakdown
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Category Breakdown", style = MaterialTheme.typography.titleMedium)
                PieChart(
                    data = mapOf("Food" to 40f, "Travel" to 25f, "Bills" to 35f),
                    colors = listOf(Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFFF9800)),
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Bar Chart - Monthly
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Monthly Expenses", style = MaterialTheme.typography.titleMedium)
                BarChart(
                    data = mapOf("Jan" to 2000f, "Feb" to 3500f, "Mar" to 1800f, "Apr" to 4000f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Line Chart - Trend
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Spending Trend", style = MaterialTheme.typography.titleMedium)
                LineChart(
                    data = listOf(500f, 1200f, 800f, 2000f, 1800f, 2200f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // AI Insights
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                aiInsights,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

// ----------------- PREVIEWS -----------------

@Preview(showBackground = true)
@Composable
fun PreviewReportsScreen() {
    MaterialTheme {
        ReportsScreenA()
    }
}
