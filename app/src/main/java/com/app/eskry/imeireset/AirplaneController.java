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

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 26.03.17.
 */

public class AirplaneController {
    /*
        Данный класс включает и выключает режим полета
     */
    public static final String KEY_LOG = "SHELL_COMMAND";

    public static String executer() {
        StringBuffer output = new StringBuffer();
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(p.getOutputStream());

            os.writeBytes("settings put global airplane_mode_on 1\n");
            Log.d(KEY_LOG, "settings put global airplane_mode_on 1\n");

            //os.writeBytes("am broadcast -a android.intent.action.AIRPLANE_MODE\n");
            //Log.d(KEY_LOG, "am broadcast -a android.intent.action.AIRPLANE_MODE\n");

            os.writeBytes("am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true\n");
            Log.d(KEY_LOG, "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true\n");

            TimeUnit.SECONDS.sleep(1);

            os.writeBytes("settings put global airplane_mode_on 0\n");
            Log.d(KEY_LOG, "settings put global airplane_mode_on 0\n");

            //os.writeBytes("am broadcast -a android.intent.action.AIRPLANE_MODE\n");
            //Log.d(KEY_LOG, "am broadcast -a android.intent.action.AIRPLANE_MODE\n");

            os.writeBytes("am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false\n");
            Log.d(KEY_LOG, "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false\n");

            os.writeBytes("exit\n");
            os.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String result = output.toString();

        return result;
    }

    public static boolean airplaneIsEnabled(Context context){
        boolean isEnabled = Settings.System.getInt( context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1;
        Log.d("AIRPLANE STATE", isEnabled ? "true" : "false");
        return isEnabled;
    }

    public static void setAirplaneState(Context context, boolean state){
        Settings.System.putInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, state ? 1 : 0);

        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", state);
        context.sendBroadcast(intent);
    }


}
