package com.ct.daan.recordingyourlife.DbTable;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by info on 2017/11/10.
 */

public class NoteDbTable {
    private  String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    public String SQLiteTable_Name= "便條"; //資料表的名字
    private String CREATE_NOTE_TABLE="CREATE TABLE if not exists '"+SQLiteTable_Name+"'(" +
            "_id INTEGER  PRIMARY KEY NOT NULL," +
            "'便條標題' TEXT NOT NULL," +
            "'便條內容' TEXT)";
    public NoteDbTable(String path, SQLiteDatabase Database) {
        SQLiteDB_Path = path;
        db = Database;
    }

    //打開或新增資料表
    public void OpenOrCreateTb(){
        try{
            db.execSQL(CREATE_NOTE_TABLE);
            Log.v("資料表","資料表建立或開啟成功");
        }catch (Exception ex){
            Log.e("#002","資料表建立或開啟錯誤");
        }
    }

    public void insertNoteData(String title,String content){ //不用第一的ID
        try {
            ContentValues row = new ContentValues();
            row.put("便條標題", title);
            row.put("便條內容", content);
            db.insert(SQLiteTable_Name, null, row);
            Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s", SQLiteTable_Name, "便條標題", title, "便條內容",content));
        } catch (Exception e){
            Log.e("#003", "資料列新增失敗");
        }
    }

    public void updateNoteData(int id,String title,String content){
        try {
            ContentValues row = new ContentValues();
            row.put("_id", id);
            row.put("便條標題", title);
            row.put("便條內容", content);
            db.update(SQLiteTable_Name, row, "_id=" + id, null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s=%s,%s=%s,%s=%s", SQLiteTable_Name,"_id",id,"便條標題",title,"便條內容",content));
        } catch (Exception e) {
            Log.e("#004", "資料列更新失敗");
        }
    }

    public void deleteNoteData(int Delete_id) { //不用改
        try {
            db.delete(SQLiteTable_Name, "_id=" + Delete_id, null);
            Log.v("刪除資料列", String.format("在%s刪除一筆資料：%s=%d", SQLiteTable_Name, "_id", Delete_id));
        } catch (Exception ex) {
            Log.e("#005", "刪除資料列錯誤");
        }
    }

    public void AddNoteData(){
        String title[]={"帶","智障","怎樣","??","最棒了","=.=","喔","幼稚園","你好","好喔","喔","模考行程","動漫名稱","想買的書","成績公布","下載","今天吃的","帶","帶"
        };
        String content[]={"班費","哇哈哈哈哈","那群人","HOWHOW","帶泳衣","考試","就是這樣","呵呵","安安","專題發表","嘿嘿嘿","第一次10月19~20日\r\n第二次12月19~20日\r\n第三次\r\n2月26~27日\r\n第四次\r\n不參加第五次4月9~10日","kingofthehill","BL進化論","成績公布","下載","老地方","班費200","國文課本"};
        for(int i=0;i<title.length&&i<content.length;i++){
            insertNoteData(title[i],content[i]);
        }
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s'",SQLiteTable_Name),null);
    }

    public Cursor getCursor(int Note_id){
        return db.rawQuery(String.format("SELECT *  FROM '%s' WHERE _id = "+Note_id,SQLiteTable_Name),null);
    }


    public Cursor getCursor(String Where_cmd){
        String cmd=String.format("SELECT *  FROM '%s' WHERE %s",SQLiteTable_Name,Where_cmd);
        Log.v("cmd",cmd);
        return db.rawQuery(cmd,null);
    }

    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }
}
