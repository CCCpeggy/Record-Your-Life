package com.example.info.scheduleapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by info on 2017/11/2.
 */

public class DairyDbTable {
    private  String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    private final static String SQLiteTable_Name="日記";
    private final static String CREATE_Dairy_TABLE=
            "CREATE TABLE if not exists '日記'(" +
                    "_id INTEGER  PRIMARY KEY NOT NULL," +
                    "'日期' INTEGER NOT NULL," +
                    "'日記內容' TEXT)";
    public DairyDbTable(String path, SQLiteDatabase Database) {
        SQLiteDB_Path = path;
        db = Database;
    }

    //打開或新增資料表
    public void OpenOrCreateTb(){
        try{
            db.execSQL(CREATE_Dairy_TABLE);
            Log.v("資料表","資料表建立或開啟成功");
        }catch (Exception ex){
            Log.e("#002","資料表建立或開啟錯誤");
        }
    }

    public void insertDairyData(String  date,String content){
        try {
        ContentValues row = new ContentValues();
        row.put("日期", date);
        row.put("日記內容", content);
        db.insert(SQLiteTable_Name, null, row);
        Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s", SQLiteTable_Name, "日期", date, "日記內容",content));
        } catch (Exception e) {
            Log.e("#003", "資料列新增失敗");
        }
    }

    public void updateDairyData(int id , String  date,String content){
        try {
            ContentValues row = new ContentValues();
            row.put("日期", date);
            row.put("日記內容", content);
            db.update(SQLiteTable_Name, row, "_id=" + id, null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s,%s,%s,%s", SQLiteTable_Name, "日期", date, "日記內容", content));
        } catch (Exception e) {
            Log.e("#004", "資料列更新失敗");
        }
    }

    public void deleteDairyData(int Delete_id) {
        try {
            db.delete(SQLiteTable_Name, "_id=" + Delete_id, null);
            Log.v("刪除資料列", String.format("在%s刪除一筆資料：%s=%d", SQLiteTable_Name, "_id", Delete_id));
        } catch (Exception ex) {
            Log.e("#005", "刪除資料列錯誤");
        }
    }

    public void AddDairyData(){
        String  date[]={"2017-11-22","2017-11-08","2017-10-18","2017-11-22","2017-12-08","2017-11-01","2017-10-10","2017-11-07","2017-10-27","2017-10-08","2017-11-27","2017-09-02","2017-11-13","2017-10-03","2017-11-19","2017-09-08","2017-10-19","2017-12-06","2017-09-10"
        };
        String content[]={"今天天氣真好","麻麻說我壞壞QAQ","安安是笨蛋","菁菁這個磨人的小妖精","呵呵","涵涵涵涵涵涵涵涵","\\專題/\\專題/\\專題/\\專題/\\專題/\\專題/","加油加油","今天的紀錄是","喔喔喔喔喔倒數200天了","今天學壞了","老師說我交到壞朋友了","12345上山打老虎","啊啊啊啊啊黑賴好帥","YOOOOO","我推薦的BL小說","是","恩呵呵呵呵呵呵呵","19篇日記完成"};
        for(int i=0;i< date.length&&i<content.length;i++){
            insertDairyData( date[i],content[i]);
        }
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s'  ORDER BY 日期",SQLiteTable_Name),null);
    }


    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }

}
