package com.les4elefantastiq.comeeting.models.linkedin;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LinkedInDate {

    // -------------- Objects, Variables -------------- //

    private int month;
    private int year;


    // ----------------- Constructor ------------------ //

    public LinkedInDate(int month, int year) {
        this.month = month;
        this.year = year;
    }


    // ---------------- Public Methods ---------------- //

    public String getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1); // Beurk.
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(calendar.getTime());
    }

}