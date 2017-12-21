package com.ct.daan.recordingyourlife.Table;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ct.daan.recordingyourlife.Class.CalendarFunction;
import com.ct.daan.recordingyourlife.Class.OthersFunction;
import com.ct.daan.recordingyourlife.DbTable.ClassWeekDbTable;
import com.ct.daan.recordingyourlife.DbTable.TableDbTable;
import com.ct.daan.recordingyourlife.DbTable.WeekDbTable;
import com.ct.daan.recordingyourlife.R;

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
    Switch isMain;
    ListView ClassTime;
    TableDbTable TableDb;
    ClassWeekDbTable ClassWeekDb;
    WeekDbTable WeekDb;
    int Table_id=-100;
    List<Map<String,String>> Time;
    Button Addbtn;
    OthersFunction othersFunction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_settings);

        initView();
    }
    private void initView(){
        othersFunction=new OthersFunction();
        Start_date = (EditText) findViewById(R.id.startTime_et);
        End_date=(EditText)findViewById(R.id.endTime_et);
        Name=(EditText)findViewById(R.id.name_et);
        Days=(Spinner)findViewById(R.id.days_sp);
        isMain=(Switch)findViewById(R.id.ismain_sw);
        ClassTime =(ListView)findViewById(R.id.classtime_lv);
        Addbtn=(Button)findViewById(R.id.button);

        Start_date.setInputType(InputType.TYPE_NULL);
        End_date.setInputType(InputType.TYPE_NULL);

        Start_date.setOnClickListener(DatePick_Listener);
        End_date.setOnClickListener(DatePick_Listener);
        Addbtn.setOnClickListener(Button_Listener);
        ClassTime.setOnItemClickListener(List_listener);

        OpOrCrDb();
        ClassWeekDb=new ClassWeekDbTable(SQLiteDB_Path,db);
        ClassWeekDb.OpenOrCreateTb();

        TableDb=new TableDbTable(SQLiteDB_Path,db);
        TableDb.OpenOrCreateTb();

        WeekDb=new WeekDbTable(SQLiteDB_Path,db);
        WeekDb.OpenOrCreateTb();

        Time=new ArrayList<Map<String, String>>();

    }

    public void UpdateAdapter() {
        try {
            SimpleAdapter adapter=new SimpleAdapter(AddTableSettingsActivity.this,Time,R.layout.table_classtime_layout,new String[]{"start_time","end_time"},new int[]{R.id.text1, R.id.text2});
            ClassTime.setAdapter(adapter);
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

            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            TimePickerDialog timePicker=new TimePickerDialog(AddTableSettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String end_time=  String.format("%02d:%02d", hourOfDay, minute);
                    Log.v("選擇時間", String.format("結束時間=%s",end_time));
                    item.put("end_time",end_time);
                    UpdateAdapter();
                }
            }, hour, minute, true);
            timePicker.setTitle("結束時間");
            timePicker.show();

            c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            // Create a new instance of TimePickerDialog and return it
            timePicker= new TimePickerDialog(AddTableSettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String start_time= String.format("%02d:%02d", hourOfDay, minute);
                    Log.v("選擇時間", String.format("開始時間=%s",start_time));
                    item.put("start_time",start_time);
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
            final Map<String,String> item=Time.get(position);
            Calendar c = StringtoTCalendar(item.get("end_time"));
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            TimePickerDialog timePicker=new TimePickerDialog(AddTableSettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String end_time=  String.format("%02d:%02d", hourOfDay, minute);
                    item.put("end_time",end_time);
                    Log.v("選擇時間", String.format("結束時間=%s",end_time));
                    ClassWeekDb.updateEndTime(ClassWeek_id,end_time);
                    UpdateAdapter();
                }
            }, hour, minute, true);
            timePicker.setTitle("結束時間");
            timePicker.show();

            c = StringtoTCalendar(item.get("start_time"));
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            // Create a new instance of TimePickerDialog and return it
            timePicker= new TimePickerDialog(AddTableSettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String start_time=  String.format("%02d:%02d", hourOfDay, minute);
                    item.put("start_time",start_time);
                    Log.v("選擇時間", String.format("開始時間=%s",start_time));
                    ClassWeekDb.updateStartTime(ClassWeek_id,start_time);
                    UpdateAdapter();
                }
            }, hour, minute, true);
            timePicker.setTitle("開始時間");
            timePicker.show();

            UpdateAdapter();

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
                ,End_date.getText().toString());
        Table_id=TableDb.getTable_id(Name.getText().toString());
        insertClassWeeks();
        insertWeek();
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
                        openSubjectSettings();
                    }
                })
                .setNegativeButton("保留", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isMain.setChecked(false);
                        saveValue();
                        openSubjectSettings();
                    }
                });
        builder.create();
        builder.show();
    }

    private boolean check(){
        if(TableDb.getCursor(String.format("課表名稱='%s' AND _id !=%d ",Name.getText().toString(),Table_id)).getCount()>0){
            Toast.makeText(AddTableSettingsActivity.this,"課表名稱已存在", Toast.LENGTH_LONG).show();
            Log.w("課表名稱錯誤","課表名稱已存在");
            return false;
        } //補習班 學校
        if(!othersFunction.isEdittextNotEmpty(Name,"名稱",AddTableSettingsActivity.this)) return false;
        if(!othersFunction.isEdittextNotEmpty(Start_date,"開始日期",AddTableSettingsActivity.this)) return false;
        if(!othersFunction.isEdittextNotEmpty(End_date,"結束日期",AddTableSettingsActivity.this)) return false;
        if(!othersFunction.isDateType(Start_date,"開始日期",AddTableSettingsActivity.this)) return false;
        if(!othersFunction.CompareDate(Start_date,End_date,"開始和結束",AddTableSettingsActivity.this)) return false;
        if(isMain.isChecked()&&Table_id!=TableDb.getMain_id()) {
            check_main();
            return false;
        }
        if(Time.size()<1){
            Toast.makeText(AddTableSettingsActivity.this,"需增加課堂", Toast.LENGTH_LONG).show();
            Log.w("課堂錯誤","需增加課堂");
            return false;
        }
        if(!check_time()){
            Toast.makeText(AddTableSettingsActivity.this,"課表時間錯誤", Toast.LENGTH_LONG).show();
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

        if(KeyCode== KeyEvent.KEYCODE_BACK){
           finish();
        }
        return true;
    }

    private void openSubjectSettings(){
        Intent intent=new Intent(AddTableSettingsActivity.this,AddSubjectTableActivity.class);
        intent.putExtra("ROW",Time.size());
        intent.putExtra("COL", Integer.parseInt( Days.getSelectedItem().toString()));
        intent.putExtra("TABLE_ID",Table_id);
        startActivity(intent);
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
            Toast.makeText(AddTableSettingsActivity.this,"日期格式不符合 yyyy-MM-dd", Toast.LENGTH_SHORT).show();
        }
        return Calendar;
    }

    private Calendar StringtoTCalendar(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.TAIWAN);
        Calendar Calendar= android.icu.util.Calendar.getInstance();
        try{
            Calendar.setTime(sdf.parse(date));
        }catch (Exception e){
            Toast.makeText(AddTableSettingsActivity.this,"時間格式不符合 HH:mm", Toast.LENGTH_SHORT).show();
        }
        return Calendar;
    }

    private boolean check_time(){
        String tmp_endTime="00:00";
        for (Map<String, String> item:Time) {
            if(!othersFunction.CompareTime(item.get("start_time"),item.get("end_time"))) return false;
            if(!othersFunction.CompareTime(tmp_endTime,item.get("start_time")))return false;
            tmp_endTime=item.get("end_time");
        }
        return true;
    }
    private void insertClassWeek(String start_time, String end_time){
        ClassWeekDb.insertClassWeekData(Table_id,start_time,end_time);
    }
    private void insertClassWeeks(){
        for (Map<String, String> item:Time) {
            insertClassWeek(item.get("start_time"),item.get("end_time"));
        }
    }
    private void insertWeek(){
        Calendar cal=StringtoCalendar(Start_date.getText().toString());
        int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK)-1;
        for(int i = 0; i< Integer.parseInt(Days.getSelectedItem().toString()); i++){

            WeekDb.insertWeekData(Table_id,dayOfWeek+i);
        }
    }


    //增加動作按鈕到工具列
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_actions, menu);
        return true;
    }

    //動作按鈕回應
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if (!check())return true;
                saveValue();
                openSubjectSettings();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
