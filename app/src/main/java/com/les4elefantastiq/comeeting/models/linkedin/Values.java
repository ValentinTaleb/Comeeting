package com.les4elefantastiq.comeeting.models.linkedin;

/**
 * @see <a href="https://developer.linkedin.com/docs/fields/positions">LinkedIn doc about Position</a>
 */
public class Values {

    // -------------- Objects, Variables -------------- //

    public int id;
    public Company company;
    public Boolean isCurrent;
    public LinkedInDate startDate;
    public LinkedInDate endDate;
    public String title;


    // ----------------- Constructor ------------------ //

    public Values(int id, Company company, Boolean isCurrent, LinkedInDate startDate, LinkedInDate endDate, String title) {
        this.id = id;
        this.company = company;
        this.isCurrent = isCurrent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
    }


    // ---------------- Public Methods ---------------- //

    public Position getPosition() {

        String coworkerStartDate = startDate != null ? startDate.getDate() : null;
        String coworkerEndDate = endDate != null ? endDate.getDate() : null;

        return new Position(id, company.name, isCurrent, coworkerStartDate, coworkerEndDate, title);
    }

}