package com.ct.daan.recordingyourlife.Reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;

import com.ct.daan.recordingyourlife.Class.CalendarFunction;
import com.ct.daan.recordingyourlife.Exam.InputScoreActivity;
import com.ct.daan.recordingyourlife.MainActivity;
import com.ct.daan.recordingyourlife.R;

/**
 * Created by info on 2017/12/2.
 */

public class AlarmReceiverByInputs extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extra=intent.getExtras();
        int ReminderID=extra.getInt("TIME",0);
        int[] ExamIDs=extra.getIntArray("EXAMIDS");
        String[] names=extra.getStringArray("NAME");
        String[] Subjects=extra.getStringArray("SUBJECT");
        Log.v("setAlarm",String.format("context:%s,reminder_id:%d"
                ,context,ReminderID));

        if(ExamIDs.length<=0)return;

        Intent intent1=new Intent(context,MainActivity.class);
        PendingIntent contentIntent=PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        int ExamID;
        String Name,Subject;
        String Name_str=names[0];
        String Subject_str=Subjects[0];
        for(int i=1;i<ExamIDs.length&&i<names.length&&i<Subjects.length;i++){
            ExamID=ExamIDs[i];
            Name=names[i];
            Subject=Subjects[i];
            Name_str+=","+Name;
            Subject_str+=","+Subjects;
        }
        Log.v("setAlarm",String.format("Exam_id:%d,title:%s,content:%s",ExamIDs[0],names[0],Subjects[0]));

        /*Intent intent1=new Intent(context,InputScoreActivity.class);
        intent1.putExtra("EXAMID",ExamID);
        intent1.putExtra("NAME",name);
        intent1.putExtra("SUBJECT",Subject);
        intent1.putExtra("REMINDERID",ReminderID);*/

        Notification.Builder mBuilder=(Notification.Builder)new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Subject_str+"考試成績")
                .setContentText("請點擊記錄"+Name_str+"考試成績")
                .setAutoCancel(true)
                .setSubText("考試成績登記提醒")
                .setOngoing(false)
                .setContentIntent(contentIntent)
                .setWhen(System.currentTimeMillis());
        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ReminderID,mBuilder.build());
    }

    private Calendar StringtoCalendar(String date, String time, Context context){
        CalendarFunction calFunction=new CalendarFunction();
        return calFunction.DateTimeTextToCalendarType(date, time, context);
    }
}
