package org.hsrt.mc.emergency.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.hsrt.mc.emergency.R;
import org.hsrt.mc.emergency.services.SendingService;

/**
 * Created by Fabian on 31.05.2016.
 */
public class AppWidget extends AppWidgetProvider {



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){ // Update Widget
        // context.startService(new Intent(context, SendingService.class));

        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {

            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch Service
            Intent intent = new Intent(context, SendingService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0); // Pending Intent for the sendingService

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setOnClickPendingIntent(R.id.emergencyButton, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
