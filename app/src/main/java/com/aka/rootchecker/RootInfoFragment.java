package com.aka.rootchecker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.stericson.RootTools.RootTools;

import java.util.List;

/**
 * Created by akshayaggarwal99 on 12-07-2016.
 */

public class RootInfoFragment extends Fragment {
    TextView rootaccess, busybox, busybox_ver, path_tv, super_user;
    Tracker mTracker;
    private static final String TAG = "RootInfoFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App application = (App) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_root_info, container, false);
        rootaccess = (TextView) v.findViewById(R.id.tv_root_access);
        busybox = (TextView) v.findViewById(R.id.tv_busy_box);
        busybox_ver = (TextView) v.findViewById(R.id.tv_busy_box_ver);
        path_tv = (TextView) v.findViewById(R.id.tv_path);
        super_user = (TextView) v.findViewById(R.id.tv_super_user);
        List<String> path;
        path = RootTools.getPath();
      /*  int n = path.size();
        String path_txt = null;
        for(int i=1;i<n;i++){
            path_txt.concat(path.get(i)+",");
        }
        path_txt.concat(path.get(n));*/
        path_tv.setText(path.toString());
        if (RootTools.isProcessRunning("su")) {
            super_user.setTextColor(Color.parseColor("#11ad12"));
            super_user.setText("SU Found");
        } else {
            super_user.setTextColor(Color.parseColor("#FF332C"));
            super_user.setText("SU Not Found");
        }
        if (RootTools.isRootAvailable()) {
            //  rootStatus.setTextColor(Color.parseColor("#11ad12"));       //green color
            rootaccess.setTextColor(Color.parseColor("#11ad12"));
            // rootStatus.setText("This Device is Rooted");
            if (RootTools.isAccessGiven()) {
                rootaccess.setText("Granted");
            }
            if (RootTools.isBusyboxAvailable()) {
                busybox.setTextColor(Color.parseColor("#11ad12"));
                busybox.setText("Installed");
                busybox_ver.setText(RootTools.getBusyBoxVersion());
            } else {
                busybox.setTextColor(Color.parseColor("#FF332C"));          //Red color
                busybox_ver.setTextColor(Color.parseColor("#FF332C"));
                busybox_ver.setText("Nil");
                busybox.setText("Not Installed");
            }
        } else {
            //rootStatus.setTextColor(Color.parseColor("#FF332C"));
            rootaccess.setTextColor(Color.parseColor("#FF332C"));
            // rootStatus.setText("This Device is Rooted");
            rootaccess.setText("Not Rooted");
            busybox_ver.setTextColor(Color.parseColor("#FF332C"));
            busybox.setTextColor(Color.parseColor("#FF332C"));
            //rootStatus.setText("Device is Not Rooted");
            busybox_ver.setText("Not Installed");
            busybox.setText("Not Installed");

        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        // [START screen_view_hit]
        Log.i(TAG, "Setting screen name: ");
        mTracker.setScreenName("root_info_fragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        // [END screen_view_hit]

    }
}
