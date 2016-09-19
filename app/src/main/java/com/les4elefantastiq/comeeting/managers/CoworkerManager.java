package com.les4elefantastiq.comeeting.managers;

import android.support.annotation.NonNull;

import com.les4elefantastiq.comeeting.dataaccess.CoworkerDataAccess;
import com.les4elefantastiq.comeeting.models.Coworker;

import rx.Observable;

public class CoworkerManager {

    // -------------- Objects, Variables -------------- //



    // ---------------- Public Methods ---------------- //

    @NonNull
    public static Observable<Coworker> getCoworker(String linkedInId) {
        return CoworkerDataAccess.getCoworker(linkedInId);
    }

    @NonNull
    public static Observable<Void> Login(Coworker coworker) {
        return CoworkerDataAccess.login(coworker);
    }


    // ---------------- Private Methods --------------- //

}