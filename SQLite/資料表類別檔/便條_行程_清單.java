public class DairyDbTable {
    private  String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    private final static String SQLiteTable_Name= "便條_行程_清單"; //資料表的名字
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

    public void insertDairyData(int note){ //不用第一的ID
        try {
        ContentValues row = new ContentValues();
        row.put("便條ID", note);
        db.insert(SQLiteTable_Name, null, row);
        Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s", SQLiteTable_Name,"便條ID",note));
        } catch (Exception e) {
            Log.e("#003", "資料列新增失敗");
        }
    }

    public void updateDairyData(int id,int note){ 
        try {
            ContentValues row = new ContentValues();
			row.put("便條ID", note);
            db.update(SQLiteTable_Name, row, "_id=" + id, null);
            Log.v("更新資料列", String.format("在%s更新一筆資料：%s,%s", SQLiteTable_Name,"便條ID",note));
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
        int note[]={4,3,19,6,3,7,8,1,11,14,2,13,7,2,9,19,17,7,5};
        for(int i=0;i< date.length&&i<content.length;i++){
            insertDairyData(note[i]);
        }
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s'",SQLiteTable_Name),null);
    }

    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }

}
