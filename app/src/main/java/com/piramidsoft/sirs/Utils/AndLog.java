package com.piramidsoft.sirs.Utils;

import android.util.Log;

/**
 * Created by Tambora on 02/09/2016.
 */
public class AndLog {


    private static boolean hide = false;

    public static void ShowLog(String tag, String message) {
        if (!hide) {

            int maxLogSize = 2000;
            for (int i = 0; i <= message.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > message.length() ? message.length() : end;
//                android.util.AndLog.ShowLog(tag, message.substring(start, end));

                Log.i(tag, message.substring(start, end));
            }


        }
    }
}
