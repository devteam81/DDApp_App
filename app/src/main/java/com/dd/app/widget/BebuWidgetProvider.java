package com.dd.app.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dd.app.R;
import com.dd.app.ui.activity.MainActivity;
import com.dd.app.util.UiUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class BebuWidgetProvider extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {
        // Construct the RemoteViews object which defines the view of out widget
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        // Instruct the widget manager to update the widget
        ArrayList<String> widgetData = new ArrayList<>();
        widgetData.add("Harshit");
        widgetData.add("Bheda");
        widgetData.add("Harshit");
        widgetData.add("Bheda");
        widgetData.add("Harshit");
        widgetData.add("Bheda");
        setRemoteAdapter(context, views, widgetData);


        /** PendingIntent to launch the MainActivity when the widget was clicked **/
        Intent launchMain = new Intent(context, MainActivity.class);
        PendingIntent pendingMainIntent = PendingIntent.getActivity(context, 0, launchMain,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget, pendingMainIntent);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widget_listView);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all your widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, BebuWidgetProvider.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widget_listView);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            UiUtils.log("Widget","Received");
            UiUtils.log("Widget","Data: "+ Arrays.toString(intent.getStringArrayListExtra("array").toArray()));
            setRemoteAdapter(context,views,intent.getStringArrayListExtra("array"));
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /** Set the Adapter for out widget **/

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views, ArrayList<String> widgetData) {
        views.setRemoteAdapter(R.id.widget_listView,
                new Intent(context, BebuWidgetService.class)
                        .putStringArrayListExtra("array",widgetData));
    }


    /** Deprecated method, don't create this if you are not planning to support devices below 4.0 **/
    @SuppressWarnings("deprecation")
    private static void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(0, R.id.widget_listView,
                new Intent(context, BebuWidgetService.class));
    }

    public static void sendRefreshBroadcast(Context context, ArrayList<String> widgetData) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, BebuWidgetProvider.class));
        intent.putStringArrayListExtra("array",widgetData);
        context.sendBroadcast(intent);
    }

}