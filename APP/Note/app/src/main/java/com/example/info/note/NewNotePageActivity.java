package com.example.info.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by info on 2017/10/25.
 */


public class NewNotePageActivity extends AppCompatActivity {
    EditText et,et2;
    Button btn_Complete,btn_Delete,btn_Reminder;
    Intent intent;
    ArrayList<Integer> reminder_ids;
    final static private int REMINDERSPAGE=4651;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepage);

        reminder_ids=new ArrayList<>();

        et=(EditText) findViewById(R.id.et);
        et2=(EditText) findViewById(R.id.et2);
        btn_Complete=(Button)findViewById(R.id.btn_Complete) ;
        btn_Delete=(Button)findViewById(R.id.btn_Delete);
        btn_Reminder=(Button)findViewById(R.id.btn_reminder);
        btn_Delete.setEnabled(false);
        btn_Complete.setOnClickListener(btn_Complete_Listener);
        btn_Reminder.setOnClickListener(btn_Reminder_Listener);

        intent=getIntent();
        intent.putExtra("REMINDERIDS",reminder_ids);
        setResult(RESULT_CANCELED,intent);

        et.setText("");
        et2.setText("");
    }

    private Button.OnClickListener btn_Complete_Listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent.putExtra("CHANGED_TITLE",et.getText().toString());
            intent.putExtra("CHANGED_CONTENT",et2.getText().toString());
            intent.putExtra("REMINDERIDS",reminder_ids);
            Log.v("回傳資料",String.format("回傳資料：%s=%s,%s=%s","CHANGED_TITLE",et.getText(),"CHANGED_CONTENT",et2.getText()));
            setResult(RESULT_OK,intent);
            finish();
        }
    };

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


    private Button.OnClickListener btn_Reminder_Listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent  = new Intent(NewNotePageActivity.this,RemindersActivity2.class);
            intent.putExtra("REMINDERIDS",reminder_ids);
            startActivityForResult(intent,REMINDERSPAGE);
        }
    };
}
