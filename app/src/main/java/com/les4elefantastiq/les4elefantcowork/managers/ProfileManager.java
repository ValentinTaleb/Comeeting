package com.les4elefantastiq.les4elefantcowork.managers;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.les4elefantastiq.les4elefantcowork.activities.SignInActivity;
import com.les4elefantastiq.les4elefantcowork.dataaccess.CoworkerDataAccess;
import com.les4elefantastiq.les4elefantcowork.models.Coworker;
import com.les4elefantastiq.les4elefantcowork.models.Coworkspace;
import com.linkedin.platform.LISessionManager;

import java.util.List;

public class ProfileManager {

    // -------------- Objects, Variables -------------- //

    private static Coworker mCoworker;


    // ---------------- Public Methods ---------------- //

    public static boolean isLogged(Context context) {
        return SharedPreferencesManager.getLinkedInId(context) != null;
    }

    public static void signWithLinkedIn(final SignInActivity context) {
        LISessionManager.getInstance(context).init(context, context.buildScope(), context, true);
    }

    @Nullable
    @WorkerThread
    public static Coworkspace getCurrentCowerkspace(Context context) {
        loadCoworker(context);

        if (mCoworker != null && mCoworker.currentCoworkspaceId != null)
            return CoworkspacesManager.getCoworkspace(mCoworker.currentCoworkspaceId);
        else
            return null;
    }

    @Nullable
    @WorkerThread
    public static Coworkspace[] getFavoriteCowerkspaces(Context context) {
        loadCoworker(context);

        if (mCoworker != null) {
            List<String> favoriteCoworkspacesId = mCoworker.favoriteCoworkspacesId;
            
            if (mCoworker != null && mCoworker.favoriteCoworkspacesId != null) {
                Coworkspace[] favoriteCoworkspaces = new Coworkspace[mCoworker.favoriteCoworkspacesId.size()];

                List<Coworkspace> coworkspaces = CoworkspacesManager.getCoworkspaces();

                if (coworkspaces != null)
                    for (int i = 0; i < favoriteCoworkspacesId.size(); i++) {
                        for (Coworkspace coworkspace : coworkspaces) {
                            if (favoriteCoworkspacesId.get(i).equals(coworkspace.id)) {
                                favoriteCoworkspaces[i] = coworkspace;
                                break;
                            }
                        }
                    }

                return favoriteCoworkspaces;
            } else {
                return null;
            }
        } else
            return null;
    }

    // ---------------- Private Methods --------------- //

    private static void loadCoworker(Context context) {
        String linkedInId = SharedPreferencesManager.getLinkedInId(context);
        if (mCoworker == null && linkedInId != null)
            mCoworker = CoworkerDataAccess.getCoworker(linkedInId);
    }

}