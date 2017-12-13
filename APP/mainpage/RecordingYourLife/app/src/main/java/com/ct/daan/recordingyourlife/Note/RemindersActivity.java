package com.ct.daan.recordingyourlife.Note;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.ct.daan.recordingyourlife.Class.OthersFunction;
import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.Table.NoteDbTable;
import com.ct.daan.recordingyourlife.Table.Note_Reminder_DbTable;
import com.ct.daan.recordingyourlife.Table.ReminderDbTable;

/**
 * Created by info on 2017/12/2.
 */
public class RemindersActivity extends AppCompatActivity {
    Intent intent;
    int id;
    Cursor cursor;
    Note_Reminder_DbTable NoteReminderDb;
    ReminderDbTable ReminderDb;
    NoteDbTable NoteDb;
    ListView listView01;
    FloatingActionButton Add_btn;
    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    final static private int ADD_REMINDER=1684,UPDATE_REMINDE=25646;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_activity);
        initView();

        intent=getIntent();
        Bundle extra=intent.getExtras();
        id=extra.getInt("NOTEID");

        UpdateAdapter_Note();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            UpdateAdapter_Note();
        }
        if (resultCode == UPDATE_REMINDE && resultCode==RESULT_OK) {
            Bundle extra = data.getExtras();
            int Reminder_id = extra.getInt("REMINDER_ID");
            OthersFunction othersFunction=new OthersFunction();
            Cursor cursor=NoteDb.getCursor(id);
            cursor.moveToFirst();
            othersFunction.setReminder(RemindersActivity.this,ReminderDb.getCursor(Reminder_id),Reminder_id,cursor.getString(1),cursor.getString(2));
            UpdateAdapter_Note();
        }
    }

    private void initView(){

        Add_btn=(FloatingActionButton)findViewById(R.id.fab);
        Add_btn.setOnClickListener(Add_btn_Listener);
        listView01=(ListView)findViewById(R.id.reminder_lv);
        OpOrCrDb();

        NoteReminderDb=new Note_Reminder_DbTable(SQLiteDB_Path,db);
        NoteReminderDb.OpenOrCreateTb();
        //NoteReminderDb.deleteAllRow();
        //NoteReminderDb.AddNoteReminderData();

        ReminderDb=new ReminderDbTable(SQLiteDB_Path,db);
        ReminderDb.OpenOrCreateTb();

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


    public void UpdateAdapter_Note(){
        try{
            Cursor NoteReminder_Cursor=NoteReminderDb.getCursorByNoteID(id);
            cursor=ReminderDb.getCursor(NoteReminder_Cursor);
            if(cursor !=  null && cursor.getCount()>0){
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{"提醒日期"}, new int[]{android.R.id.text1}, 0);
                listView01.setAdapter(adapter);
                listView01.setOnItemClickListener(ListViewListener);
                Log.v("UpdateAdapter_Note", String.format("UpdateAdapter_Note() 更新成功"));
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
            startActivityForResult(intent,UPDATE_REMINDE);
        }
    };

    FloatingActionButton.OnClickListener Add_btn_Listener= new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent  = new Intent(RemindersActivity.this,AddReminderActivity.class);
            Log.v("NOTEID",id+"");
            intent.putExtra("NOTEID",id);
            startActivityForResult (intent,ADD_REMINDER);
        }
    };



}
