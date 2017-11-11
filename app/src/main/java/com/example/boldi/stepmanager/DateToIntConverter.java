package com.example.boldi.stepmanager;

import java.util.Date;

/**
 * Created by Kristóf on 11/11/2017.
 */

public final class DateToIntConverter {
    public static int DateToInt(Date date){
        String dateint = "";
        dateint += date.getYear();
        dateint += date.getMonth();
        dateint += date.getDay();
        return Integer.parseInt(dateint);
    }
    public static Date IntToDate(int dateint){
        Date date = new Date(1995,10,14);
        if(dateint >10000000 || dateint <99999999){
            String datestring =String.valueOf(dateint);
            date.setYear(Integer.parseInt(datestring.substring(0,3)));
            date.setMonth(Integer.parseInt(datestring.substring(4,5)));
            date.setDate(Integer.parseInt(datestring.substring(6,7)));
        }
        return date;
    }
}
