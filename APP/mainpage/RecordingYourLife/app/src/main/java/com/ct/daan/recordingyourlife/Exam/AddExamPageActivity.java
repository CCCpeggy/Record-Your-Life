package com.ct.daan.recordingyourlife.Exam;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.ct.daan.recordingyourlife.Class.CalendarFunction;
import com.ct.daan.recordingyourlife.Class.OthersFunction;
import com.ct.daan.recordingyourlife.DbTable.ReminderDbTable;
import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.DbTable.ClassDbTable;
import com.ct.daan.recordingyourlife.DbTable.ClassWeekDbTable;
import com.ct.daan.recordingyourlife.DbTable.ExamDbTable;
import com.ct.daan.recordingyourlife.DbTable.SubjectDbTable;
import com.ct.daan.recordingyourlife.DbTable.TableDbTable;
import com.ct.daan.recordingyourlife.DbTable.WeekDbTable;

import java.util.Locale;

/**
 * Created by info on 2017/10/31.
 */
public class AddExamPageActivity extends AppCompatActivity {
    private final static String SQLiteDB_Path="student_project.db";
    private SQLiteDatabase db=null;
    Spinner Subject_sp,Class_sp,Table_sp;
    EditText Name_et,Date_et,Content_et,Score_et;
    Intent intent;
    int Subject_id,ClassWeek_id,Week_id;
    SubjectDbTable SubjectDb;
    TableDbTable TableDb;
    WeekDbTable WeekDb;
    ClassWeekDbTable ClassWeekDb;
    ClassDbTable ClassDb;
    ExamDbTable ExamDb;
    ReminderDbTable ReminderDb;
    CalendarFunction calendarFunction;
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_page);

        intent=getIntent();
        calendarFunction=new CalendarFunction();
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
    private void initView(){
        Subject_sp=(Spinner) findViewById(R.id.Subject_sp);
        Table_sp=(Spinner)findViewById(R.id.Table_sp);
        Class_sp=(Spinner) findViewById(R.id.Class_sp);
        Name_et=(EditText) findViewById(R.id.Name_et);
        Date_et=(EditText) findViewById(R.id.Date_et);
        Content_et=(EditText) findViewById(R.id.Content_et);
        Score_et=(EditText) findViewById(R.id.Score_et);

        Date_et.setInputType(InputType.TYPE_NULL);
        Date_et.setOnClickListener(DatePick_Listener);

        initDateBase();

        Cursor cursor=SubjectDb.getCursor();
        if(cursor.getCount()>0) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(AddExamPageActivity.this, android.R.layout.simple_spinner_dropdown_item, cursor, new String[]{"科目名稱"}, new int[]{android.R.id.text1}, 0);
            Subject_sp.setAdapter(adapter);
            Subject_sp.setOnItemSelectedListener(Subject_Listener);
        }
        Subject_sp.setOnContextClickListener(null);
    }

    private void initDateBase(){
        //TableDb資料庫建立
        OpOrCrDb();
        TableDb= new TableDbTable(SQLiteDB_Path,db);
        TableDb.OpenOrCreateTb();

        //SubjectDb資料庫建立
        SubjectDb=new SubjectDbTable(SQLiteDB_Path,db);
        SubjectDb.OpenOrCreateTb();

        //ClassDb資料庫建立
        ClassDb=new ClassDbTable(SQLiteDB_Path,db);
        ClassDb.OpenOrCreateTb();

        WeekDb=new WeekDbTable(SQLiteDB_Path,db);
        WeekDb.OpenOrCreateTb();

        ClassWeekDb=new ClassWeekDbTable(SQLiteDB_Path,db);
        ClassWeekDb.OpenOrCreateTb();

        ExamDb=new ExamDbTable(SQLiteDB_Path,db);
        ExamDb.OpenOrCreateTb();

        ReminderDb=new ReminderDbTable(SQLiteDB_Path,db);
        ReminderDb.OpenOrCreateTb();
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
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(AddExamPageActivity.this, android.R.layout.simple_spinner_dropdown_item, ClassWeek_cursor, new String[]{"開始時間"}, new int[]{android.R.id.text1}, 0);
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
            Class_cursor=ClassDb.getCursor(ClassWeek_id,Week_id);
            if(Class_cursor.getCount()<=0)return;
            Class_cursor.moveToFirst();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    };

    private void setDate(Calendar calendar) {
        Score_Enabled(calendar);
        String Date=calendarFunction.getDateString(calendar);
        //加入Table_sp資料
        Cursor Table_cursor=TableDb.getCursorBydate(Date,calendarFunction.getDayOfWeek(Date));
        if(Table_cursor.getCount()>0) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_dropdown_item, Table_cursor, new String[]{"課表名稱"}, new int[]{android.R.id.text1}, 0);
            Table_sp.setAdapter(adapter);
            Table_sp.setOnItemSelectedListener(Table_Listener);
        }else{
            Table_sp.setAdapter(null);
            Class_sp.setAdapter(null);
        }
    }

    Calendar m_Calendar = Calendar.getInstance();
    private EditText.OnClickListener DatePick_Listener= new EditText.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog dataPick=new DatePickerDialog(AddExamPageActivity.this,datepicker,
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
            m_Calendar = Calendar.getInstance();
            if(!Date_et.getText().toString().isEmpty()) m_Calendar=calendarFunction.DateTextToCalendarType(Date_et.getText().toString());
            m_Calendar.set(Calendar.YEAR, year);
            m_Calendar.set(Calendar.MONTH, monthOfYear);
            m_Calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
            Date_et.setText(sdf.format(m_Calendar.getTime()));

            setDate(m_Calendar);
        }
    };

    private void Score_Enabled(Calendar Calendar){
        Calendar today= calendarFunction.getTodayCalendar();
        if(!m_Calendar.before(today)){
            Score_et.setText("");
            Score_et.setEnabled(false);
        }
        else{
            Score_et.setEnabled(true);
        }
    }

    private Calendar StringtoCalendar(String date){
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN);
        Calendar Calendar= android.icu.util.Calendar.getInstance();
        try{
            Calendar.setTime(sdf2.parse(date));
        }catch (Exception e){
            Toast.makeText(AddExamPageActivity.this,"日期格式不符合 yyyy-MM-dd", Toast.LENGTH_SHORT);
        }
        return Calendar;
    }


    //增加動作按鈕到工具列
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_actions, menu);
        return true;
    }

