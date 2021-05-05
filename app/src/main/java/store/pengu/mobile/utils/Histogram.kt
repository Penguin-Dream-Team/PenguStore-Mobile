package store.pengu.mobile.utils

import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import store.pengu.mobile.R

@Composable
fun histogram() {
    AndroidView(factory = {
        LayoutInflater.from(it).inflate(R.layout.histogram_layout, null)
    }) { inflatedLayout ->
        val barChart = inflatedLayout.findViewById<BarChart>(R.id.barChart)
        val description = Description()
        description.text = "Test"

        val barEntries = arrayListOf<BarEntry>()
        barEntries.add(BarEntry(0f, 10f))
        barEntries.add(BarEntry(1f, 20f))
        barEntries.add(BarEntry(2f, 30f))
        barEntries.add(BarEntry(3f, 40f))
        barEntries.add(BarEntry(4f, 50f))

        val barDataSet = BarDataSet(barEntries, "Dates")
        barDataSet.color = ContextCompat.getColor(inflatedLayout.context, android.R.color.holo_orange_dark)

        val barData = BarData(barDataSet)
        barData.barWidth = 0.9f
        barChart.data = barData
        barChart.description = description
        barChart.setFitBars(true)
        barChart.setBackgroundColor(ContextCompat.getColor(inflatedLayout.context, android.R.color.darker_gray))
        barChart.invalidate()
    }
}