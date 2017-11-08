package com.example.info.examactivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by info on 2017/11/3.
 */

public class WeekDbTable {
    private  String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    private final static String SQLiteTable_Name= "課表_星期3"; //資料表的名字
    private final static String CREATE_WEEK_TABLE=
            "CREATE TABLE if not exists '課表_星期3'" +
            "(_id INTEGER  PRIMARY KEY NOT NULL" +
            ",'第幾天' INTEGER NOT NULL"+
            ",'課表ID' INTEGER NOT NULL)";
    public WeekDbTable(String path, SQLiteDatabase Database) {
        SQLiteDB_Path = path;
        db = Database;
    }

    //打開或新增資料表
    public void OpenOrCreateTb(){
        try{
            db.execSQL(CREATE_WEEK_TABLE);
            Log.v("資料表","資料表建立或開啟成功");
        }catch (Exception ex){
            Log.e("#002","資料表建立或開啟錯誤");
        }
    }

    public void insertWeekData(int table_id,int dayOfWeek){
        try {
            ContentValues row = new ContentValues();
            row.put("課表ID", table_id);
            row.put("第幾天", dayOfWeek);
            db.insert(SQLiteTable_Name, null, row);
            Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s", SQLiteTable_Name,"課表ID",table_id,"第幾天",dayOfWeek));
        } catch (Exception e) {
            Log.e("#003", "資料列新增失敗");
        }
    }

    public void updateWeekData(int id,int table_id,int dayOfWeek){
        try {
            ContentValues row = new ContentValues();
            row.put("課表ID",table_id);
            row.put("第幾天", dayOfWeek);
            db.update(SQLiteTable_Name, row, "_id=" + id, null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s=%s,%s=%s", SQLiteTable_Name,"課表ID",table_id,"第幾天",dayOfWeek));
        } catch (Exception e) {
            Log.e("#004", "資料列更新失敗");
        }
    }

    public void deleteWeekData(int Delete_id) { //不用改
        try {
            db.delete(SQLiteTable_Name, "_id=" + Delete_id, null);
            Log.v("刪除資料列", String.format("在%s刪除一筆資料：%s=%d", SQLiteTable_Name, "_id", Delete_id));
        } catch (Exception ex) {
            Log.e("#005", "刪除資料列錯誤");
        }
    }

    public void AddWeekData(){
        int table_id[]={1,1,1,1,1,2,2,2,3,3};
        int dayOfWeek[]={1,2,3,4,5,1,2,3,1,2};
        for(int i=0;i< table_id.length ;i++){
            insertWeekData(table_id[i],dayOfWeek[i]);
        }
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s'",SQLiteTable_Name),null);
    }

    public Cursor getCursor(int Table_id,int days){
        return db.rawQuery(String.format("SELECT *  FROM '%s' WHERE (課表ID=%s AND 第幾天 = %s)",SQLiteTable_Name,Table_id,days),null);
    }

    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }
}
