package com.les4elefantastiq.les4elefantcowork.models.linkedinmodels;

import com.google.gson.annotations.SerializedName;
import com.les4elefantastiq.les4elefantcowork.models.Coworker;
import com.les4elefantastiq.les4elefantcowork.models.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Math on 06/08/16.
 */
public class LinkedInCoworker {

    // -------------- Objects, Variables -------------- //

    @SerializedName(value = "id")
    public String linkedInId;
    public String firstName;
    public String lastName;
    public PictureUrls pictureUrls;
    public String summary;
    public String headline;
    public Positions positions;

    @SerializedName(value = "favoriteCoworkspaces")
    List<String> favoriteCoworkspacesId;

    @SerializedName(value = "currentCoworkspace")
    public String currentCoworkspaceId;


    // ----------------- Constructor ------------------ //

    public LinkedInCoworker(String linkedInId, String firstName, String lastName, PictureUrls pictureUrls, String summary, String headline, Positions positions, List<String> favoriteCoworkspacesId, String currentCoworkspaceId) {
        this.linkedInId = linkedInId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pictureUrls = pictureUrls;
        this.summary = summary;
        this.headline = headline;
        this.positions = positions;
        this.favoriteCoworkspacesId = favoriteCoworkspacesId;
        this.currentCoworkspaceId = currentCoworkspaceId;
    }

    // ---------------- Public Methods ---------------- //

    public Coworker getCoworker() {
        String pictureUrl = pictureUrls.values.length > 0 ? pictureUrls.values[0] : null;

        List<Position> coworkerPositions = new ArrayList<>();
        if (positions.values != null) {
            for (Values value : positions.values) { // TODO: rename Values in linkedInPosition
                coworkerPositions.add(value.getPosition());
            }
        }


        return new Coworker(linkedInId, firstName, lastName, pictureUrl, summary, headline, coworkerPositions, null, favoriteCoworkspacesId, currentCoworkspaceId);
    }

    // ---------------- Private Methods --------------- //

    // ----------------- Miscellaneous ---------------- //

}
