package com.example.myprojectv2;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.widget.CheckBox;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {



    TextView facebook1,  facebook2, facebook3;
    String facebooksec, facebookmin, facebookhour, starti, firstboot;


    public CheckBox check;
    CompoundButton start;
    SharedPreferences spf;
    int z;

    int currentapiVersion = android.os.Build.VERSION.SDK_INT;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("CheckBox");


        facebook1 = (TextView) findViewById(R.id.facebook1);
        facebook2 = (TextView) findViewById(R.id.facebook2);
        facebook3 = (TextView) findViewById(R.id.facebook3);
       //Обработчик checkbox
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

            start = (Switch) findViewById(R.id.start);

        } else {

            start = (CheckBox) findViewById(R.id.start);

        }
        load();

        	start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

                if (arg1 == true) {

                    save("start", "true");

                    if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP
                            || currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {

                        startService(new Intent(MainActivity.this,
                                LollipopService.class));

                    } else {

                        startService(new Intent(MainActivity.this,
                                ServiceSocial.class));

                    }

                    Toast.makeText(getApplicationContext(),
                            "MyProject started", Toast.LENGTH_SHORT)
                            .show();


                } else {

                    save("start", "false");


                    if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP
                            || currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {

                        stopService(new Intent(MainActivity.this,
                                LollipopService.class));

                    } else {

                        stopService(new Intent(MainActivity.this,
                                ServiceSocial.class));

                    }

                }

            }
        });

    }
    //Преобразует SharedPreferences в строки
    public void save(String key, String value) {

        spf = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(key, value);
        edit.commit();

    }

    public void load() {

        spf = PreferenceManager.getDefaultSharedPreferences(this);
        String fbcheck = spf.getString("facebook", "true");
        starti = spf.getString("start", "false");

        if (fbcheck.isEmpty()) {

            save("facebook", "true");

        }
        if (starti.equals("true") && isMyServiceRunning(ServiceSocial.class)
                || isMyServiceRunning(LollipopService.class)
                && starti.equals("true")) {

            start.setChecked(true);

        } else {
            start.setChecked(false);

        }

        facebooksec = spf.getString("facebooksec", "00");
        if (facebooksec.isEmpty()) {
            save("facebooksec", "00");
        } else {
            facebook3.setText(facebooksec);
        }

        facebookmin = spf.getString("facebookmin", "00");
        if (facebookmin.isEmpty()) {
            save("facebookmin", "00");
        } else {
            facebook2.setText(facebookmin);
        }

        facebookhour = spf.getString("facebookhour", "00");
        if (facebookhour.isEmpty()) {
            save("facebookhour", "00");
        } else {
            facebook1.setText(facebookhour);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}