package com.example.weighttracker

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class WeightWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            // 1. Load the last saved weight from local storage
            val prefs = context.getSharedPreferences("WeightPrefs", Context.MODE_PRIVATE)
            val lastWeight = prefs.getString("last_weight", "--")

            // 2. Prepare the view (The UI)
            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            views.setTextViewText(R.id.tv_last_weight, "$lastWeight kg")

            // 3. Create the click listener (Opens the Input Activity)
            val intent = Intent(context, InputActivity::class.java)
            // PendingIntent.FLAG_IMMUTABLE is required for Android 12+
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            views.setOnClickPendingIntent(R.id.widget_container, pendingIntent)

            // 4. Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}