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

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by root on 27.03.17.
 */

public class YourService extends IntentService {

    public YourService() {
        super("YourService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName thisWidget = new ComponentName(this, Widget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        if (allWidgetIds == null)
            allWidgetIds = appWidgetIds;
        for (int widgetID : allWidgetIds) {
            // здесь делаете то, что вам нужно, после чего отправляете broadcast (если после этих действий вам нужно обновить виджет)

            String imei1 = MainActivity.generateIMEI();
            String imei2 = MainActivity.generateIMEI();

            ShellExecuter exe = new ShellExecuter();

            exe.executer(imei1, "EGMR=1,7", "/dev/radio/pttycmd1");
            exe.executer(imei2, "EGMR=1,10", "/dev/radio/pttycmd1");

            AirplaneController.executer();

            Intent i = new Intent(Widget.STOP_WIDGET_ANIMATION);
            i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
            sendBroadcast(i);
        }
    }

}
