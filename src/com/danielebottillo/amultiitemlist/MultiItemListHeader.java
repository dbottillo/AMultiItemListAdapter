package com.danielebottillo.amultiitemlist;

import android.view.View;

/**
 * Created by danielebottillo on 9/24/13.
 */
public class MultiItemListHeader {

    private int index;
    private View view;

    public MultiItemListHeader(int index, View view){
        this.index = index;
        this.view = view;
    }

    public int getIndex(){
        return index;
    }

    public View getView(){
        return view;
    }
}
