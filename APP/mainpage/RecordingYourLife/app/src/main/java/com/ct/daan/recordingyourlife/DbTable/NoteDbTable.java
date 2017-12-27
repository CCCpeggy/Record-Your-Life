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
        String title[]={"帶","想看的動漫名稱","帶","看視力","模考行程","模考時間","想買的書","修改","晚餐列表","帶","帶"
        };
        String content[]={"班費","奇諾之旅\r\n刀劍神域_序列爭戰\r\n你的名字\r\n干小妹","泳衣","帶視力回條","國文\r\n英文\r\n專二\r\n數學\r\n專一","第一次10月19~20日\r\n第二次12月19~20日\r\n第三次2月26~27日\r\n第四次不參加\r\n第五次4月9~10日","BL進化論\r\n 10 Count\r\n特殊傳說第二部","專題程式","老地方\r\n四海豆漿店\r\n家伙","班費200","國文課本"};
        for(int i=0;i<title.length&&i<content.length;i++){
            insertNoteData(title[i],content[i]);
        }
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s'",SQLiteTable_Name),null);
    }

    public Cursor getCursorbyDESC(){
        return db.rawQuery(String.format("SELECT *  FROM '%s' ORDER BY _id DESC",SQLiteTable_Name),null);
    }

    public Cursor getCursor(int Note_id){
        return db.rawQuery(String.format("SELECT *  FROM '%s' WHERE _id = "+Note_id,SQLiteTable_Name),null);
    }

    public Cursor getCursorbyDESC(int Note_id){
        return db.rawQuery(String.format("SELECT *  FROM '%s' WHERE _id = %s ORDER BY _id DESC",SQLiteTable_Name,Note_id),null);
    }

       public Cursor getCursor(String Where_cmd){
        String cmd=String.format("SELECT *  FROM '%s' WHERE %s",SQLiteTable_Name,Where_cmd);
        Log.v("cmd",cmd);
        return db.rawQuery(cmd,null);
    }

    public Cursor getCursorbyDESC(String Where_cmd){
        String cmd=String.format("SELECT *  FROM '%s' WHERE %s ORDER BY _id DESC",SQLiteTable_Name,Where_cmd);
        Log.v("cmd",cmd);
        return db.rawQuery(cmd,null);
    }


    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }
}
