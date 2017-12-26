package com.ct.daan.recordingyourlife.Note;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.ct.daan.recordingyourlife.Class.CalendarFunction;
import com.ct.daan.recordingyourlife.Class.OthersFunction;
import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.DbTable.NoteDbTable;
import com.ct.daan.recordingyourlife.DbTable.Note_Reminder_DbTable;
import com.ct.daan.recordingyourlife.DbTable.ReminderDbTable;


import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {

    private SQLiteDatabase db=null;
    private final static int LISTVIEWPAGE=498,ADDLISTVIEWPAGE=1578;
    final private static int RESULT_DELETE=200;
    private String SQLiteDB_Path="student_project.db";
    Note_Reminder_DbTable NoteReminderDb;
    ReminderDbTable ReminderDb;
    NoteDbTable NoteDb;
    ListView listView01;
    CalendarFunction calFunction;
    OthersFunction othersFunction;
    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);
        calFunction=new CalendarFunction();
        othersFunction=new OthersFunction();
        initView();

        //其餘動作
        OpOrCrDb();
        NoteDb=new NoteDbTable(SQLiteDB_Path,db);
        NoteDb.OpenOrCreateTb();

        NoteReminderDb=new Note_Reminder_DbTable(SQLiteDB_Path,db);
        NoteReminderDb.OpenOrCreateTb();

        ReminderDb=new ReminderDbTable(SQLiteDB_Path,db);
        ReminderDb.OpenOrCreateTb();

        UpdateAdapter_Note();

    }

    private void initView() {
        //變數指定物件
        listView01=(ListView)findViewById(R.id.listv);
        btnAdd=(FloatingActionButton)findViewById(R.id.fab);
        btnAdd.setImageResource(R.drawable.icon_add);

        //監控事件
        btnAdd.setOnClickListener(btnAddClick);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode==RESULT_DELETE){
            Bundle extra= data.getExtras();
            int Selected_id = extra.getInt("SELECTED_ID");
            NoteDb.deleteNoteData(Selected_id);
            UpdateAdapter_Note();
        }
        if(resultCode==RESULT_CANCELED&&requestCode==ADDLISTVIEWPAGE){
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
                    case LISTVIEWPAGE:
                        int Selected_id = extra.getInt("SELECTED_ID");
                        Changed_title = extra.getString("CHANGED_TITLE");
                        Changed_content = extra.getString("CHANGED_CONTENT");
                        NoteDb.updateNoteData( Selected_id,Changed_title,Changed_content);

                        break;
                    case ADDLISTVIEWPAGE:
                        Changed_title = extra.getString("CHANGED_TITLE");
                        Changed_content = extra.getString("CHANGED_CONTENT");
                        ArrayList<Integer> reminder_ids=extra.getIntegerArrayList("REMINDERIDS");
                        NoteDb.insertNoteData(Changed_title,Changed_content);
                        Cursor Note_cursor=NoteDb.getCursor();
                        Note_cursor.moveToLast();
                        int Note_id=Note_cursor.getInt(0);
                        for (int Reminder_id:reminder_ids){
                            NoteReminderDb.insertNoteReminderData(Reminder_id,Note_id);
                            othersFunction.setReminder(NoteActivity.this,ReminderDb.getCursor(Reminder_id),Reminder_id,Changed_title,Changed_content);
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


    private FloatingActionButton.OnClickListener btnAddClick=new FloatingActionButton.OnClickListener(){
        int n=0;
        @Override
        public void onClick(View v) {
            Intent intent  = new Intent(NoteActivity.this,NewNotePageActivity.class);
            startActivityForResult(intent,ADDLISTVIEWPAGE);
        }
    };
    Cursor cursor;
    public void UpdateAdapter_Note(){
        try{
            cursor=NoteDb.getCursor();
            if(cursor !=  null && cursor.getCount()>0){
                //ListView格式自訂
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.note_listview_item_layout, cursor, new String[]{"便條標題", "便條內容"}, new int[]{R.id.text1,R.id.text2}, 0);
                //ListView格式預設
                //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[]{"便條標題", "便條內容"}, new int[]{android. R.id.text1,android.R.id.text2}, 0);
                listView01.setAdapter(adapter);
                listView01.setOnItemLongClickListener(List_Long_Listener);
                listView01.setOnItemClickListener(List_listener);
                Log.v("UpdateAdapter_Note", String.format("UpdateAdapter_Note() 更新成功"));
            }
        }catch (Exception e){
            Log.e("#004","清單更新失敗");
        }

    }

    public void UpdateAdapter_Note(String Search_word){
        try{
            cursor=NoteDb.getCursor(" 便條標題 LIKE '%"+Search_word+"%' OR  便條內容 LIKE '%"+Search_word+"%'");
            if(cursor !=  null && cursor.getCount()>0){
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[]{"便條標題", "便條內容"}, new int[]{android. R.id.text1,android.R.id.text2}, 0);
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
            startActivityForResult(intent,LISTVIEWPAGE);
        }
    };

    private ListView.OnItemLongClickListener List_Long_Listener = new ListView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            cursor.moveToPosition(position);
            int Click_ID=cursor.getInt(0);
            try{

                Log.v("刪除資料列", String.format("在%s刪除一筆資料：%s=%d",NoteDb.SQLiteTable_Name,"ID",Click_ID));
            }catch (Exception e){
                Log.e("#006","資料列刪除失敗");
            }
            return false;
        }
    };


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_actions, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(NoteActivity.this,query,Toast.LENGTH_SHORT).show();
                UpdateAdapter_Note(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                UpdateAdapter_Note(newText);
                return false;
            }
        });

        // 顯示完成鈕
        //searchView.setSubmitButtonEnabled(true);


        return true;
    }

    //動作按鈕回應
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast toast１ =Toast.makeText(this,"Search",Toast.LENGTH_LONG);


                toast１.show();
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
