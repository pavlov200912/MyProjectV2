package com.example.myprojectv2;

/**
 * Created by пк on 01.05.2017.
 */
        import java.lang.reflect.Field;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.List;
        import android.annotation.SuppressLint;
        import android.annotation.TargetApi;
        import android.app.ActivityManager;
        import android.app.Service;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.SharedPreferences.Editor;
        import android.os.Build;
        import android.os.Handler;
        import android.os.IBinder;
        import android.os.Message;
        import android.preference.PreferenceManager;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LollipopService extends Service {

    private Handler mainhandler;
    private Handler customHandlerfacebook = new Handler();

    Boolean facebook;


    SharedPreferences spf;

    int f;

    int x = 60;

    int y = 3600;

    public static int facebooktemp1, facebooktemp2, facebooktemp3;


    public static String fbcheck, fb1, fb2, fb3, not, clear, rich, state, totals, start,starti;

    private String[] getLollipop() {

        final int PROCESS_STATE_TOP = 2;

        try {
            Field processStateField = ActivityManager.RunningAppProcessInfo.class
                    .getDeclaredField("processState");

            List<ActivityManager.RunningAppProcessInfo> processes = activityManager()
                    .getRunningAppProcesses();

            for (ActivityManager.RunningAppProcessInfo process : processes) {

                if (
                        process.importance <= ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                                &&
                                process.importanceReasonCode == 0) {

                    int state = processStateField.getInt(process);

                    if (state == PROCESS_STATE_TOP)
                        return process.pkgList;
                }
            }
        } catch (NoSuchFieldException e) {

            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {

        }

        return new String[] {};
    }

    private ActivityManager activityManager() {

        return (ActivityManager) getApplicationContext().getSystemService(
                Context.ACTIVITY_SERVICE);
    }

    @Override
    public IBinder onBind(Intent arg0) {

        return null;
    }



    public void load() {

        fbcheck = spf.getString("facebook", "true");
        rich = spf.getString("rich", "false");
        totals = spf.getString("total", "0.0");

        state = spf.getString("state", "low");

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
        spf = PreferenceManager.getDefaultSharedPreferences(this);

        super.onStartCommand(intent, flags, startId);

        mainhandler = new Handler() {

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


                if (starti.equals("true")) {

                    String[] activePackages = getLollipop();

                    for (String activePackage : activePackages) {

                        if (fbcheck.equals("true")) {

                            if (activePackage.equals("com.facebook.katana")
                                    || activePackage
                                    .equals("com.facebook.orca")
                                    || activePackage
                                    .equals("app.fastfacebook.com")
                                    || activePackage
                                    .equals("com.rapid.facebook.magicdroid")
                                    || activePackage
                                    .equals("com.androdb.fastlitefb")
                                    || activePackage
                                    .equals("com.abewy.klyph_beta")
                                    || activePackage
                                    .equals("uk.co.senab.blueNotifyFree")
                                    || activePackage
                                    .equals("com.platinumapps.facedroid")
                                    || activePackage
                                    .equals("com.spatiolabs.spatio")
                                    || activePackage
                                    .equals("com.for_wd.streampro")
                                    || activePackage.contains("facebook")) {

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
        Editor edit = spf.edit();
        edit.putInt(key, integer);
        edit.commit();

    }

    public void onDestroy() {

        super.onDestroy();

        start = spf.getString("start", "true");

        String close = spf.getString("close", "no");


        if (start.equals("true")  && close.equals("no")  ) {


            startService(new Intent(LollipopService.this, LollipopService.class));

        } else if(start.equals("true") && close.equals("yes") ){



        }else{


        }
    }

    public void save(String key, String value) {

        Editor edit = spf.edit();
        edit.putString(key, value);
        edit.commit();

    }

    private Runnable updateTimerThreadfacebook = new Runnable() {

        public void run() {

            try {

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (facebook = true) {

                facebooktemp3++;

                String facesec, facemin, facehour;

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





};