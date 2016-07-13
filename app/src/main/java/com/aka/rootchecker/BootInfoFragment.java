package com.aka.rootchecker;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by akshayaggarwal99 on 12-07-2016.
 */
public class BootInfoFragment extends android.support.v4.app.Fragment {
    TextView device,android,boot_loader,radio,fingerprint,display,hardware,host,cpu_ab1,cpu_ab2,id,board
            ,serial,sdk_int,ker_name,ker_ver,xposed,runtime,debug,manufacturer,user,codename,tag,type,incremental;
    Tracker mTracker;
    private static final String TAG = "BootInfoFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App application = (App) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_build_info, container, false);
        device = (TextView)v.findViewById(R.id.device);
        android = (TextView)v.findViewById(R.id.android);
        fingerprint= (TextView)v.findViewById(R.id.fingerprint);
        boot_loader = (TextView)v.findViewById(R.id.boot_loader);
        radio = (TextView)v.findViewById(R.id.radio);
        sdk_int = (TextView)v.findViewById(R.id.sdk_int);
        ker_name = (TextView)v.findViewById(R.id.ker_name);
        ker_ver = (TextView)v.findViewById(R.id.ker_ver);
        xposed = (TextView)v.findViewById(R.id.xposed);
        runtime = (TextView)v.findViewById(R.id.runtime);
        debug = (TextView)v.findViewById(R.id.debug);
        manufacturer = (TextView)v.findViewById(R.id.manufacturer);
        user = (TextView)v.findViewById(R.id.user);
        board = (TextView)v.findViewById(R.id.board);
        host = (TextView)v.findViewById(R.id.host);
        serial = (TextView)v.findViewById(R.id.serial);
        id = (TextView)v.findViewById(R.id.id);
        hardware = (TextView)v.findViewById(R.id.hardware);
        cpu_ab1 = (TextView)v.findViewById(R.id.cpu_ab1);
        cpu_ab2 = (TextView)v.findViewById(R.id.cpu_ab2);
        display = (TextView)v.findViewById(R.id.display);
        codename = (TextView)v.findViewById(R.id.codename);
        tag = (TextView)v.findViewById(R.id.tags);
        type = (TextView)v.findViewById(R.id.type);
        incremental = (TextView)v.findViewById(R.id.incremental);


        device.setText(Build.MANUFACTURER+" "+Build.MODEL);        //samsung +s3

        fingerprint.setText(Build.FINGERPRINT);                         //Fingerprint
        android.setText(String.format("%s", "Android " + Build.VERSION.RELEASE));     //Android version
        boot_loader.setText(Build.BOOTLOADER);
        final boolean isART;
        final int currentVersion = Build.VERSION.SDK_INT;
        sdk_int.setText(String.valueOf(currentVersion));
        ker_name.setText(System.getProperty("os.name"));        //kernel name
        ker_ver.setText(System.getProperty("os.version"));
        id.setText(String.format(Build.ID));
        host.setText(String.format(Build.HOST));
        hardware.setText(String.format(Build.HARDWARE));
        serial.setText(String.format(Build.SERIAL));
        board.setText(String.format(Build.BOARD));
        display.setText(String.format(Build.DISPLAY));
        cpu_ab1.setText(String .format(Build.CPU_ABI));
        cpu_ab2.setText(String.format(Build.CPU_ABI2));
        user.setText(String.format(Build.USER));
        manufacturer.setText(String.format(Build.MANUFACTURER));
        codename.setText(String .format(Build.VERSION.CODENAME));
        type.setText(String.format(Build.TYPE));
        tag.setText(String.format(Build.TAGS));
        incremental.setText(String.format(Build.VERSION.INCREMENTAL));

        //kernel version
        isART = getIsArtInUse();
        final boolean xposed_installed = appInstalled("de.robv.android.xposed.installer");

        if (xposed_installed){
            xposed.setTextColor(Color.parseColor("#11ad12"));
            xposed.setText("Installed");
        }
        else{
            xposed.setTextColor(Color.parseColor("#FF332C"));
            xposed.setText("Not Installed");
        }
        if(isART){
            runtime.setText("ART");             //For ART
        }
        else{
            runtime.setText("Dalvik");          //For Dalvik
        }
        if(currentVersion>= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            radio.setText(Build.getRadioVersion());             //Radio version after ics
        else{
            radio.setText(Build.RADIO);                     //radio version before ics
        }
        if(Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.ADB_ENABLED, 0) == 1){
            debug.setTextColor(Color.parseColor("#11ad12"));
            debug.setText("ON");
        }else{
            debug.setTextColor(Color.parseColor("#FF332C"));
            debug.setText("OFF");
        }

        return v;
    }
    private boolean getIsArtInUse() {
        final String vmVersion = System.getProperty("java.vm.version");
        return vmVersion != null && vmVersion.startsWith("2");
    }
    private boolean appInstalled(String s) {

        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed = false;

        try{
            pm.getPackageInfo(s,PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;

        }
        return app_installed;
    }
    @Override
    public void onResume() {
        super.onResume();

        // [START screen_view_hit]
        Log.i(TAG, "Setting screen name: ");
        mTracker.setScreenName("boot_info_fragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        // [END screen_view_hit]

    }
}
