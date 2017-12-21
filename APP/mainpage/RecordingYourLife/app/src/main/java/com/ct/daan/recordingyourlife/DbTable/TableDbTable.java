package com.ct.daan.recordingyourlife.DbTable;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by info on 2017/11/5.
 */

public class TableDbTable {

    private  String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    private final static String SQLiteTable_Name= "課表3"; //資料表的名字
    private final static String CREATE_Table_TABLE= "CREATE TABLE if not exists '課表3'(" +
            "_id INTEGER  PRIMARY KEY NOT NULL" +
            ",'課表名稱' TEXT" +
            ",'課表天數' INTEGER NOT NULL" +
            ",'主要' INTEGER NOT NULL" +
            ",'課表開始日' TEXT NOT NULL" +
            ",'課表結束日' TEXT )";
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

    public void insertTableData(String name,int days,int isMain,String schedule_start,String schedule_end){ //不用第一的ID
        try {
            ContentValues row = new ContentValues();
            row.put("課表名稱", name);
            row.put("課表天數", days);
            row.put("主要", isMain);
            row.put("課表開始日", schedule_start);
            row.put("課表結束日", schedule_end);
            db.insert(SQLiteTable_Name, null, row);
            Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s,%s=%s,%s=%s,%s=%s", SQLiteTable_Name, "課表名稱", name, "課表天數", days,"主要", isMain,"課表開始日2", schedule_start,"課表結束日", schedule_end));
        } catch (Exception e) {
            Log.e("#003", "資料列新增失敗");
        }
    }

    public void insertTableData(String name,String days,int isMain,String schedule_start,String schedule_end){ //不用第一的ID
        try {
            ContentValues row = new ContentValues();
            row.put("課表名稱", name);
            row.put("課表天數", days);
            row.put("主要", isMain);
            row.put("課表開始日", schedule_start);
            row.put("課表結束日", schedule_end);
            db.insert(SQLiteTable_Name, null, row);
            Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s,%s=%s,%s=%s,%s=%s,%s=%s", SQLiteTable_Name, "課表名稱", name, "課表天數", days,"主要", isMain,"課表開始日", schedule_start,"課表結束日", schedule_end));
        } catch (Exception e) {
            Log.e("#003", "資料列新增失敗");
        }
    }

    public void updateTableData(int id,String name,String days,int isMain,String schedule_start,String schedule_end){
        try {
            ContentValues row = new ContentValues();
            row.put("課表名稱", name);
            row.put("課表天數", days);
            row.put("主要", isMain);
            row.put("課表開始日", schedule_start);
            row.put("課表結束日", schedule_end);
            db.update(SQLiteTable_Name, row, "_id=" + id, null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s=%s,%s=%s,%s=%s,%s=%s,%s=%s,%s=%s", SQLiteTable_Name,"課表名稱",name,"課表天數",days,"主要",isMain,"課表開始日",schedule_start,"課表結束日",schedule_end));
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
        for(int i=0;i< days.length&&i<name.length;i++){
            insertTableData( name[i],days[i],main[i],schedule_start[i],schedule_end[i]);
        }
    }
    public Cursor getCursor(int Table_id){
        return getCursor("_id = "+Table_id);
    }

    public Cursor getCursor(String where_cmd){
        String cmd=String.format("SELECT *  FROM '%s' WHERE %s",SQLiteTable_Name,where_cmd);
        Log.v("WeekDbTable.getCursor",cmd);
        return db.rawQuery(cmd,null);
    }

    public int getTable_id(String Name){
        Cursor cursor=getCursor("課表名稱= '"+Name+"'");
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public int getMain_id(){
        Cursor cursor=getMain();
        if(cursor.getCount()<=0) cursor=getCursor();
        if(cursor.getCount()<=0) return 0;
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public void updateMain_id(){
        Cursor cursor=getMain();
        cursor.moveToFirst();
        updateTableData(cursor.getInt(0),cursor.getString(1),cursor.getString(2),0,cursor.getString(4),cursor.getString(5));
    }

    public String getMain_Name(){
        Cursor cursor=getMain();
        cursor.moveToFirst();
        return cursor.getString(1);
    }

    public Cursor getMain(){
        Cursor cursor=getCursor("主要= "+1);
        return cursor;
    }


    public Cursor getCursor(){
        String cmd=String.format("SELECT *  FROM '%s' ",SQLiteTable_Name);
        Log.v("TableDbTable.getCursor",cmd);
        return db.rawQuery(cmd,null);
    }

    public Cursor getCursorBydate(String date){
        String cmd=String.format("SELECT *  FROM '%s'  WHERE " +
                "AND( 課表開始日 < '%s' , 課表結束日 > '%s' ) ",SQLiteTable_Name,date,date);
        Log.v("TableDbTable.getCursor",cmd);
        return db.rawQuery(cmd,null);
    }


    public String getTable_name(int id){
        Cursor cursor=getCursor("_id= "+id);
        cursor.moveToFirst();
        return cursor.getString(1);
    }


    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }


}


