package sample.sdk.holler.rainmakerlabs.com.hollersdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by thanhtritran on 30/11/16.
 */

public class UserLocalStorage {

    /**
     * Gets the settings.
     *
     * @param context the context
     * @return the settings
     */
    private static SharedPreferences getSettings(Context context) {
        return context.getSharedPreferences(context.getPackageName(), 0);
    }

    /**
     * Gets the editor.
     *
     * @param context the context
     * @return the editor
     */
    private static SharedPreferences.Editor getEditor(Context context) {
        return context.getSharedPreferences(context.getPackageName(), 0).edit();
    }


}
