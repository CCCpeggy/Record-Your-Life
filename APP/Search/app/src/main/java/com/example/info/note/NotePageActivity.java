package com.example.info.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by info on 2017/10/25.
 */


public class NotePageActivity extends AppCompatActivity {
    EditText et,et2;
    Button btn_Complete,btn_Delete;
    Intent intent;
    private final static int RESULT_DELETE=200;
    int id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepage);

        et=(EditText) findViewById(R.id.et);
        et2=(EditText) findViewById(R.id.et2);
        btn_Complete=(Button)findViewById(R.id.btn_Complete) ;
        btn_Delete=(Button)findViewById(R.id.btn_Delete);

        btn_Complete.setOnClickListener(btn_Complete_Listener);
        btn_Delete.setOnClickListener(btn_Delete_Listener);

        intent=getIntent();
        Bundle extra=intent.getExtras();
        id=extra.getInt("SELECTED_ID");
        et.setText(extra.getString("SELECTED_TITLE"));
        et2.setText(extra.getString("SELECTED_CONTENT"));
    }

    private Button.OnClickListener btn_Complete_Listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent.putExtra("SELECTED_ID",id);
            intent.putExtra("CHANGED_TITLE",et.getText().toString());
            intent.putExtra("CHANGED_CONTENT",et2.getText().toString());
            Log.v("回傳資料",String.format("回傳資料：%s=%d,%s=%s,%s=%s","SELECTED_ID",id,"CHANGED_TITLE",et.getText(),"CHANGED_CONTENT",et2.getText()));
            setResult(RESULT_OK,intent);
            finish();
        }
    };
    private Button.OnClickListener btn_Delete_Listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent.putExtra("SELECTED_ID",id);
            Log.v("回傳資料",String.format("回傳資料：%s=%d","SELECTED_ID",id));
            setResult(RESULT_DELETE,intent);
            finish();
        }
    };
}
