package com.app.eskry.imeireset;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 26.03.17.
 */

public class ShowSettingsAirplane {

    public static void showDiag(Context context) {
        if (android.os.Build.VERSION.SDK_INT < 17) {
            try {
                // read the airplane mode setting
                boolean isEnabled = Settings.System.getInt(
                        context.getContentResolver(),
                        Settings.System.AIRPLANE_MODE_ON, 0) == 1;

                // toggle airplane mode
                Settings.System.putInt(
                        context.getContentResolver(),
                        Settings.System.AIRPLANE_MODE_ON, isEnabled ? 0 : 1);

                // Post an intent to reload
                Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
                intent.putExtra("state", !isEnabled);
                context.sendBroadcast(intent);
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            try {
                Intent intent = new Intent(android.provider.Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                try {
                    Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    //Toast.makeText(buttonView.getContext(), R.string.not_able_set_airplane, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

}
