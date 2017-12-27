package com.ct.daan.google_calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;


import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;



public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    private String tagReport = "CalendarProviderActivity";
    private ArrayList<String> calendarEvents = new ArrayList<String>();
    private Hashtable<String,String> accountInfo = new Hashtable<String,String>();
    private String URI_CALENDAR_BASE = "content://com.android.calendar/calendars";
    private String URI_CALENDAR_RANGE_QUERY = "content://com.android.calendar/instances/when"; // content://com.android.calendar/instances/when/begin/end
    private SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getInfoFromCalendarDB()>0) {
            for(String var:calendarEvents){
                Log.v("array",var);
            }
        }
    }
    private int getInfoFromCalendarDB() {
        long dateRangeBegin = new Date().getTime();
        Date today = new Date(dateRangeBegin + DateUtils.DAY_IN_MILLIS);
        long dateRangeEnd=new Date(today.getYear(),today.getMonth(),today.getDate()).getTime();

        Log.e(tagReport,"date range from '"+dateRangeBegin +"' to '"+dateRangeEnd+"'");

        ContentResolver contentResolver = this.getContentResolver();
        Cursor accountInfoCursor = contentResolver.query(Uri.parse(URI_CALENDAR_BASE), (new String[] { "_id", "displayName", "selected", "_sync_account" }), null, null, null);
        while (accountInfoCursor.moveToNext()) {
            String _id = accountInfoCursor.getString(0);
            String displayName = accountInfoCursor.getString(1);
            String syncAccount = accountInfoCursor.getString(3);
            if( !accountInfoCursor.getString(2).equals("0") ) { // selected => enable
                accountInfo.put( syncAccount, displayName ); // use for check or display the account name
                accountInfo.put( _id, displayName );
            }
        }

        // show all columns
        String targetQuery = URI_CALENDAR_RANGE_QUERY+"/"+dateRangeBegin+"/"+dateRangeEnd;
        Log.e(tagReport,"Range Query:"+targetQuery);
        Cursor eventCursor = contentResolver.query(Uri.parse(targetQuery), null, null, null, "startDay ASC, startMinute ASC");
        for( String c : eventCursor.getColumnNames() )
            Log.e(tagReport,"column name:"+c);

        // add all events
        calendarEvents.clear();
        eventCursor = contentResolver.query(Uri.parse(URI_CALENDAR_RANGE_QUERY+"/"+dateRangeBegin+"/"+dateRangeEnd), new String[] { "title", "begin", "end", "calendar_id"}, null,null,"startDay ASC, startMinute ASC");
        while (eventCursor.moveToNext()) {
            String title = eventCursor.getString(0);
            Long begin = eventCursor.getLong(1);
            Long end = eventCursor.getLong(2);
            String accountId = eventCursor.getString(3);
            if( accountInfo.containsKey(accountId) ) { // skip the account which is not enabled
                calendarEvents.add("["+TimeFormat.format(new Date(begin))+"-"+TimeFormat.format(new Date(end))+"] "+title+"("+accountInfo.get(accountId)+")" );
            }
        }
        return calendarEvents.size();
    }
}



