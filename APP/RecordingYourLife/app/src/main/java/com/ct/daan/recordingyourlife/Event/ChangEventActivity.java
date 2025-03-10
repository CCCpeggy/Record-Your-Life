package com.ct.daan.recordingyourlife.Event;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.DbTable.EventDbTable;

import java.util.Locale;

public class ChangEventActivity extends AppCompatActivity {

    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    EventDbTable EventDb;
    EditText Start_date,End_date,Name,Remark;
    Intent intent;
    int Event_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_eventpage);
        initView();
        intent=getIntent();
        getValue();

    }

    private void initView() {
        Start_date = (EditText) findViewById(R.id.startTime_et);
        End_date=(EditText)findViewById(R.id.endTime_et);
        Name=(EditText)findViewById(R.id.name_et);
        Remark=(EditText)findViewById(R.id.remark_et);

        Start_date.setInputType(InputType.TYPE_NULL);
        End_date.setInputType(InputType.TYPE_NULL);

        Start_date.setOnClickListener(DatePick_Listener);
        End_date.setOnClickListener(DatePick_Listener);

        OpOrCrDb();
        EventDb=new EventDbTable(SQLiteDB_Path,db);
        EventDb.OpenOrCreateTb();

    }
    private void OpOrCrDb(){
        try{
            db=openOrCreateDatabase(SQLiteDB_Path,MODE_PRIVATE,null);
            Log.v("資料庫","資料庫載入成功");
        }catch (Exception ex){
            Log.e("#001","資料庫載入錯誤");
        }
    }

    private void getValue(){
        Bundle extra=intent.getExtras();
        Event_id=extra.getInt("SELECTED_ID");

        Cursor cursor =EventDb.getCursor(Event_id);
        cursor.moveToFirst();
        Name.setText(cursor.getString(1));
        Start_date.setText(cursor.getString(2));
        End_date.setText(cursor.getString(3));
        Remark.setText(cursor.getString(4));


    }
    Calendar m_Calendar = Calendar.getInstance();
    private EditText.OnClickListener DatePick_Listener= new EditText.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText et=(EditText)v;
            DatePickerDialog dataPick;
            if (v==Start_date){
                dataPick=new DatePickerDialog(ChangEventActivity.this,datepicker,
                        m_Calendar.get(Calendar.YEAR),
                        m_Calendar.get(Calendar.MONTH),
                        m_Calendar.get(Calendar.DAY_OF_MONTH));
                dataPick.show();
            }
            else{
                dataPick=new DatePickerDialog(ChangEventActivity.this,datepicker2,
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
            if(End_date.getText().toString().equals(""))End_date.setText(sdf.format(m_Calendar.getTime()));
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

    private void Complete() {
        if(!compareDate(Start_date.getText().toString(),End_date.getText().toString()))return;
        intent.putExtra("ID",Event_id);
        intent.putExtra("NAME",Name.getText().toString());
        intent.putExtra("STARTDATE",Start_date.getText().toString());
        intent.putExtra("ENDDATE",End_date.getText().toString());
        intent.putExtra("REMARK",Remark.getText().toString());
        Log.v("回傳資料", String.format("回傳資料：%s=%s,%s=%s,%s=%s,%s=%s","NAME",Name.getText().toString(),"STARTDATE",Start_date.getText().toString(),"ENDDATE",End_date.getText().toString(),"REMARK",Remark.getText().toString()));
        setResult(RESULT_OK,intent);
        finish();

    }

    private boolean compareDate(String startdate, String enddate){
        Calendar cal=StringtoCalendar(startdate);
        Calendar cal2=StringtoCalendar(enddate);
        if (cal.equals("")||cal2.equals(""))return false;
        Log.v("傳入日期", String.format("YEAR=%d/%d,MONTH=%d/%d,DATE=%d/%d",cal.get(Calendar.YEAR),cal2.get(Calendar.YEAR)
                ,cal.get(Calendar.MONTH),cal2.get(Calendar.MONTH)
                ,cal.get(Calendar.DATE),cal2.get(Calendar.DATE)));
        if(cal.get(Calendar.YEAR)>cal2.get(Calendar.YEAR))return false;
        if(cal.get(Calendar.YEAR)<cal2.get(Calendar.YEAR))return true;
        if(cal.get(Calendar.MONTH)>cal2.get(Calendar.MONTH))return false;
        if(cal.get(Calendar.MONTH)<cal2.get(Calendar.MONTH))return true;
        return !(cal.get(Calendar.DATE)>cal2.get(Calendar.DATE));
    }

    private Calendar StringtoCalendar(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN);
        Calendar Calendar= android.icu.util.Calendar.getInstance();
        try{
            Calendar.setTime(sdf.parse(date));
        }catch (Exception e){
            Toast.makeText(ChangEventActivity.this,"日期格式不符合 yyyy-MM-dd", Toast.LENGTH_SHORT).show();
            return null;
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

    //動作按鈕回應
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                Complete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
