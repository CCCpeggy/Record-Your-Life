package com.ct.daan.recordingyourlife.Table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by info on 2017/11/10.
 */

public class Note_Reminder_DbTable {
    private String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    public String SQLiteTable_Name= "便條_提醒_清單2"; //資料表的名字
    private String CREATE_Dairy_TABLE=
            "CREATE TABLE if not exists '"+SQLiteTable_Name+"'(" +
                    "_id INTEGER  PRIMARY KEY NOT NULL," +
                    "'便條ID' INTEGER NOT NULL)";
    public Note_Reminder_DbTable(String path, SQLiteDatabase Database) {
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

    public void insertNoteReminderData(int reminder_id,int note){ //不用第一的ID
        try {
            ContentValues row = new ContentValues();
            row.put("_id", reminder_id);
            row.put("便條ID", note);
            db.insert(SQLiteTable_Name, null, row);
            Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s", SQLiteTable_Name,"_id",reminder_id,"便條ID",note));
        } catch (Exception e) {
            Log.e("#003", "資料列新增失敗");
        }
    }

    public void updateNoteReminderData(int id,int note){
        try {
            ContentValues row = new ContentValues();
            row.put("便條ID", note);
            db.update(SQLiteTable_Name, row, "_id=" + id, null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s=%s", SQLiteTable_Name,"便條ID",note));
        } catch (Exception e) {
            Log.e("#004", "資料列更新失敗");
        }
    }

    public void deleteNoteReminderData(int Delete_id) {
        try {
            db.delete(SQLiteTable_Name, "_id=" + Delete_id, null);
            Log.v("刪除資料列", String.format("在%s刪除一筆資料：%s=%d", SQLiteTable_Name, "_id", Delete_id));
        } catch (Exception ex) {
            Log.e("#005", "刪除資料列錯誤");
        }
    }

    public void deleteNoteReminderDataByNoteID(int Note_id) {
        try {
            db.delete(SQLiteTable_Name, "便條ID=" + Note_id, null);
            Log.v("刪除資料列", String.format("在%s刪除一筆資料：%s=%d", SQLiteTable_Name, "便條ID", Note_id));
        } catch (Exception ex) {
            Log.e("#005", "刪除資料列錯誤");
        }
    }


    public void AddNoteReminderData(){
        int reminder[]={2,3,4,5,6, 8,9,10,12,13, 15,16,17,18,20, 21,25,26,27,28};
        int note[]={18,11,6,10,7 ,5,16,7,10,8 ,2,19,17,16,8 ,9,3,7,1};
        for(int i=0;i< note.length ;i++){
            insertNoteReminderData(reminder[i],note[i]);
        }
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s'",SQLiteTable_Name),null);
    }

    public Cursor getCursor(String Where_cmd){
        String cmd= String.format("SELECT *  FROM '%s' WHERE %s",SQLiteTable_Name,Where_cmd);
        Log.v("ClassWeekDb.getCursor",cmd);
        return db.rawQuery(cmd,null);
    }

    public Cursor getCursor(int id){
        return getCursor("_id = "+id);
    }

    public Cursor getCursorByNoteID(int id){
        return getCursor("便條ID = "+id);
    }

    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }

}
