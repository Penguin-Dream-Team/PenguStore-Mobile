package store.pengu.mobile.utils.geo

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class GeoTimeTask {
    private var url: String = ""

    fun init(mContext: Context) {
        val apiKey: String?
        mContext.packageManager.getApplicationInfo(mContext.packageName, PackageManager.GET_META_DATA)
            .apply {
                apiKey = metaData.getString("com.google.android.geo.API_KEY")
            }
        url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=source&destinations=destiny&mode=driving&language=fr-FR&avoid=tolls&key=${apiKey}"
    }

    fun getTime(source: String, destiny: String): String? {
        try {
            url = url.replace("source", source)
                .replace("destiny", destiny)
            Log.d("URL", url)

            val con = URL(url).openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.connect()
            val statusCode = con.responseCode
            if (statusCode == HttpURLConnection.HTTP_OK) {
                val br = BufferedReader(InputStreamReader(con.inputStream))
                val sb = StringBuilder()
                var line = br.readLine()
                while (line != null) {
                    sb.append(line)
                    line = br.readLine()
                }
                val json = sb.toString()
                Log.d("JSON", json)
                val root = JSONObject(json)
                val arrayRows = root.getJSONArray("rows")
                Log.d("JSON", "array_rows:$arrayRows")
                val objectRows = arrayRows.getJSONObject(0)
                Log.d("JSON", "object_rows:$objectRows")
                val arrayElements = objectRows.getJSONArray("elements")
                Log.d("JSON", "array_elements:$arrayElements")
                val objectElements = arrayElements.getJSONObject(0)
                Log.d("JSON", "object_elements:$objectElements")
                val objectDuration = objectElements.getJSONObject("duration")
                val objectDistance = objectElements.getJSONObject("distance")
                Log.d("JSON", "object_duration:$objectDuration")
                return objectDuration.getString("value") + "," + objectDistance.getString("value")
            }
        } catch (e: MalformedURLException) {
            e.message?.let { Log.d("error", it) }
        } catch (e: IOException) {
            e.message?.let { Log.d("error", it) }
        } catch (e: JSONException) {
            e.message?.let { Log.d("error", it) }
        }
        return null
    }
}