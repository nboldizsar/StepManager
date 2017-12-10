package com.example.boldi.stepmanager;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kristóf on 11/11/2017.
 */

public final class DateToIntConverter {
    public static int DateToInt(Calendar date){
        String dateint = "";
        dateint += date.get(Calendar.YEAR);
        int mounth =date.get(Calendar.MONTH);
        if (mounth < 10){
            dateint+= "0"+mounth;
        }else{
            dateint+=mounth;
        }
        int day =date.get(Calendar.DAY_OF_MONTH);
        if (day < 10){
            dateint+= "0"+day;
        }else{
            dateint+=day;
        }
        return Integer.parseInt(dateint);
    }
    public static Calendar IntToDate(int dateint){
        Calendar date = Calendar.getInstance();
        if(dateint >10000000 || dateint <99999999){
            String datestring =String.valueOf(dateint);
            date.set(Calendar.YEAR,Integer.parseInt(datestring.substring(0,3)));
            date.set(Calendar.MONTH,Integer.parseInt(datestring.substring(4,5)));
            date.set(Calendar.DAY_OF_MONTH,Integer.parseInt(datestring.substring(6,7)));
        }
        return date;
    }
}
