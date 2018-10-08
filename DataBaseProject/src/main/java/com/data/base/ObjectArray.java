package com.data.base;

import java.util.ArrayList;

/**
 * Since im saving the rows internally as a string, I need to return it to the result set as an Object in order
 * to follow the project requirment. Object Array converts the ArrayList of rows to an ArrayList of objects
 *
 * the following methods are pretty trivial and dont need explanations
 */

public class ObjectArray {
    public ArrayList<Object> row = new ArrayList<Object>();

    ObjectArray(){

    }

    public void add(Object o){
        row.add(o);
    }

    public ArrayList<Object> getRowList() {
        return row;
    }


    public Object getRow(int index){
        return row.get(index);

    }
}
