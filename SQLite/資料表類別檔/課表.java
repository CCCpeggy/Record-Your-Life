public class DairyDbTable {
    private  String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    private final static String SQLiteTable_Name= "�Ҫ�"; //��ƪ��W�r
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

    public void insertDairyData(String name,String date,boolean main,String schedule_start,String schedule_end,boolean isReplace){ //���βĤ@��ID
        try {
        ContentValues row = new ContentValues();
		row.put("�Ҫ�W��", name);
        row.put("�Ҫ�Ѽ�", date);
		row.put("�D�n", main);
        row.put("�Ҫ�}�l��", schedule_start);
        row.put("�Ҫ�����", schedule_end);
		row.put("�ɶ����ƮɬO�_���N", isReplace);
        db.insert(SQLiteTable_Name, null, row);
        Log.v("�s�W��ƦC", String.format("�b%s�s�W�@����ơG%s=%s,%s=%s,%s=%s,%s=%s,%s=%s,%s=%s", SQLiteTable_Name, "�Ҫ�W��", name, "�Ҫ�Ѽ�", date,"�D�n", main,"�Ҫ�}�l��2", schedule_start,"�Ҫ�����2", schedule_end,"�ɶ����ƮɬO�_���N", isReplace));
        } catch (Exception e) {
            Log.e("#003", "��ƦC�s�W����");
        }
    }

    public void updateDairyData(int id,String name,String date,boolean main,String schedule_start,String schedule_end,boolean isReplace){ 
        try {
            ContentValues row = new ContentValues();
            row.put("�Ҫ�W��", name);
			row.put("�Ҫ�Ѽ�", date);
			row.put("�D�n", main);
			row.put("�Ҫ�}�l��2", schedule_start);
			row.put("�Ҫ�����2", schedule_end);
			row.put("�ɶ����ƮɬO�_���N", isReplace);
            db.update(SQLiteTable_Name, row, "_id=" + id, null);
            Log.v("��s��ƦC", String.format("�b%s��s�@����ơG%s,%s,%s,%s,%s,%s", SQLiteTable_Name,"�Ҫ�W��",name,"�Ҫ�Ѽ�",date,"�D�n",main,"�Ҫ�}�l��2",schedule_start,"�Ҫ�����2",schedule_end,"�ɶ����ƮɬO�_���N",isReplace));
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
        String  name[]={"�Ǯ�,�ɲ߯Z,����"};
		int  date[]={5,3,2};
		boolean  main[]={TRUE,FALSE,FALSE};
		String  sch1[]={"2017/10/18","2017/10/30","2017/11/5"};
		String  sch2[]={"2018/1/26","2018/2/7","2018/2/13"};
		boolean  yn[]={FALSE,FALSE,FALSE};
        for(int i=0;i< date.length&&i<content.length;i++){
            insertDairyData( name[i],date[i],main[i],schedule_start[i],schedule_end[i],isReplace[i]);
        }
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s'",SQLiteTable_Name),null);
    }

    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }

}
