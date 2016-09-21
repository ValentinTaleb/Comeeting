package com.les4elefantastiq.comeeting.managers;

import android.content.Context;

import com.les4elefantastiq.comeeting.dataaccess.CoworkerDataAccess;
import com.les4elefantastiq.comeeting.models.Coworkspace;

import java.util.List;

import rx.Observable;

public class ProfileManager {

    // ---------------- Public Methods ---------------- //

    /**
     * @return true if the user is logged in, false otherwise
     */
    public static boolean isLogged(Context context) {
        return SharedPreferencesManager.getLinkedInId(context) != null;
    }

    public static Observable<Coworkspace> getCurrentCowerkspace(Context context) {

        String linkedInId = SharedPreferencesManager.getLinkedInId(context);

        return
                // Get the user Coworker
                CoworkerDataAccess.getCoworker(linkedInId)
                        // Get the id of the current Coworkspace
                        .map(coworker -> coworker.currentCoworkspaceId)
                        // Get the Coworkspace linked to this CoworkspaceId
                        .flatMap(CoworkspaceManager::getCoworkspace);
    }

    public static Observable<List<Coworkspace>> getFavoriteCoworkspaces() {
        //        Observable favoritesCoworkspaces =
        //                // Get the user Coworker
        //                CoworkerDataAccess.getCoworker(linkedInId)
        //                        // Get a list of the id of the favorites Coworkspaces
        //                        .map(coworker -> coworker.favoriteCoworkspacesId)
        //                        // Transform the Observable<List<String>> to
        //                        .map(Observable::from)
        //                        .flatMapIterable()

        return null;
    }

}