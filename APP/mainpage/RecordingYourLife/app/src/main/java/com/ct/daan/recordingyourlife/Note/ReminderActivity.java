package com.ct.daan.recordingyourlife.Note;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.DbTable.ReminderDbTable;

public class ReminderActivity extends AppCompatActivity {
    Intent intent;
    int id;
    Cursor cursor;
    ReminderDbTable ReminderDb;
    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    EditText date,time;
    Switch isReplace;
    Spinner ReplaceType;
    Button Complete_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_page);

        intent=getIntent();
        Bundle extra=intent.getExtras();
        id=extra.getInt("REMINDERID");

        initView();
    }
    private void initView(){
        date=(EditText)findViewById(R.id.date_et);
        time=(EditText)findViewById(R.id.time_et);
        isReplace=(Switch)findViewById(R.id.isreplace_sw);
        ReplaceType=(Spinner) findViewById(R.id.replacetype_sp);
        Complete_btn=(Button)findViewById(R.id.btn_Complete2);

        OpOrCrDb();
        ReminderDb=new ReminderDbTable(SQLiteDB_Path,db);
        ReminderDb.OpenOrCreateTb();
        Cursor cursor=ReminderDb.getCursor(id);
        cursor.moveToFirst();
        date.setText(cursor.getString(1));
        time.setText(cursor.getString(2));
        isReplace.setChecked(cursor.getInt(3)==1);
        ReplaceType.setSelection(cursor.getInt(4));
        Complete_btn.setOnClickListener(Complete_btn_Listener);
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

    Button.OnClickListener Complete_btn_Listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            ReminderDb.updateReminderData(id,date.getText().toString(),time.getText().toString(),isReplace.isChecked()?1:0 ,ReplaceType.getSelectedItemPosition());
            intent.putExtra("REMINDER_ID",id);
            setResult(RESULT_OK,intent);
            finish();
        }
    };
}
