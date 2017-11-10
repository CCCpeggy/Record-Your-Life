package com.example.info.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by info on 2017/10/25.
 */


public class NewNotePageActivity extends AppCompatActivity {
    EditText et,et2;
    Button btn_Complete,btn_Delete;
    Intent intent;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepage);

        et=(EditText) findViewById(R.id.et);
        et2=(EditText) findViewById(R.id.et2);
        btn_Complete=(Button)findViewById(R.id.btn_Complete) ;
        btn_Delete=(Button)findViewById(R.id.btn_Delete);
        btn_Delete.setEnabled(false);
        btn_Complete.setOnClickListener(btn_Complete_Listener);

        intent=getIntent();

        et.setText("");
        et2.setText("");
    }

    private Button.OnClickListener btn_Complete_Listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent.putExtra("CHANGED_TITLE",et.getText().toString());
            intent.putExtra("CHANGED_CONTENT",et2.getText().toString());
            Log.v("回傳資料",String.format("回傳資料：%s=%s,%s=%s","CHANGED_TITLE",et.getText(),"CHANGED_CONTENT",et2.getText()));
            setResult(RESULT_OK,intent);
            finish();
        }
    };
}
