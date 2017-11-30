package com.example.info.scheduleapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ScheduleDbTable {
    private  String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    private final static String SQLiteTable_Name= "行程"; //資料表的名字
    private final static String CREATE_Schedule_TABLE=
            "CREATE TABLE if not exists '課表'(" +
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

    public void insertScheduleData(String name,String time_start,String day_start){ //不用第一的ID
        try {
            ContentValues row = new ContentValues();
            row.put("提醒日期", name);
            row.put("重複",time_start);
            row.put("行程開始日期",day_start);
            db.insert(SQLiteTable_Name, null, row);
            Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s,%s=%s", SQLiteTable_Name,"行程名稱", name,"行程開始時間",time_start,time_start,"行程開始日期",day_start));
        } catch (Exception e) {
            Log.e("#003", "資料列新增失敗");
        }
    }

    public void updateScheduleData(int id,String name,String time_start,String day_start){
        try {
            ContentValues row = new ContentValues();
            row.put("行程名稱", name);
            row.put("行程開始時間",time_start);
            row.put("行程開始日期",day_start);
            db.update(SQLiteTable_Name, row, "_id=" + id, null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s=%s,%s=%s,%s=%s", SQLiteTable_Name,"行程名稱", name,"行程開始時間",time_start,time_start,"行程開始日期",day_start));
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
        String name[]={"第1件事","第2件事","第3件事","第4件事","第5件事","第6件事","第7件事","第8件事","第9件事","第10件事","第11件事","第12件事","第13件事","第14件事","第15件事","第16件事","第17件事","第18件事","第19件事"};
        String time_start[]={"1900-1-0","1900-1-0","1900-1-0","1900-1-0","1900-1-0","1900-1-0","1900-1-0","1900-1-0","1900-1-0","1900-1-0","1900-1-0","1900-1-0","1900-1-0","1900-1-0","1900-1-0","1900-1-0","1900-1-0","1900-1-0","1900-1-0"};
        String day_start[]={"2017-9-3","2017-10-12","2017-12-8","2017-9-15","2017-11-24","2017-11-7","2017-10-14","2017-10-15","2017-10-17","2017-10-31","2017-10-17","2017-10-26","2017-11-30","2017-9-13","2017-9-11","2017-11-3","2017-10-23","2017-11-5","2017-9-12"};
        for(int i=0;i<name.length;i++){
            insertScheduleData(name[i],time_start[i],day_start[i]);
        }
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s'",SQLiteTable_Name),null);
    }

    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }
}
