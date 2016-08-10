package com.les4elefantastiq.comeeting.managers;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.les4elefantastiq.comeeting.dataaccess.CoworkerDataAccess;
import com.les4elefantastiq.comeeting.models.Coworker;

public class CoworkerManager {

    // -------------- Objects, Variables -------------- //



    // ---------------- Public Methods ---------------- //

    @Nullable
    @WorkerThread
    public static Coworker getCoworker(String linkedInId) {
        return CoworkerDataAccess.getCoworker(linkedInId);
    }

    @Nullable
    @WorkerThread
    public static boolean login(Coworker coworker) {
        return CoworkerDataAccess.login(coworker);
    }


    // ---------------- Private Methods --------------- //

}