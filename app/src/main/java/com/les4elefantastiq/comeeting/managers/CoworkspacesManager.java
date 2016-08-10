package com.les4elefantastiq.comeeting.managers;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.les4elefantastiq.comeeting.dataaccess.CoworkspaceDataAccess;
import com.les4elefantastiq.comeeting.models.Coworker;
import com.les4elefantastiq.comeeting.models.Coworkspace;

import java.util.List;

public class CoworkspacesManager {

    // -------------- Objects, Variables -------------- //

    private static List<Coworkspace> mCoworkspaces;


    // ---------------- Public Methods ---------------- //

    @Nullable
    @WorkerThread
    public static Coworkspace getCoworkspace(String coworkspaceId) {
        List<Coworkspace> coworkspaces = getCoworkspaces();

        if (coworkspaces != null)
            for (Coworkspace coworkspace : coworkspaces)
                if (coworkspace.id.equals(coworkspaceId))
                    return coworkspace;

        return null;
    }

    @Nullable
    @WorkerThread
    public static List<Coworkspace> getCoworkspaces() {
        loadCoworkspaces();

        return mCoworkspaces;
    }

    @Nullable
    @WorkerThread
    public static List<Coworker> getCoworkers(Coworkspace coworkspace) {
        return CoworkspaceDataAccess.getCoworkers(coworkspace);
    }

    @Nullable
    @WorkerThread
    public static void checkIn(Coworkspace coworkspace, Coworker coworker) {
        CoworkspaceDataAccess.setCheckIn(coworkspace, coworker, true);
    }

    @Nullable
    @WorkerThread
    public static void checkOut(Coworkspace coworkspace, Coworker coworker) {
        CoworkspaceDataAccess.setCheckIn(coworkspace, coworker, false);
    }

    @Nullable
    @WorkerThread
    public static Coworkspace getCoworkspaceWithName(String name) {
        loadCoworkspaces();

        for (Coworkspace coworkspace : mCoworkspaces) {
            if (coworkspace.name.equals(name)) {
                return coworkspace;
            }
        }

        return null;
    }


    // ---------------- Private Methods --------------- //

    private static void loadCoworkspaces() {
        if (mCoworkspaces == null)
            mCoworkspaces = CoworkspaceDataAccess.getAllCoworkspace();
    }

}