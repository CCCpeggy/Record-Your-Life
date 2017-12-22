package com.ct.daan.recordingyourlife.Class;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class CalendarFunction {

    public String getTodayDate(){
        Calendar cal=Calendar.getInstance();
        return getDateString(cal);
    }

    public Calendar getTodayCalendar(){
        Calendar cal=Calendar.getInstance();
        return cal;
    }

    public int getDayOfWeek(String date){
        Calendar cal=DateTextToCalendarType(date);
        int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK)-1;
        Log.v(date,"星期"+dayOfWeek);
        return dayOfWeek;
    }

    public String getTheDayAfter(String date){
        Calendar calendar=DateTextToCalendarType(date);
        calendar=getTheDayAfter(calendar);
        return getDateString(calendar);
    }

    public String getTheDayBefore(String date){
        Calendar calendar=DateTextToCalendarType(date);
        calendar=getTheDayBefore(calendar);
        return getDateString(calendar);
    }

    public Calendar getTheDayBefore(Calendar calendar){
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        return calendar;
    }

    public Calendar getTheDayAfter(Calendar calendar){
        calendar.add(Calendar.DAY_OF_MONTH,1);
        return calendar;
    }

    public Calendar DateTimeTextToCalendarType (String date, String time ){
        return DateTimeTextToCalendarType(date+" "+time);
    }

    public Calendar DateTimeTextToCalendarType (String datetime ){
        return toCalendarType("yyyy-MM-dd HH:mm",datetime);
    }

    public Calendar DateTextToCalendarType (String date){
        return toCalendarType("yyyy-MM-dd",date);
    }

    public Calendar TimeTextToCalendarType ( String time){
        return toCalendarType("hh:mm",time);
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
        return toCalendarType("hh:mm",time,context);
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
        Calendar calendar= Calendar.getInstance();
        try{
            calendar.setTime(simpleDateFormat.parse(text));
        }catch (Exception e){
            Toast.makeText(context,"格式不符合"+format,Toast.LENGTH_SHORT).show();
            Log.v("格式不符合：",format+" By: "+text);
            return null;
        }
        return calendar;
    }

    private Calendar toCalendarType (String format, String text){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.TAIWAN);
        Calendar calendar= Calendar.getInstance();
        try{
            calendar.setTime(simpleDateFormat.parse(text));
        }catch (Exception e){
            Log.v("格式不符合：",format+" By: "+text);
            return null;
        }
        return calendar;
    }

    public boolean isCalendarType(String text,String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.TAIWAN);
        Calendar calendar= Calendar.getInstance();
        try{
            calendar.setTime(simpleDateFormat.parse(text));
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public String getString(Calendar calendar,String format){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format, Locale.TAIWAN);
        String Cal_String=simpleDateFormat.format(calendar);
        return Cal_String;
    }

    public String getDateString(Calendar calendar){
        return getString(calendar,"yyyy-MM-dd");
    }

    public String getTimeString(Calendar calendar){
        return getString(calendar,"HH:mm");
    }

    public String getDateTimeString(Calendar calendar){
        return getString(calendar,"yyyy-MM-dd HH:mm");
    }

    public void LogDate(String Title,String StringFormatText,Calendar calendar){
        Log.v(Title,String.format(StringFormatText,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)));
    }

    public void LogTime(String Title,String StringFormatText,Calendar calendar){
        Log.v(Title,String.format(StringFormatText,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE)));
    }

    public void LogDateTime(String Title,String StringFormatText,Calendar calendar){
        Log.v(Title,String.format(StringFormatText
                ,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE)));
    }

}
