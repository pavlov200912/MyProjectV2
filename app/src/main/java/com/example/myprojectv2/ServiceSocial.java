package com.example.myprojectv2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;

/**
 * Created by пк on 01.05.2017.
 */
public class ServiceSocial extends Service{

    private Handler mainhandler;
    private Handler customHandlerfacebook = new Handler();
    Boolean facebook;
    SharedPreferences spf;
    int f, t, tu, w, i, k, b, a, s, km;

    int x = 60;

    int y = 3600;



    public static int facebooktemp1, facebooktemp2, facebooktemp3;
    public static String fbcheck, starti, fb1, fb2, fb3 , not,clear,packageName,start;
    @Override
    public IBinder onBind(Intent arg0) {

        return null;
    }

    public void load() {

        fbcheck = spf.getString("facebook", "true");
        starti = spf.getString("start", "false");

        fb1 = spf.getString("facebooksec", "0");
        fb2 = spf.getString("facebookmin", "0");
        fb3 = spf.getString("facebookhour", "0");

        facebooktemp1 = Integer.parseInt(fb3);
        facebooktemp2 = Integer.parseInt(fb2);
        facebooktemp3 = Integer.parseInt(fb1);

    }
    @Override
    public void onCreate() {
        spf = PreferenceManager.getDefaultSharedPreferences(this);
        not = spf.getString("not", "false");
        clear = spf.getString("clear", "false");

        if (not.isEmpty()) {

            save("not", "false");

        }

        if (clear.isEmpty()) {

            save("clear", "false");

        }


    }


    @SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        spf = PreferenceManager.getDefaultSharedPreferences(this);


        mainhandler = new Handler() {

            @SuppressWarnings("deprecation")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                    if (clear.equals("true")) {

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        String strDate = sdf.format(c.getTime());

                        if (strDate.equals("00:00:00")) {

                            save("facebooksec", "00");
                            save("facebookmin", "00");
                            save("facebookhour", "00");
                        }
                    }

                    load();
                ActivityManager am = (ActivityManager) getApplicationContext()
                        .getSystemService(Activity.ACTIVITY_SERVICE);

                packageName = am.getRunningTasks(1).get(0).topActivity
                        .getPackageName();

                if (starti.equals("true")) {

                    if (fbcheck.equals("true")) {

                        if (packageName.equals("com.facebook.katana")
                                || packageName.equals("com.facebook.orca")
                                || packageName.equals("app.fastfacebook.com")
                                || packageName
                                .equals("com.rapid.facebook.magicdroid")
                                || packageName.equals("com.androdb.fastlitefb")
                                || packageName.equals("com.abewy.klyph_beta")
                                || packageName
                                .equals("uk.co.senab.blueNotifyFree")
                                || packageName
                                .equals("com.platinumapps.facedroid")
                                || packageName.equals("com.spatiolabs.spatio")
                                || packageName.equals("com.for_wd.streampro")) {

                            if (f == 1) {

                            } else {

                                f = 1;
                                facebook = true;

                                customHandlerfacebook.postDelayed(
                                        updateTimerThreadfacebook, 0);
                            }

                        } else {

                            if (f == 1) {

                                facebook = false;

                                customHandlerfacebook
                                        .removeCallbacks(updateTimerThreadfacebook);
                                f = 2;
                            }
                        }
                    } else {

                        if (f == 1) {

                            facebook = false;

                            customHandlerfacebook
                                    .removeCallbacks(updateTimerThreadfacebook);
                            f = 2;
                        }

                    }

                }
            }

        };

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {

                        Thread.sleep(1000);
                        mainhandler.sendEmptyMessage(0);

                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }

                }

            }
        }).start();

        return START_STICKY;
    }

    public void saveint(String key, int integer) {

        spf = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt(key, integer);
        edit.commit();

    }
    public void onDestroy() {
        super.onDestroy();

        String close = spf.getString("close", "no");

        if (start.equals("true") && close.equals("no")) {

            startService(new Intent(ServiceSocial.this, ServiceSocial.class));

        }

    }
    private Runnable updateTimerThreadfacebook = new Runnable() {

        String facesec, facemin, facehour;

        public void run() {

            try {

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (facebook = true) {

                facebooktemp3++;

                if (facebooktemp3 >= 60) {
                    facebooktemp3 = 0;
                    facebooktemp2 += 1;

                }
                if (facebooktemp2 >= 60) {
                    facebooktemp2 = 0;
                    facebooktemp1 += 1;
                }

                facemin = "" + facebooktemp1;
                facesec = "" + facebooktemp3;
                facehour = "" + facebooktemp1;



                if (facebooktemp3 < 10) {

                    facesec = "0" + facebooktemp3;

                }

                if (facebooktemp2 < 10) {

                    facemin = "0" + facebooktemp2;

                }

                if (facebooktemp1 < 10) {

                    facehour = "0" + facebooktemp1;

                }



                save("facebooksec", facesec);
                save("facebookmin", facemin);
                save("facebookhour", facehour);

                customHandlerfacebook.postDelayed(this, 0);

            }
        }

    };

    public void save(String key, String value) {
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(key, value);
        edit.commit();

    }
}
