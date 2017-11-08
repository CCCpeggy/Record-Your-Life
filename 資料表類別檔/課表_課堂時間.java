public class DairyDbTable {
    private  String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    private final static String SQLiteTable_Name= "課表_課堂時間"; //資料表的名字
    private final static String CREATE_Dairy_TABLE=
            "CREATE TABLE if not exists '課表'(" +
                    "_id INTEGER  PRIMARY KEY NOT NULL," +
                    "'課表' INTEGER NOT NULL," +
                    "'日記內容' TEXT)";
    public DairyDbTable(String path, SQLiteDatabase Database) {
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

    public void insertDairyData(int table,string time_start,string time_end){ //不用第一的ID
        try {
        ContentValues row = new ContentValues();
        row.put("課表ID", table);
		row.put("開始時間",time_start);
        row.put("結束時間",time_end);
        db.insert(SQLiteTable_Name, null, row);
        Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s,%s=%s", SQLiteTable_Name,"課表ID", table,"開始時間",time_start,"結束時間",time_end));
        } catch (Exception e) {
            Log.e("#003", "資料列新增失敗");
        }
    }

    public void updateDairyData(int id,int table,string time_start,string time_end){ 
        try {
            ContentValues row = new ContentValues();
            row.put("課表ID", table);
			row.put("開始時間",time_start);
			row.put("結束時間",time_end);
            db.update(SQLiteTable_Name, row, "_id=" + id, null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s,%s", SQLiteTable_Name,"課表ID", table,"開始時間",time_start,"結束時間",time_end));
        } catch (Exception e) {
            Log.e("#004", "資料列更新失敗");
        }
    }

    public void deleteDairyData(int Delete_id) { //不用改
        try {
            db.delete(SQLiteTable_Name, "_id=" + Delete_id, null);
            Log.v("刪除資料列", String.format("在%s刪除一筆資料：%s=%d", SQLiteTable_Name, "_id", Delete_id));
        } catch (Exception ex) {
            Log.e("#005", "刪除資料列錯誤");
        }
    }

    public void AddDairyData(){
		int table[]={1,1,1,1,1,1,1,2,2,3,3,3};
		string time_start[]={"08:20","09:20","10:20","11:20","13:10","14:10","15:10","18:20","20:10","17:00","17:00","17:00"};
		string time_end[]={"09:10","10:10","11:10","12:10","14:00","15:00","16:00","19:50","21:30","21:00","22:00","23:00"}
        for(int i=0;i< date.length&&i<content.length;i++){
            insertDairyData(table[i],time_start[i],time_end[i]);
        }
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s'",SQLiteTable_Name),null);
    }

    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }

}
