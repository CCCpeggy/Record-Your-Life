public class DairyDbTable {
    private  String SQLiteDB_Path = null;
    private SQLiteDatabase db = null;
    private final static String SQLiteTable_Name= "����"; //��ƪ��W�r
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

    public void insertDairyData(string name,string time_start,string day_start,string time_end,string day_end,boolean interruption){ //���βĤ@��ID
        try {
        ContentValues row = new ContentValues();
        row.put("�������", name);
		row.put("����",time_start);
        row.put("��{�}�l���",day_start);
		row.put("��{�����ɶ�", time_end);
		row.put("��{�������",day_end);
        row.put("�H�Ѥ��_",interruption);
        db.insert(SQLiteTable_Name, null, row);
        Log.v("�s�W��ƦC", String.format("�b%s�s�W�@����ơG%s=%s,%s=%s,%s=%s,%s=%s,%s=%s,%s=%s", SQLiteTable_Name,"��{�W��", name,"��{�}�l�ɶ�",time_start,time_start,"��{�}�l���",day_start,"��{�����ɶ�", time_end,"��{�������",day_end,"�H�Ѥ��_",interruption));
        } catch (Exception e) {
            Log.e("#003", "��ƦC�s�W����");
        }
    }

    public void updateDairyData(int id,string name,string time_start,string day_start,string time_end,string day_end,boolean interruption){ 
        try {
            ContentValues row = new ContentValues();
            row.put("��{�W��", name);
			row.put("��{�}�l�ɶ�",time_start);
			row.put("��{�}�l���",day_start);
			row.put("��{�����ɶ�", time_end);
			row.put("��{�������",day_end);
			row.put("�H�Ѥ��_",interruption);
            db.update(SQLiteTable_Name, row, "_id=" + id, null);
            Log.v("��s��ƦC", String.format("�b%s��s�@����ơG%s,%s,%s,%s,%s,%s", SQLiteTable_Name,"��{�W��", name,"��{�}�l�ɶ�",time_start,time_start,"��{�}�l���",day_start,"��{�����ɶ�", time_end,"��{�������",day_end,"�H�Ѥ��_",interruption));
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
		String name[]={"��1���","��2���","��3���","��4���","��5���","��6���","��7���","��8���","��9���","��10���","��11���","��12���","��13���","��14���","��15���","��16���","��17���","��18���","��19���"};
		string time_start[]={"1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0"};
		string day_start[]={"2017/9/3","2017/10/12","2017/12/8","2017/9/15","2017/11/24","2017/11/7","2017/10/14","2017/10/15","2017/10/17","2017/10/31","2017/10/17","2017/10/26","2017/11/30","2017/9/13","2017/9/11","2017/11/3","2017/10/23","2017/11/5","2017/9/12"};
		string time_end[]={"1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0","1900/1/0"};
		string day_end[]={"2017/9/5","2017/10/18","2017/12/9","2017/9/17","2017/11/29","2017/11/10","2017/10/28","2017/10/19","2017/10/22","2017/11/6","2017/10/20","2017/10/31","2017/12/8","2017/9/17","2017/9/13","2017/11/8","2017/10/27","2017/11/13","2017/9/13"};
		boolean interruption[]={}
        for(int i=0;i< date.length&&i<content.length;i++){
            insertDairyData(name[i]time_start[i],day_start[i],time_end[i],day_end[i],interruption[i]);
        }
    }

    public Cursor getCursor(){
        return db.rawQuery(String.format("SELECT *  FROM '%s'",SQLiteTable_Name),null);
    }

    public void deleteAllRow(){
        db.execSQL("DELETE FROM "+SQLiteTable_Name);
    }

}
