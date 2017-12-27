package com.ct.daan.recordingyourlife.Reminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.ct.daan.recordingyourlife.Class.CalendarFunction;
import com.ct.daan.recordingyourlife.Exam.InputScoreActivity;
import com.ct.daan.recordingyourlife.Note.NotePageActivity;
import com.ct.daan.recordingyourlife.R;

import java.util.Locale;

/**
 * Created by info on 2017/12/2.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extra=intent.getExtras();
        int reminderID=extra.getInt("REMINDERID",1000);
        int nodeID=extra.getInt("NOTEID",1);
        String title=extra.getString("TITLE","");
        String content=extra.getString("CONTENT","");
        Log.v("currentTimeMillis", System.currentTimeMillis()+"");

        Intent intent1=new Intent(context,NotePageActivity.class);
        intent1.putExtra("SELECTED_ID", nodeID);
        PendingIntent contentIntent=PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder=(NotificationCompat.Builder)new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setSubText("便條提醒")
                .setOngoing(false)
                .setWhen(System.currentTimeMillis());
        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(reminderID,mBuilder.build());
    }

    private Calendar StringtoCalendar(String date, String time, Context context){
        CalendarFunction calFunction=new CalendarFunction();
        return calFunction.DateTimeTextToCalendarType(date, time, context);
    }
}
