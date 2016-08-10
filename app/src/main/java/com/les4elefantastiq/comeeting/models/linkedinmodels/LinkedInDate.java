package com.les4elefantastiq.comeeting.models.linkedinmodels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Math on 06/08/16.
 */
public class LinkedInDate {

    int month;
    int year;

    public LinkedInDate(int month, int year) {
        this.month = month;
        this.year = year;
    }

    public String getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1); // MA: Beurk.
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.getDefault()).format(calendar.getTime());
    }
}
