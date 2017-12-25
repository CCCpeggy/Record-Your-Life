package com.ct.daan.recordingyourlife.Schedule;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.ct.daan.recordingyourlife.DbTable.ScheduleDbTable;
import com.ct.daan.recordingyourlife.R;

import java.util.Locale;

public class SchedulePageActivity extends AppCompatActivity {
    private final static String SQLiteDB_Path="student_project.db";
    private SQLiteDatabase db=null;
    EditText Name_et,Date_et,Time_et;
    Intent intent;
    ScheduleDbTable ScheduleDb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_page);

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
        ScheduleDb= new ScheduleDbTable(SQLiteDB_Path,db);
        ScheduleDb.OpenOrCreateTb();

    }
    private void initView(){
        Name_et=(EditText) findViewById(R.id.Name_et);
        Date_et=(EditText) findViewById(R.id.Date_et);
        Time_et=(EditText) findViewById(R.id.Time_et);

        Date_et.setInputType(InputType.TYPE_NULL);
        Time_et.setInputType(InputType.TYPE_NULL);

        Date_et.setOnClickListener(DatePick_Listener);
        Time_et.setOnClickListener(TimePick_Listener);
        initDataBase();

    }

    Calendar m_Calendar = Calendar.getInstance();
    private EditText.OnClickListener DatePick_Listener= new EditText.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog dataPick=new DatePickerDialog(SchedulePageActivity.this,datepicker,
                    m_Calendar.get(Calendar.YEAR),
                    m_Calendar.get(Calendar.MONTH),
                    m_Calendar.get(Calendar.DAY_OF_MONTH));
            dataPick.show();
        }
    };

    private EditText.OnClickListener TimePick_Listener= new EditText.OnClickListener() {
        @Override
        public void onClick(View v) {
            TimePickerDialog dataPick=new TimePickerDialog(SchedulePageActivity.this,TimePickerDialog,
                    m_Calendar.get(Calendar.HOUR_OF_DAY),
                    m_Calendar.get(Calendar.MINUTE),true);
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
        }
    };


    TimePickerDialog.OnTimeSetListener TimePickerDialog=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time= String.format("%02d:%02d", hourOfDay, minute);
            Time_et.setText(time);
        }
    };


    private void Complete() {
        ScheduleDb.insertScheduleData(Name_et.getText().toString(),Time_et.getText().toString(),Date_et.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
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
