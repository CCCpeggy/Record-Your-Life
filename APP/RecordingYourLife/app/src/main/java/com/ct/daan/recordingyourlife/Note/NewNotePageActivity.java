package com.ct.daan.recordingyourlife.Note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ct.daan.recordingyourlife.R;

import java.util.ArrayList;

/**
 * Created by info on 2017/10/25.
 */


public class NewNotePageActivity extends AppCompatActivity {
    EditText et,et2;
    Intent intent;
    ArrayList<Integer> reminder_ids;
    final static private int REMINDERSPAGE=4651;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_notepage);

        reminder_ids=new ArrayList<>();

        et=(EditText) findViewById(R.id.et);
        et2=(EditText) findViewById(R.id.et2);

        intent=getIntent();
        intent.putExtra("REMINDERIDS",reminder_ids);
        setResult(RESULT_CANCELED,intent);

        et.setText("");
        et2.setText("");
    }

    void Complete() {
            intent.putExtra("CHANGED_TITLE",et.getText().toString());
            intent.putExtra("CHANGED_CONTENT",et2.getText().toString());
            intent.putExtra("REMINDERIDS",reminder_ids);
            Log.v("回傳資料", String.format("回傳資料：%s=%s,%s=%s","CHANGED_TITLE",et.getText(),"CHANGED_CONTENT",et2.getText()));
            setResult(RESULT_OK,intent);
            finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case REMINDERSPAGE:
                    Bundle extra = data.getExtras();
                    if (extra .isEmpty()) return;
                    reminder_ids =extra.getIntegerArrayList("REMINDERIDS");
                    for (int Reminder_id : reminder_ids) {
                        Log.v("Reminder_id", Reminder_id + "");
                    }
                    break;
            }
        }
    }


    void Reminder(){
        Intent intent  = new Intent(NewNotePageActivity.this,RemindersActivityByNew.class);
        intent.putExtra("REMINDERIDS",reminder_ids);
        startActivityForResult(intent,REMINDERSPAGE);
    }


    //增加動作按鈕到工具列
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.remind_delete_actions, menu);
        return true;
    }

    //動作按鈕回應
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reminder:
                Reminder();
                return true;
            case R.id.action_done:
                Complete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
