package com.example.info.note;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.info.note.Table.NoteDbTable;
import com.example.info.note.Table.Note_Reminder_DbTable;
import com.example.info.note.Table.ReminderDbTable;

import java.util.Locale;

public class AddReminderActivity extends AppCompatActivity {
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
    NoteDbTable NoteDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        intent=getIntent();
        Bundle extra=intent.getExtras();
        id=extra.getInt("NOTEID");
        Log.v("NOTEID",id+"");

        initView();
    }

    private Calendar StringtoCalendar(String date,String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddhh:mm", Locale.TAIWAN);
        Calendar Calendar= android.icu.util.Calendar.getInstance();
        try{
            Calendar.setTime(sdf.parse(date+time));
        }catch (Exception e){
            Toast.makeText(AddReminderActivity.this,"yyyy-MM-ddhh:mm",Toast.LENGTH_SHORT).show();
        }
        return Calendar;
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
        NoteDb=new NoteDbTable(SQLiteDB_Path,db);
        NoteDb.OpenOrCreateTb();
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
    private void setReminder(Context context,String date,String time,int reminderId){
        Calendar cal=StringtoCalendar(date,time);
        String BROADCAST_ACTION="net.macdidi.broadcast01.action.MYBROADCAST01";
        Cursor Note_cursor=NoteDb.getCursor(id);
        Note_cursor.moveToFirst();
        Intent intent=new Intent(BROADCAST_ACTION);
        intent.putExtra("REMINDERID",reminderId);
        intent.putExtra("TITLE",Note_cursor.getString(1));
        intent.putExtra("CONTENT",Note_cursor.getString(2));
        PendingIntent sender=PendingIntent.getBroadcast(context,0,intent,0);
        AlarmManager alarm= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis()+5000,3000,sender);
    }

    Button.OnClickListener Complete_btn_Listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            ReminderDb.insertReminderData(date.getText().toString(),isReplace.isChecked()?1:0 ,ReplaceType.getSelectedItemPosition());
            Cursor cursor=ReminderDb.getCursor();
            cursor.moveToLast();
            NoteReminderDb.insertNoteReminderData(cursor.getInt(0),id);
            setReminder(AddReminderActivity.this,date.getText().toString(),"08:00",cursor.getInt(0));
            setResult(RESULT_OK,intent);
            finish();
        }
    };
}
