package com.example.info.note;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

public class AddReminderActivity2 extends AppCompatActivity {
    Intent intent;
    int id;
    ReminderDbTable ReminderDb;
    Note_Reminder_DbTable NoteReminderDb;
    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    EditText date;
    Switch isReplace;
    Spinner ReplaceType;
    Button Complete_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        intent=getIntent();

        initView();
    }
    private void initView(){
        date=(EditText)findViewById(R.id.date_et);
        isReplace=(Switch)findViewById(R.id.isreplace_sw);
        ReplaceType=(Spinner) findViewById(R.id.replacetype_sp);
        Complete_btn=(Button)findViewById(R.id.btn_Complete2);

        OpOrCrDb();
        NoteReminderDb=new Note_Reminder_DbTable(SQLiteDB_Path,db);
        NoteReminderDb.OpenOrCreateTb();
        ReminderDb=new ReminderDbTable(SQLiteDB_Path,db);
        ReminderDb.OpenOrCreateTb();
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
    private void ClickMe(){
        NotificationCompat.Builder mBuilder=(NotificationCompat.Builder)new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Test")
                .setContentText("This is test");
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,mBuilder.build());
    }

    Button.OnClickListener Complete_btn_Listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            ReminderDb.insertReminderData(date.getText().toString(),isReplace.isChecked()?1:0 ,ReplaceType.getSelectedItemPosition());
            Cursor cursor=ReminderDb.getCursor();
            cursor.moveToLast();
            intent.putExtra("REMINDERID",cursor.getInt(0));
            setResult(RESULT_OK,intent);
            finish();
        }
    };
}
