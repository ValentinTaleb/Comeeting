package com.les4elefantastiq.comeeting.models.linkedinmodels;

import com.les4elefantastiq.comeeting.models.Position;

/**
 * Created by Math on 06/08/16.
 */
public class Values {

    int id;
    Company company;
    Boolean isCurrent;
    LinkedInDate startDate;
    LinkedInDate endDate;
    String title;

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
