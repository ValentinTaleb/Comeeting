package com.les4elefantastiq.les4elefantcowork.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.les4elefantastiq.les4elefantcowork.models.Coworker;

/**
 * Created by Math on 06/08/16.
 */
public class SharedPreferencesManager {

    private static final String LINKEDINID = "LINKEDINID";
    private static final String PROFILE = "PROFILE";

    public static void setLinkedInId(Context context, String linkedInId) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(LINKEDINID, linkedInId);
        editor.commit();
    }

    public static String getLinkedInId(Context context) {
        return getSharedPreferences(context).getString(LINKEDINID, null);
    }

    public static void saveProfile(Context context, Coworker coworker) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PROFILE, new Gson().toJson(coworker));
        editor.commit();
    }

    public static Coworker getProfile(Context context) {
        String jsonProfile = getSharedPreferences(context).getString(PROFILE, null);
        if (jsonProfile != null) {
            return new Gson().fromJson(jsonProfile, Coworker.class);
        }
        return null;
    }


    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
    }
}
