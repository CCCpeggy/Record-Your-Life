package com.ct.daan.recordingyourlife.Class;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class CalenderFunction {

    public Calendar DateTimeTextToCalendarType (String date, String time ){
        return DateTimeTextToCalendarType(date+" "+time);
    }

    public Calendar DateTimeTextToCalendarType (String datetime ){
        return toCalendarType("yyyy-MM-dd HH:mm",datetime);
    }

    public Calendar DateTextToCalendarType (String date ){
        return toCalendarType("yyyy-MM-dd",date);
    }

    public Calendar TimeTextToCalendarType ( String time){
        return toCalendarType("yyyy-MM-dd HH:mm",time);
    }

    public boolean CompareDateTime(String BeforeDate,String BeforeTime,String AfterDate,String AfterTime ){
        Calendar BeforeCal=DateTimeTextToCalendarType(BeforeDate,BeforeTime);
        Calendar AfterCal=DateTimeTextToCalendarType(AfterDate,AfterTime);
        return CompareCalendar(BeforeCal,AfterCal);
    }

    public boolean CompareDateTime(String BeforeDateTime,String AfterDateTime ){
        Calendar BeforeCal=DateTimeTextToCalendarType(BeforeDateTime);
        Calendar AfterCal=DateTimeTextToCalendarType(AfterDateTime);
        return CompareCalendar(BeforeCal,AfterCal);
    }

    public boolean CompareDate(String BeforeDate,String AfterDate ){
        Calendar BeforeCal=DateTextToCalendarType(BeforeDate);
        Calendar AfterCal=DateTextToCalendarType(AfterDate);
        return CompareCalendar(BeforeCal,AfterCal);
    }

    public boolean CompareTime(String BeforeTime,String AfterTime ){
        Calendar BeforeCal=TimeTextToCalendarType(BeforeTime);
        Calendar AfterCal=TimeTextToCalendarType(AfterTime);
        return CompareCalendar(BeforeCal,AfterCal);
    }

    public Calendar DateTimeTextToCalendarType (String date, String time, Context context){
        return DateTimeTextToCalendarType(date+" "+time,context);
    }

    public Calendar DateTimeTextToCalendarType (String datetime, Context context){
        return toCalendarType("yyyy-MM-dd HH:mm",datetime,context);
    }

    public Calendar DateTextToCalendarType (String date, Context context){
        return toCalendarType("yyyy-MM-dd",date,context);
    }

    public Calendar TimeTextToCalendarType ( String time, Context context){
        return toCalendarType("yyyy-MM-dd HH:mm",time,context);
    }

    public boolean CompareDateTime(String BeforeDate,String BeforeTime,String AfterDate,String AfterTime,Context context){
        Calendar BeforeCal=DateTimeTextToCalendarType(BeforeDate,BeforeTime,context);
        Calendar AfterCal=DateTimeTextToCalendarType(AfterDate,AfterTime,context);
        return CompareCalendar(BeforeCal,AfterCal);
    }

    public boolean CompareDateTime(String BeforeDateTime,String AfterDateTime,Context context){
        Calendar BeforeCal=DateTimeTextToCalendarType(BeforeDateTime,context);
        Calendar AfterCal=DateTimeTextToCalendarType(AfterDateTime,context);
        return CompareCalendar(BeforeCal,AfterCal);
    }

    public boolean CompareDate(String BeforeDate,String AfterDate,Context context){
        Calendar BeforeCal=DateTextToCalendarType(BeforeDate,context);
        Calendar AfterCal=DateTextToCalendarType(AfterDate,context);
        return CompareCalendar(BeforeCal,AfterCal);
    }

    public boolean CompareTime(String BeforeTime,String AfterTime,Context context){
        Calendar BeforeCal=TimeTextToCalendarType(BeforeTime,context);
        Calendar AfterCal=TimeTextToCalendarType(AfterTime,context);
        return CompareCalendar(BeforeCal,AfterCal);
    }

    private boolean CompareCalendar(Calendar BeforeCal,Calendar AfterCal){
        LogDateTime("BeforeCal","Year:%s,Month:%s,Day:%s,Hour:%s,Minute:%s",BeforeCal);
        LogDateTime("AfterCal","Year:%s,Month:%s,Day:%s,Hour:%s,Minute:%s",AfterCal);
        return !BeforeCal.after(AfterCal);
    }

    private Calendar toCalendarType (String format, String text, Context context){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.TAIWAN);
        android.icu.util.Calendar Calendar= android.icu.util.Calendar.getInstance();
        try{
            Calendar.setTime(simpleDateFormat.parse(text));
        }catch (Exception e){
            Toast.makeText(context,"格式不符合"+format,Toast.LENGTH_SHORT).show();
            Log.v("格式不符合：",format+" By: "+text);
            return null;
        }
        return Calendar;
    }

    private Calendar toCalendarType (String format, String text){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.TAIWAN);
        android.icu.util.Calendar Calendar= android.icu.util.Calendar.getInstance();
        try{
            Calendar.setTime(simpleDateFormat.parse(text));
        }catch (Exception e){
            Log.v("格式不符合：",format+" By: "+text);
            return null;
        }
        return Calendar;
    }

    public String getString(Calendar calendar,String format){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
        String Cal_String=simpleDateFormat.format(calendar);
        return Cal_String;
    }

    public String getDate(Calendar calendar,String format){
        return getString(calendar,"yyyy-MM-dd");
    }

    public String getTime(Calendar calendar,String format){
        return getString(calendar,"HH:mm");
    }

    public void LogDate(String Title,String StringFormatText,Calendar calendar){
        Log.v(Title,String.format(StringFormatText,calendar.YEAR,calendar.MONTH,calendar.DAY_OF_MONTH));
    }

    public void LogTime(String Title,String StringFormatText,Calendar calendar){
        Log.v(Title,String.format(StringFormatText,calendar.HOUR_OF_DAY,calendar.MINUTE));
    }

    public void LogDateTime(String Title,String StringFormatText,Calendar calendar){
        Log.v(Title,String.format(StringFormatText
                ,calendar.YEAR,calendar.MONTH,calendar.DAY_OF_MONTH,calendar.HOUR_OF_DAY,calendar.MINUTE));
    }

}
