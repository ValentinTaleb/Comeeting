package com.les4elefantastiq.comeeting.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.les4elefantastiq.comeeting.models.Coworker;

public class SharedPreferencesManager {

    // -------------- Objects, Variables -------------- //

    private static final String LINKEDINID = "LINKEDINID";
    private static final String PROFILE = "PROFILE";


    // ---------------- Public Methods ---------------- //

    /**
     * Save the linkedInId of the user in the SharedPreferences
     */
    public static void setLinkedInId(Context context, String linkedInId) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(LINKEDINID, linkedInId);
        editor.apply();
    }

    /**
     * @return The linkedInId of the user if exists, return null otherwise
     */
    @Nullable
    public static String getLinkedInId(Context context) {
        return getSharedPreferences(context).getString(LINKEDINID, null);
    }

    /**
     * Save the Coworker object of the user in the SharedPreferences
     */
    public static void saveProfile(Context context, Coworker coworker) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PROFILE, new Gson().toJson(coworker));
        editor.apply();
    }

    /**
     * @return The Coworker object of the user if exists, return null otherwise
     */
    public static Coworker getProfile(Context context) {
        String jsonProfile = getSharedPreferences(context).getString(PROFILE, null);

        if (jsonProfile != null) {
            return new Gson().fromJson(jsonProfile, Coworker.class);
        }

        return null;
    }


    // ---------------- Private Methods --------------- //

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
    }

}