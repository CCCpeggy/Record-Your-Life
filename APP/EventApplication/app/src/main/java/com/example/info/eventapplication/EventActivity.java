package com.example.info.eventapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.Locale;

public class EventActivity extends AppCompatActivity {
    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    EventDbTable EventDb;
    ListView EventList;
    CalendarView calView;
    private final static int ADDEVENTPAGE=1895,CHANGEEVENTPAGE=1494;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(onClickListener);

        OpOrCrDb();
        EventDb=new EventDbTable(SQLiteDB_Path,db);
        EventDb.OpenOrCreateTb();
        EventDb.deleteAllRow();
        EventDb.AddEventData();

        EventList =(ListView)findViewById(R.id.Event);
        calView = (CalendarView)findViewById(R.id.calendarView);
        calView.setOnDateChangeListener(CalViewListener);
        UpdateAdapter();


    }

    FloatingActionButton.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent  = new Intent(EventActivity.this,AddEventActivity.class);
            startActivityForResult(intent,ADDEVENTPAGE);
        }
    };

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        /*if(resultCode==RESULT_DELETE){
            Bundle extra= data.getExtras();
            String Changed_title ;
            String Changed_content;
            int Selected_id = extra.getInt("SELECTED_ID");
            NoteDb.deleteNoteData(Selected_id);
            UpdateAdapter_Note();
        }*/
        if(resultCode==RESULT_OK) {
            Bundle extra= data.getExtras();
            String Name ,Start_date="",End_date,Remark;

            if (!extra.isEmpty()) {
                switch (requestCode) {
                    case CHANGEEVENTPAGE:
                        int Selected_id = extra.getInt("SELECTED_ID");
                        Name = extra.getString("NAME");
                        Start_date= extra.getString("STARTDATE");
                        End_date= extra.getString("ENDDATE");
                        Remark= extra.getString("REMARK");
                        EventDb.updateEventData( Selected_id,Name ,Start_date,End_date,Remark);

                        break;
                    case ADDEVENTPAGE:
                        Name = extra.getString("NAME");
                        Start_date= extra.getString("STARTDATE");
                        End_date= extra.getString("ENDDATE");
                        Remark= extra.getString("REMARK");
                        EventDb.insertEventData(Name ,Start_date,End_date,Remark);
                        UpdateAdapter(Start_date);
                        break;
                }

            }
        }
    }


    public void UpdateAdapter() {
        Calendar cal=Calendar.getInstance();
        String date=String.format("%04d-%02d-%02d",cal.get(Calendar.YEAR),(cal.get(Calendar.MONTH)+1),cal.get(Calendar.DAY_OF_MONTH));
        UpdateAdapter(date);

    }
    public void UpdateAdapter(String date) {
        if(date.equals("")){
            UpdateAdapter();
            return;
        }
        try {
            Cursor Event_Cursor=EventDb.getCursorByDay(date);
            SimpleCursorAdapter adapter=new SimpleCursorAdapter(EventActivity.this,android.R.layout.simple_list_item_1,Event_Cursor,new String[]{"日子名稱"},new int[]{android.R.id.text1},0);
            EventList.setAdapter(adapter);
            EventList.setOnItemClickListener(List_listener);
            Log.v("UpdateAdapter", String.format("UpdateAdapter() 更新成功"));

        } catch (Exception e) {
            Log.e("#004", "清單更新失敗");
        }

    }
    String date_selected;
    private CalendarView.OnDateChangeListener CalViewListener=new CalendarView.OnDateChangeListener() {
        @Override
        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
            String date=String.format("%04d-%02d-%02d",year,(month+1),dayOfMonth);
            date_selected=date;
            UpdateAdapter(date);
        }
    };

    private Calendar StringtoCalendar(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN);
        Calendar Calendar= android.icu.util.Calendar.getInstance();
        try{
            Calendar.setTime(sdf.parse(date));
        }catch (Exception e){
            Log.v("日期格式不符合",date);
        }
        return Calendar;
    }


    private void OpOrCrDb(){
        try{
            db=openOrCreateDatabase(SQLiteDB_Path,MODE_PRIVATE,null);
            Log.v("資料庫","資料庫載入成功");
        }catch (Exception ex){
            Log.e("#001","資料庫載入錯誤");
        }
    }

    private ListView.OnItemClickListener List_listener=new ListView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Cursor cursor=EventDb.getCursorByDay(date_selected);
            cursor.moveToPosition(position);
            int Selected_ID=cursor.getInt(0);
            Log.v("Selected_ID",cursor.getInt(0)+"");
            Intent intent  = new Intent(EventActivity.this,ChangEventActivity.class);
            intent.putExtra("SELECTED_ID",Selected_ID);
            startActivityForResult(intent,CHANGEEVENTPAGE);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
