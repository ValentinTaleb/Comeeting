package com.les4elefantastiq.comeeting.models;

public class Position {

    int id;
    String companyName;
    Boolean isCurrent;
    String startDate;
    String endDate;
    String title;

    public Position(int id, String companyName, Boolean isCurrent, String startDate, String endDate, String title) {
        this.id = id;
        this.companyName = companyName;
        this.isCurrent = isCurrent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
    }

}