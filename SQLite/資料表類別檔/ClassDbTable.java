package com.example.info.table_complete_application.Table;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by info on 2017/11/5.
 */

public class ClassDbTable {
    private  String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    public String SQLiteTable_Name = "課表_課堂";
    public String CREATE_CLASS_TABLE="CREATE TABLE if not exists  '"+SQLiteTable_Name+"'  (" +
            "_id INTEGER NOT NULL" +
            ",星期ID INTEGER NOT NULL" +
            ",科目ID INTEGER NOT NULL" +
            ",PRIMARY KEY( _id , 星期ID ))";

    public ClassDbTable(String path, SQLiteDatabase Database) {
        SQLiteDB_Path = path;
        db = Database;
    }

    //打開或新增資料表
    public void OpenOrCreateTb(){
        try{
            db.execSQL(CREATE_CLASS_TABLE);
            Log.v("資料表","資料表建立或開啟成功");
        }catch (Exception ex){
            Log.e("#002","資料表建立或開啟錯誤");
        }
    }

    public void insertClassData(int ClassWeek_id,int week_id,int subject_id) {
        try {
            ContentValues row = new ContentValues();
            row.put("_id",ClassWeek_id);
            row.put("星期ID", week_id);
            row.put("科目ID", subject_id);
            db.insert(SQLiteTable_Name, null, row);
            Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s,%s=%s", SQLiteTable_Name, "_id",ClassWeek_id,"星期ID",week_id, "科目ID", subject_id));
        } catch (Exception e) {
            Log.e("#003", "資料列新增失敗");
        }
    }

    public void updateClassData(int ClassWeek_id,int week_id,int subject_id){
        try {
            ContentValues row = new ContentValues();
            row.put("科目ID", subject_id);
            db.update(SQLiteTable_Name, row, "_id=" + ClassWeek_id +"AND 星期ID = "+ week_id , null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s=%s,%s=%s,%s=%s", SQLiteTable_Name,"_id",ClassWeek_id, "星期ID",week_id, "科目ID", subject_id));
        } catch (Exception e) {
            Log.e("#004", "資料列更新失敗");
        }
    }

    public void deleteClassData(int ClassWeek_id,int week_id) {
        try {
            db.delete(SQLiteTable_Name, "_id=" + ClassWeek_id +"AND 星期ID = "+ week_id, null);
            Log.v("刪除資料列", String.format("在%s刪除一筆資料：%s=%d,%s=%d", SQLiteTable_Name, "_id", ClassWeek_id,"星期ID",week_id));
        } catch (Exception ex) {
            Log.e("#005", "刪除資料列錯誤");
        }
    }

    public void AddClassData(){
        int ClassWeek_id[]={1,2,3,4,5,6,7,1,2,3,4,5,6,7,1,2,3,4,5,6,7,1,2,3,4,5,6,7,1,2,3,4,5,6,7,8,9,8,9,8,9,10,11,12,10,11,12};
        int week_id[]={1,1,1,1,1,1,1,2,2,2,2,2,2,2,3,3,3,3,3,3,3,4,4,4,4,4,4,4,5,5,5,5,5,5,5,6,6,7,7,8,8,9,9,9,10,10,10};
        int subject_id[]={19,12,10,10,9,3,18,19,5,16,9,14,19,14,10,7,14,3,10,9,12,4,3,3,10,18,2,2,3,3,8,1,8,2,19,18,19,17,1,7,16,4,17,10,10,6,9};

        for(int i=0;i< week_id.length;i++){
            insertClassData(ClassWeek_id[i],week_id[i],subject_id[i]);
        }
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s' ",SQLiteTable_Name),null);
    }


    public Cursor getCursor(int ClassWeek_id,int Week_id){
        return db.rawQuery(String.format("SELECT *  FROM '%s'  WHERE _id = %s AND 星期ID = %s",SQLiteTable_Name, ClassWeek_id,Week_id),null);
    }

    public Cursor getCursor(String where_cmd){
        return db.rawQuery(String.format("SELECT *  FROM '%s' WHERE %s",SQLiteTable_Name,where_cmd),null);
    }

    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }


}
