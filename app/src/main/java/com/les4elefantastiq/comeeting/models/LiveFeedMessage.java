package com.les4elefantastiq.comeeting.models;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LiveFeedMessage implements Comparable<LiveFeedMessage> {

    // -------------- Objects, Variables -------------- //

    public static final int TYPE_ARRIVAL = 0;
    public static final int TYPE_TWITTER = 1;
    public static final int TYPE_COWORKSPACE_ADMIN = 2;
    public static final int TYPE_COWORKSPACE_OPENING = 3;

    public String title;
    public String text;
    public int type;
    public String dateTime;
    public String tweetLink;
    public String sender;
    public String coworkerLinkedInId;
    public Boolean isBirthday;
    public String pictureUrl;


    // ----------------- Constructor ------------------ //

    public LiveFeedMessage(String title, String text, int type, String dateTime, String tweetLink, String sender, String coworkerLinkedInId, Boolean isBirthday, String pictureUrl) {
        this.title = title;
        this.text = text;
        this.type = type;
        this.tweetLink = tweetLink;
        this.sender = sender;
        this.coworkerLinkedInId = coworkerLinkedInId;
        this.isBirthday = isBirthday;
        this.pictureUrl = pictureUrl;
        this.dateTime = dateTime;
    }


    // ---------------- Public Methods ---------------- //

    public Date getDate() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    // ----------------- Miscellaneous ---------------- //

    @Override
    public int compareTo(@NonNull LiveFeedMessage liveFeedMessage) {
        return - getDate().compareTo(liveFeedMessage.getDate());
    }

}