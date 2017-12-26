package com.ct.daan.recordingyourlife.DbTable;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.util.Log;

import java.util.Locale;

public class EventDbTable {

    private String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    public String SQLiteTable_Name= "日子"; //資料表的名字
    private String CREATE_NOTE_TABLE="CREATE TABLE if not exists '"+SQLiteTable_Name+"'(" +
            "_id INTEGER  PRIMARY KEY NOT NULL," +
            "'日子名稱' TEXT NOT NULL," +
            "'日期開始' TEXT NOT NULL," +
            "'日期結束' TEXT NOT NULL," +
            "'備註' TEXT)";

    public EventDbTable(String path, SQLiteDatabase Database) {
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

    public void insertEventData(String Name, String Start_day, String End_day, String Remark){
        try {
            ContentValues row = new ContentValues();
            row.put("日子名稱", Name);
            row.put("日期開始", Start_day );
            row.put("日期結束", End_day);
            row.put("備註", Remark);
            db.insert(SQLiteTable_Name, null, row);
            Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s,%s=%s,%s=%s", SQLiteTable_Name,"日子名稱",Name,"日期開始",Start_day,"日期結束",End_day,"備註", Remark));
        } catch (Exception e){
            Log.e("#003", "資料列新增失敗");
        }
    }

    public void updateEventData(int id, String Name, String Start_day, String End_day, String Remark){
        try {
            ContentValues row = new ContentValues();
            row.put("_id", id);
            row.put("日子名稱", Name);
            row.put("日期開始", Start_day );
            row.put("日期結束", End_day);
            row.put("備註", Remark);
            db.update(SQLiteTable_Name, row, "_id=" + id, null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s=%s,%s=%s,%s=%s,%s=%s", SQLiteTable_Name,"日子名稱",Name,"日期開始",Start_day,"日期結束",End_day,"備註", Remark));
        } catch (Exception e) {
            Log.e("#004", "資料列更新失敗");
        }
    }

    public void deleteEventData(int Delete_id) {
        try {
            db.delete(SQLiteTable_Name, "_id=" + Delete_id, null);
            Log.v("刪除資料列", String.format("在%s刪除一筆資料：%s=%d", SQLiteTable_Name, "_id", Delete_id));
        } catch (Exception ex) {
            Log.e("#005", "刪除資料列錯誤");
        }
    }

    public void AddEventData(){
        String Name[]={"光棍節","中秋節","第二次段考","第二次模擬考","端午節","運動會","國慶四天連假","教師節","聖誕節","妹妹生日","建功衝刺班報名","媽媽出國","元旦放假","專題報告發表","段考","休業式"};
        String Start_day[]={"2017-11-11","2017-10-04","2017-11-28","2017-12-19","2017-05-30","2017-10-27","2017-10-07","2017-09-28","2017-12-25","2017-12-29","2017-12-18","2017-12-31","2018-01-01","2018-01-05","2018-01-17","2018-01-19"};
        String End_day[]=  {"2017-11-11","2017-10-04","2017-11-29","2017-12-20","2017-05-30","2017-10-27","2017-10-10","2017-09-28","2017-12-25","2017-12-29","2017-12-31","2018-01-07","2018-01-01","2018-01-05","2018-01-18","2018-01-19"};
        String Remark[]={"Noooo","約個烤肉","範圍：","範圍：","訂了粽子","比自由式","準備考試","約同學會","妹妹生日","","","","","","",""};
        for(int i=0;i<Name.length;i++){
            insertEventData(Name[i],Start_day[i],End_day[i],Remark[i]);
        }
    }

    public Cursor getCursor(){
        Cursor cursor=db.rawQuery(String.format("SELECT *  FROM '%s'",SQLiteTable_Name),null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getCursor(String Where_cmd){
        String cmd= String.format("SELECT *  FROM '%s' WHERE %s",SQLiteTable_Name,Where_cmd);
        Log.v(SQLiteTable_Name+"_cmd",cmd);
        Cursor cursor=db.rawQuery(cmd,null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getCursor(int EventID){
        return getCursor("_id = "+EventID);
    }

    public String getName(int EventID){
        return getCursor(EventID).getString(1);
    }

    public String getStartDay(int EventID){
        return getCursor(EventID).getString(2);
    }

    public Cursor getCursorByDay(String Day){
        Calendar cal=StringtoCalendar(Day);
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH)+1;
        int date=cal.get(Calendar.DAY_OF_MONTH);

        String cmd= String.format("((YEAR < '%04d') OR ",year,month,date);
        cmd += String.format("(YEAR = '%04d' AND MONTH < '%02d') OR ",year,month,date);
        cmd += String.format("(YEAR = '%04d' AND MONTH = '%02d' AND  DATE <= '%02d')) AND ",year,month,date);

        cmd += String.format("((END_YEAR > '%04d') OR ",year,month,date);
        cmd += String.format("(END_YEAR = '%04d' AND END_MONTH > '%02d') OR ",year,month,date);
        cmd += String.format("(END_YEAR = '%04d' AND END_MONTH = '%02d' AND  END_DATE >= '%02d'))",year,month,date);
        //String cmd=String.format("YEAR <= '%d'",year,month,date);
        Log.v(SQLiteTable_Name+"_cmd",cmd);
        return getCursorHasDay(cmd);
    }

    public Cursor getCursorHasDay(String Where_cmd){
        String cmd="SELECT ";
        cmd+= String.format("%s AS '%s',%s AS '%s',%s AS '%s',%s AS '%s',","_id","_id","日子名稱","日子名稱","日期開始","日期開始","日期結束","日期結束");
        cmd+= String.format("%s AS %s,%s AS %s,%s AS %s,","strftime('%Y',日期開始)","YEAR","strftime('%m',日期開始) ","MONTH"," strftime('%d',日期開始) ","DATE");
        cmd+= String.format("%s AS %s,%s AS %s,%s AS %s ","strftime('%Y',日期結束)","END_YEAR","strftime('%m',日期結束) ","END_MONTH"," strftime('%d',日期結束) ","END_DATE");
        cmd+="FROM '"+SQLiteTable_Name+"' WHERE "+Where_cmd;
        Log.v(SQLiteTable_Name+"_cmd",cmd);
        Cursor cursor=db.rawQuery(cmd,null);
        cursor.moveToFirst();
        return cursor;
    }

    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);

    }

    public void LogRow(Cursor cursor_row){
        Log.v(SQLiteTable_Name+"_EventCurosr", String.format("%s=%s,%s=%s,%s=%s,%s=%s,%s=%s", "id",cursor_row.getInt(0),"日子名稱", cursor_row.getString(1),"日期開始", cursor_row.getString(2),"日期結束", cursor_row.getString(3),"備註", cursor_row.getString(4)));
    }

    private Calendar StringtoCalendar(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN);
        Calendar Calendar= android.icu.util.Calendar.getInstance();
        try{
            Calendar.setTime(sdf.parse(date));
        }catch (Exception e){
            Log.v("日期格式不符合",date);
        }
        return Calendar;
    }


}
