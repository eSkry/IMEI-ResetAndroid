//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.

//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package com.app.eskry.imeireset;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by root on 27.03.17.
 */

public class Widget extends AppWidgetProvider {
    public final static String STOP_WIDGET_ANIMATION = "ru.ztrap.STOP_WIDGET_ANIMATION";
    public final static String FORCE_WIDGET_UPDATE = "ru.ztrap.FORCE_WIDGET_UPDATE";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, id);
        }
    }

    //вызывается каждый раз, при отлове broadcast'a с установленным для виджета фильтром
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        //Проверка action и при соответствии - выполнение вашего кода
        if (FORCE_WIDGET_UPDATE.equals(intent.getAction())) {
            int widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int[] allWidgetIds = { widgetID };
            Intent i = new Intent(context, YourService.class);
            i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

            String imei1 = MainActivity.generateIMEI();
            String imei2 = MainActivity.generateIMEI();

            ShellExecuter exe = new ShellExecuter();

            exe.executer(imei1, "EGMR=1,7", "/dev/radio/pttycmd1");
            exe.executer(imei2, "EGMR=1,10", "/dev/radio/pttycmd1");

            AirplaneController.executer();

            context.startService(i);
        }
        if (STOP_WIDGET_ANIMATION.equals(intent.getAction())) {
            Log.d("AAAASS", "fffffffffffffffffffffffffff");

        }
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetID) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_mywidget);
        Intent updateIntent = new Intent(context, Widget.class);
        updateIntent.setAction(FORCE_WIDGET_UPDATE);
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID); // id виджета, который от которого будет послан broadcast
        int[] allWidgetIds = {widgetID}; // id виджетов, которые необходимо будет обновить
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, widgetID, updateIntent, 0); // создаём PendingIntent который будет отправляться при нажатии
        views.setOnClickPendingIntent(R.id.imei_widget_bt, pIntent); // id кнопки, по нажатию на которую будет отправлен broadcast
        appWidgetManager.updateAppWidget(widgetID, views);
    }
}
