package com.les4elefantastiq.comeeting.managers;

import android.content.Context;

import com.les4elefantastiq.comeeting.dataaccess.CoworkerDataAccess;
import com.les4elefantastiq.comeeting.models.Coworkspace;

import java.util.List;

import rx.Observable;

public class ProfileManager {

    // -------------- Objects, Variables -------------- //

    //private static Coworker mCoworker;


    // ---------------- Public Methods ---------------- //

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

//    @Nullable
//    @WorkerThread
//    public static Coworkspace getCurrentCowerkspace(Context context) {
//        loadCoworker(context);
//
//        if (mCoworker != null && mCoworker.currentCoworkspaceId != null)
//            return CoworkspaceManager.getCoworkspace(mCoworker.currentCoworkspaceId);
//        else
//            return null;
//    }
//
//    @Nullable
//    @WorkerThread
//    public static Coworkspace[] getFavoriteCowerkspaces(Context context) {
//        loadCoworker(context);
//
//        if (mCoworker != null) {
//            List<String> favoriteCoworkspacesId = mCoworker.favoriteCoworkspacesId;
//
//            if (mCoworker != null && mCoworker.favoriteCoworkspacesId != null) {
//                Coworkspace[] favoriteCoworkspaces = new Coworkspace[mCoworker.favoriteCoworkspacesId.size()];
//
//                List<Coworkspace> coworkspaces = CoworkspaceManager.getCoworkspaces();
//
//                if (coworkspaces != null)
//                    for (int i = 0; i < favoriteCoworkspacesId.size(); i++) {
//                        for (Coworkspace coworkspace : coworkspaces) {
//                            if (favoriteCoworkspacesId.get(i).equals(coworkspace.id)) {
//                                favoriteCoworkspaces[i] = coworkspace;
//                                break;
//                            }
//                        }
//                    }
//
//                return favoriteCoworkspaces;
//            } else {
//                return null;
//            }
//        } else
//            return null;
//    }
//
//    // ---------------- Private Methods --------------- //
//
//
//    private static void loadCoworker(Context context) {
//        String linkedInId = SharedPreferencesManager.getLinkedInId(context);
//        if (mCoworker == null && linkedInId != null)
//            mCoworker = CoworkerDataAccess.getCoworker(linkedInId);
//    }

}