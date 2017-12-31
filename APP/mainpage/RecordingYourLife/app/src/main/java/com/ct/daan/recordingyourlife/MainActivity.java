package com.ct.daan.recordingyourlife;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ct.daan.recordingyourlife.DbTable.ClassDbTable;
import com.ct.daan.recordingyourlife.DbTable.ClassWeekDbTable;
import com.ct.daan.recordingyourlife.DbTable.DiaryDbTable;
import com.ct.daan.recordingyourlife.DbTable.EventDbTable;
import com.ct.daan.recordingyourlife.DbTable.ExamDbTable;
import com.ct.daan.recordingyourlife.DbTable.NoteDbTable;
import com.ct.daan.recordingyourlife.DbTable.NoteScheduleDbTable;
import com.ct.daan.recordingyourlife.DbTable.Note_Reminder_DbTable;
import com.ct.daan.recordingyourlife.DbTable.ReminderDbTable;
import com.ct.daan.recordingyourlife.DbTable.ScheduleDbTable;
import com.ct.daan.recordingyourlife.DbTable.SubjectDbTable;
import com.ct.daan.recordingyourlife.DbTable.TableDbTable;
import com.ct.daan.recordingyourlife.DbTable.WeekDbTable;
import com.ct.daan.recordingyourlife.Exam.InputScoreActivity;
import com.ct.daan.recordingyourlife.MainPage.MainPageActivity;
import com.ct.daan.recordingyourlife.MainPage.PagerAdapter;
import com.ct.daan.recordingyourlife.Schedule.ScheduleActivity;

public class MainActivity extends AppCompatActivity  implements MainPageActivity.OnFragmentInteractionListener,ScheduleActivity.OnFragmentInteractionListener{
    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initDateBase();
        //deleteAllData();
        //AddAllData();
        //Test(MainActivity.this);
        TabLayout tabLayout=(TabLayout)findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("主畫面"));
        tabLayout.addTab(tabLayout.newTab().setText("行程"));

        final ViewPager viewPager=(ViewPager)findViewById(R.id.pager);
        final PagerAdapter adapter=new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    
    ClassDbTable classDbTable;
    ClassWeekDbTable classWeekDbTable;
    DiaryDbTable diaryDbTable;
    EventDbTable eventDbTable;
    ExamDbTable examDbTable;
    Note_Reminder_DbTable note_reminder_dbTable;
    NoteDbTable noteDbTable;
    NoteScheduleDbTable noteScheduleDbTable;
    ReminderDbTable reminderDbTable;
    ScheduleDbTable scheduleDbTable;
    SubjectDbTable subjectDbTable;
    TableDbTable tableDbTable;
    WeekDbTable weekDbTable;
    
    private void initDateBase(){
        OpOrCrDb();
        classDbTable=new ClassDbTable(SQLiteDB_Path,db);
        classWeekDbTable=new ClassWeekDbTable(SQLiteDB_Path,db);
        diaryDbTable=new DiaryDbTable(SQLiteDB_Path,db);
        eventDbTable=new EventDbTable(SQLiteDB_Path,db);
        examDbTable=new ExamDbTable(SQLiteDB_Path,db);
        note_reminder_dbTable=new Note_Reminder_DbTable(SQLiteDB_Path,db);
        noteDbTable=new NoteDbTable(SQLiteDB_Path,db);
        noteScheduleDbTable=new NoteScheduleDbTable(SQLiteDB_Path,db);
        reminderDbTable=new ReminderDbTable(SQLiteDB_Path,db);
        scheduleDbTable=new ScheduleDbTable(SQLiteDB_Path,db);
        subjectDbTable=new SubjectDbTable(SQLiteDB_Path,db);
        tableDbTable=new TableDbTable(SQLiteDB_Path,db);
        weekDbTable=new WeekDbTable(SQLiteDB_Path,db);

        classDbTable.OpenOrCreateTb();
        classWeekDbTable.OpenOrCreateTb();
        diaryDbTable.OpenOrCreateTb();
        eventDbTable.OpenOrCreateTb();
        examDbTable.OpenOrCreateTb();
        note_reminder_dbTable.OpenOrCreateTb();
        noteDbTable.OpenOrCreateTb();
        noteScheduleDbTable.OpenOrCreateTb();
        reminderDbTable.OpenOrCreateTb();
        scheduleDbTable.OpenOrCreateTb();
        subjectDbTable.OpenOrCreateTb();
        tableDbTable.OpenOrCreateTb();
        weekDbTable.OpenOrCreateTb();
    }
    
    private void deleteAllData(){
        initDateBase();
        classDbTable.deleteAllRow();
        classWeekDbTable.deleteAllRow();
        diaryDbTable.deleteAllRow();
        eventDbTable.deleteAllRow();
        examDbTable.deleteAllRow();
        note_reminder_dbTable.deleteAllRow();
        noteDbTable.deleteAllRow();
        //noteScheduleDbTable.deleteAllRow();
        reminderDbTable.deleteAllRow();
        scheduleDbTable.deleteAllRow();
        subjectDbTable.deleteAllRow();
        tableDbTable.deleteAllRow();
        weekDbTable.deleteAllRow();
    }
    
    private void AddAllData(){
        deleteAllData();
        classDbTable.AddClassData();
        classWeekDbTable.AddClassWeekData();
        diaryDbTable.AddDiaryData();
        eventDbTable.AddEventData();
        examDbTable.AddExamData();
        note_reminder_dbTable.AddNoteReminderData();
        noteDbTable.AddNoteData();
        //noteScheduleDbTable.AddNoteScheduleData();
        reminderDbTable.AddReminderData();
        scheduleDbTable.AddScheduleData();
        subjectDbTable.AddSubjectData();
        tableDbTable.AddTableData();
        weekDbTable.AddWeekData();
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    void Test(Context context){

        int reminderID=1;
        String name="全真模擬卷";
        String Subject="國文";
        if(reminderID==0)return;
        Log.v("currentTimeMillis", System.currentTimeMillis()+"");

        Intent intent1=new Intent(context,InputScoreActivity.class);
        intent1.putExtra("EXAMID",1);
        PendingIntent contentIntent=PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder mBuilder=(Notification.Builder)new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Subject+"考試成績")
                .setContentText("請點擊記錄"+name+"考試成績")
                .setAutoCancel(true)
                .setSubText("考試成績登記提醒")
                .setOngoing(false)
                .setContentIntent(contentIntent)
                .setWhen(System.currentTimeMillis());
        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(reminderID,mBuilder.build());
    }

    void setTheme(){
        /*SharedPreferences.Editor pref = getSharedPreferences("RECORDINGYOURLIFE", 0).edit();
        pref.putInt("THEME_INDEX" ,1);
        pref.apply();*/

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
                theme=R.style.AppTheme_white;
                break;
            case 0:
            default:
                theme=R.style.AppTheme;
                break;
        }
        setTheme(theme);
    }

}
