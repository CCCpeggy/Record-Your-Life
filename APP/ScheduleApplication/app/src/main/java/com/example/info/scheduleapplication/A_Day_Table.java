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
    private List<Integer> weekIds;

    public A_Day_Table(String path, SQLiteDatabase Database,String date) {
        super(path,Database);

        initAllDb(path,Database);
        AddTableData();
        setDays(date);

        weekIds=new ArrayList<Integer>();
        weekIds=getWeekId();
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

    public Cursor getWeek_cursor(){
        Cursor Week_cursor;
        String Ids_string="";
        boolean not_start=false;
        for (int i:weekIds) {
            if(not_start)Ids_string+=",";
            else not_start=!not_start;
            Ids_string+=i;
        }
        Week_cursor=super.getCursor("_id IN ("+Ids_string+")" );
        return Week_cursor;
    }

    private List<Integer> getWeekId(){
        List<Integer> WeekIds=new ArrayList<Integer>();
        //取得所有課表
        Cursor Table_cursor= TableDb.getCursor();
        if(Table_cursor.getCount()<=0) return null;
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
        return WeekIds;
    }

    public void outputAllWeekIds(){
        for (int i : weekIds){
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

    //多個課表
    public String[][][] TablesClassInDay(){
        if(weekIds.size()<=0)return null;
        String [][][]Tables=new String[weekIds.size()][][];
        int i=0;
        for (int id:weekIds){
            Tables[i]=OneTableClassInDay(id);
            i++;
        }
        return Tables;
    }

    //0科目名稱 1開始時間 2結束時間
    public String[][] OneTableClassInDay(int Week_id){
        String Table[][]=new String[3][];
        if(weekIds.size()<=0)return null;
        //int Week_id=weekIds.get(0);
        Cursor ClassCursor = ClassDb.getCursor("星期ID = "+Week_id);
        if(ClassCursor.getCount()<=0)return null;
        ClassCursor.moveToFirst();
        int i=0;
        do{
            if(i==0){
                int count=ClassCursor.getCount();
                Table[0]=new String[count];
                Table[1]=new String[count];
                Table[2]=new String[count];
            }
            Table[0][i]=getClassSubject(ClassCursor);
            Table[1][i]=ClassWeekDb.getByIndex(ClassCursor.getInt(0),2);
            Table[2][i]=ClassWeekDb.getByIndex(ClassCursor.getInt(0),3);
            Log.v("Class Table",String.format("[0]%s,[1]%s,[2]%s",Table[0][i],Table[1][i],Table[2][i]));
            i++;
        }while (ClassCursor.moveToNext());

        return Table;
    }

    public String[] getTableName(){
        String Ids_string="";
        boolean not_start=false;
        for (int i:weekIds) {
            if(not_start)Ids_string+=",";
            else not_start=!not_start;
            Ids_string+=i;
        }
        Cursor tmp_cursor= super.getCursor("課表ID","_id IN ("+Ids_string+")");
        if (tmp_cursor.getCount()<=0)return null;
        Log.v("tmp_cursor.getCount()",tmp_cursor.getCount()+"");
        String[] Name=new String[tmp_cursor.getCount()];
        tmp_cursor.moveToFirst();
        int i=0;
        do{
            Name[i]=TableDb.getTable_name( tmp_cursor.getInt(0));
            i++;
        }while(tmp_cursor.moveToNext());
        return Name;
    }


    public String getClassSubject(int ClassWeek_id,int Week_id){
        Cursor ClassCursor=getClassCursor(ClassWeek_id,Week_id);
        ClassCursor.moveToFirst();
        return SubjectDb.getSubjectName(ClassCursor.getInt(2));
    }

    public String getClassSubject(Cursor ClassCursor){
        return SubjectDb.getSubjectName(ClassCursor.getInt(2));
    }

    public Cursor getClassCursor(int ClassWeek_id,int Week_id){
        return ClassDb.getCursor("_id = "+ClassWeek_id+" AND 星期ID ="+Week_id);
    }



}
