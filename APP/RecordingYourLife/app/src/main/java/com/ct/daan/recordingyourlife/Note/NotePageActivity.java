package com.ct.daan.recordingyourlife.Note;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.ct.daan.recordingyourlife.DbTable.All_Table;
import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.Table.TableActivity;

/**
 * Created by info on 2017/10/25.
 */


public class NotePageActivity extends AppCompatActivity {
    EditText et,et2;
    Intent intent;
    private final static int RESULT_DELETE=200;
    int id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_notepage);

        et=(EditText) findViewById(R.id.et);
        et2=(EditText) findViewById(R.id.et2);

        intent=getIntent();
        Bundle extra=intent.getExtras();
        id=extra.getInt("SELECTED_ID");
        et.setText(extra.getString("SELECTED_TITLE"));
        et2.setText(extra.getString("SELECTED_CONTENT"));
    }

    void Complete() {
        intent.putExtra("SELECTED_ID",id);
        intent.putExtra("CHANGED_TITLE",et.getText().toString());
        intent.putExtra("CHANGED_CONTENT",et2.getText().toString());
        Log.v("回傳資料", String.format("回傳資料：%s=%d,%s=%s,%s=%s","SELECTED_ID",id,"CHANGED_TITLE",et.getText(),"CHANGED_CONTENT",et2.getText()));
        setResult(RESULT_OK,intent);
        finish();
    }


    void Delete() {
        new AlertDialog.Builder(NotePageActivity.this)
                .setMessage(R.string.delete_content)
                .setTitle(R.string.delete_tilte)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent.putExtra("SELECTED_ID",id);
                        Log.v("回傳資料", String.format("回傳資料：%s=%d","SELECTED_ID",id));
                        setResult(RESULT_DELETE,intent);
                        finish();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    void Reminder() {
        Intent intent  = new Intent(NotePageActivity.this,RemindersActivity.class);
        intent.putExtra("NOTEID",id);
        startActivity(intent);
    }


    //增加動作按鈕到工具列
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.remind_complete_delete_actions, menu);
        return true;
    }

    //動作按鈕回應
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reminder:
                Reminder();
                return true;
            case R.id.action_delete:
                Delete();
                return true;
            case R.id.action_done:
                Complete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
