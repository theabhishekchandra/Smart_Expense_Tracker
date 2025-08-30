package com.abhishek.smartexpensetracker.ui.screens.expense

import android.graphics.Paint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*

data class ExpenseCategory(
    val name: String,
    val percentage: Float,
    val color: Color
)

/**
 * Generate distinct colors for a given list size.
 * Uses HSL hue shifting for visually different colors.
 */
fun generateColors(size: Int): List<Color> {
    return List(size) { index ->
        val hue = (index * 360f / size) % 360f
        Color.hsl(hue, 1f, 0.55f) // medium saturation & lightness
    }
}

@Composable
fun ExpenseReportScreen() {
    val totalExpense = 35200

    val categoryData = listOf(
        "My Kharcha App" to 4.3f,
        "Party" to 6.1f,
        "Mother" to 5.3f,
        "Sonutai Marriage" to 38f,
        "Ghar Kharcha" to 17.6f,
        "Petrol" to 0.6f,
        "Gym" to 5.4f,
        "Saloon" to 0.6f,
        "Charity" to 0.6f,
        "Hotel" to 2.0f,
        "Rent" to 19.5f,
    )

    val colors = generateColors(categoryData.size)

    val categories = categoryData.mapIndexed { index, (name, percentage) ->
        ExpenseCategory(name, percentage, colors[index])
    }

    var selectedCategory by remember { mutableStateOf<ExpenseCategory?>(null) }
    var showPercentage by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Total Expense
        Text(
            text = "₹ $totalExpense",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Toggle for Percentage vs Figures
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = showPercentage,
                onClick = { showPercentage = true }
            )
            Text("Percentage")

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = !showPercentage,
                onClick = { showPercentage = false }
            )
            Text("Figures")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Donut Chart
        DonutChart(
            categories = categories,
            totalExpense = totalExpense,
            onSliceClicked = { category ->
                selectedCategory = category
            },
            selectedCategory = selectedCategory,
            showPercentage = showPercentage
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Line Chart
        LineChart(
            categories = categories,
            totalExpense = totalExpense,
            onPointClicked = { category ->
                selectedCategory = category
            },
            selectedCategory = selectedCategory,
            showPercentage = showPercentage
        )
        Spacer(modifier = Modifier.height(16.dp))

        BarChart(
            categories = categories,
            totalExpense = totalExpense,
            onBarClicked = { category -> selectedCategory = category },
            selectedCategory = selectedCategory,
            showPercentage = showPercentage
        )


        Spacer(modifier = Modifier.height(16.dp))

        // Legend
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .padding(end = 8.dp)
                            .background(it.color, RoundedCornerShape(4.dp))
                    )
                    Text(
                        text = "${it.name} (${it.percentage}%)",
                        fontSize = 14.sp,
                        fontWeight = if (selectedCategory == it) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedCategory == it) MaterialTheme.colorScheme.primary else Color.Unspecified
                    )
                }
            }
        }
    }
}

@Composable
fun DonutChart(
    categories: List<ExpenseCategory>,
    totalExpense: Int,
    onSliceClicked: (ExpenseCategory) -> Unit,
    selectedCategory: ExpenseCategory?,
    showPercentage: Boolean
) {
    Canvas(
        modifier = Modifier
            .size(260.dp)
            .pointerInput(true) {
                detectTapGestures { offset ->
                    val center = size.width / 2f
                    val dx = offset.x - center
                    val dy = offset.y - center
                    val angle = (atan2(dy, dx) * (180f / Math.PI)).toFloat() + 180f

                    var startAngle = -90f
                    categories.forEach { category ->
                        val sweep = (category.percentage / 100f) * 360f
                        val endAngle = startAngle + sweep
                        if (angle in startAngle..endAngle) {
                            onSliceClicked(category)
                            return@detectTapGestures
                        }
                        startAngle += sweep
                    }
                }
            }
    ) {
        var startAngle = -90f
        val chartSize = size.minDimension

        categories.forEach { category ->
            val sweep = (category.percentage / 100f) * 360f
            val isSelected = category == selectedCategory
            val pushOut = if (isSelected) 20f else 0f
            val angleRad = Math.toRadians((startAngle + sweep / 2).toDouble())

            translate(
                left = (cos(angleRad) * pushOut).toFloat(),
                top = (sin(angleRad) * pushOut).toFloat()
            ) {
                drawArc(
                    color = category.color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true,
                    size = Size(chartSize, chartSize)
                )

                if (isSelected) {
                    val radius = chartSize / 2f + 30f
                    val textX = (cos(angleRad) * radius + chartSize / 2f)
                    val textY = (sin(angleRad) * radius + chartSize / 2f)

                    val label = if (showPercentage) {
                        "${category.percentage}%"
                    } else {
                        val amount = (totalExpense * category.percentage / 100).roundToInt()
                        "₹$amount"
                    }

                    drawContext.canvas.nativeCanvas.drawText(
                        label,
                        textX.toFloat(),
                        textY.toFloat(),
                        Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = 36f
                            textAlign = Paint.Align.CENTER
                            isFakeBoldText = true
                        }
                    )
                }
            }
            startAngle += sweep
        }

        // Donut hole
        drawCircle(
            color = Color.White,
            radius = chartSize / 7f,
            center = Offset(chartSize / 2f, chartSize / 2f)
        )
    }
}

