package com.ct.daan.recordingyourlife.Table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by info on 2017/11/5.
 */

public class All_Table extends TableDbTable {
    private SQLiteDatabase db = null;
    ClassDbTable ClassDb;
    SubjectDbTable SubjectDb;
    ClassWeekDbTable ClassWeekDb;
    WeekDbTable WeekDb;
    Cursor cursor;
    private int Table_id;

    public All_Table(String path, SQLiteDatabase Database,int id) {
        super(path, Database);


        initAllTable(path,Database);
        AddTableData();
        setTable(id);
    }

    public All_Table(String path, SQLiteDatabase Database) {
        super(path, Database);

        initAllTable(path,Database);
        AddTableData();

        setTable(getMain_id());
    }
    public All_Table(String path, SQLiteDatabase Database,String TableName,int days,int isMain,String schedule_start,String schedule_end,int isReplace,String[][] subject,String time_start[],String time_end[]) {
        super(path, Database);
        initAllTable(path,Database);
        AddTableData();
        int table_id=newTable(TableName,days,isMain,schedule_start,schedule_end,isReplace,subject,time_start,time_end);
        setTable(table_id);
    }


    private int newTable(String TableName,int days,int isMain,String schedule_start,String schedule_end,int isReplace,String[][] subject,String time_start[],String time_end[]){
        super.insertTableData(TableName,days,isMain,schedule_start,schedule_end,isReplace);
        int table_id=super.getTable_id(TableName);
        for (int i=0;i<subject[0].length;i++){
            WeekDb.insertWeekData(table_id,i+1);
        }
        for (int i=0;i<subject.length;i++){
            ClassWeekDb.insertClassWeekData(table_id,time_start[i],time_end[i]);
            int classWeek_id=ClassWeekDb.getClassWeek_id(table_id,time_start[i]);
            for (int j=0;j<subject[i].length;j++){
                int week_id=WeekDb.getWeek_id(table_id,j+1);
                ClassDb.insertClassData(classWeek_id,week_id,SubjectDb.getSubjectID(subject[i][j]));
            }
        }

        return table_id;
    }

    public void setTable(int id){
        Table_id=id;
        cursor=super.getCursor("_id = "+id);
        cursor.moveToFirst();
    }

    public void initAllTable(String path, SQLiteDatabase db){
        ClassDb=new ClassDbTable(path,db);
        SubjectDb=new SubjectDbTable(path,db);
        ClassWeekDb=new ClassWeekDbTable(path,db);
        WeekDb=new WeekDbTable(path,db);

        ClassDb.OpenOrCreateTb();
        SubjectDb.OpenOrCreateTb();
        ClassWeekDb.OpenOrCreateTb();
        WeekDb.OpenOrCreateTb();
        super.OpenOrCreateTb();
    }


    public void AddTableData(){
        /*ClassDb.deleteAllRow();
        ClassDb.AddClassData();
        SubjectDb.deleteAllRow();
        SubjectDb.AddSubjectData();
        ClassWeekDb.deleteAllRow();
        ClassWeekDb.AddClassWeekData();
        WeekDb.deleteAllRow();
        WeekDb.AddWeekData();
        super.deleteAllRow();
        super.AddTalbeData();*/
    }

    public Cursor getAllTableCursors() {
        return super.getCursor();
    }

    public Cursor getClassCursor(){
        return ClassDb.getCursor();
    }

    public Cursor getClassCursor(int ClassWeek_id,int Week_id){
        return ClassDb.getCursor("_id = "+ClassWeek_id+" AND 星期ID ="+Week_id);
    }

    public String getClassSubject(int ClassWeek_id,int Week_id){
        Cursor ClassCursor=getClassCursor(ClassWeek_id,Week_id);
        ClassCursor.moveToFirst();
        return SubjectDb.getSubjectName(ClassCursor.getInt(2));
    }

