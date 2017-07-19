package com.app.eskry.imeireset;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView current_imei1, current_imei2, log_text;
    Button bt_ok, bt_gen, bt_bb, bt_airplane;
    EditText ed_imei;
    RadioButton rd_17, rd_110;
    Spinner sp_select;

    ImeiPack imeiPack;

    private String selectStrings[] = {"/dev/radio/pttycmd1", "/dev/pttycmd1", "/dev/smd0", "/dev/smd11" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rd_17 = (RadioButton) findViewById(R.id.rd_bt_17);
        rd_110 = (RadioButton) findViewById(R.id.rd_bt_110);

        rd_17.setOnCheckedChangeListener(myRadioListener);
        rd_110.setOnCheckedChangeListener(myRadioListener);

        current_imei1 = (TextView) findViewById(R.id.current_imei1_text);
        current_imei2 = (TextView) findViewById(R.id.current_imei2_text);
        bt_ok         = (Button) findViewById(R.id.ok_bt);
        bt_gen        = (Button) findViewById(R.id.generate_imei);
        bt_airplane = (Button) findViewById(R.id.airplane_bt);
        ed_imei       = (EditText) findViewById(R.id.edit_txt_imei);
        bt_bb         = (Button) findViewById(R.id.bbbb);
        log_text      = (TextView) findViewById(R.id.log_text);
        sp_select     = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapterSpiner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, selectStrings);
        adapterSpiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_select.setAdapter(adapterSpiner);
        sp_select.setSelection(0);

        bt_ok.setOnClickListener(this);
        bt_gen.setOnClickListener(this);
        bt_bb.setOnClickListener(this);
        bt_airplane.setOnClickListener(this);

        // DEFAULT STATES
        ed_imei.setText(generateIMEI());
        rd_17.setChecked(true);


        setImei();

        ed_imei.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkCorrect();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkCorrect();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkCorrect();
            }
        });

        checkCorrect();
    }

    AdapterView.OnItemSelectedListener spinerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    CompoundButton.OnCheckedChangeListener myRadioListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int id = buttonView.getId();

            if (id == rd_17.getId() && isChecked){
                rd_110.setChecked(false);
                checkCorrect();
            }

            if (id == rd_110.getId() && isChecked){
                rd_17.setChecked(false);
                checkCorrect();
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == bt_bb.getId()){
            setImei();
        }

        if (id == bt_ok.getId()){
            ShellExecuter exe = new ShellExecuter();

            final String gg = "SET IMEI TO";
            Log.d(gg, ed_imei.getText().toString());

            String EGMR = null;

            if (rd_17.isChecked()) EGMR = "EGMR=1,7";
            if (rd_110.isChecked()) EGMR = "EGMR=1,10";

            String aaa = exe.executer(ed_imei.getText().toString(), EGMR, selectStrings[ sp_select.getSelectedItemPosition() ]);

            Toast.makeText(this, "Команда выполнена!", Toast.LENGTH_SHORT).show();

            log_text.setText(aaa);
            Log.d("=====Output Shell=====", aaa);

            //Log.d("OUTPUT SHELL", aaa.output);
        }

        if (id == bt_gen.getId()){
            checkCorrect();
            final String aa = "NEW GEN IMEI";
            String imei = generateIMEI();
            ed_imei.setText(imei);
            Log.d(aa, imei);
        }

        if (id == bt_airplane.getId()){
            log_text.setText( AirplaneController.executer() );




            /*log_text.setText("Если вместо IMEI у вас пишется \"IMEI не был получен\", то скорее всего ваша версия android не поддерживается!\n\n" +
                    "Для приложения необходимы права супер пользователя (root)");*/
            /*if (AirplaneController.airplaneIsEnabled(this) == false){
                AirplaneController.setAirplaneState(this, true);
                //SystemClock.sleep(1000);
                AirplaneController.setAirplaneState(this, false);
            } else {
                AirplaneController.setAirplaneState(this, false);
            }*/

            //boolean state = AirplaneController.airplaneIsEnabled(this);

            //ShowSettingsAirplane.showDiag(this); // INFO: Вызывает диалог настроек с режимом полета

            /*if (state){
                toggleAirplaneMode(0, state);
            } else {
                toggleAirplaneMode(1, state);
            }*/

        }

    }

    private void setImei(){
        imeiPack = ImeiData.getData(this);

        if (imeiPack != null){
            if (imeiPack.first != null){
                current_imei1.setText(imeiPack.first);
            } else {
                current_imei1.setText("IMEI не был получен");
            }

            if (imeiPack.second != null){
                current_imei2.setText(imeiPack.second);
            } else {
                current_imei2.setText("IMEI не был получен");
            }
        }
    }

    private boolean checkCorrect(){
        boolean radioBt = (rd_17.isChecked() || rd_110.isChecked());
        boolean imeiCorrect = (ed_imei.getText().toString().length() == 15);

        if (radioBt && imeiCorrect){
            bt_ok.setEnabled(true);
            return true;
        }

        bt_ok.setEnabled(false);
        return false;
    }

    public static String generateIMEI(){
        String number = new String();
        Random rand = new Random();
        for (int i = 0; i < 15; i++){
            number += Integer.toString(rand.nextInt(10));
        }
        return number;
    }

    private String getDeviceImei(){
        String current_imeiNumber = null;
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); // INFO: Get data phone
        current_imeiNumber = telephonyManager.getDeviceId().toString();
        return current_imeiNumber;
    }

    @SuppressLint("NewApi")
    public void toggleAirplaneMode(int value, boolean state) {
        // toggle airplane mode
        // check the version
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) { // if
            // less
            // then
            // version
            // 4.2

            Settings.System.putInt(getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, value);

        } else {
            Settings.Global.putInt(getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, value);
        }
        // broadcast an intent to inform
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", !state);
        sendBroadcast(intent);
    }
}