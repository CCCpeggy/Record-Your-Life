package com.example.info.scheduleapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by info on 2017/11/17.
 */

public class A_Day_Table  extends WeekDbTable{
    private SQLiteDatabase db = null;
    ClassDbTable ClassDb;
    SubjectDbTable SubjectDb;
    ClassWeekDbTable ClassWeekDb;
    TableDbTable TableDb;
    private int DayOfWeek /*星期幾*/;
    private List<Integer> WeekIds;

    public A_Day_Table(String path, SQLiteDatabase Database,String date) {
        super(path,Database);

        initAllDb(path,Database);
        AddTableData();
        setDays(date);

        WeekIds=new ArrayList<Integer>();
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
        DayOfWeek=days;
    }

    public void setDays(String date){
        setDays(DayOfWeek(date));
    }

    public void getTable_cursor(){
        //取得所有課表
        Cursor Table_cursor= TableDb.getCursor();
        if(Table_cursor.getCount()<=0)return;
        Table_cursor.moveToFirst();
        //一一尋找week
        do{
            int days=getDays(Table_cursor,4);
            Cursor tmp_week_cursor=super.getCursor(Table_cursor.getInt(0),days);
            //判斷week是否存在
            if(tmp_week_cursor.getCount()<=0)continue;
            tmp_week_cursor.moveToFirst();
            WeekIds.add(tmp_week_cursor.getInt(0));
        }while (Table_cursor.moveToNext());
    }

    public void outputAllWeekIds(){
        for (int i : WeekIds){
            Log.v("List<Integer> WeekIds",i+"");
        }
    }

    private int DayOfWeek(String date){
        Calendar cal=StringtoCalendar(date);
        int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK)-1;
        Log.v("星期幾",dayOfWeek+"");
        return dayOfWeek;
    }

    private int getDays(Cursor cursor,int index){
        int days=DayOfWeek-DayOfWeek(cursor.getString(index))+1;
        Log.v("第幾天",days+"");
        return days;
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
