package com.example.info.examactivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by info on 2017/11/3.
 */

public class ClassWeekDbTable {
    private  String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    public String SQLiteTable_Name= "課表_課堂時間"; //資料表的名字
    public  String CREATE_CLASSWEEK_TABLE=
            "CREATE TABLE if not exists '"+SQLiteTable_Name+"' (" +
                    "_id INTEGER  PRIMARY KEY NOT NULL" +
                    ",課表ID INTEGER NOT NULL" +
                    ",開始時間 TEXT NOT NULL" +
                    ",結束時間 TEXT NOT NULL)";
    public ClassWeekDbTable(String path, SQLiteDatabase Database) {
        SQLiteDB_Path = path;
        db = Database;
    }

    //打開或新增資料表
    public void OpenOrCreateTb(){
        try{
            db.execSQL(CREATE_CLASSWEEK_TABLE);
            Log.v("資料表","資料表建立或開啟成功");
        }catch (Exception ex){
            Log.e("#002","資料表建立或開啟錯誤");
        }
    }

    public void insertClassWeekData(int table_id,String time_start,String time_end){
        try {
            ContentValues row = new ContentValues();
            row.put("課表ID", table_id);
            row.put("開始時間",time_start);
            row.put("結束時間",time_end);
            db.insert(SQLiteTable_Name, null, row);
            Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s,%s=%s", SQLiteTable_Name,"課表ID", table_id,"開始時間",time_start,"結束時間",time_end));
        } catch (Exception e) {
            Log.e("#003", "資料列新增失敗");
        }
    }

    public void updateClassWeekData(int id,int table_id,String time_start,String time_end){
        try {
            ContentValues row = new ContentValues();
            row.put("課表ID", table_id);
            row.put("開始時間",time_start);
            row.put("結束時間",time_end);
            db.update(SQLiteTable_Name, row, "_id=" + id, null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s,%s", SQLiteTable_Name,"課表ID", table_id,"開始時間",time_start,"結束時間",time_end));
        } catch (Exception e) {
            Log.e("#004", "資料列更新失敗");
        }
    }

    public void deleteClassWeekData(int Delete_id) { //不用改
        try {
            db.delete(SQLiteTable_Name, "_id=" + Delete_id, null);
            Log.v("刪除資料列", String.format("在%s刪除一筆資料：%s=%d", SQLiteTable_Name, "_id", Delete_id));
        } catch (Exception ex) {
            Log.e("#005", "刪除資料列錯誤");
        }
    }

    public void AddClassWeekData(){
        int table_id[]={1,1,1,1,1,1,1,2,2,3,3,3};
        String time_start[]={"08:20","09:20","10:20","11:20","13:10","14:10","15:10","18:20","20:10","17:00","18:00","19:00"};
        String time_end[]={"09:10","10:10","11:10","12:10","14:00","15:00","16:00","19:50","21:30","18:00","19:00","20:00"};
        for(int i=0;i< time_start.length&&i<table_id.length;i++){
            insertClassWeekData(table_id[i],time_start[i],time_end[i]);
        }
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s'",SQLiteTable_Name),null);
    }

    public Cursor getCursor(int Table_id){
        return db.rawQuery(String.format("SELECT *  FROM '%s' WHERE 課表ID=%s",SQLiteTable_Name,Table_id),null);
    }

    public int getTable_id(int ClassWeek_id){
        Cursor cursor=db.rawQuery(String.format("SELECT *  FROM '%s' WHERE _id=%d",SQLiteTable_Name,ClassWeek_id),null);
        cursor.moveToFirst();
        if(cursor.getCount()>0)
            return cursor.getInt(1);
        return 0;
    }

    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }
}
