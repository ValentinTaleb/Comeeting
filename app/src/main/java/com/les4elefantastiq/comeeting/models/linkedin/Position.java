package com.les4elefantastiq.comeeting.models.linkedin;

// FIXME : This class is totally useless. Delete it !
public class Position {

    // -------------- Objects, Variables -------------- //

    public int id;
    public String companyName;
    public Boolean isCurrent;
    public String startDate;
    public String endDate;
    public String title;


    // ----------------- Constructor ------------------ //

    public Position(int id, String companyName, Boolean isCurrent, String startDate, String endDate, String title) {
        this.id = id;
        this.companyName = companyName;
        this.isCurrent = isCurrent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
    }

}