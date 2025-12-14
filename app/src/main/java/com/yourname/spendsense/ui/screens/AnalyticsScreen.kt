// ===========================================
// ANALYTICS SCREEN WITH PIE CHART
// Location: app/src/main/java/com/yourname/spendsense/ui/screens/AnalyticsScreen.kt
// ===========================================

package com.yourname.spendsense.ui.screens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourname.spendsense.ui.components.categories
import com.yourname.spendsense.ui.theme.Background
import com.yourname.spendsense.ui.theme.Primary
import com.yourname.spendsense.viewmodel.FinanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: FinanceViewModel,
    onNavigateBack: () -> Unit
) {
    val totalExpenses by viewModel.totalExpenses.collectAsState()

    val categoryData = remember(totalExpenses) {
        categories.map { category ->
            CategoryData(
                name = category.name,
                amount = viewModel.getCategoryExpenses(category.name),
                color = category.color,
                icon = category.icon
            )
        }.filter { it.amount > 0 }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analytics", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            if (categoryData.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Spending Distribution",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        PieChart(
                            data = categoryData,
                            totalAmount = totalExpenses
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        CategoryLegend(data = categoryData)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Category Breakdown",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            categories.forEach { category ->
                val categoryAmount = viewModel.getCategoryExpenses(category.name)
                val percentage =
                    if (totalExpenses > 0) ((categoryAmount / totalExpenses) * 100).toInt() else 0

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            category.color.copy(alpha = 0.2f),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = category.icon,
                                        contentDescription = category.name,
                                        tint = category.color
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    category.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "₹${String.format("%.2f", categoryAmount)}",
                                    fontWeight = FontWeight.Bold,
                                    color = category.color
                                )
                                Text("$percentage%", fontSize = 12.sp, color = Color.Gray)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = if (totalExpenses > 0)
                                (categoryAmount / totalExpenses).toFloat()
                            else 0f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = category.color,
                            trackColor = Color.LightGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Primary)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Total Spending This Month", color = Color.White)
                    Text(
                        "₹${String.format("%.2f", totalExpenses)}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// ===========================================
// DATA MODEL
// ===========================================

data class CategoryData(
    val name: String,
    val amount: Double,
    val color: Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

// ===========================================
// PIE CHART
// ===========================================

@Composable
fun PieChart(
    data: List<CategoryData>,
    totalAmount: Double,
    modifier: Modifier = Modifier
) {
    var animationPlayed by remember { mutableStateOf(false) }

    val animateFloat by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(1000, easing = LinearOutSlowInEasing),
        label = "pie_chart_animation"
    )

    LaunchedEffect(Unit) { animationPlayed = true }

    Box(
        modifier = modifier.size(250.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(220.dp)) {
            val radius = size.minDimension / 2
            val center = Offset(size.width / 2, size.height / 2)

            var startAngle = -90f

            data.forEach {
                val sweepAngle =
                    ((it.amount / totalAmount) * 360.0).toFloat() * animateFloat

                drawArc(
                    color = it.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2)
                )

                drawArc(
                    color = Color.White,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = 3f)
                )

                startAngle += sweepAngle
            }
        }

        Canvas(modifier = Modifier.size(120.dp)) {
            drawCircle(Color.White)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Total", fontSize = 14.sp, color = Color.Gray)
            Text(
                "₹${String.format("%.0f", totalAmount)}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

// ===========================================
// LEGEND
// ===========================================

@Composable
fun CategoryLegend(data: List<CategoryData>) {
    Column {
        data.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(it.color, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(it.name, fontSize = 12.sp, color = Color.Gray)
                    }
                }
                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
