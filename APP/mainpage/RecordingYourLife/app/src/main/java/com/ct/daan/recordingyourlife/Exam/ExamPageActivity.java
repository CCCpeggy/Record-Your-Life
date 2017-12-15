package com.ct.daan.recordingyourlife.Exam;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.ct.daan.recordingyourlife.Class.OthersFunction;
import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.Table.ClassDbTable;
import com.ct.daan.recordingyourlife.Table.ClassWeekDbTable;
import com.ct.daan.recordingyourlife.Table.ExamDbTable;
import com.ct.daan.recordingyourlife.Table.SubjectDbTable;
import com.ct.daan.recordingyourlife.Table.TableDbTable;
import com.ct.daan.recordingyourlife.Table.WeekDbTable;

import java.util.Locale;

public class ExamPageActivity extends AppCompatActivity {
    private final static String SQLiteDB_Path="student_project.db";
    private SQLiteDatabase db=null;
    Spinner Subject_sp,Class_sp,Table_sp;
    EditText Name_et,Date_et,Content_et,Score_et;
    Button Complete_btn;
    Intent intent;
    int days;
    private final static int UPDATEPAGE_EXAMPAGE=0,ADD_EXAMPAGE=1,RESULT_DELETE=100;
    int Subject_id,Exam_id,ClassWeek_id,Week_id,Table_id;
    SubjectDbTable SubjectDb;
    TableDbTable TableDb;
    WeekDbTable WeekDb;
    ClassWeekDbTable ClassWeekDb;
    ClassDbTable ClassDb;
    ExamDbTable ExamDb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_page);

        initView();
    }

    //打開或新增資料庫
    private void OpOrCrDb(){
        try{
            db=openOrCreateDatabase(SQLiteDB_Path,MODE_PRIVATE,null);
            Log.v("資料庫","資料庫載入成功");
        }catch (Exception ex){
            Log.e("#001","資料庫載入錯誤");
        }
    }
    private void initDataBase(){
        OpOrCrDb();
        TableDb= new TableDbTable(SQLiteDB_Path,db);
        TableDb.OpenOrCreateTb();
        TableDb.deleteAllRow();
        TableDb.AddTalbeData();

        SubjectDb=new SubjectDbTable(SQLiteDB_Path,db);
        SubjectDb.OpenOrCreateTb();
        SubjectDb.deleteAllRow();
        SubjectDb.AddSubjectData();

        ClassDb=new ClassDbTable(SQLiteDB_Path,db);
        ClassDb.OpenOrCreateTb();

        WeekDb=new WeekDbTable(SQLiteDB_Path,db);
        WeekDb.OpenOrCreateTb();
        WeekDb.deleteAllRow();
        WeekDb.AddWeekData();

        ClassWeekDb=new ClassWeekDbTable(SQLiteDB_Path,db);
        ClassWeekDb.OpenOrCreateTb();
        ClassWeekDb.deleteAllRow();
        ClassWeekDb.AddClassWeekData();

        ExamDb=new ExamDbTable(SQLiteDB_Path,db);
        ExamDb.OpenOrCreateTb();
        ExamDb.deleteAllRow();
        ExamDb.AddExamData();
    }
    private void putValue(){
        //加入Table_sp資料
        Cursor Table_cursor=TableDb.getCursor();
        if(Table_cursor.getCount()>0) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_dropdown_item, Table_cursor, new String[]{"課表名稱"}, new int[]{android.R.id.text1}, 0);
            Table_sp.setAdapter(adapter);
            Table_sp.setOnItemSelectedListener(Table_Listener);
        }

        //加入Subject_sp資料
        Cursor cursor=SubjectDb.getCursor();
        if(cursor.getCount()>0) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_dropdown_item, cursor, new String[]{"科目名稱"}, new int[]{android.R.id.text1}, 0);
            Subject_sp.setAdapter(adapter);
            Subject_sp.setOnItemSelectedListener(Subject_Listener);
        }

        //放入資料
        intent=getIntent();
        Bundle extra=intent.getExtras();

        Exam_id=extra.getInt("SELECTED_ID");
        Date_et.setText(extra.getString("SELECTED_DATE"));
        ClassWeek_id=extra.getInt("SELECTED_CLASS");
        Table_id=ClassWeekDb.getTable_id(ClassWeek_id) ;
        Content_et.setText(extra.getString("SELECTED_CONTENT"));
        Score_et.setText(extra.getInt("SELECTED_SCORE")==-100?"":extra.getInt("SELECTED_SCORE")+"");
        Name_et.setText(extra.getString("SELECTED_NAME"));

        Log.v("傳入Table_id",Table_id+"");

        try {
            setSpinnerByValue(Table_sp, Table_id, TableDb.getCursor(), 0);
            setSpinnerByValue(Class_sp, ClassWeek_id, ClassWeekDb.getCursor(Table_id), 0);
            setSpinnerByValue(Subject_sp, extra.getInt("SELECTED_SUBJECT"), cursor, 0);

        }catch (Exception e){

        }
    }
    private void initView(){
        Subject_sp=(Spinner) findViewById(R.id.Subject_sp);
        Table_sp=(Spinner)findViewById(R.id.Table_sp);
        Class_sp=(Spinner) findViewById(R.id.Class_sp);
        Name_et=(EditText) findViewById(R.id.Name_et);
        Date_et=(EditText) findViewById(R.id.Date_et);
        Content_et=(EditText) findViewById(R.id.Content_et);
        Score_et=(EditText) findViewById(R.id.Score_et);
        Complete_btn=(Button)findViewById(R.id.btn_Complete);

        Date_et.setInputType(InputType.TYPE_NULL);

        Date_et.setOnClickListener(DatePick_Listener);
        Complete_btn.setOnClickListener(Complete_btn_Listener);

        initDataBase();

        putValue();
        //setSpinnerByValue(Class_sp);
        //Date_et.setText(extra.getString("SELECTED_DATE").equals("")?"日期":extra.getString("SELECTED_DATE"));
        //et2.setText(extra.getString("SELECTED_CONTENT"));
        //Score_Enabled(Date_et.getText().toString());
    }
    String tmp_Subject="";
    private Spinner.OnItemSelectedListener Subject_Listener=new Spinner.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            Cursor cursor=(Cursor)parent.getSelectedItem();
            String SelectItemName=cursor.getString(1);
            if(Name_et.getText().toString().equals(tmp_Subject)||Name_et.getText().toString().equals(""))
                Name_et.setText(SelectItemName);
            Subject_id=cursor.getInt(0);
            tmp_Subject=SelectItemName;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    };
    Cursor Class_cursor;
    private Spinner.OnItemSelectedListener Table_Listener=new Spinner.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Cursor Table_cursor=(Cursor)parent.getSelectedItem();

            //取得第幾天
            Calendar cal=StringtoCalendar( Date_et.getText().toString());
            int Test_dayOfWeek=cal.get(Calendar.DAY_OF_WEEK)-1;//考試星期

            cal=StringtoCalendar(Table_cursor.getString(4));
            int Start_dayOfWeek=cal.get(Calendar.DAY_OF_WEEK)-1;//課表開始星期
            Log.v("星期", String.format("今天：%s,課表開始%s",Test_dayOfWeek,Start_dayOfWeek));

            int days=Test_dayOfWeek-Start_dayOfWeek+1;
            days+=days<0?7:0;

            //取得星期cursor
            Cursor Week_cursor;
            Week_cursor = WeekDb.getCursor(Table_cursor.getInt(0),days);
            if(Week_cursor.getCount()<=0){
                Class_sp.setAdapter(null);
                return;
            }
            Week_cursor.moveToFirst();
            Week_id=Week_cursor.getInt(0);
            //測試取得資料
            /*do
                Log.v("Week_cursor", Week_cursor.getInt(0)+ "/"+Week_cursor.getInt(1)+ "/"+Week_cursor.getInt(2));
            while(Week_cursor.moveToNext());*/

            //取得Classcursor
            /*Cursor Class_cursor;
            Class_cursor = ClassDb.getCursor(Table_cursor.getInt(0));
            if(Class_cursor.getCount()<=0){
                Class_sp.setAdapter(null);
                return;
            }
            Class_cursor.moveToFirst();*/

            //取得ClassWeekcursor
            Cursor ClassWeek_cursor;
            ClassWeek_cursor = ClassWeekDb.getCursor(Table_cursor.getInt(0));
            if(ClassWeek_cursor.getCount()<=0){
                Class_sp.setAdapter(null);
                return;
            }
            ClassWeek_cursor.moveToFirst();



            //加入_sp資料
            if(ClassWeek_cursor.getCount()>0) {
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(ExamPageActivity.this, android.R.layout.simple_spinner_dropdown_item, ClassWeek_cursor, new String[]{"開始時間"}, new int[]{android.R.id.text1}, 0);
                Class_sp.setAdapter(adapter);
                Class_sp.setOnItemSelectedListener(Class_Listener);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    };

    private Spinner.OnItemSelectedListener Class_Listener=new Spinner.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor=(Cursor)parent.getSelectedItem();
            ClassWeek_id=cursor.getInt(0);

            Calendar cal= Calendar.getInstance();

            //String cmd=String.format("SELECT S._id  FROM '%s' AS C,'%s' AS S WHERE  C.星期ID = %s AND C._id = %s AND C.科目ID = S._id ",ClassDb.SQLiteTable_Name,SubjectDb.SQLiteTable_Name,Week_id,ClassWeek_id);
            //Log.v("db.rawQuery",cmd);
            //Cursor Subject_cursor=db.rawQuery(cmd,null);
            //Cursor Subject_cursor=db.rawQuery(String.format("SELECT S.科目名稱  FROM '%s' AS C WHERE 星期ID = %s INNER JOIN '%s' AS S ON  C._id = S._id ",ClassDb.SQLiteTable_Name,Class_id,SubjectDb.SQLiteTable_Name),null);

            //Subject_cursor.moveToFirst();
            //Log.v("Subject_cursor",Subject_cursor.getInt(0)+"");

            Class_cursor=ClassDb.getCursor(ClassWeek_id,Week_id);
            if(Class_cursor.getCount()<=0)return;
            Class_cursor.moveToFirst();

            //setSpinnerByValue(Subject_sp,Class_cursor.getInt(2),SubjectDb.getCursor(),0);
            //測試取得資料
            /*do
                            Log.v("Subject_cursor",Subject_cursor.getInt(0)+"");
                        while(Subject_cursor.moveToNext());*/
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    };

    Calendar m_Calendar = Calendar.getInstance();
    private EditText.OnClickListener DatePick_Listener= new EditText.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog dataPick=new DatePickerDialog(ExamPageActivity.this,datepicker,
                    m_Calendar.get(Calendar.YEAR),
                    m_Calendar.get(Calendar.MONTH),
                    m_Calendar.get(Calendar.DAY_OF_MONTH));
            dataPick.show();
        }


    };


    DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            m_Calendar.set(Calendar.YEAR, year);
            m_Calendar.set(Calendar.MONTH, monthOfYear);
            m_Calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
            Date_et.setText(sdf.format(m_Calendar.getTime()));

            Calendar today= Calendar.getInstance();
            myFormat = "yyyyMMdd";
            sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);

            if(Integer.parseInt(sdf.format(m_Calendar.getTime()))> Integer.parseInt(sdf.format(today.getTime()))){
                Score_et.setText("");
                Score_et.setEnabled(false);
            }
            else{
                Score_et.setEnabled(true);
            }

            Score_Enabled(m_Calendar);
        }
    };

    private void Score_Enabled(Calendar Calendar){
        android.icu.util.Calendar today=Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.TAIWAN);

        if(Integer.parseInt(sdf.format(Calendar.getTime()))> Integer.parseInt(sdf.format(today.getTime()))){
            Score_et.setText("");
            Score_et.setEnabled(false);
        }
        else{
            Score_et.setEnabled(true);
        }
    }

    private void Score_Enabled(String date){
        Score_Enabled(StringtoCalendar(date));
    }

    private Calendar StringtoCalendar(String date){
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN);
        Calendar Calendar= android.icu.util.Calendar.getInstance();
        try{
            Calendar.setTime(sdf2.parse(date));
        }catch (Exception e){
            Toast.makeText(ExamPageActivity.this,"日期格式不符合 yyyy-MM-dd", Toast.LENGTH_SHORT);
        }
        return Calendar;
    }

    private boolean IsStringtoCalendar(String date){
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN);
        Calendar Calendar= android.icu.util.Calendar.getInstance();
        try{
            Calendar.setTime(sdf2.parse(date));
        }catch (Exception e){
            return false;
        }
        return true;
    }



    private Button.OnClickListener Complete_btn_Listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            String score= Score_et.getText().toString();
            /*if(score.equals("")){
                OthersFunction othersFunction=new OthersFunction();
                othersFunction.setReminder(ExamPageActivity.this,);
            }*/
            ExamDb.updateExamData(Exam_id,ClassWeek_id,Subject_id,Date_et.getText().toString(),Name_et.getText().toString(),Content_et.getText().toString(),score.equals("")?-100:Integer.parseInt(score));
            setResult(RESULT_OK,intent);
            finish();
        }
    };

    private Button.OnClickListener Delete_btn_Listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult(RESULT_DELETE,intent);
            finish();
        }
    };

    /*private void toInt(String Name,EditText et){
        int i;
        try {
            Log.e("1","1");
            String text=et.getText().toString();
            Log.e("1","1");
            if(text.isEmpty()){
                intent.putExtra("CHANGED_SCORE", 0);
                return;
            }
            i=Integer.parseInt(text);
            Log.e("1","1");
            intent.putExtra("CHANGED_SCORE", i);
            Log.e("1","1");
            return;
        }
        catch (Exception e){
            Log.e("1","1");
            intent.putExtra("CHANGED_SCORE", 0);
            Log.e("1","1");
        }

    }*/

    private boolean Isformat(){
        if(!IsStringtoCalendar(Date_et.getText().toString())){
            Toast.makeText(ExamPageActivity.this,"日期格式錯誤 yyyy-MM-dd", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void setSpinnerByValue(Spinner spinner, int value, Cursor cursor, int Col){
        if(cursor==null|| cursor.getCount()<=0)return;
        cursor.moveToFirst();
        do{
            Log.v("比對資料", String.format("%s=%s,%s=%s,%s=%s",cursor.getColumnName(0),cursor.getInt(0),cursor.getColumnName(1),cursor.getString(1),"value",value));
            if (value==cursor.getInt(Col)) {
                spinner.setSelection(cursor.getPosition(), true);
                break;
            }
        }while(cursor.moveToNext());
        cursor.moveToFirst();
    }
}
