package com.example.info.note;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public void insertReminderData(String date,int isReplace,int ReplaceType){
        try {
            ContentValues row = new ContentValues();
            row.put("提醒日期", date);
            row.put("重複", isReplace);
            row.put("重複單位", ReplaceType);
            db.insert(SQLiteTable_Name, null, row);
            Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s,%s=%s", SQLiteTable_Name,"提醒日期", date,"重複", isReplace,"重複單位", ReplaceType));
        } catch (Exception e) {
            Log.e("#003", "資料列新增失敗");
        }
    }

    public void updateReminderData(int id,String date,int isReplace,int ReplaceType){
        try {
            ContentValues row = new ContentValues();
            row.put("提醒日期", date);
            row.put("重複", isReplace);
            row.put("重複單位", ReplaceType);
            db.update(SQLiteTable_Name, row, "_id=" + id, null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s=%s,%s=%s,%s=%s", SQLiteTable_Name,"提醒日期", date,"重複", isReplace,"重複單位", ReplaceType));
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
        int isReplace[]={1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,0,1,0};
        int ReplaceType[]={0,0,1,2,1,0,0,3,1,0,0,0,0,0,2,3,2,0,1,2};
        for(int i=0;i< date.length ;i++){
            insertReminderData(date[i],isReplace[i],ReplaceType[i]);
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

    public Cursor getCursor(int id){
        return getCursor("_id = "+id);
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

    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }

    public void deleteRows(String Where_cmd){
        String cmd=String.format("DELETE * FROM '%s' WHERE %s",SQLiteTable_Name,Where_cmd);
        Log.v("DeleteRow",cmd);
        db.execSQL(cmd);
    }


}
