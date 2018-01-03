package com.ct.daan.recordingyourlife.Event;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;

import com.ct.daan.mylibrary.SwipeLayout;
import com.ct.daan.recordingyourlife.Class.CalendarFunction;
import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.DbTable.EventDbTable;


public class EventActivity extends AppCompatActivity {
    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    EventDbTable EventDb;
    ListView EventList;
    CalendarView calView;
    CalendarFunction calendarFunction;
    private final static int ADDEVENTPAGE=1895,CHANGEEVENTPAGE=1494;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);

        calendarFunction=new CalendarFunction();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(onClickListener);
        fab.setImageResource(R.drawable.icon_add);

        OpOrCrDb();
        EventDb=new EventDbTable(SQLiteDB_Path,db);
        EventDb.OpenOrCreateTb();

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
        UpdateAdapter();
        if(resultCode==RESULT_OK) {
            Bundle extra= data.getExtras();
            String Name ,Start_date="",End_date,Remark;

            if (!extra.isEmpty()) {
                switch (requestCode) {
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
        date_selected=calendarFunction.getDateString(cal);
        UpdateAdapter(date_selected);
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
            //SimpleCursorAdapter adapter=new SimpleCursorAdapter(EventActivity.this,android.R.layout.simple_list_item_1,Event_Cursor,new String[]{"日子名稱"},new int[]{android.R.id.text1},0);
            ListViewAdapter adapter=new ListViewAdapter(EventActivity.this,Event_Cursor,EventDb,EventList,List_listener,date);
            EventList.setAdapter(adapter);
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
            ((SwipeLayout)(EventList.getChildAt(position - EventList.getFirstVisiblePosition()))).open(true);
        }
    };

    void setTheme(){
        SharedPreferences prefs = getSharedPreferences("RECORDINGYOURLIFE", 0);
        int theme_index = prefs.getInt("THEME_INDEX" ,0);
        int theme=0;
        switch (theme_index){
            case 1:
                theme=R.style.AppTheme1;
                break;
            case 2:
                theme=R.style.AppTheme2;
                break;
            case 3:
                theme= R.style.AppTheme3;
                break;
            case 4:
                theme=R.style.AppTheme4;
                break;
            case 5:
                theme=R.style.AppTheme5;
                break;
            case 6:
                theme=R.style.AppTheme6;
                break;
            case 7:
                theme=R.style.AppTheme7;
                break;
            case 8:
                theme=R.style.AppTheme8;
                break;
            case 9:
                theme=R.style.AppTheme9;
                break;
            case 10:
                theme=R.style.AppTheme10;
                break;
            case 0:
            default:
                theme=R.style.AppTheme0;
                break;
        }
        setTheme(theme);
    }
}
