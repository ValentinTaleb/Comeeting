package com.les4elefantastiq.comeeting.managers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.les4elefantastiq.comeeting.dataaccess.CoworkspaceDataAccess;
import com.les4elefantastiq.comeeting.models.Coworker;
import com.les4elefantastiq.comeeting.models.Coworkspace;

import java.util.List;

import rx.Observable;

public class CoworkspaceManager {

    // ---------------- Public Methods ---------------- //

    @NonNull
    public static Observable<List<Coworkspace>> getCoworkspaces() {
        return CoworkspaceDataAccess.getCoworkspaces();
    }

    @NonNull
    public static Observable<Coworkspace> getCoworkspace(String coworkspaceId) {
        return CoworkspaceManager.getCoworkspaces()
                .flatMapIterable(coworkspaces -> coworkspaces)
                .first(coworkspace -> coworkspace.id.equals(coworkspaceId));
    }

    @Nullable
    @WorkerThread
    public static Observable<List<Coworker>> getCoworkers(Coworkspace coworkspace) {
        return CoworkspaceDataAccess.getCoworkers(coworkspace);
    }

}