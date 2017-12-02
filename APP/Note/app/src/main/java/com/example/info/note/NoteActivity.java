package com.example.info.note;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
//修改內容

public class NoteActivity extends AppCompatActivity {

    private SQLiteDatabase db=null;
    private final static int LISTPAGE_QAQQ=0,NEWLISTPAGE_QAQQ=1;
    final private static int RESULT_DELETE=200;
    private String SQLiteDB_Path="student_project.db";
    Note_Reminder_DbTable NoteReminderDb;
    ReminderDbTable ReminderDb;
    NoteDbTable NoteDb;
    ListView listView01;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        initView();

        //其餘動作
        OpOrCrDb();
        NoteDb=new NoteDbTable(SQLiteDB_Path,db);
        NoteDb.OpenOrCreateTb();
        NoteDb.deleteAllRow();
        NoteDb.AddNoteData();

        NoteReminderDb=new Note_Reminder_DbTable(SQLiteDB_Path,db);
        NoteReminderDb.OpenOrCreateTb();
        NoteReminderDb.deleteAllRow();
        NoteReminderDb.AddNoteReminderData();

        ReminderDb=new ReminderDbTable(SQLiteDB_Path,db);
        ReminderDb.OpenOrCreateTb();
        ReminderDb.deleteAllRow();
        ReminderDb.AddReminderData();

        UpdateAdapter_Note();
    }

    private void initView() {
        //變數指定物件
        listView01=(ListView)findViewById(R.id.listv);
        btnAdd=(Button)findViewById(R.id.Add);

        //監控事件
        btnAdd.setOnClickListener(btnAddClick);
    }


    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode==RESULT_DELETE){
            Bundle extra= data.getExtras();
            String Changed_title ;
            String Changed_content;
            int Selected_id = extra.getInt("SELECTED_ID");
            NoteDb.deleteNoteData(Selected_id);
            UpdateAdapter_Note();
        }
        if(resultCode==RESULT_CANCELED&&requestCode==NEWLISTPAGE_QAQQ){
            Bundle extra= data.getExtras();
            if(extra.isEmpty())return;
            ArrayList<Integer> reminder_ids=extra.getIntegerArrayList("REMINDERIDS");
            for(int i:reminder_ids){
                ReminderDb.deleteReminderData(i);
            }
        }
        if(resultCode==RESULT_OK) {
            Bundle extra= data.getExtras();
            String Changed_title ;
            String Changed_content;

            if (!extra.isEmpty()) {
                switch (requestCode) {
                    case LISTPAGE_QAQQ:
                        int Selected_id = extra.getInt("SELECTED_ID");
                        Changed_title = extra.getString("CHANGED_TITLE");
                        Changed_content = extra.getString("CHANGED_CONTENT");
                        NoteDb.updateNoteData( Selected_id,Changed_title,Changed_content);

                        break;
                    case NEWLISTPAGE_QAQQ:
                        Changed_title = extra.getString("CHANGED_TITLE");
                        Changed_content = extra.getString("CHANGED_CONTENT");
                        ArrayList<Integer> reminder_ids=extra.getIntegerArrayList("REMINDERIDS");
                        NoteDb.insertNoteData(Changed_title,Changed_content);
                        Cursor Note_cursor=NoteDb.getCursor();
                        Note_cursor.moveToLast();
                        for (int Reminder_id:reminder_ids){
                            NoteReminderDb.insertNoteReminderData(Reminder_id,Note_cursor.getInt(0));
                        }
                        break;
                }
                UpdateAdapter_Note();
            }
        }
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

    //打開或新增資料表
    private void OpOrCrTb(String cmd){
        try{
            db.execSQL(cmd);
            Log.v("資料表","資料表建立或開啟成功");
        }catch (Exception ex){
            Log.e("#002","資料表建立或開啟錯誤");
        }
    }

    private Button.OnClickListener btnUpdateClick=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

        }
    };

    private Button.OnClickListener btnAddClick=new Button.OnClickListener(){

        int n=0;
        @Override
        public void onClick(View v) {
            Intent intent  = new Intent(NoteActivity.this,NewNotePageActivity.class);
            startActivityForResult(intent,NEWLISTPAGE_QAQQ);
        }

    };
    Cursor cursor;
    public void UpdateAdapter_Note(){
        try{
            cursor=NoteDb.getCursor();
            if(cursor !=  null && cursor.getCount()>0){
                //ListView格式自訂
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.listviewitem_layout, cursor, new String[]{"便條標題", "便條內容"}, new int[]{R.id.text1,R.id.text2}, 0);
                //ListView格式預設
                //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[]{"便條標題", "便條內容"}, new int[]{android. R.id.text1,android.R.id.text2}, 0);
                listView01.setAdapter(adapter);
                listView01.setOnItemLongClickListener(List_Long_Listener);
                listView01.setOnItemClickListener(List_listener);
                Log.v("UpdateAdapter_Note",String.format("UpdateAdapter_Note() 更新成功"));
            }
        }catch (Exception e){
            Log.e("#004","清單更新失敗");
        }

    }

    private ListView.OnItemClickListener List_listener=new ListView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            cursor.moveToPosition(position);
            int Selected_ID=cursor.getInt(0);
            Intent intent  = new Intent(NoteActivity.this,NotePageActivity.class);
            intent.putExtra("SELECTED_ID",Selected_ID);
            intent.putExtra("SELECTED_TITLE",cursor.getString(1));
            intent.putExtra("SELECTED_CONTENT",cursor.getString(2));
            startActivityForResult(intent,LISTPAGE_QAQQ);
        }
    };

    private ListView.OnItemLongClickListener List_Long_Listener = new ListView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            cursor.moveToPosition(position);
            int Click_ID=cursor.getInt(0);
            try{

                Log.v("刪除資料列",String.format("在%s刪除一筆資料：%s=%d",NoteDb.SQLiteTable_Name,"ID",Click_ID));
            }catch (Exception e){
                Log.e("#006","資料列刪除失敗");
            }
            return false;
        }
    };

}
