package com.ct.daan.recordingyourlife.Reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ct.daan.recordingyourlife.Class.CalendarFunction;
import com.ct.daan.recordingyourlife.Exam.InputScoreActivity;
import com.ct.daan.recordingyourlife.MainActivity;
import com.ct.daan.recordingyourlife.R;

/**
 * Created by info on 2017/12/2.
 */

public class AlarmReceiverByInput extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extra=intent.getExtras();
        int ReminderID=extra.getInt("REMINDERID",0);
        int ExamID=extra.getInt("EXAMID",0);
        String name=extra.getString("NAME","");
        String Subject=extra.getString("SUBJECT","");
        Log.v("setAlarm",String.format("context:%s,reminder_id:%d,Exam_id:%d,title:%s,content:%s"
                ,context,ReminderID,ExamID,name,Subject));
        intent=null;

        if(ExamID==0)return;
        Log.v("currentTimeMillis", System.currentTimeMillis()+"");

        Intent intent1=new Intent(context,InputScoreActivity.class);
        intent1.putExtra("EXAMID",ExamID);
        intent1.putExtra("NAME",name);
        intent1.putExtra("SUBJECT",Subject);
        intent1.putExtra("REMINDERID",ReminderID);
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
        notificationManager.notify(ReminderID,mBuilder.build());
    }

    private Calendar StringtoCalendar(String date, String time, Context context){
        CalendarFunction calFunction=new CalendarFunction();
        return calFunction.DateTimeTextToCalendarType(date, time, context);
    }
}
