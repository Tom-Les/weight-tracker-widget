package com.example.weighttracker

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class InputActivity : Activity() {

    private val scriptUrl = "https://script.google.com/macros/s/AKfycbwXnD12T_ol0q2-87cy6Ul4BsjppHsnbtpSoN-1RMmHMuRAZ3ZYHEu7ErdpwfTE0vL-bQ/exec"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        val etWeight = findViewById<EditText>(R.id.et_weight)
        val btnSave = findViewById<Button>(R.id.btn_save)

        btnSave.setOnClickListener {
            val weight = etWeight.text.toString()
            if (weight.isNotEmpty()) {
                Toast.makeText(this, "Sending...", Toast.LENGTH_SHORT).show()
                sendToGoogleSheets(weight)
            }
        }
    }

    private fun sendToGoogleSheets(weight: String) {
        thread {
            try {
                val url = URL(scriptUrl)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")

                val jsonPayload = "{\"weight\": \"$weight\"}"
                conn.outputStream.use { it.write(jsonPayload.toByteArray()) }

                val responseCode = conn.responseCode

                runOnUiThread {
                    if (responseCode == 200 || responseCode == 302) {
                        saveLocallyAndUpdateWidget(weight)
                        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
                        finish() // Close the popup
                    } else {
                        Toast.makeText(this, "Error: $responseCode", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun saveLocallyAndUpdateWidget(newWeight: String) {
        // 1. Save locally so the widget updates instantly
        val prefs = getSharedPreferences("WeightPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("last_weight", newWeight).apply()

        // 2. Force the widget to refresh
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val ids = appWidgetManager.getAppWidgetIds(ComponentName(this, WeightWidget::class.java))

        for (id in ids) {
            WeightWidget.updateAppWidget(this, appWidgetManager, id)
        }
    }
}