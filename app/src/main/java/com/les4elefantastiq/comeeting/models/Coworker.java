package com.les4elefantastiq.comeeting.models;

import com.google.gson.annotations.SerializedName;
import com.les4elefantastiq.comeeting.models.linkedin.Position;

import java.util.List;

/**
 * Representation of somebody that go to Coworkspaces
 */
public class Coworker {

    // -------------- Objects, Variables -------------- //

    public final String linkedInId;
    public String firstName;
    public String lastName;
    public String pictureUrl;
    public String summary;
    public String headline;
    public List<Position> positions;

    @SerializedName(value = "favoriteCoworkspaces")
    public List<String> favoriteCoworkspacesId;

    @SerializedName(value = "currentCoworkspace")
    public String currentCoworkspaceId;


    // ----------------- Constructor ------------------ //

    public Coworker(String linkedInId, String firstName, String lastName, String pictureUrl, String summary, String headline, List<Position> positions, List<String> favoriteCoworkspacesId, String currentCoworkspaceId) {
        this.linkedInId = linkedInId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pictureUrl = pictureUrl;
        this.summary = summary;
        this.headline = headline;
        this.positions = positions;
        this.favoriteCoworkspacesId = favoriteCoworkspacesId;
        this.currentCoworkspaceId = currentCoworkspaceId;
    }

}