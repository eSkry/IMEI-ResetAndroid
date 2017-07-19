package com.app.eskry.imeireset;

/**
 * Created by root on 25.03.17.
 */

import android.provider.ContactsContract;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

public class ShellExecuter {

    public ShellExecuter() {

    }

    // EGMR=1,7
    // EGMR=1,10


    public String executer(String numIMEI, String typeEMGR, String dev) {
        StringBuffer output = new StringBuffer();

        Process p = null;
        try {
            p = Runtime.getRuntime().exec("su");
            //p.waitFor();

            String construct_command = "echo 'AT+" + typeEMGR + ",\"" + numIMEI + "\"' > " + dev;

            Log.d("SHELL_COMAND", construct_command);

            DataOutputStream os = new DataOutputStream(p.getOutputStream());

            os.writeBytes(construct_command + "\n");
            //os.writeBytes("echo 'AT+" + typeEMGR + ",\"" + numIMEI + "\"' >/dev/pttycmd1" + "\n");
            //os.writeBytes("echo 'AT +" + typeEMGR + ",\"" + numIMEI + "\"' > " + dev + "\n");
            //os.writeBytes("ls\n");
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

}