package com.example.info.trysettingsapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by info on 2017/11/9.
 */

public class AddTableSettingsActivity extends AppCompatActivity {
    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    EditText Start_date,End_date,Name;
    Spinner Days;
    Switch isCover,isMain;
    ListView ClassTime;
    TableDbTable TableDb;
    ClassWeekDbTable ClassWeekDb;
    Cursor cursor;
    int Table_id=-100;
    List<Map<String,String>> Time;
    Button Addbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablesettings);

        initView();
    }
    private void initView(){
        Start_date = (EditText) findViewById(R.id.startTime_et);
        End_date=(EditText)findViewById(R.id.endTime_et);
        Name=(EditText)findViewById(R.id.name_et);
        Days=(Spinner)findViewById(R.id.days_sp);
        isCover=(Switch) findViewById(R.id.iscover_sw);
        isMain=(Switch)findViewById(R.id.ismain_sw);
        ClassTime =(ListView)findViewById(R.id.classtime_lv);
        Addbtn=(Button)findViewById(R.id.button);

        Start_date.setInputType(InputType.TYPE_NULL);
        End_date.setInputType(InputType.TYPE_NULL);

        Start_date.setOnClickListener(DatePick_Listener);
        End_date.setOnClickListener(DatePick_Listener);
        Addbtn.setOnClickListener(Button_Listener);

        OpOrCrDb();
        ClassWeekDb=new ClassWeekDbTable(SQLiteDB_Path,db);
        ClassWeekDb.OpenOrCreateTb();
        ClassWeekDb.deleteAllRow();
        ClassWeekDb.AddClassWeekData();


        TableDb=new TableDbTable(SQLiteDB_Path,db);
        TableDb.OpenOrCreateTb();
        TableDb.deleteAllRow();
        TableDb.AddTalbeData();

        Time=new ArrayList<Map<String, String>>();

    }

    public void UpdateAdapter() {
        try {
            SimpleAdapter adapter=new SimpleAdapter(AddTableSettingsActivity.this,Time,android.R.layout.simple_list_item_2,new String[]{"start_time","end_time"},new int[]{android.R.id.text1, android.R.id.text2});            ClassTime.setAdapter(adapter);
            ClassTime.setAdapter(adapter);
            Log.v("UpdateAdapter", String.format("UpdateAdapter() 更新成功"));

        } catch (Exception e) {
            Log.e("#004", "清單更新失敗");
        }

    }
    int i=8;
    private Button.OnClickListener Button_Listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Map<String,String> item=new HashMap<String, String>();

            Calendar c =Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            TimePickerDialog timePicker=new TimePickerDialog(AddTableSettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String end_time=  String.format("%02d:%02d", hourOfDay, minute);
                    Log.v("選擇時間",String.format("結束時間=%s",end_time));
                    item.put("end_time",end_time);
                    UpdateAdapter();
                }
            }, hour, minute, true);
            timePicker.setTitle("結束時間");
            timePicker.show();

            c =Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            // Create a new instance of TimePickerDialog and return it
            timePicker= new TimePickerDialog(AddTableSettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String start_time= String.format("%02d:%02d", hourOfDay, minute);
                    Log.v("選擇時間",String.format("開始時間=%s",start_time));
                    item.put("start_time",start_time);
                    //Time.add(item);
                    UpdateAdapter();
                }
            }, hour, minute, true);
            timePicker.setTitle("開始時間");
            timePicker.show();


            Time.add(item);
            UpdateAdapter();
        }
    };

    int ClassWeek_id;
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
            TimePickerDialog timePicker=new TimePickerDialog(AddTableSettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String end_time=  hourOfDay + ":" + minute;
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
            timePicker= new TimePickerDialog(AddTableSettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String start_time=  hourOfDay + ":" + minute;
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
        TableDb.insertTableData(
                Name.getText().toString()
                ,Days.getSelectedItem().toString()
                ,(isMain.isChecked()?1:0)
                ,Start_date.getText().toString()
                ,End_date.getText().toString()
                ,isCover.isChecked()?1:0);
        Table_id=TableDb.getTable_id(Name.getText().toString());
        insertClassWeeks();
    }


    private void check_main(){

        AlertDialog.Builder builder =new AlertDialog.Builder(AddTableSettingsActivity.this);
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
        if(TableDb.getCursor(String.format("課表名稱='%s' AND _id !=%d ",Name.getText().toString(),Table_id)).getCount()>0){
            Toast.makeText(AddTableSettingsActivity.this,"課表名稱已存在",Toast.LENGTH_LONG).show();
            Log.w("課表名稱錯誤","課表名稱已存在");
            return false;
        } //補習班 學校
        if(Name.getText().toString().equals("")){
            Toast.makeText(AddTableSettingsActivity.this,"課表名稱為空",Toast.LENGTH_LONG).show();
            Log.w("課表名稱錯誤","課表名稱為空");
            return false;
        }
        if(Start_date.getText().toString().equals("")){
            Toast.makeText(AddTableSettingsActivity.this,"課表開始日期不可為空",Toast.LENGTH_LONG).show();
            Log.w("課表開始日期錯誤","課表開始日期不可為空");
            return false;
        }
        if(!IsStringtoCalendar(Start_date.getText().toString())){
            Toast.makeText(AddTableSettingsActivity.this,"課表開始日期格式錯誤",Toast.LENGTH_LONG).show();
            Log.w("課表開始日期錯誤","課表開始日期格式錯誤");
            return false;
        }
        if(!End_date.getText().toString().equals("")&&!IsStringtoCalendar(End_date.getText().toString())){
            Toast.makeText(AddTableSettingsActivity.this,"課表結束日期格式錯誤",Toast.LENGTH_LONG).show();
            Log.w("課表結束日期錯誤","課表結束日期格式錯誤");
            return false;
        }
        if (!End_date.getText().toString().equals("")&&!compareDate(Start_date.getText().toString(),End_date.getText().toString())){
            Toast.makeText(AddTableSettingsActivity.this,"課表日期錯誤",Toast.LENGTH_LONG).show();
            Log.w("課表日期錯誤","課表日期錯誤");
            return false;
        }
        if(isMain.isChecked()&&Table_id!=TableDb.getMain_id()) {
            check_main();
            return false;
        }
        if(!check_time()){
            Toast.makeText(AddTableSettingsActivity.this,"課表時間錯誤",Toast.LENGTH_LONG).show();
            Log.w("課表時間錯誤","課表時間錯誤");
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
                dataPick=new DatePickerDialog(AddTableSettingsActivity.this,datepicker,
                        m_Calendar.get(Calendar.YEAR),
                        m_Calendar.get(Calendar.MONTH),
                        m_Calendar.get(Calendar.DAY_OF_MONTH));
                dataPick.show();
            }
            else{
                dataPick=new DatePickerDialog(AddTableSettingsActivity.this,datepicker2,
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
            Toast.makeText(AddTableSettingsActivity.this,"日期格式不符合 yyyy-MM-dd",Toast.LENGTH_SHORT).show();
        }
        return Calendar;
    }

    private Calendar StringtoTCalendar(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.TAIWAN);
        Calendar Calendar= android.icu.util.Calendar.getInstance();
        try{
            Calendar.setTime(sdf.parse(date));
        }catch (Exception e){
            Toast.makeText(AddTableSettingsActivity.this,"時間格式不符合 hh:mm",Toast.LENGTH_SHORT).show();
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

    private boolean compareTime(String starttime,String endtime){
        Calendar cal=StringtoTCalendar(starttime);
        Calendar cal2=StringtoTCalendar(endtime);
        Log.v("傳入日期",String.format("HOUR=%d/%d,MINUTE=%d/%d"
                ,cal.get(Calendar.HOUR),cal2.get(Calendar.HOUR)
                ,cal.get(Calendar.MINUTE),cal2.get(Calendar.MINUTE)));

        if(cal.get(Calendar.HOUR)>cal2.get(Calendar.HOUR))return false;
        if(cal.get(Calendar.HOUR)<cal2.get(Calendar.HOUR))return true;
        return (cal.get(Calendar.MINUTE)<=cal2.get(Calendar.MINUTE));
    }

    private boolean check_time(){
        String tmp_endTime="00:00";
        for (Map<String, String> item:Time) {
            if(!compareTime(item.get("start_time"),item.get("end_time"))) return false;
            if(!compareTime(tmp_endTime,item.get("start_time")))return false;
            tmp_endTime=item.get("end_time");
        }
        return true;
    }
    private void insertClassWeek(String start_time,String end_time){
        ClassWeekDb.insertClassWeekData(Table_id,start_time,end_time);
    }
    private void insertClassWeeks(){
        for (Map<String, String> item:Time) {
            insertClassWeek(item.get("start_time"),item.get("end_time"));
        }
    }

}
