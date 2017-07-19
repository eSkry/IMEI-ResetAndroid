package com.app.eskry.imeireset;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import java.util.Arrays;

public class mywidget extends AppWidgetProvider {
    final String LOG_TAG = "myLogs";

    final String ACTION_TO_START_WORK = "ACTION_TO_START_WORK";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));

        for (int i : appWidgetIds){
            widgetUpdate(context, appWidgetManager, i);
        }


    }

    protected void widgetUpdate(Context ctx, AppWidgetManager appWidgetManager, int id){
        RemoteViews widgetView = new RemoteViews(ctx.getPackageName(), R.layout.activity_mywidget);

        Intent countIntent = new Intent(ctx, WidgetService.class);
        countIntent.setAction(ACTION_TO_START_WORK);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, id, countIntent, 0);
        widgetView.setOnClickPendingIntent(R.id.imei_widget_bt, pendingIntent);

    }

    @Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);

        if (ACTION_TO_START_WORK.equals(intent.getAction())){
            Log.d(LOG_TAG, "LLOLOLOLLLOLOLOLOL");
        }
        Log.d(LOG_TAG, "LLOLOLOLLLOLOLOLOL");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
    }

}
