package com.example.info.appwidgetapplication;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SimpleCursorAdapter;

/**
 * The configuration screen for the {@link MyWidgetActivity MyWidgetActivity} AppWidget.
 */
public class MyWidgetActivityConfigureActivity extends Activity {


    private static final String PREFS_NAME = "com.example.info.appwidgetapplication.MyWidgetActivity";
    private static final String PREF_PREFIX_KEY = "MYWIDGET_NOTE_ID";
    private static final String PREF_PREFIX_CONTENT = "MYWIDGET_CONTENT";

    private SQLiteDatabase db = null;
    private String SQLiteDB_Path = "student_project.db";
    NoteDbTable NoteDb;

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    ListView.OnItemClickListener mOnClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Context context = MyWidgetActivityConfigureActivity.this;

            // When the button is clicked, store the string locally
            cursor.moveToPosition(position);
            String widgetText =cursor.getString(1)+"\r\n"+cursor.getString(2);
            saveTitlePref(context, mAppWidgetId, cursor.getInt(0),widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            MyWidgetActivity.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }

    };


    public MyWidgetActivityConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, int Note_id,String Content) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, Note_id);
        prefs.putString(PREF_PREFIX_CONTENT + appWidgetId, Content);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String Content= prefs.getString(PREF_PREFIX_CONTENT + appWidgetId, null);
        if ( Content!=null) {
            return Content;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    /*static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String Content= prefs.getString(PREF_PREFIX_CONTENT + appWidgetId, null);

        if ( Content!=null) {
            return Content;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }*/

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.my_widget_activity_configure);
        //findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        initDatabase();
        UpdateAdapter_Note();
    }
    public void initDatabase(){
        OpOrCrDb();
        NoteDb = new NoteDbTable(SQLiteDB_Path, db);
        NoteDb.OpenOrCreateTb();
        //NoteDb.deleteAllRow();
        //NoteDb.AddNoteData();

    }

    //打開或新增資料庫
    private void OpOrCrDb() {
        try {
            db = openOrCreateDatabase(SQLiteDB_Path, MODE_PRIVATE, null);
            Log.v("資料庫", "資料庫載入成功");
        } catch (Exception ex) {
            Log.e("#001", "資料庫載入錯誤");
        }
    }
    Cursor cursor;
    public void UpdateAdapter_Note() {
        try {
            ListView listView01 = (ListView) findViewById(R.id.list_item);
            cursor = NoteDb.getCursor();
            if (cursor != null && cursor.getCount() > 0) {
                //ListView格式自訂
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[]{"便條標題", "便條內容"}, new int[]{android.R.id.text1, android.R.id.text2}, 0);
                //ListView格式預設
                //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[]{"便條標題", "便條內容"}, new int[]{android. R.id.text1,android.R.id.text2}, 0);
                listView01.setAdapter(adapter);
                listView01.setOnItemClickListener(mOnClickListener);
                Log.v("UpdateAdapter_Note", String.format("UpdateAdapter_Note() 更新成功"));
            }
        } catch (Exception e) {
            Log.e("#004", "清單更新失敗");
        }

    }
}