@Composable
fun LineChart(
    categories: List<ExpenseCategory>,
    totalExpense: Int,
    onPointClicked: (ExpenseCategory) -> Unit,
    selectedCategory: ExpenseCategory?,
    showPercentage: Boolean
) {
    Box(Modifier.border(BorderStroke(2.dp, Color.Black)).padding(15.dp)) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .pointerInput(true) {
                    detectTapGestures { offset ->
                        // Check if user tapped close to any point
                        val chartWidth = size.width
                        val chartHeight = size.height
                        val maxValue = categories.maxOf { it.percentage }

                        val spacingX = chartWidth / (categories.size + 1)
                        categories.forEachIndexed { index, category ->
                            val x = spacingX * (index + 1)
                            val y = chartHeight - (category.percentage / maxValue) * chartHeight

                            val distance = hypot(offset.x - x, offset.y - y)
                            if (distance < 40f) { // tap radius threshold
                                onPointClicked(category)
                                return@detectTapGestures
                            }
                        }
                    }
                }
        ) {
            val chartWidth = size.width
            val chartHeight = size.height
            val maxValue = categories.maxOf { it.percentage }
            val spacingX = chartWidth / (categories.size + 1)

            // Draw lines between points
            var prevX: Float? = null
            var prevY: Float? = null
            categories.forEachIndexed { index, category ->
                val x = spacingX * (index + 1)
                val y = chartHeight - (category.percentage / maxValue) * chartHeight

                if (prevX != null && prevY != null) {
                    drawLine(
                        color = Color.Gray,
                        start = Offset(prevX!!, prevY!!),
                        end = Offset(x, y),
                        strokeWidth = 4f
                    )
                }
                prevX = x
                prevY = y
            }

            // Draw points
            categories.forEachIndexed { index, category ->
                val x = spacingX * (index + 1)
                val y = chartHeight - (category.percentage / maxValue) * chartHeight
                val isSelected = category == selectedCategory

                drawCircle(
                    color = category.color,
                    radius = if (isSelected) 16f else 10f,
                    center = Offset(x, y)
                )

                if (isSelected) {
                    val label = if (showPercentage) {
                        "${category.percentage}%"
                    } else {
                        val amount = (totalExpense * category.percentage / 100).roundToInt()
                        "₹$amount"
                    }

                    drawContext.canvas.nativeCanvas.drawText(
                        label,
                        x,
                        y - 20f,
                        Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = 36f
                            textAlign = Paint.Align.CENTER
                            isFakeBoldText = true
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun BarChart(
    categories: List<ExpenseCategory>,
    totalExpense: Int,
    onBarClicked: (ExpenseCategory) -> Unit,
    selectedCategory: ExpenseCategory?,
    showPercentage: Boolean
) {
    val maxPercentage = categories.maxOf { it.percentage }

    Box(
        Modifier.border(BorderStroke(2.dp, Color.Black)).padding(5.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(8.dp)
                .pointerInput(true) {
                    detectTapGestures { offset ->
                        // Determine which bar was tapped
                        val barWidth = size.width / categories.size
                        val index = (offset.x / barWidth).toInt()
                        if (index in categories.indices) {
                            onBarClicked(categories[index])
                        }
                    }
                }
        ) {
            val barWidth = size.width / categories.size
            categories.forEachIndexed { index, category ->
                val barHeight = (category.percentage / maxPercentage) * size.height
                val left = index * barWidth
                val top = size.height - barHeight
                val right = left + barWidth * 0.7f // spacing between bars

                // Highlight selected bar
                val isSelected = category == selectedCategory
                val shiftY = if (isSelected) 20f else 0f

                // Draw bar
                drawRect(
                    color = category.color,
                    topLeft = Offset(left + barWidth * 0.15f, top - shiftY),
                    size = Size(right - (left + barWidth * 0.15f), barHeight + shiftY)
                )

                if (isSelected) {
                    // Draw value above bar
                    val label = if (showPercentage) {
                        "${category.percentage}%"
                    } else {
                        val amount = (totalExpense * category.percentage / 100).roundToInt()
                        "₹$amount"
                    }

                    drawContext.canvas.nativeCanvas.drawText(
                        label,
                        left + barWidth / 2f,
                        top - 20f,
                        Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = 32f
                            textAlign = Paint.Align.CENTER
                            isFakeBoldText = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SmoothLineChart(
    modifier: Modifier = Modifier,
    data: List<Float>,
    lineColor: Color = Color.Blue
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        if (data.isEmpty()) return@Canvas

        val maxValue = data.maxOrNull() ?: 0f
        val minValue = data.minOrNull() ?: 0f

        val chartHeight = size.height
        val chartWidth = size.width

        val pointSpacing = chartWidth / (data.size - 1)

        val points = data.mapIndexed { index, value ->
            Offset(
                x = index * pointSpacing,
                y = chartHeight - (value - minValue) / (maxValue - minValue) * chartHeight
            )
        }

        val path = Path().apply {
            moveTo(points.first().x, points.first().y)

            for (i in 1 until points.size) {
                val prev = points[i - 1]
                val curr = points[i]

                val midPoint = Offset((prev.x + curr.x) / 2, (prev.y + curr.y) / 2)

                cubicTo(
                    prev.x, prev.y,
                    midPoint.x, midPoint.y,
                    curr.x, curr.y
                )
            }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 6f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewSRep() {
    SmoothLineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp),
        data = listOf(10f, 40f, 30f, 80f, 60f, 90f, 70f),
        lineColor = Color(0xFF6200EE)
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewExpenseRep() {
    ExpenseReportScreen()
}
