package store.pengu.mobile.utils

import android.graphics.Color
import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import store.pengu.mobile.R


@Composable
fun Histogram(ratings: List<Int>) {
    AndroidView(factory = {
        LayoutInflater.from(it).inflate(R.layout.histogram_layout, null)
    }) { inflatedLayout ->
        val barChart = inflatedLayout.findViewById<HorizontalBarChart>(R.id.barChart)

        val barEntries = arrayListOf<BarEntry>()
        ratings.groupingBy { it }.eachCount().forEach { rating ->
            barEntries.add(BarEntry(rating.key.toFloat(), rating.value.toFloat()))
        }

        val barDataSet = BarDataSet(barEntries, "Stars")
        barDataSet.colors = mutableListOf (
            Color.parseColor("#FF4500"),
            Color.parseColor("#FFA500"),
            Color.parseColor("#F9DB22"),
            ContextCompat.getColor(inflatedLayout.context, android.R.color.holo_green_light),
            ContextCompat.getColor(inflatedLayout.context, android.R.color.holo_green_dark),
        )
        barDataSet.setDrawValues(true)

        val barData = BarData(barDataSet)
        barData.setDrawValues(true)
        barData.setValueTextSize(20f)
        barData.barWidth = 0.9f
        barChart.data = barData
        barChart.description.isEnabled = false
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
        barChart.animateXY(1000, 1000)
        barChart.setFitBars(true)
        barChart.setBackgroundColor(Color.parseColor("#121212"))
        barChart.invalidate()
    }
}