package com.example.info.table_complete_application;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by info on 2017/11/8.
 */

public class TableSettingsActivity extends AppCompatActivity {
    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    EditText Start_date,End_date,Name;
    Spinner Days;
    Switch isCover,isMain;
    ListView ClassTime;
    TableDbTable TableDb;
    ClassWeekDbTable ClassWeekDb;
    Cursor cursor;
    int Table_id=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablesettingsnonewclass);
        getTable_id();

        initView();
        getValue();
    }
    private void getTable_id(){
        Intent intent=getIntent();
        Bundle extra=intent.getExtras();
        Table_id= extra.getInt("TABLE_ID");
    }
    private void initView(){
        Start_date = (EditText) findViewById(R.id.startTime_et);
        End_date=(EditText)findViewById(R.id.endTime_et);
        Name=(EditText)findViewById(R.id.name_et);
        Days=(Spinner)findViewById(R.id.days_sp);
        isCover=(Switch) findViewById(R.id.iscover_sw);
        isMain=(Switch)findViewById(R.id.ismain_sw);
        ClassTime =(ListView)findViewById(R.id.classtime_lv);

        Start_date.setInputType(InputType.TYPE_NULL);
        End_date.setInputType(InputType.TYPE_NULL);

        Start_date.setOnClickListener(DatePick_Listener);
        End_date.setOnClickListener(DatePick_Listener);

        OpOrCrDb();
        ClassWeekDb=new ClassWeekDbTable(SQLiteDB_Path,db);
        ClassWeekDb.OpenOrCreateTb();
        //ClassWeekDb.deleteAllRow();
        //ClassWeekDb.AddClassWeekData();

        UpdateAdapter();

        TableDb=new TableDbTable(SQLiteDB_Path,db);
        TableDb.OpenOrCreateTb();
        //TableDb.deleteAllRow();
        //TableDb.AddTalbeData();
    }

    public void UpdateAdapter() {
        try {
            cursor = ClassWeekDb.getCursor(Table_id);
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[]{"開始時間", "結束時間"}, new int[]{android.R.id.text1, android.R.id.text2}, 0);
            ClassTime.setAdapter(adapter);
            ClassTime.setOnItemClickListener(List_listener);
            ClassTime.setOnItemSelectedListener(List_listener2);
            Log.v("UpdateAdapter", String.format("UpdateAdapter() 更新成功"));

        } catch (Exception e) {
            Log.e("#004", "清單更新失敗");
        }

    }
    int ClassWeek_id;
    private ListView.OnItemSelectedListener List_listener2=new ListView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Cursor ClassWeek_cursor=(Cursor)parent.getSelectedItem();
            Log.v("132", ClassWeek_cursor.getCount()+"");
            ClassWeek_cursor.moveToFirst();
            ClassWeek_id=ClassWeek_cursor.getInt(0);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private ListView.OnItemClickListener List_listener=new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor ClassWeek_cursor=ClassWeekDb.getCursor(Table_id);
            Log.v("13", ClassWeek_cursor.getCount()+"");
            ClassWeek_cursor.moveToPosition(position);
            ClassWeek_id=ClassWeek_cursor.getInt(0);


            Calendar c = StringtoTCalendar(ClassWeek_cursor.getString(3));
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            TimePickerDialog timePicker=new TimePickerDialog(TableSettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String end_time= String.format("%02d:%02d", hourOfDay, minute);
                    Log.v("選擇時間",String.format("結束時間=%s",end_time));
                    ClassWeekDb.updateEndTime(ClassWeek_id,end_time);
                    UpdateAdapter();
                }
            }, hour, minute, true);
            timePicker.setTitle("結束時間");
            timePicker.show();

            c = StringtoTCalendar(ClassWeek_cursor.getString(2));
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            // Create a new instance of TimePickerDialog and return it
             timePicker= new TimePickerDialog(TableSettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String start_time=  String.format("%02d:%02d", hourOfDay, minute);
                    Log.v("選擇時間",String.format("開始時間=%s",start_time));
                    ClassWeekDb.updateStartTime(ClassWeek_id,start_time);
                    UpdateAdapter();
                }
            }, hour, minute, true);
            timePicker.setTitle("開始時間");
            timePicker.show();

        }
    };



    private void OpOrCrDb(){
        try{
            db=openOrCreateDatabase(SQLiteDB_Path,MODE_PRIVATE,null);
            Log.v("資料庫","資料庫載入成功");
        }catch (Exception ex){
            Log.e("#001","資料庫載入錯誤");
        }
    }

    private void saveValue(){
        TableDb.updateTableData(
                Table_id
                ,Name.getText().toString()
                ,Days.getSelectedItem().toString()
                ,(isMain.isChecked()?1:0)
                ,Start_date.getText().toString()
                ,End_date.getText().toString()
                ,isCover.isChecked()?1:0);
    }

    private void getValue(){
        //_id, 課表名稱, 課表天數, 主要, 課表開始日, 課表結束日, 時間重複時是否取代
        Cursor cursor = TableDb.getCursor(Table_id);
        cursor.moveToFirst();
        Start_date.setText(cursor.getString(4));
        End_date.setText(cursor.getString(5));
        Name.setText(cursor.getString(1));
        Days.setSelection(cursor.getInt(2)-1);
        isMain.setChecked(cursor.getInt(3)==1);
        isCover.setChecked(cursor.getInt(6)==1);

    }

    private void check_main(){

        AlertDialog.Builder builder =new AlertDialog.Builder(TableSettingsActivity.this);
        builder.setTitle("確認視窗")
                .setMessage("是否取代"+TableDb.getMain_Name()+"作為主要課表")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TableDb.updateMain_id();
                        saveValue();
                        finish();
                    }
                })
                .setNegativeButton("保留", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isMain.setChecked(false);
                        saveValue();
                        finish();
                    }
                });
        builder.create();
        builder.show();
    }

    private boolean check(){
                /*,Name.getText().toString()
                ,Days.getSelectedItem().toString()
                ,(isMain.isChecked()?1:0)
                ,Start_time.getText().toString()
                ,End_time.getText().toString()
                ,isCover.isChecked()?1:0);*/
        if(TableDb.getCursor(String.format("課表名稱='%s' AND _id !=%d ",Name.getText().toString(),Table_id)).getCount()>0){
            Toast.makeText(TableSettingsActivity.this,"課表名稱已存在",Toast.LENGTH_LONG).show();
            Log.w("課表名稱錯誤","課表名稱已存在");
            return false;
        } //補習班 學校
        if(Start_date.getText().toString().equals("")){
            Toast.makeText(TableSettingsActivity.this,"課表開始日期不可為空",Toast.LENGTH_LONG).show();
            Log.w("課表開始日期錯誤","課表開始日期不可為空");
            return false;
        }
        if(!IsStringtoCalendar(Start_date.getText().toString())){
            Toast.makeText(TableSettingsActivity.this,"課表開始日期格式錯誤",Toast.LENGTH_LONG).show();
            Log.w("課表開始日期錯誤","課表開始日期格式錯誤");
            return false;
        }
        if(!End_date.getText().toString().equals("")&&!IsStringtoCalendar(End_date.getText().toString())){
            Toast.makeText(TableSettingsActivity.this,"課表結束日期格式錯誤",Toast.LENGTH_LONG).show();
            Log.w("課表結束日期錯誤","課表結束日期格式錯誤");
            return false;
        }
        if (!End_date.getText().toString().equals("")&&!compareDate(Start_date.getText().toString(),End_date.getText().toString())){
            Toast.makeText(TableSettingsActivity.this,"課表日期錯誤",Toast.LENGTH_LONG).show();
            Log.w("課表日期錯誤","課表日期錯誤");
            return false;
        }
        if(isMain.isChecked()&&Table_id!=TableDb.getMain_id()) {
            check_main();
            return false;
        }

        try{

        }catch (Exception e){
            Toast.makeText(TableSettingsActivity.this,"　",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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


    public boolean onKeyDown(int KeyCode, KeyEvent event){

        if(KeyCode==KeyEvent.KEYCODE_BACK){
            if (!check())return true;
            saveValue();
            finish();
        }
        return true;
    }

    Calendar m_Calendar = Calendar.getInstance();
    private EditText.OnClickListener DatePick_Listener= new EditText.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText et=(EditText)v;
            DatePickerDialog dataPick;
            if (v==Start_date){
                dataPick=new DatePickerDialog(TableSettingsActivity.this,datepicker,
                        m_Calendar.get(Calendar.YEAR),
                        m_Calendar.get(Calendar.MONTH),
                        m_Calendar.get(Calendar.DAY_OF_MONTH));
                dataPick.show();
            }
            else{
                dataPick=new DatePickerDialog(TableSettingsActivity.this,datepicker2,
                        m_Calendar.get(Calendar.YEAR),
                        m_Calendar.get(Calendar.MONTH),
                        m_Calendar.get(Calendar.DAY_OF_MONTH));
                dataPick.show();
            }


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
            Start_date.setText(sdf.format(m_Calendar.getTime()));
        }
    };

    DatePickerDialog.OnDateSetListener datepicker2 = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            m_Calendar.set(Calendar.YEAR, year);
            m_Calendar.set(Calendar.MONTH, monthOfYear);
            m_Calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
            End_date.setText(sdf.format(m_Calendar.getTime()));
        }
    };

    private Calendar StringtoCalendar(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN);
        Calendar Calendar= android.icu.util.Calendar.getInstance();
        try{
            Calendar.setTime(sdf.parse(date));
        }catch (Exception e){
            Toast.makeText(TableSettingsActivity.this,"日期格式不符合 yyyy-MM-dd",Toast.LENGTH_SHORT).show();
        }
        return Calendar;
    }

    private Calendar StringtoTCalendar(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.TAIWAN);
        Calendar Calendar= android.icu.util.Calendar.getInstance();
        try{
            Calendar.setTime(sdf.parse(date));
        }catch (Exception e){
            Toast.makeText(TableSettingsActivity.this,"時間格式不符合 hh:mm",Toast.LENGTH_SHORT).show();
        }
        return Calendar;
    }

    private boolean compareDate(String startdate,String enddate){
        Calendar cal=StringtoCalendar(startdate);
        Calendar cal2=StringtoCalendar(enddate);
        Log.v("傳入日期",String.format("YEAR=%d/%d,MONTH=%d/%d,DATE=%d/%d",cal.get(Calendar.YEAR),cal2.get(Calendar.YEAR)
                ,cal.get(Calendar.MONTH),cal2.get(Calendar.MONTH)
                ,cal.get(Calendar.DATE),cal2.get(Calendar.DATE)));
        if(cal.get(Calendar.YEAR)>cal2.get(Calendar.YEAR))return false;
        if(cal.get(Calendar.YEAR)<cal2.get(Calendar.YEAR))return true;
        if(cal.get(Calendar.MONTH)>cal2.get(Calendar.MONTH))return false;
        if(cal.get(Calendar.MONTH)<cal2.get(Calendar.MONTH))return true;
        return !(cal.get(Calendar.DATE)>cal2.get(Calendar.DATE));
    }

}
