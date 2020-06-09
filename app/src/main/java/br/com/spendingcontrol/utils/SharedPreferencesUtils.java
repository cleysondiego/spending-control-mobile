package br.com.spendingcontrol.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
    private static final String LOGGED_IN = "logged_in";
    private static final String USER_TOKEN = "user_token";
    private static final String USER_NAME = "user_name";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("spendingcontrolbr", Context.MODE_PRIVATE);
    }

    public void setLoggedIn(Context context, boolean loggedIn, String token, String name) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN, loggedIn);
        editor.putString(USER_TOKEN, token);
        editor.putString(USER_NAME, name);
        editor.apply();
    }

    public void setLogoff(Context context) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN, false);
        editor.putString(USER_TOKEN, "");
        editor.putString(USER_NAME, "");
        editor.apply();
    }

    public boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN, false);
    }

    public String getUserToken(Context context) {
        return getPreferences(context).getString(USER_TOKEN, "");
    }

    public String getUserName(Context context) {
        return getPreferences(context).getString(USER_NAME, "");
    }
}
