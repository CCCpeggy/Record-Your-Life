package com.example.info.note;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by info on 2017/12/2.
 */
public class RemindersActivity extends AppCompatActivity {
    Intent intent;
    int id;
    Cursor cursor;
    Note_Reminder_DbTable NoteReminderDb;
    ReminderDbTable ReminderDb;
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
    }
    private void initView(){
        listView01=(ListView)findViewById(R.id.reminder_lv);
        OpOrCrDb();

        NoteReminderDb=new Note_Reminder_DbTable(SQLiteDB_Path,db);
        NoteReminderDb.OpenOrCreateTb();
        NoteReminderDb.deleteAllRow();
        NoteReminderDb.AddNoteReminderData();

        ReminderDb=new ReminderDbTable(SQLiteDB_Path,db);
        ReminderDb.OpenOrCreateTb();
        ReminderDb.deleteAllRow();
        ReminderDb.AddReminderData();
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


    public void UpdateAdapter_Note(){
        try{
            Cursor NoteReminder_Cursor=NoteReminderDb.getCursorByNoteID(id);
            cursor=ReminderDb.getCursor(NoteReminder_Cursor);
            if(cursor !=  null && cursor.getCount()>0){
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{"提醒日期"}, new int[]{android.R.id.text1}, 0);
                listView01.setAdapter(adapter);
                listView01.setOnItemClickListener(ListViewListener);
                Log.v("UpdateAdapter_Note",String.format("UpdateAdapter_Note() 更新成功"));
            }
        }catch (Exception e){
            Log.e("#004","清單更新失敗");
        }

    }
    ListView.OnItemClickListener ListViewListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            cursor.moveToPosition(position);
            Intent intent  = new Intent(RemindersActivity.this,ReminderActivity.class);
            intent.putExtra("REMINDERID",cursor.getInt(0));
            startActivity(intent);
        }
    };


}
