package com.ct.daan.recordingyourlife.Diary;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.ct.daan.recordingyourlife.R;

import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class DiaryPageActivity extends AppCompatActivity {

    EditText et2;
    Button btn_Date;
    Intent intent;
    int id;

    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_page);

        initView();

        intent=getIntent();
        Bundle extra=intent.getExtras();
        extra.getInt("TYPE");
        id=extra.getInt("SELECTED_ID");
        btn_Date.setText(extra.getString("SELECTED_DATE").equals("")?"日期":extra.getString("SELECTED_DATE"));
        et2.setText(extra.getString("SELECTED_CONTENT"));
    }

    public void initView(){
        et2=(EditText) findViewById(R.id.et2);
        btn_Date=(Button)findViewById(R.id.Selected_Date);

        btn_Date.setOnClickListener(btn_Date_Listener);
    }

    private void Complete() {
        intent.putExtra("SELECTED_ID",id);
        intent.putExtra("CHANGED_DATE",btn_Date.getText().toString());
        intent.putExtra("CHANGED_CONTENT",et2.getText().toString());
        Log.v("回傳資料", String.format("回傳資料：%s=%d,%s=%s,%s=%s","SELECTED_ID",id,"CHANGED_DATE",btn_Date.getText(),"CHANGED_CONTENT",et2.getText()));
        setResult(RESULT_OK,intent);
        finish();
    }


    Calendar m_Calendar = Calendar.getInstance();
    private Button.OnClickListener btn_Date_Listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog dataPick=new DatePickerDialog(DiaryPageActivity.this,datepicker,
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
                Complete();
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
