package com.ct.daan.recordingyourlife.DbTable;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by info on 2017/10/31.
 */

public class ExamDbTable {
    private  String SQLiteDB_Path = null;
    private  SQLiteDatabase db = null;
    private final static String SQLiteTable_Name = "考試";
    private final static String CREATE_EXAM_TABLE =
            "CREATE TABLE if not exists  '考試' (" +
                    "_id INTEGER  PRIMARY KEY NOT NULL" +
                    ",考試節數ID INTEGER" +
                    " ,考試科目ID INTEGER NOT NULL" +
                    ",考試日期 TEXT NOT NULL" +
                    ",考試名稱 TEXT NOT NULL" +
                    ",考試內容 TEXT" +
                    ",考試成績 INTEGER)";

    public ExamDbTable(String path, SQLiteDatabase Database) {
        SQLiteDB_Path = path;
        db = Database;
    }



    //打開或新增資料表
    public void OpenOrCreateTb(){
        try{
            db.execSQL(CREATE_EXAM_TABLE);
            Log.v("資料表","資料表建立或開啟成功");
        }catch (Exception ex){
            Log.e("#002","資料表建立或開啟錯誤");
        }
    }

    public void insertExamData(int Class,int subject,String date,String name,String content,int score) {
        try {
            ContentValues row = new ContentValues();
            row.put("考試節數ID", Class);
            row.put("考試科目ID", subject);
            row.put("考試日期", date);
            row.put("考試名稱", name);
            row.put("考試內容", content);
            row.put("考試成績", score);
            db.insert(SQLiteTable_Name, null, row);
            Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s,%s=%s,%s=%s,%s=%s,%s=%s", SQLiteTable_Name, "考試節數ID", Class, "考試科目ID", subject, "考試日期", date, "考試名稱", name, "考試內容", content, "考試成績", score));
        } catch (Exception e) {
            Log.e("#003", "資料列新增失敗");
        }
    }
    public void updateExamData(int id,int Class,int subject,String date,String name,String content,int score){
        try {
            ContentValues row = new ContentValues();
            row.put("考試節數ID", Class);
            row.put("考試科目ID", subject);
            row.put("考試日期", date);
            row.put("考試名稱", name);
            row.put("考試內容", content);
            row.put("考試成績", score);
            db.update(SQLiteTable_Name, row, "_id=" +id, null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s=%s,%s=%s,%s=%s,%s=%s,%s=%s,%s=%s", SQLiteTable_Name, "考試節數ID", Class, "考試科目ID", subject, "考試日期", date, "考試名稱", name, "考試內容", content, "考試成績", score));
        } catch (Exception e) {
            Log.e("#004", "資料列更新失敗");
        }
    }

    public void updateExamData(int id,int score){
        try {
            ContentValues row = new ContentValues();
            row.put("考試成績", score);
            db.update(SQLiteTable_Name, row, "_id=" +id, null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s=%s", SQLiteTable_Name,  "考試成績", score));
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

    public void AddExamData(){
        int Class[]={1,15,4,3,18,2,6,5,2,7,12,10,13,18,18,10,7,4,19};
        int subject[]={1,1,3,2,8,14,2,8,3,7,11,13,1,14,14,2,3,1,1};
        //"國文","英文","數學","地理","歷史","公民","基本電學","電子學","實習","程式","音樂","美術","體育","綜合活動","地球科學","生物","物理","化學","週會"
        String date[]={"2017-10-25","2017-10-18","2017-09-20","2017-10-18","2017-11-29","2017-11-07","2017-09-07","2017-10-01","2017-11-09","2017-10-26","2017-10-05","2017-10-19","2017-11-06","2017-09-14","2017-10-26","2017-11-29","2017-10-10","2017-11-21","2017-10-25"};
        String name[]={"模考卷","複習卷","大卷","高頻","大卷","專二模考卷","高頻","複習卷","複習卷","交流電","段考","桌球","默寫","大卷","複習卷","高頻","模考卷","國文","水水"};
        String content[]={"102年","1~4冊","數列級數","U25","U9","103年","U23","U1~U8","1~2冊","U8","唱歌&報告","對打50次","典論論文第三段","U7","U1~U3","第16單元","U24","103數C","p38~p41"};
        int score[]={61,56,90,78,55,66,59,84,50,90,88,62,66,81,81,97,63,76,69};

        for(int i=0;i< date.length;i++){
            insertExamData( Class[i],subject[i],date[i],name[i],content[i],score[i]);
        }
    }

    public Cursor getCursorOrderById(){
        return db.rawQuery(String.format("SELECT *  FROM '%s' ORDER BY _id",SQLiteTable_Name),null);
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s' ORDER BY 考試日期 DESC,考試科目ID",SQLiteTable_Name),null);
    }

    public Cursor getCursor(int Subject_id){
        return db.rawQuery(String.format("SELECT *  FROM '%s WHERE 考試科目ID=%s ORDER BY 考試日期 DESC", SQLiteTable_Name,Subject_id+""),null);
    }


    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }

}

