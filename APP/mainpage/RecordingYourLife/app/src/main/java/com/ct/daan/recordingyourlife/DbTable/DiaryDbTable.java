package com.ct.daan.recordingyourlife.DbTable;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by info on 2017/11/2.
 */

public class DiaryDbTable {
    private  String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    private final static String SQLiteTable_Name="日記";
    private final static String CREATE_DIARY_TABLE=
            "CREATE TABLE if not exists '"+SQLiteTable_Name+"'(" +
                    "_id INTEGER  PRIMARY KEY NOT NULL," +
                    "'日期' INTEGER NOT NULL," +
                    "'日記內容' TEXT)";
    public DiaryDbTable(String path, SQLiteDatabase Database) {
        SQLiteDB_Path = path;
        db = Database;
    }

    //打開或新增資料表
    public void OpenOrCreateTb(){
        try{
            db.execSQL(CREATE_DIARY_TABLE);
            Log.v("資料表","資料表建立或開啟成功");
        }catch (Exception ex){
            Log.e("#002","資料表建立或開啟錯誤");
        }
    }

    public void insertDiaryData(String  date,String content){
        try {
        ContentValues row = new ContentValues();
        row.put("日期", date);
        row.put("日記內容", content);
        db.insert(SQLiteTable_Name, null, row);
        Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s", SQLiteTable_Name, "日期", date, "日記內容",content));
        } catch (Exception e) {
            Log.e("#003", "資料列新增失敗");
        }
    }

    public void updateDiaryData(int id , String  date,String content){
        try {
            ContentValues row = new ContentValues();
            row.put("日期", date);
            row.put("日記內容", content);
            db.update(SQLiteTable_Name, row, "_id=" + id, null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s,%s,%s,%s", SQLiteTable_Name, "日期", date, "日記內容", content));
        } catch (Exception e) {
            Log.e("#004", "資料列更新失敗");
        }
    }

    public void deleteDiaryData(int Delete_id) {
        try {
            db.delete(SQLiteTable_Name, "_id=" + Delete_id, null);
            Log.v("刪除資料列", String.format("在%s刪除一筆資料：%s=%d", SQLiteTable_Name, "_id", Delete_id));
        } catch (Exception ex) {
            Log.e("#005", "刪除資料列錯誤");
        }
    }

    public void AddDiaryData(){
        String  date[]={"2017-11-22","2017-11-08","2017-10-18","2017-11-22","2017-12-08","2017-11-01","2017-10-10","2017-11-07","2017-10-27","2017-10-08","2017-11-27","2017-09-02","2017-11-13","2017-10-03","2017-11-19","2017-09-08","2017-10-19","2017-12-06","2017-09-10"
        };
        String content[]={"嗷嗷嗷嗷 開學第一天，希望能交到好朋友(握拳"
                ,"今天跟我前面那位同學搭話了誒 -////- 好開薰!!，她竟然也(誒?)是個腐女!!((發現了大秘密=口="
                ,"昨天下了一場大雨，稀哩嘩啦的，讓我想唱「昨天我打從你門前過你正提著水桶往外潑潑在我的皮鞋上路上的行人笑的呵呵呵你什麼話也沒有對我說" +
                "，你只是瞇著眼睛望著我~嚕啦啦嚕啦啦嚕啦嚕拉勒嚕啦嚕啦嚕啦嚕啦嚕啦勒嚕啦啦嚕啦啦...」都是HOWHOW的歌洗腦我TAT"
                ,"今天我睡過頭了，到學校時已經中午了，然後班上開始流行一個奇怪的動作，護額頭 啥?? 黑人問號.jpg"
                ,"運動會要到了~~~~可是不干高三生的事，好悲哀Q口Q"
                ,"今天要考數學，但我昨天再看動漫因此忘記了，然後數學課發考卷時，被老師說我學壞了…小失落 ._."
                ,"好緊張喔 星期五就要來看專題了，雖說不會打成績，但印象很重要RRR><","剛考完段考，我們又要換位子了OuO，希望可以換個好位子"
                ,"「這麼多年的兄弟\r\n有誰比我更了解你\r\n太多太多不容易\r\n磨平了歲月和脾氣\r\n時間轉眼就過去\r\n這身後不散的筵席\r\n只因為我們還在\r\n" +
                "心留在原地\r\n張開手 需要多大的勇氣\r\n這片天 你我一起撐起\r\n更努力 只為了我們想要的明天\r\n好好的 這份情好好珍惜\r\n我們不一樣\r\n" +
                "每個人都有不同的境遇\r\n我們在這裡\r\n在這裡等你\r\n我們不一樣\r\n雖然會經歷不同的事情\r\n我們都希望\r\n來生還能相遇\r\n這麼多年的兄弟\r\n" +
                "有誰比我更了解你\r\n太多太多不容易\r\n磨平了歲月和脾氣\r\n時間轉眼就過去\r\n這身後不散的筵席\r\n\r\n只因為我們還在\r\n心留在原地\r\n" +
                "張開手 需要多大的勇氣\r\n這片天 你我一起撐起\r\n更努力 只為了我們想要的明天\r\n好好的 這份情好好珍惜\r\n我們不一樣\r\n" +
                "每個人都有不同的境遇\r\n我們在這裡\r\n在這裡等你\r\n我們不一樣\r\n雖然會經歷不同的事情\r\n我們都希望\r\n來生還能相遇\r\n" +
                "我們不一樣\r\n每個人都有不同的境遇\r\n我們在這裡\r\n在這裡等你\r\n我們不一樣\r\n雖然會經歷不同的事情\r\n我們都希望\r\n" +
                "來生還能相遇\r\n我們不一樣\r\n雖然會經歷不同的事情\r\n我們都希望\r\n來生還能相遇\r\n我們都希望\r\n來生還能相遇」回家載來聽~ O_O"
                ,"涵涵見不得我好，不讓我只寫9篇日記，所以我來寫第10篇日記了哼哼 >皿<凸",
                "今天天氣真好","麻麻說我壞壞QAQ","今天的紀錄是"
                ,"喔喔喔喔喔倒數200天了","今天學壞了","老師說我交到壞朋友了","我推薦的BL小說","安安是個磨人的小妖精  030","19篇日記完成"};
        for(int i=0;i< date.length&&i<content.length;i++){
            insertDiaryData( date[i],content[i]);
        }
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s'  ORDER BY 日期 DESC",SQLiteTable_Name),null);
    }

    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }

}
