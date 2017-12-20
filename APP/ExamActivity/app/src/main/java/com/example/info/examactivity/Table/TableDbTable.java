package com.example.info.examactivity.Table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by info on 2017/11/3.
 */

public class TableDbTable {

    private  String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    private final static String SQLiteTable_Name= "課表"; //資料表的名字
    private final static String CREATE_Table_TABLE= "CREATE TABLE if not exists '課表'(" +
            "_id INTEGER  PRIMARY KEY NOT NULL" +
            ",'課表名稱' TEXT" +
            ",'課表天數' INTEGER NOT NULL" +
            ",'主要' INTEGER NOT NULL" +
            ",'課表開始日' TEXT NOT NULL" +
            ",'課表結束日' TEXT" +
            ",'時間重複時是否取代' INTEGER NOT NULL )";
    public TableDbTable(String path, SQLiteDatabase Database) {
        SQLiteDB_Path = path;
        db = Database;
    }

    //打開或新增資料表
    public void OpenOrCreateTb(){
        try{
            db.execSQL(CREATE_Table_TABLE);
            Log.v("資料表","資料表建立或開啟成功");
        }catch (Exception ex){
            Log.e("#002","資料表建立或開啟錯誤");
        }
    }

    public void insertTableData(String name,int days,int isMain,String schedule_start,String schedule_end,int isReplace){ //不用第一的ID
        try {
            ContentValues row = new ContentValues();
            row.put("課表名稱", name);
            row.put("課表天數", days);
            row.put("主要", isMain);
            row.put("課表開始日", schedule_start);
            row.put("課表結束日", schedule_end);
            row.put("時間重複時是否取代", isReplace);
            db.insert(SQLiteTable_Name, null, row);
            Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s,%s=%s,%s=%s,%s=%s,%s=%s", SQLiteTable_Name, "課表名稱", name, "課表天數", days,"主要", isMain,"課表開始日2", schedule_start,"課表結束日2", schedule_end,"時間重複時是否取代", isReplace));
        } catch (Exception e) {
            Log.e("#003", "資料列新增失敗");
        }
    }

    public void updateTableData(int id,String name,String days,int isMain,String schedule_start,String schedule_end,int isReplace){
        try {
            ContentValues row = new ContentValues();
            row.put("課表名稱", name);
            row.put("課表天數", days);
            row.put("主要", isMain);
            row.put("課表開始日", schedule_start);
            row.put("課表結束日", schedule_end);
            row.put("時間重複時是否取代", isReplace);
            db.update(SQLiteTable_Name, row, "_id=" + id, null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s,%s,%s,%s,%s,%s", SQLiteTable_Name,"課表名稱",name,"課表天數",days,"主要",isMain,"課表開始日2",schedule_start,"課表結束日2",schedule_end,"時間重複時是否取代",isReplace));
        } catch (Exception e) {
            Log.e("#004", "資料列更新失敗");
        }
    }

    public void deleteTableData(int Delete_id) { //不用改
        try {
            db.delete(SQLiteTable_Name, "_id=" + Delete_id, null);
            Log.v("刪除資料列", String.format("在%s刪除一筆資料：%s=%d", SQLiteTable_Name, "_id", Delete_id));
        } catch (Exception ex) {
            Log.e("#005", "刪除資料列錯誤");
        }
    }

    public void AddTalbeData(){
        String  name[]={"學校","補習班","假日"};
        int  days[]={5,3,2};
        int  main[]={1,0,0};
        String  schedule_start[]={"2017-09-04","2017-07-04","2017-07-08"};
        String  schedule_end[]={"2018-01-26","2018-02-07","2018-02-13"};
        int  isReplace[]={0,0,0};
        for(int i=0;i< days.length&&i<name.length;i++){
            insertTableData( name[i],days[i],main[i],schedule_start[i],schedule_end[i],isReplace[i]);
        }
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s'",SQLiteTable_Name),null);
    }

    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }


}

