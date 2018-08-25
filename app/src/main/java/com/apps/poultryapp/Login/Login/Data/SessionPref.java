package com.apps.poultryapp.Login.Login.Data;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.apps.poultryapp.Login.Model.User;


public class SessionPref {

    public static final String PREFS_NAME = "POULTRY LOGIN PREFS";
    public static final String PREF_USER_ID = "PREF_USER_ID";
    public static final String PREF_USER_NAME = "PREF_USER_NAME";
    public static final String PREF_USER_EMAIL = "PREF_USER_EMAIL";
    public static final String PREF_USER_COMPANY = "PREF_USER_COMPANY";


    private final SharedPreferences mPrefs;

    private boolean mIsLoggedIn = false;

    private static SessionPref INSTANCE;

    public static SessionPref get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SessionPref(context);
        }
        return INSTANCE;
    }

    private SessionPref(Context context) {
        mPrefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        mIsLoggedIn = !TextUtils.isEmpty(mPrefs.getString(PREF_USER_EMAIL, null));
    }

    public boolean isLoggedIn() {
        return mIsLoggedIn;
    }

    public void saveUser(User user) {
        if (user != null) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putInt(PREF_USER_ID, user.getId());
            editor.putString(PREF_USER_NAME, user.getName());
            editor.putString(PREF_USER_EMAIL, user.getEmail());
            editor.putString(PREF_USER_COMPANY, user.getCompany());
            System.out.println("login succes" + user.getName());
            editor.apply();

            mIsLoggedIn = true;
        }
    }

    public void logOut(){
        mIsLoggedIn = false;
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(PREF_USER_ID, 0);
        editor.putString(PREF_USER_NAME, null);
        editor.putString(PREF_USER_EMAIL, null);
        editor.putString(PREF_USER_COMPANY, null);
        editor.apply();
    }
    public String getPrefUserCompany(){
        String company = mPrefs.getString(PREF_USER_COMPANY, "no");
    return company;
    }
    public String getPrefUserId(){
        int id = mPrefs.getInt(PREF_USER_ID, 0);
        System.out.println("--------------"+id);
        String.valueOf(id);
        return String.valueOf(id);
    }
}

