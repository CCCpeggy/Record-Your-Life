package com.ct.daan.recordingyourlife.DbTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ct.daan.recordingyourlife.Class.OthersFunction;

/**
 * Created by info on 2017/11/10.
 */

public class ReminderDbTable {
    private  String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    public String SQLiteTable_Name= "提醒"; //資料表的名字
    private String CREATE_Dairy_TABLE=
            "CREATE TABLE if not exists '"+SQLiteTable_Name+"'(" +
                    "_id INTEGER  PRIMARY KEY NOT NULL," +
                    "'提醒日期' TEXT NOT NULL,"+
                    "'提醒時間' TEXT NOT NULL,"+
                    "'重複' INTEGER NOT NULL,"+
                    "'重複單位' INTEGER NOT NULL)";
    public ReminderDbTable(String path, SQLiteDatabase Database) {
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

    public void insertReminderData(String date,String time,int isReplace,int ReplaceType){
        try {
            ContentValues row = new ContentValues();
            row.put("提醒日期", date);
            row.put("提醒時間", time);
            row.put("重複", isReplace);
            row.put("重複單位", ReplaceType);
            db.insert(SQLiteTable_Name, null, row);
            Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s,%s=%s,%s=%s", SQLiteTable_Name,"提醒日期", date,"提醒時間", time,"重複", isReplace,"重複單位", ReplaceType));
        } catch (Exception e) {
            Log.e("#003", "資料列新增失敗");
        }
    }

    public void updateReminderData(int id,String date,String time,int isReplace,int ReplaceType){
        try {
            ContentValues row = new ContentValues();
            row.put("提醒日期", date);
            row.put("提醒時間", time);
            row.put("重複", isReplace);
            row.put("重複單位", ReplaceType);
            db.update(SQLiteTable_Name, row, "_id=" + id, null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s=%s,%s=%ss,%s=%s,%s=%s", SQLiteTable_Name,"提醒日期", date,"提醒時間", time,"重複", isReplace,"重複單位", ReplaceType));
        } catch (Exception e) {
            Log.e("#004", "資料列更新失敗");
        }
    }

    public void deleteReminderData(int Delete_id) { //不用改
        try {
            db.delete(SQLiteTable_Name, "_id=" + Delete_id, null);
            Log.v("刪除資料列", String.format("在%s刪除一筆資料：%s=%d", SQLiteTable_Name, "_id", Delete_id));
        } catch (Exception ex) {
            Log.e("#005", "刪除資料列錯誤");
        }
    }

    public void AddReminderData(){
        String date[]={"2017-10-16","2017-10-17","2017-10-18","2017-10-19","2017-10-20","2017-10-21","2017-10-22","2017-10-23","2017-10-24","2017-10-25","2017-10-26","2017-10-27","2017-10-28","2017-10-29","2017-10-30","2017-10-31","2017-11-01","2017-11-02","2017-11-03"};
        String time[]={"02:08","21:26","05:52","06:29","05:28","12:16","17:59","05:15","09:38","22:07","04:15","07:01","01:02","06:27","03:13","14:04","06:46","09:00","05:56"};
        int isReplace[]={1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,0,1,0};
        int ReplaceType[]={0,0,1,2,1,0,0,3,1,0,0,0,0,0,2,3,2,0,1,2};
        for(int i=0;i< date.length ;i++){
            insertReminderData(date[i],time[i],isReplace[i],ReplaceType[i]);
        }
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s'",SQLiteTable_Name),null);
    }

    public Cursor getCursor(String Where_cmd){
        String cmd=String.format("SELECT *  FROM '%s' WHERE %s",SQLiteTable_Name,Where_cmd);
        Log.v("ClassWeekDb.getCursor",cmd);
        return db.rawQuery(cmd,null);
    }

    public String getRemindDateCursor(int Reminder_id){
        Cursor cursor=getCursor("_id = "+Reminder_id);
        cursor.moveToFirst();
        return cursor.getString(1);
    }
    public String getRemindTimeCursor(int Reminder_id){
        Cursor cursor=getCursor("_id = "+Reminder_id);
        cursor.moveToFirst();
        return cursor.getString(2);
    }
    public String getRemindTypeCursor(int Reminder_id){
        Cursor cursor=getCursor("_id = "+Reminder_id);
        cursor.moveToFirst();
        return cursor.getString(4);
    }
    public Cursor getCursor(int id){
        Cursor cursor=getCursor("_id = "+id);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getCursor(Cursor cursor) {
        cursor.moveToFirst();
        if(cursor.getCount()<=0)return null;
        String reminder_ids="";
        boolean isStart=true;
        do{
            if(!isStart) reminder_ids+=",";
            reminder_ids+=cursor.getInt(0)+"";
            isStart=false;
        }while (cursor.moveToNext());
        String Where_cmd=String.format(" _id IN (%s)",reminder_ids);
        return  getCursor( Where_cmd);
    }

    public Cursor setAllExamReminderTime(String time,String date){
        String cmd=String.format("UPDATE '%s'  SET  '提醒時間' = '%s'  WHERE '%s'=-1",SQLiteTable_Name,time,"重複");
        Log.v("ReminderDb.update",cmd);
        db.execSQL(cmd);
        cmd=String.format("SELECT *  FROM '%s' WHERE '%s'=-1 AND '提醒日期'>='%s'",SQLiteTable_Name,"重複",date);
        Log.v("ReminderDb.getCursor",cmd);
        OthersFunction othersFunction=new OthersFunction();
        Cursor cursor = db.rawQuery(cmd,null);
        cursor.moveToFirst();
        return cursor;
    }

    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }

    public void deleteRows(String Where_cmd){
        String cmd=String.format("DELETE * FROM '%s' WHERE %s",SQLiteTable_Name,Where_cmd);
        Log.v("DeleteRow",cmd);
        db.execSQL(cmd);
    }


}
