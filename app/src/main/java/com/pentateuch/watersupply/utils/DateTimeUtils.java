package com.pentateuch.watersupply.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtils {
    private final Calendar cal;
    public DateTimeUtils(){
       cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
    }
    public String getCurrentDateAndTime() {
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
        return df.format(date);
    }

    public String getCurrentTime() {
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        return dateFormat.format(date);
    }

    public String getCurrentDate(){
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        return df.format(date);
    }

    public String getCurrentTime(int minutes){
        Calendar calendar = cal;
        calendar.add(Calendar.MINUTE,minutes);
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        return dateFormat.format(date);
    }

}
