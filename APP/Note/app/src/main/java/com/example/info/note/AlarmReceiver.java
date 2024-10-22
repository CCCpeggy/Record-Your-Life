package com.example.info.note;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by info on 2017/12/2.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extra=intent.getExtras();
        int reminderID=extra.getInt("REMINDERID",1000);
        String title=extra.getString("TITLE","");
        String content=extra.getString("CONTENT","");
        Log.v("currentTimeMillis",System.currentTimeMillis()+"");
        NotificationCompat.Builder mBuilder=(NotificationCompat.Builder)new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSubText("便條提醒")
                .setOngoing(true)
                .setWhen(System.currentTimeMillis());
        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(reminderID,mBuilder.build());
    }
    private long getTimeMillis(String date,String time,Context context){
        Calendar cal=StringtoCalendar(date, time,context);
        Calendar cal2=Calendar.getInstance();
        return (cal.getTime().getTime()-cal2.getTime().getTime());
    }
    private Calendar StringtoCalendar(String date,String time,Context context){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddhh:mm", Locale.TAIWAN);
        Calendar Calendar= android.icu.util.Calendar.getInstance();
        try{
            Calendar.setTime(sdf.parse(date+time));
        }catch (Exception e){
            Toast.makeText(context,"yyyy-MM-ddhh:mm",Toast.LENGTH_SHORT).show();
        }
        return Calendar;
    }
}
