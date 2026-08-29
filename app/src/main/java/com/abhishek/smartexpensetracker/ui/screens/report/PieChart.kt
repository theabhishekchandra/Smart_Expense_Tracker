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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.ui.theme.AppSpacing

// A small, theme-derived palette shared by the charts in this package so segments/bars/lines
// pull from the active Material 3 color scheme (and therefore adapt to flavor + dark mode)
// instead of using arbitrary hardcoded hex colors.
@Composable
fun chartColorPalette(): List<Color> {
    val scheme = MaterialTheme.colorScheme
    return listOf(
        scheme.primary,
        scheme.tertiary,
        scheme.secondary,
        scheme.error,
        scheme.primaryContainer,
        scheme.tertiaryContainer,
        scheme.secondaryContainer,
    )
}

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
    val barSpacing = AppSpacing.md

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
                    modifier = Modifier.padding(top = AppSpacing.xs)
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
    aiInsights: String = "Your travel expenses are 20% higher this month"
) {
    val palette = chartColorPalette()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppSpacing.md)
    ) {
        Text("Reports & Analytics", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(AppSpacing.md))

        // Pie Chart - Category Breakdown
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(Modifier.padding(AppSpacing.md)) {
                Text("Category Breakdown", style = MaterialTheme.typography.titleMedium)
                PieChart(
                    data = mapOf("Food" to 40f, "Travel" to 25f, "Bills" to 35f),
                    colors = palette,
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

        Spacer(Modifier.height(AppSpacing.md))

        // Bar Chart - Monthly
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(Modifier.padding(AppSpacing.md)) {
                Text("Monthly Expenses", style = MaterialTheme.typography.titleMedium)
                BarChart(
                    data = mapOf("Jan" to 2000f, "Feb" to 3500f, "Mar" to 1800f, "Apr" to 4000f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }

        Spacer(Modifier.height(AppSpacing.md))

        // Line Chart - Trend
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(Modifier.padding(AppSpacing.md)) {
                Text("Spending Trend", style = MaterialTheme.typography.titleMedium)
                LineChart(
                    data = listOf(500f, 1200f, 800f, 2000f, 1800f, 2200f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }

        Spacer(Modifier.height(AppSpacing.md))

        // AI Insights
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                aiInsights,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(AppSpacing.md)
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
