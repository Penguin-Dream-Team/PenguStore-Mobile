package store.pengu.mobile.utils

import android.graphics.Color
import android.view.LayoutInflater
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import store.pengu.mobile.R
import kotlin.math.roundToInt

@Composable
private fun StarLabeled(amount: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
    ) {
        Icon(imageVector = Icons.Filled.Star, contentDescription = "$amount star")
        Text(
            "$amount",
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

private class RatingFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return when (value) {
            0f -> ""
            else -> "${value.roundToInt()}"
        }
    }
}

@Composable
fun Histogram(ratings: List<Int>) {
    val stars = stringResource(R.string.stars)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(200.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 28.dp)
        ) {
            repeat(5) {
                StarLabeled(amount = 5 - it)
            }
        }

        AndroidView(
            factory = {
                LayoutInflater.from(it).inflate(R.layout.histogram_layout, null)
            },
            modifier = Modifier
        ) { inflatedLayout ->
            val barChart = inflatedLayout.findViewById<HorizontalBarChart>(R.id.barChart)

            val barEntries = arrayListOf<BarEntry>()
            val missingRatings = mutableListOf(1, 2, 3, 4, 5)
            ratings.groupingBy { it }.eachCount().forEach { rating ->
                missingRatings.remove(rating.key)
                barEntries.add(BarEntry(rating.key.toFloat(), rating.value.toFloat()))
            }

            missingRatings.forEach {
                barEntries.add(BarEntry(it.toFloat(), 0f))
            }

            val barDataSet = BarDataSet(barEntries, stars)
            barDataSet.colors = mutableListOf(
                ContextCompat.getColor(inflatedLayout.context, android.R.color.holo_green_light),
                Color.parseColor("#F9DB22"),
                ContextCompat.getColor(inflatedLayout.context, android.R.color.holo_green_dark),
                Color.parseColor("#FFA500"),
                Color.parseColor("#FF4500"),
            )
            barDataSet.setDrawValues(true)

            val barData = BarData(barDataSet)
            barData.setDrawValues(true)
            barData.setValueFormatter(RatingFormatter())
            barData.setValueTextSize(18f)
            barData.setValueTextColor(Color.parseColor("#222222"))
            barData.isHighlightEnabled = false
            barData.barWidth = 0.9f
            barChart.data = barData
            barChart.description.isEnabled = false
            barChart.setDrawBorders(false)
            barChart.setDrawGridBackground(false)
            barChart.setDrawValueAboveBar(false)
            barChart.isHighlightFullBarEnabled = false
            barChart.xAxis.setDrawGridLines(false)
            barChart.axisLeft.setDrawGridLines(false)
            barChart.axisRight.setDrawGridLines(false)
            barChart.axisRight.setDrawLimitLinesBehindData(false)
            barChart.axisLeft.setDrawLabels(false)
            barChart.axisRight.setDrawLabels(false)
            barChart.xAxis.setDrawLabels(false)
            barChart.xAxis.setDrawLimitLinesBehindData(false)
            barChart.legend.isEnabled = false
            barChart.animateXY(300, 300)
            barChart.setFitBars(true)
            barChart.setPinchZoom(false)
            barChart.isDoubleTapToZoomEnabled = false
            barChart.isClickable = false
            barChart.isDragEnabled = false
            barChart.setBorderWidth(0f)
            barChart.invalidate()
        }
    }
}
