package com.example.info.examactivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by info on 2017/11/1.
 */

public class SubjectDbTable {
    private  String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    public String SQLiteTable_Name = "科目";
    public String CREATE_SUBJECT_TABLE="CREATE TABLE if not exists  '"+SQLiteTable_Name+"' (" +
            "_id INTEGER  PRIMARY KEY NOT NULL" +
            ",科目名稱 TEXT NOT NULL UNIQUE" +
            ",科目老師 TEXT)";
    public SubjectDbTable(String path, SQLiteDatabase Database) {
        SQLiteDB_Path = path;
        db = Database;
    }

    //打開或新增資料表
    public void OpenOrCreateTb(){
        try{
            db.execSQL(CREATE_SUBJECT_TABLE);
            Log.v("資料表","資料表建立或開啟成功");
        }catch (Exception ex){
            Log.e("#002","資料表建立或開啟錯誤");
        }
    }

    public void insertSubjectData(String name,String teacher) {
        try {
            ContentValues row = new ContentValues();
            row.put("科目名稱", name);
            row.put("科目老師", teacher);
            db.insert(SQLiteTable_Name, null, row);
            Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s", SQLiteTable_Name,"科目名稱",name, "科目老師", teacher));
        } catch (Exception e) {
            Log.e("#003", "資料列新增失敗");
        }
    }

    public void updateExamData(int id,String name,String teacher){
        try {
            ContentValues row = new ContentValues();
            row.put("科目名稱", name);
            row.put("科目老師", teacher);
            db.update(SQLiteTable_Name, row, "_id=" +id, null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s=%s,%s=%s", SQLiteTable_Name,"科目名稱",name, "科目老師", teacher));
        } catch (Exception e) {
            Log.e("#004", "資料列更新失敗");
        }
    }

    public void deleteExamData(int Delete_id) {
        try {
            db.delete(SQLiteTable_Name, "_id=" + Delete_id, null);
            Log.v("刪除資料列", String.format("在%s刪除一筆資料：%s=%d", SQLiteTable_Name, "_id", Delete_id));
        } catch (Exception ex) {
            Log.e("#005", "刪除資料列錯誤");
        }
    }

    public void AddSubjectData(){
        String name[]={"國文","英文","數學","地理","歷史","公民","基本電學","電子學","實習","程式","音樂","美術","體育","綜合活動","地球科學","生物","物理","化學","週會"};
        String teacher[]={"A老師","B老師","C老師","D老師","E老師","F老師","G老師","H老師","I老師","J老師","K老師","L老師","M老師","N老師","O老師","P老師","Q老師","R老師","S老師"};

        for(int i=0;i< name.length;i++){
            insertSubjectData( name[i],teacher[i]);
        }
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s' ",SQLiteTable_Name),null);
    }
    public Cursor getCursor(int Subject_id){
        return db.rawQuery(String.format("SELECT *  FROM '%s WHERE _id=%d",SQLiteTable_Name,Subject_id),null);
    }

    public String getSubjectName(int Subject_id){
        Cursor cursor =db.rawQuery(String.format("SELECT *  FROM '%s WHERE _id=%d",SQLiteTable_Name,Subject_id),null);
        return cursor.getString(1);
    }

    public String getSubjectID(String Subject_Name){
        Cursor cursor =db.rawQuery(String.format("SELECT *  FROM '%s WHERE 科目名稱=%s",SQLiteTable_Name,Subject_Name),null);
        return cursor.getString(1);
    }


    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }


}
