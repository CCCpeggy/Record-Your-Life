package com.ct.daan.recordingyourlife.Settings;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.ct.daan.recordingyourlife.Class.CalendarFunction;
import com.ct.daan.recordingyourlife.Exam.ExamPageActivity;
import com.ct.daan.recordingyourlife.Note.NotePageActivity;
import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.Table.AddTableSettingsActivity;

import java.util.Locale;

/**
 * Created by info on 2017/12/25.
 */

public class Settings extends AppCompatActivity{
    Spinner theme_sp;
    EditText Time_et;
    CalendarFunction calendarFunction;
    int theme_this;

    Calendar m_Calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_content);

        initView();
    }
    void initView(){
        theme_sp=(Spinner)findViewById(R.id.theme_sp);
        theme_sp.setSelection(theme_this);
        theme_sp.setOnItemSelectedListener(Theme_sp_Listener);
        Time_et=(EditText)findViewById(R.id.time_et);
        Time_et.setOnClickListener(TimePick_Listener);
        calendarFunction=new CalendarFunction();

        SharedPreferences prefs = getSharedPreferences("RECORDINGYOURLIFE", 0);
        String Time = prefs.getString("ExamRemindTime" ,"");
        Time_et.setText(Time);
    }

    private EditText.OnClickListener TimePick_Listener= new EditText.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!Time_et.getText().toString().isEmpty()) m_Calendar=calendarFunction.TimeTextToCalendarType(Time_et.getText().toString());
            int hour = m_Calendar.get(Calendar.HOUR_OF_DAY);
            int minute = m_Calendar.get(Calendar.MINUTE);
            // Create a new instance of TimePickerDialog and return it
            TimePickerDialog timePicker= new TimePickerDialog(Settings.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String Time= String.format("%02d:%02d", hourOfDay, minute);
                    Log.v("選擇時間", String.format("考試填入提醒時間=%s",Time));
                    saveData("ExamRemindTime",Time);
                    Time_et.setText(Time);
                }
            }, hour, minute, true);
            timePicker.setTitle("考試填入提醒時間");
            timePicker.show();

        }


    };


    private Spinner.OnItemSelectedListener Theme_sp_Listener= new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String theme_str=adapterView.getSelectedItem().toString();
            int theme_index=theme_sp.getSelectedItemPosition();
            if(theme_index==theme_this)return;
            saveTheme(theme_index,theme_str);
        }



        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    void saveTheme(final int theme_index,String theme_str) {
        new android.app.AlertDialog.Builder(Settings.this)
                .setMessage(String.format("確認要換成%s主題嗎",theme_str))
                .setTitle(R.string.delete_tilte)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor pref = getSharedPreferences("RECORDINGYOURLIFE", 0).edit();
                        pref.putInt("THEME_INDEX" , theme_index);
                        pref.apply();
                        Restart();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    void saveData(String Key,String Value){
        SharedPreferences.Editor pref = getSharedPreferences("RECORDINGYOURLIFE", 0).edit();
        pref.putString(Key , Value);
        pref.apply();
    }

    void Restart(){
        Intent i=getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    void setTheme(){
        SharedPreferences prefs = getSharedPreferences("RECORDINGYOURLIFE", 0);
        theme_this = prefs.getInt("THEME_INDEX" ,0);
        int theme;
        switch (theme_this){
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
                theme=R.style.AppTheme_white;
                break;
            case 0:
            default:
                theme=R.style.AppTheme;
                break;
        }
        setTheme(theme);
    }
}