    public String getTableName(){
        cursor.moveToFirst();
        return cursor.getString(1);
    }

    public int getDayCount(){
        Cursor WeekCursor=getWeekCursor();
        int count=WeekCursor.getCount();
        Log.v("ClassDayCount",count+"");
        return count;
    }

    public int getClassWeekCount(){
        Cursor ClassWeekCursor=getClassWeekCursor();
        int count=ClassWeekCursor.getCount();
        Log.v("ClassWeekCount",count+"");
        return count;
    }

    public String[] ClassInOneDay(int Week_id){
        int ClassWeek_count=getClassWeekCount();
        if(ClassWeek_count<=0) return null;
        String Tables[]=new String[ClassWeek_count];
        Cursor ClassWeekCursor=getClassWeekCursor();
        ClassWeekCursor.moveToFirst();
        for (String Table:Tables) {
            int ClassWeek_id=ClassWeekCursor.getInt(0);
            Table=getClassSubject(ClassWeek_id,Week_id);
            Log.v("ClassInOneDay",String.format("表格[%d,%d]=%s",ClassWeek_id,Week_id,getClassSubject(ClassWeek_id,Week_id)));
            try{
                ClassWeekCursor.moveToNext();
            }catch (Exception ex){
            }
        }
        return Tables;
    }

    public String[] ClassInOneWeek(int ClassWeek_id){
        int days=getDayCount();
        if(days<=0) return null;
        String Tables[]=new String[days];
        Cursor WeekCursor=getWeekCursor();
        int Week_id;
        WeekCursor.moveToFirst();

        Week_id=WeekCursor.getInt(0);
        Tables[0]=getClassSubject(ClassWeek_id,Week_id);
        Log.v("ClassInOneDay",String.format("表格[%d,%d]=%s",ClassWeek_id,Week_id,getClassSubject(ClassWeek_id,Week_id)));
        for(int i=1;WeekCursor.moveToNext();i++){
            Week_id=WeekCursor.getInt(0);
            Tables[i]=getClassSubject(ClassWeek_id,Week_id);
            Log.v("ClassInOneDay",String.format("表格[%d,%d]=%s",ClassWeek_id,Week_id,getClassSubject(ClassWeek_id,Week_id)));
        }
        return Tables;
    }

    public String[][] ClassInTable(){
        int ClassWeek_count=getClassWeekCount();
        if(ClassWeek_count<=0) return null;
        String Tables[][]=new String[ClassWeek_count][];
        Cursor ClassWeekCursor=getClassWeekCursor();
        int ClassWeek_id;
        ClassWeekCursor.moveToFirst();

        ClassWeek_id=ClassWeekCursor.getInt(0);
        Log.v("ClassWeek_id",ClassWeek_id+"");
        Tables[0]=ClassInOneWeek(ClassWeek_id);
        for(int i=1;ClassWeekCursor.moveToNext();i++){
            ClassWeek_id=ClassWeekCursor.getInt(0);
            Log.v("ClassWeek_id",ClassWeek_id+"");
            Tables[i]=ClassInOneWeek(ClassWeek_id);
        }
        return Tables;
    }


    public Cursor getClassWeekCursor(){
        return ClassWeekDb.getCursor("課表ID = "+Table_id);
    }


    public Cursor getWeekCursor(){
        return WeekDb.getCursor("課表ID = "+Table_id);
    }

    public void updateWeek(int id,int table_id,int dayOfWeek){
        WeekDb.updateWeekData(id,table_id,dayOfWeek);
    }

    public void updateWeekClass(int id,int table_id,String time_start,String time_end){
        ClassWeekDb.updateClassWeekData(id,table_id,time_start,time_end);
    }

    public void updatClass(int ClassWeek_id,int week_id,int subject_id){
        ClassDb.updateClassData(ClassWeek_id,week_id,subject_id);
    }

    /*private int days(){
        Calendar cal=Calendar.getInstance();

        return ;
    }*/


}
