package com.example.info.dairyactivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Locale;

import static android.media.CamcorderProfile.get;

/**
 * Created by info on 2017/10/27.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class DairyPageActivity  extends AppCompatActivity {

    EditText et2;
    Button btn_Complete,btn_Delete,btn_Date;
    Intent intent;
    private final static int UPDATE_TYPE=0,ADD_TYPE=1,DELETE_TYPE=2;
    int id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diarypage);

        initView();

        intent=getIntent();
        Bundle extra=intent.getExtras();
        switch (extra.getInt("TYPE")){
            case UPDATE_TYPE:
                id=extra.getInt("SELECTED_ID");
                btn_Date.setText(extra.getString("SELECTED_DATE").equals("")?"日期":extra.getString("SELECTED_DATE"));
                /*if(!extra.getString("SELECTED_DATE").equals("")) btn_Date.setText(extra.getString("SELECTED_DATE"));
                else btn_Date.setText("日期");*/
                et2.setText(extra.getString("SELECTED_CONTENT"));
                break;
            case ADD_TYPE:
                btn_Date.setText("日期");
                et2.setText("");
                break;
        }


    }

    public void initView(){
        et2=(EditText) findViewById(R.id.et2);
        btn_Complete=(Button)findViewById(R.id.btn_Complete) ;
        btn_Delete=(Button)findViewById(R.id.btn_Delete);
        btn_Date=(Button)findViewById(R.id.Selected_Date);

        btn_Complete.setOnClickListener(btn_Complete_Listener);
        btn_Delete.setOnClickListener(btn_Delete_Listener);
        btn_Date.setOnClickListener(btn_Date_Listener);
    }

    private Button.OnClickListener btn_Complete_Listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent.putExtra("TYPE",UPDATE_TYPE);
            intent.putExtra("SELECTED_ID",id);
            intent.putExtra("CHANGED_DATE",btn_Date.getText().toString());
            intent.putExtra("CHANGED_CONTENT",et2.getText().toString());
            Log.v("回傳資料",String.format("回傳資料：%s=%d,%s=%s,%s=%s","SELECTED_ID",id,"CHANGED_DATE",btn_Date.getText(),"CHANGED_CONTENT",et2.getText()));
            setResult(RESULT_OK,intent);
            finish();
        }
    };
    private Button.OnClickListener btn_Delete_Listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent.putExtra("TYPE",DELETE_TYPE);
            intent.putExtra("SELECTED_ID",id);
            Log.v("回傳資料",String.format("回傳資料：%s=%d","SELECTED_ID",id));
            setResult(RESULT_OK,intent);
            finish();
        }
    };

    Calendar m_Calendar = Calendar.getInstance();
    private Button.OnClickListener btn_Date_Listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog dataPick=new DatePickerDialog(DairyPageActivity.this,datepicker,
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
