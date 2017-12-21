package com.ct.daan.recordingyourlife.DbTable;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ct.daan.recordingyourlife.Class.CalendarFunction;
import com.ct.daan.recordingyourlife.Class.Table.Table;
import com.ct.daan.recordingyourlife.Class.Table.TablesClass;
import com.ct.daan.recordingyourlife.Class.Table.Class;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by info on 2017/11/17.
 */

public class A_Day_Table  extends WeekDbTable{
    private SQLiteDatabase db = null;
    ClassDbTable ClassDb;
    SubjectDbTable SubjectDb;
    ClassWeekDbTable ClassWeekDb;
    TableDbTable TableDb;
    CalendarFunction calFunction;
    private int DayOfWeek /*星期幾*/;
    private List<Integer> weekIds;

    public A_Day_Table(String path, SQLiteDatabase Database, String date) {
        super(path,Database);
        calFunction=new CalendarFunction();
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
        DayOfWeek=days;
        weekIds=new ArrayList<Integer>();
        weekIds=getWeekId();
    }

    public void setDays(String date){
        setDays(calFunction.getDayOfWeek(date));
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
            Cursor tmp_week_cursor2=super.getCursor(Table_cursor.getInt(0),days+7);
            //判斷week是否存在
            if(tmp_week_cursor.getCount()<=0&&tmp_week_cursor2.getCount()<=0)continue;
            if(tmp_week_cursor2.getCount()<=0){
                tmp_week_cursor.moveToFirst();
                WeekIds.add(tmp_week_cursor.getInt(0));
            }
            else{
                tmp_week_cursor2.moveToFirst();
                WeekIds.add(tmp_week_cursor2.getInt(0));
            }

        }while (Table_cursor.moveToNext());
        return WeekIds;
    }

    public void outputAllWeekIds(){
        for (int i : weekIds){
            Log.v("List<Integer> WeekIds",i+"");
        }
    }

    private int getDays(Cursor cursor, int index){
        int days=DayOfWeek-calFunction.getDayOfWeek(cursor.getString(index))+1;
        Log.v("第幾天",days+"");
        return days;
    }

    //多個課表
    public String[][][] TablesClassInDay(){
        if(weekIds.size()<=0)return null;
        String[][][]Tables=new String[weekIds.size()][][];
        int i=0;
        for (int id:weekIds){
            Tables[i]=OneTableClassInDay(id);
            i++;
        }
        for(i=0;i<Tables.length-1;i++){
            for(int j=i+1;j<Tables.length;j++){
                Log.v("Date", String.format("date1=%s,date2=%s", Tables[i][0][1],Tables[j][0][1]));
                if(!calFunction.CompareTime( Tables[i][0][1],Tables[j][0][1])){
                    String[][] tmp_array=Tables[i];
                    Tables[i]=Tables[j];
                    Tables[j]=tmp_array;
                    Log.v("changedDate", String.format("date1=%s,date2=%s", Tables[i][0][1],Tables[j][0][1]));
                }
            }
        }
        return Tables;
    }

    //0科目名稱 1開始時間
    public String[][] OneTableClassInDay(int Week_id){
        if(weekIds.size()<=0)return null;
        Cursor ClassCursor = ClassDb.getCursor("星期ID = "+Week_id);
        if(ClassCursor.getCount()<=0)return null;
        Log.v("ClassCursor.getCount()",ClassCursor.getCount()+"");
        String Table[][]=new String[ClassCursor.getCount()][];
        ClassCursor.moveToFirst();
        int i=0;
        do{
            Table[i]=new String[2];
            Table[i][0]=getClassSubject(ClassCursor);
            Table[i][1]=ClassWeekDb.getByIndex(ClassCursor.getInt(0),2);
            Log.v("Class Table", String.format("[0]%s,[1]%s",Table[i][0],Table[i][1]));
            i++;
        }while (ClassCursor.moveToNext());

        return Table;
    }

    //取得多個課表
    public TablesClass getTablesClass(){
        Log.v("weekIds.size()",weekIds.size()+"");
        TablesClass Tables=new TablesClass(weekIds.size());
        if(weekIds.size()<=0)return Tables;
        int i=0;
        for (int id:weekIds){
            Tables.Tables[i]=getClass(id);
            i++;
        }
        //排序課表
        for(i=0;i<Tables.Tables.length-1;i++){
            for(int j=i+1;j<Tables.Tables.length;j++){
                Log.v("Date", String.format("date1=%s,date2=%s", Tables.Tables[i].classes[0].start_time,Tables.Tables[j].classes[0].start_time));
                if(!calFunction.CompareTime( Tables.Tables[i].classes[0].start_time,Tables.Tables[j].classes[0].start_time)){
                    Table tmp_Table=Tables.Tables[i];
                    Tables.Tables[i]=Tables.Tables[j];
                    Tables.Tables[j]=tmp_Table;
                    Log.v("changedDate", String.format("date1=%s,date2=%s", Tables.Tables[i].classes[0].start_time,Tables.Tables[j].classes[0].start_time));
                }
            }
        }
        return Tables;
    }

    //0科目名稱 1開始時間
    public Table getClass(int Week_id){
        if(weekIds.size()<=0)return null;
        Cursor ClassCursor = ClassDb.getCursor("星期ID = "+Week_id);
        if(ClassCursor.getCount()<=0)return null;
        Log.v("ClassCursor.getCount()",ClassCursor.getCount()+"");
        Table table=new Table(ClassCursor.getCount(),getTableName(Week_id));
        ClassCursor.moveToFirst();
        int i=0;
        do{
            table.classes[i]=new Class(ClassWeekDb.getByIndex(ClassCursor.getInt(0),2),getClassSubject(ClassCursor));
            Log.v("Class Table", String.format("[Subject]%s,[start_time]%s",table.classes[i].Subject,table.classes[i].start_time));
            i++;
        }while (ClassCursor.moveToNext());

        return table;
    }

    public String getTableName(int week_id){
        String Ids_string="";
        boolean not_start=false;
        Cursor tmp_cursor= super.getCursor("課表ID","_id = "+week_id);
        if (tmp_cursor.getCount()<=0)return null;
        Log.v("tmp_cursor.getCount()",tmp_cursor.getCount()+"");
        tmp_cursor.moveToFirst();
        return TableDb.getTable_name(tmp_cursor.getInt(0));
    }

    public String getClassSubject(int ClassWeek_id, int Week_id){
        Cursor ClassCursor=getClassCursor(ClassWeek_id,Week_id);
        ClassCursor.moveToFirst();
        return SubjectDb.getSubjectName(ClassCursor.getInt(2));
    }

    public String getClassSubject(Cursor ClassCursor){
        return SubjectDb.getSubjectName(ClassCursor.getInt(2));
    }

    public Cursor getClassCursor(int ClassWeek_id, int Week_id){
        return ClassDb.getCursor("_id = "+ClassWeek_id+" AND 星期ID ="+Week_id);
    }



}
