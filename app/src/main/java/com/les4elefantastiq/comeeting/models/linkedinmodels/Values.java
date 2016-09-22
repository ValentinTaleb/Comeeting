package com.les4elefantastiq.comeeting.models.linkedinmodels;

import com.les4elefantastiq.comeeting.models.Position;

public class Values {

    public int id;
    public Company company;
    public Boolean isCurrent;
    public LinkedInDate startDate;
    public LinkedInDate endDate;
    public String title;

    public Values(int id, Company company, Boolean isCurrent, LinkedInDate startDate, LinkedInDate endDate, String title) {
        this.id = id;
        this.company = company;
        this.isCurrent = isCurrent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
    }

    public Position getPosition() {

        String coworkerStartDate = startDate != null ? startDate.getDate() : null;
        String coworkerEndDate = endDate != null ? endDate.getDate() : null;

        return new Position(id, company.name, isCurrent, coworkerStartDate, coworkerEndDate, title);
    }

}
