package com.example.info.note;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ReminderActivity extends AppCompatActivity {
    Intent intent;
    int id;
    Cursor cursor;
    Note_Reminder_DbTable NoteReminderDb;
    ListView listView01;
    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        initView();

        intent=getIntent();
        Bundle extra=intent.getExtras();
        id=extra.getInt("NOTEID");

        UpdateAdapter_Note();
        ClickMe();
    }
    private void initView(){
        listView01=(ListView)findViewById(R.id.reminder_lv);
        OpOrCrDb();
        NoteReminderDb=new Note_Reminder_DbTable(SQLiteDB_Path,db);
        NoteReminderDb.OpenOrCreateTb();
        NoteReminderDb.deleteAllRow();
        NoteReminderDb.AddNoteReminderData();
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

    public void UpdateAdapter_Note(){
        try{
            cursor=NoteReminderDb.getCursorByNoteID(id);
            if(cursor !=  null && cursor.getCount()>0){
                //ListView格式自訂
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{"_id"}, new int[]{android.R.id.text1}, 0);
                //ListView格式預設
                //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[]{"便條標題", "便條內容"}, new int[]{android. R.id.text1,android.R.id.text2}, 0);
                listView01.setAdapter(adapter);
                Log.v("UpdateAdapter_Note",String.format("UpdateAdapter_Note() 更新成功"));
            }
        }catch (Exception e){
            Log.e("#004","清單更新失敗");
        }

    }
}
