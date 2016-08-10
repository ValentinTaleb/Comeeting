package com.les4elefantastiq.les4elefantcowork.models.linkedinmodels;

import com.les4elefantastiq.les4elefantcowork.models.Position;

import java.util.List;

/**
 * Created by Math on 06/08/16.
 */
public class Positions {

    int _total;
    List<Values> values;

    public Positions(int _total, List<Values> values) {
        this._total = _total;
        this.values = values;
    }

}
