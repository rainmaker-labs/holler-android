package sample.sdk.holler.rainmakerlabs.com.hollersdk;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import com.rainmakerlabs.holler.sdk.HollerSDK;
import com.rainmakerlabs.holler.sdk.analytic.Analytic;

import java.util.List;

/**
 * Created by thanhtritran on 13/12/16.
 */

public class HollerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
         HollerSDK.takeOf("8db68001685c98ac8559fcbb929d3c2318807804", "fd8109ec151b3c6be179874aa88bf93c7481f369");
    }

    /**
     * Method checks if the app is in background or not
     *
     * @param context
     * @return
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am
                    .getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am
                    .getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }


}
