package com.ct.daan.recordingyourlife.Event;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.ct.daan.recordingyourlife.Class.CalendarFunction;
import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.DbTable.EventDbTable;

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
        setContentView(R.layout.event_activity);

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
            intent.putExtra("DATE",calView.getDate());
            startActivityForResult(intent,ADDEVENTPAGE);
        }
    };

    public void onActivityResult(int requestCode,int resultCode,Intent data){

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
        Calendar cal= Calendar.getInstance();
        String date= String.format("%04d-%02d-%02d",cal.get(Calendar.YEAR),(cal.get(Calendar.MONTH)+1),cal.get(Calendar.DAY_OF_MONTH));
        UpdateAdapter(date);

    }
    public void UpdateAdapter(String date) {
        if(date.equals("")){
            UpdateAdapter();
            return;
        }
        try {
            CalendarFunction calendarFunction=new CalendarFunction();
            Calendar calendar=calendarFunction.DateTextToCalendarType(date);
            calView.setDate(calendar.getTimeInMillis());
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
            String date= String.format("%04d-%02d-%02d",year,(month+1),dayOfMonth);
            date_selected=date;
            UpdateAdapter(date);
        }
    };


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


}
