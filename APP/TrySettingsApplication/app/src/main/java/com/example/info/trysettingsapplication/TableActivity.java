package com.example.info.trysettingsapplication;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Locale;
import java.util.Set;

/**
 * Created by info on 2017/11/8.
 */

public class TableActivity extends AppCompatActivity {
    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    EditText Start_date,End_date,Name;
    Spinner Days;
    Switch isCover,isMain;
    TableDbTable TableDb;
    int Table_id=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablesettings);

        initView();
        getValue();
    }
    private void initView(){
        Start_date = (EditText) findViewById(R.id.startTime_et);
        End_date=(EditText)findViewById(R.id.endTime_et);
        Name=(EditText)findViewById(R.id.name_et);
        Days=(Spinner)findViewById(R.id.days_sp);
        isCover=(Switch) findViewById(R.id.iscover_sw);
        isMain=(Switch)findViewById(R.id.ismain_sw);

        Start_date.setInputType(InputType.TYPE_NULL);
        End_date.setInputType(InputType.TYPE_NULL);

        Start_date.setOnClickListener(DatePick_Listener);
        End_date.setOnClickListener(DatePick_Listener);

        OpOrCrDb();
        TableDb=new TableDbTable(SQLiteDB_Path,db);
        TableDb.OpenOrCreateTb();
        //TableDb.deleteAllRow();
        //TableDb.AddTalbeData();
    }

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

        AlertDialog.Builder builder =new AlertDialog.Builder(TableActivity.this);
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
            Toast.makeText(TableActivity.this,"課表名稱已存在",Toast.LENGTH_LONG).show();
            Log.w("課表名稱錯誤","課表名稱已存在");
            return false;
        } //補習班 學校
        if(!IsStringtoCalendar(Start_date.getText().toString())){
            Toast.makeText(TableActivity.this,"課表開始日期格式錯誤",Toast.LENGTH_LONG).show();
            Log.w("課表開始日期錯誤","課表開始日期格式錯誤");
            return false;
        }
        if(!IsStringtoCalendar(End_date.getText().toString())){
            Toast.makeText(TableActivity.this,"課表結束日期格式錯誤",Toast.LENGTH_LONG).show();
            Log.w("課表結束日期錯誤","課表結束日期格式錯誤");
            return false;
        }
        if (!compareDate(Start_date.getText().toString(),End_date.getText().toString())){
            Toast.makeText(TableActivity.this,"課表日期錯誤",Toast.LENGTH_LONG).show();
            Log.w("課表日期錯誤","課表日期錯誤");
            return false;
        }
        if(isMain.isChecked()&&Table_id!=TableDb.getMain_id()) {
            check_main();
            return false;
        }

        try{

        }catch (Exception e){
            Toast.makeText(TableActivity.this,"　",Toast.LENGTH_LONG).show();
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
                dataPick=new DatePickerDialog(TableActivity.this,datepicker,
                        m_Calendar.get(Calendar.YEAR),
                        m_Calendar.get(Calendar.MONTH),
                        m_Calendar.get(Calendar.DAY_OF_MONTH));
                dataPick.show();
            }
            else{
                dataPick=new DatePickerDialog(TableActivity.this,datepicker2,
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
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN);
        Calendar Calendar= android.icu.util.Calendar.getInstance();
        try{
            Calendar.setTime(sdf2.parse(date));
        }catch (Exception e){
            Toast.makeText(TableActivity.this,"日期格式不符合 yyyy-MM-dd",Toast.LENGTH_SHORT);
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
