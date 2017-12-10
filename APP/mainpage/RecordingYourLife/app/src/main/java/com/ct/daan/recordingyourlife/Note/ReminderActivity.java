package com.ct.daan.recordingyourlife.Note;

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

import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.Table.Note_Reminder_DbTable;
import com.ct.daan.recordingyourlife.Table.ReminderDbTable;

public class ReminderActivity extends AppCompatActivity {
    Intent intent;
    int id;
    Cursor cursor;
    ReminderDbTable ReminderDb;
    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    EditText date;
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
        isReplace=(Switch)findViewById(R.id.isreplace_sw);
        ReplaceType=(Spinner) findViewById(R.id.replacetype_sp);
        Complete_btn=(Button)findViewById(R.id.btn_Complete2);

        OpOrCrDb();
        ReminderDb=new ReminderDbTable(SQLiteDB_Path,db);
        ReminderDb.OpenOrCreateTb();
        Cursor cursor=ReminderDb.getCursor(id);
        cursor.moveToFirst();
        date.setText(cursor.getString(1));
        isReplace.setChecked(cursor.getInt(2)==1);
        ReplaceType.setSelection(cursor.getInt(3));
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
            ReminderDb.updateReminderData(id,date.getText().toString(),isReplace.isChecked()?1:0 ,ReplaceType.getSelectedItemPosition());
            setResult(RESULT_OK,intent);
            finish();
        }
    };
}
