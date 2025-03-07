package com.ct.daan.recordingyourlife.Diary;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.ct.daan.recordingyourlife.Class.OthersFunction;
import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.Table.DiaryDbTable;

import java.util.Locale;

public class AddDiaryPageActivity extends AppCompatActivity {

    EditText et2;
    Button btn_Complete,btn_Date;
    Intent intent;
    DiaryDbTable DiaryDb;
    int id;

    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_page);

        initView();
        intent=getIntent();
        btn_Date.setText("日期");
        et2.setText("");

        OpOrCrDb();
        DiaryDb=new DiaryDbTable(SQLiteDB_Path,db);
        DiaryDb.OpenOrCreateTb();
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

    public void initView(){
        et2=(EditText) findViewById(R.id.et2);
        btn_Complete=(Button)findViewById(R.id.btn_Complete) ;
        btn_Date=(Button)findViewById(R.id.Selected_Date);

        btn_Complete.setOnClickListener(btn_Complete_Listener);
        btn_Date.setOnClickListener(btn_Date_Listener);
    }

    private Button.OnClickListener btn_Complete_Listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            DiaryDb.insertDiaryData(btn_Date.getText().toString(),et2.getText().toString());
            setResult(RESULT_OK,intent);
            finish();
        }
    };

    Calendar m_Calendar = Calendar.getInstance();
    private Button.OnClickListener btn_Date_Listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog dataPick=new DatePickerDialog(AddDiaryPageActivity.this,datepicker,
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
            btn_Date.setText(sdf.format(m_Calendar.getTime()));
        }
    };
}
