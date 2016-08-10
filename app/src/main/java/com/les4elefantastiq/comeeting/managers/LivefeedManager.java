package com.les4elefantastiq.comeeting.managers;

import com.les4elefantastiq.comeeting.dataaccess.LivefeedDataAccess;
import com.les4elefantastiq.comeeting.models.Coworkspace;
import com.les4elefantastiq.comeeting.models.LiveFeedMessage;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Math on 05/08/16.
 */
public class LivefeedManager {

    // -------------- Objects, Variables -------------- //

    private static HashMap<Coworkspace, List<LiveFeedMessage>> mLiveFeedMessagesMap = new HashMap<>();


    // ---------------- Public Methods ---------------- //

    public static List<LiveFeedMessage> getLiveFeedMessages(Coworkspace coworkspace) {
        loadLiveFeedMessages(coworkspace);
        return mLiveFeedMessagesMap.get(coworkspace);
    }


    // ---------------- Private Methods --------------- //

    private static synchronized void loadLiveFeedMessages(Coworkspace coworkspace) {
        if (mLiveFeedMessagesMap.get(coworkspace) == null) {
            List<LiveFeedMessage> liveFeedMessages = LivefeedDataAccess.getLivefeedMessages(coworkspace);

            if (liveFeedMessages != null) {
                Collections.sort(liveFeedMessages);
                mLiveFeedMessagesMap.put(coworkspace, liveFeedMessages);
            }
        }
    }

}

