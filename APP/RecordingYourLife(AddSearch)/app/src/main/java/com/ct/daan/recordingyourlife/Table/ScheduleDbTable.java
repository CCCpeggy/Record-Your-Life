package com.ct.daan.recordingyourlife.Table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ct.daan.recordingyourlife.Class.Schedule.ScheduleClass;
import com.ct.daan.recordingyourlife.Class.Schedule.item;

public class ScheduleDbTable {
    private String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    public String SQLiteTable_Name= "行程"; //資料表的名字
    private String CREATE_Schedule_TABLE=
            "CREATE TABLE if not exists '"+SQLiteTable_Name+"' (" +
                    "_id INTEGER  PRIMARY KEY NOT NULL," +
                    "'行程名稱' TEXT," +
                    "'行程開始時間' TEXT NOT NULL," +
                    "'行程開始日期' TEXT NOT NULL)";
    public ScheduleDbTable(String path, SQLiteDatabase Database) {
        SQLiteDB_Path = path;
        db = Database;
    }

    //打開或新增資料表
    public void OpenOrCreateTb(){
        try{
            db.execSQL(CREATE_Schedule_TABLE);
            Log.v("資料表","資料表建立或開啟成功");
        }catch (Exception ex){
            Log.e("#002","資料表建立或開啟錯誤");
        }
    }

    public void insertScheduleData(String name, String time_start, String day_start){ //不用第一的ID
        try {
            ContentValues row = new ContentValues();
            row.put("行程名稱", name);
            row.put("行程開始時間",time_start);
            row.put("行程開始日期",day_start);
            db.insert(SQLiteTable_Name, null, row);
            Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s,%s=%s", SQLiteTable_Name,"行程名稱", name,"行程開始時間",time_start,"行程開始日期",day_start));
        } catch (Exception e) {
            Log.e("#003", "資料列新增失敗");
        }
    }

    public void updateScheduleData(int id, String name, String time_start, String day_start){
        try {
            ContentValues row = new ContentValues();
            row.put("行程名稱", name);
            row.put("行程開始時間",time_start);
            row.put("行程開始日期",day_start);
            db.update(SQLiteTable_Name, row, "_id=" + id, null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s=%s,%s=%s,%s=%s", SQLiteTable_Name,"行程名稱", name,"行程開始時間",time_start,"行程開始日期",day_start));
        } catch (Exception e) {
            Log.e("#004", "資料列更新失敗");
        }
    }

    public void deleteScheduleData(int Delete_id) { //不用改
        try {
            db.delete(SQLiteTable_Name, "_id=" + Delete_id, null);
            Log.v("刪除資料列", String.format("在%s刪除一筆資料：%s=%d", SQLiteTable_Name, "_id", Delete_id));
        } catch (Exception ex) {
            Log.e("#005", "刪除資料列錯誤");
        }
    }

    public void AddScheduleData(){
        String name[]={"第1件事","第2件事","第3件事","第4件事","第5件事","第6件事","第7件事","第8件事","第9件事","第10件事","第11件事","第12件事","第13件事","第14件事","第15件事","第16件事","第17件事","第18件事","第19件事","第20件事","第21件事","第22件事"};
        String time_start[]={"08:00","09:00","10:00","11:00","20:00","12:00","13:00","14:00","15:00","09:00","16:00","17:00","18:00","09:00","19:00","20:00","21:00","22:00","23:00","08:00","09:00","10:00"};
        String day_start[]={"2017-9-3","2017-10-12","2017-12-8","2017-9-15","2017-11-22","2017-11-24","2017-11-7","2017-10-14","2017-10-15","2017-11-22","2017-10-17","2017-10-31","2017-11-22","2017-10-17","2017-10-26","2017-11-30","2017-9-13","2017-9-11","2017-11-3","2017-10-23","2017-11-5","2017-9-12"};
        for(int i=0;i<name.length;i++){
            insertScheduleData(name[i],time_start[i],day_start[i]);
        }
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s'",SQLiteTable_Name),null);
    }

    public Cursor getCursor(String date){
        String cmd= String.format("SELECT *  FROM '%s' WHERE 行程開始日期='%s' ORDER BY 行程開始時間",SQLiteTable_Name,date);
        Log.v("cmd",cmd);
        return db.rawQuery(cmd,null);
    }

    public String[][] getScheduleArray(String date){
        Cursor tmp_cursor= getCursor(date);
        if (tmp_cursor.getCount()<=0)return null;
        Log.v("tmp_cursor.getCount()",tmp_cursor.getCount()+"");
        String[][] Schedule=new String[tmp_cursor.getCount()][];
        tmp_cursor.moveToFirst();
        int i=0;
        do{
            Schedule[i]=new String[2];
            Schedule[i][0]=tmp_cursor.getString(1);
            Schedule[i][1]=tmp_cursor.getString(2);
            i++;
        }while(tmp_cursor.moveToNext());
        return Schedule;
    }

    public ScheduleClass getScheduleClass(String date){
        Cursor tmp_cursor= getCursor(date);
        Log.v("tmp_cursor.getCount()",tmp_cursor.getCount()+"");
        ScheduleClass schedule=new ScheduleClass(tmp_cursor.getCount());
        if (tmp_cursor.getCount()<=0)return schedule;
        tmp_cursor.moveToFirst();
        int i=0;
        do{
            schedule.items[i]=new item(tmp_cursor.getString(1),tmp_cursor.getString(2));
            i++;
        }while(tmp_cursor.moveToNext());
        return schedule;
    }

    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }
}
