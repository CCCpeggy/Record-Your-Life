public class DairyDbTable {
    private  String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    private final static String SQLiteTable_Name= "�Ҹ�_��{_�M��"; //��ƪ��W�r
    private final static String CREATE_Dairy_TABLE=
            "CREATE TABLE if not exists '�Ҫ�'(" +
                    "_id INTEGER  PRIMARY KEY NOT NULL," +
                    "'�Ҫ�' INTEGER NOT NULL," +
                    "'��O���e' TEXT)";
    public DairyDbTable(String path, SQLiteDatabase Database) {
        SQLiteDB_Path = path;
        db = Database;
    }

    //���}�ηs�W��ƪ�
    public void OpenOrCreateTb(){
        try{
            db.execSQL(CREATE_Dairy_TABLE);
            Log.v("��ƪ�","��ƪ�إߩζ}�Ҧ��\");
        }catch (Exception ex){
            Log.e("#002","��ƪ�إߩζ}�ҿ��~");
        }
    }

    public void insertDairyData(int id,int schedule){ //���βĤ@��ID
        try {
        ContentValues row = new ContentValues();
		row.put("_id", id);
        row.put("��{ID", schedule);
        db.insert(SQLiteTable_Name, null, row);
        Log.v("�s�W��ƦC", String.format("�b%s�s�W�@����ơG%s=%s,%s=%s", SQLiteTable_Name,"_id",id,"��{ID",schedule));
        } catch (Exception e) {
            Log.e("#003", "��ƦC�s�W����");
        }
    }

    public void updateDairyData(int id,int schedule){ 
        try {
            ContentValues row = new ContentValues();
            row.put("_id",id);
			row.put("��{ID",schedule);
            db.update(SQLiteTable_Name, row, "_id=" + id, null);
            Log.v("��s��ƦC", String.format("�b%s��s�@����ơG%s,%s", SQLiteTable_Name,"_id",id,"��{ID",schedule));
        } catch (Exception e) {
            Log.e("#004", "��ƦC��s����");
        }
    }

    public void deleteDairyData(int Delete_id) { //���Χ�
        try {
            db.delete(SQLiteTable_Name, "_id=" + Delete_id, null);
            Log.v("�R����ƦC", String.format("�b%s�R���@����ơG%s=%d", SQLiteTable_Name, "_id", Delete_id));
        } catch (Exception ex) {
            Log.e("#005", "�R����ƦC���~");
        }
    }

    public void AddDairyData(){
        int id[]={6,4,8,8,16,12,17,8,14,5,1,3,10,10};
		int schedule[]={1,2,4,6,7,8,10,11,13,14,15,16,17,19};
        for(int i=0;i< date.length&&i<content.length;i++){
            insertDairyData( id[i],schedule[i]);
        }
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s'",SQLiteTable_Name),null);
    }

    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }

}