/*
    private void setReminder(Context context, Cursor cursor, int reminderId) {
        OthersFunction othersFunction=new OthersFunction();
        Cursor Reminder_cursor=ReminderDb.getCursor(reminderId);
        Reminder_cursor.moveToFirst();
        othersFunction.setReminder(context,Reminder_cursor,reminderId,ExamID,);
    }
*/

    //動作按鈕回應
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                OthersFunction othersFunction=new OthersFunction();
                if(!othersFunction.isEdittextNotEmpty(Date_et,"日期",AddExamPageActivity.this))return true;
                String score= Score_et.getText().toString();
                ExamDb.insertExamData(ClassWeek_id,Subject_id,Date_et.getText().toString(),Name_et.getText().toString(),Content_et.getText().toString(),score.equals("")?-100:Integer.parseInt(score));
                Cursor cursor=ExamDb.getCursorOrderById();
                cursor.moveToLast();
                int ExamId=cursor.getInt(0);
                ReminderDb.insertReminderData(Date_et.getText().toString(),"19:46",0,0);
                Cursor Reminder_cursor=ReminderDb.getCursor();
                Reminder_cursor.moveToLast();
                othersFunction.setReminderByInput(AddExamPageActivity.this,Reminder_cursor,Reminder_cursor.getInt(0),ExamId,Name_et.getText().toString(),SubjectDb.getSubjectName(Subject_id));
                setResult(RESULT_OK,intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    void setTheme(){
        SharedPreferences prefs = getSharedPreferences("RECORDINGYOURLIFE", 0);
        int theme_index = prefs.getInt("THEME_INDEX" ,0);
        int theme=0;
        switch (theme_index){
            case 1:
                theme=R.style.AppTheme_brown;
                break;
            case 2:
                theme=R.style.AppTheme_orange;
                break;
            case 3:
                theme= R.style.AppTheme_purple;
                break;
            case 4:
                theme=R.style.AppTheme_red;
                break;
            case 5:
                break;
            case 0:
            default:
                theme=R.style.AppTheme;
                break;
        }
        setTheme(theme);
    }
}
