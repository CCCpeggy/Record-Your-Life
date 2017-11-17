package com.example.info.scheduleapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.util.Log;

import java.util.Locale;

/**
 * Created by info on 2017/11/17.
 */

public class A_Day_Table  extends WeekDbTable{
    private SQLiteDatabase db = null;
    ClassDbTable ClassDb;
    SubjectDbTable SubjectDb;
    ClassWeekDbTable ClassWeekDb;
    TableDbTable TableDb;
    Cursor cursor;
    private int Days /*第幾天*/;

    public A_Day_Table(String path, SQLiteDatabase Database,String date) {
        super(path,Database);

        initAllDb(path,Database);
        AddTableData();
        setDays(date);
    }

    public void initAllDb(String path, SQLiteDatabase db){
        ClassDb=new ClassDbTable(path,db);
        SubjectDb=new SubjectDbTable(path,db);
        ClassWeekDb=new ClassWeekDbTable(path,db);
        TableDb=new TableDbTable(path,db);

        ClassDb.OpenOrCreateTb();
        SubjectDb.OpenOrCreateTb();
        ClassWeekDb.OpenOrCreateTb();
        TableDb.OpenOrCreateTb();
        super.OpenOrCreateTb();
    }

    public void AddTableData(){
        ClassDb.deleteAllRow();
        ClassDb.AddClassData();
        SubjectDb.deleteAllRow();
        SubjectDb.AddSubjectData();
        ClassWeekDb.deleteAllRow();
        ClassWeekDb.AddClassWeekData();
        super.deleteAllRow();
        super.AddWeekData();
        TableDb.deleteAllRow();
        TableDb.AddTalbeData();
    }

    public void setDays(int days){
        Days=days;
    }

    public void setDays(String date){
        setDays(DayOfWeek(date));
    }

    public void getTable_cursor(){

    }

    private int DayOfWeek(String date){
        Calendar cal=StringtoCalendar(date);
        int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK)-1;
        Log.v("星期幾",dayOfWeek+"");
        return dayOfWeek;
    }

    private int Date_to_Days(String Start_date,String date){
        int days=DayOfWeek(date)-DayOfWeek(Start_date)+1;
        Log.v("第幾天",days+"");
        return days;
    }

    private Calendar StringtoCalendar(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN);
        Calendar Calendar= android.icu.util.Calendar.getInstance();
        try{
            Calendar.setTime(sdf.parse(date));
        }catch (Exception e){
            Log.v("日期格式不符合",date);
        }
        return Calendar;
    }

}
