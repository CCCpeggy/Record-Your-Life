package com.ct.daan.recordingyourlife.Exam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ct.daan.recordingyourlife.Class.OthersFunction;
import com.ct.daan.recordingyourlife.DbTable.ExamDbTable;
import com.ct.daan.recordingyourlife.DbTable.ReminderDbTable;
import com.ct.daan.recordingyourlife.R;

/**
 * Created by info on 2017/12/23.
 */

public class InputScoreActivity extends AppCompatActivity{

    private final static String SQLiteDB_Path="student_project.db";
    private SQLiteDatabase db=null;
    Intent intent;
    int ExamID,ReminderID;
    ExamDbTable ExamDb;
    ReminderDbTable ReminderDb;
    TextView tv;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_input_score);

        intent=getIntent();
        Bundle extra=intent.getExtras();
        ExamID=extra.getInt("EXAMID",0);
        ReminderID=extra.getInt("REMINDERID",0);
        Log.v("ExamID",ExamID+"");
        Log.v("ReminderID",ReminderID+"");

        name=extra.getString("NAME","");


        initView();
    }

    void initView(){
        tv=(TextView)findViewById(R.id.text) ;
        tv.setText("請輸入"+name+"的成績");
        OpOrCrDb();
        ExamDb=new ExamDbTable(SQLiteDB_Path,db);
        ExamDb.OpenOrCreateTb();

        ReminderDb=new ReminderDbTable(SQLiteDB_Path,db);
        ReminderDb.OpenOrCreateTb();

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
                String score= ((EditText)findViewById(R.id.Score_et)).getText().toString();
                if(score.isEmpty()){
                    Toast.makeText(InputScoreActivity.this,"成績不可為空",Toast.LENGTH_LONG).show();
                    return true;
                }
                ExamDb.updateExamData(ExamID,score.equals("")?-100:Integer.parseInt(score));
                ReminderDb.deleteReminderData(ReminderID);
                finish();
                return true;
            case android.R.id.home:
                finish();
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
                theme=R.style.AppTheme1;
                break;
            case 2:
                theme=R.style.AppTheme2;
                break;
            case 3:
                theme= R.style.AppTheme3;
                break;
            case 4:
                theme=R.style.AppTheme4;
                break;
            case 5:
                theme=R.style.AppTheme5;
                break;
            case 6:
                theme=R.style.AppTheme6;
                break;
            case 7:
                theme=R.style.AppTheme7;
                break;
            case 8:
                theme=R.style.AppTheme8;
                break;
            case 9:
                theme=R.style.AppTheme9;
                break;
            case 10:
                theme=R.style.AppTheme10;
                break;
            case 0:
            default:
                theme=R.style.AppTheme0;
                break;
        }
        setTheme(theme);
    }
}
