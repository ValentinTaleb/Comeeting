package com.les4elefantastiq.comeeting.models.linkedin;

import java.util.List;

/**
 * @see <a href="https://developer.linkedin.com/docs/fields/basic-profile">LinkedIn doc about Basic Profile fields</a>
 */
public class Positions {

    // -------------- Objects, Variables -------------- //

    public int _total;
    public List<Values> values;


    // ----------------- Constructor ------------------ //

    public Positions(int _total, List<Values> values) {
        this._total = _total;
        this.values = values;
    }

}