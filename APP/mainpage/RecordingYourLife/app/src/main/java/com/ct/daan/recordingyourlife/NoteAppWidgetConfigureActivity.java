package com.ct.daan.recordingyourlife;

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
import android.widget.SimpleCursorAdapter;

import com.ct.daan.recordingyourlife.DbTable.NoteDbTable;

/**
 * The configuration screen for the {@link NoteAppWidget NoteAppWidget} AppWidget.
 */
public class NoteAppWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.ct.daan.recordingyourlife.NoteAppWidget";
    private static final String PREF_PREFIX_ID = "appwidget_id_";
    private static final String PREF_PREFIX_KEY1 = "appwidget1_";
    private static final String PREF_PREFIX_KEY2 = "appwidget2_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    ListView mAppWidgetView;


    private SQLiteDatabase db = null;
    private String SQLiteDB_Path = "student_project.db";
    NoteDbTable NoteDb;


    public NoteAppWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, int Note_id, String content,String title) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_ID + appWidgetId, Note_id);
        prefs.putString(PREF_PREFIX_KEY1 + appWidgetId, content);
        prefs.putString(PREF_PREFIX_KEY2 + appWidgetId, title);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY1 + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.note_appwidget_text);
        }
    }

    static String loadContentPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY2 + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.note_appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_ID + appWidgetId);
        prefs.remove(PREF_PREFIX_KEY1 + appWidgetId);
        prefs.remove(PREF_PREFIX_KEY2 + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.note_app_widget_configure);
        mAppWidgetView = (ListView) findViewById(R.id.listv);

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
    public void UpdateAdapter_Note(){
        try{
            cursor=NoteDb.getCursor();
            if(cursor !=  null && cursor.getCount()>0){
                //ListView格式自訂
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.note_listview_item_layout, cursor, new String[]{"便條標題", "便條內容"}, new int[]{R.id.text1,R.id.text2}, 0);
                //ListView格式預設
                //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[]{"便條標題", "便條內容"}, new int[]{android. R.id.text1,android.R.id.text2}, 0);
                mAppWidgetView.setAdapter(adapter);
                mAppWidgetView.setOnItemClickListener(mOnClickListener);
                Log.v("UpdateAdapter_Note", String.format("UpdateAdapter_Note() 更新成功"));
            }
        }catch (Exception e){
            Log.e("#004","清單更新失敗");
        }

    }

    ListView.OnItemClickListener mOnClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Context context = NoteAppWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            cursor.moveToPosition(position);
            //String widgetText =cursor.getString(1)+"\r\n"+cursor.getString(2);
            saveTitlePref(context, mAppWidgetId, cursor.getInt(0),cursor.getString(1),cursor.getString(2));

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            NoteAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }

    };
}

