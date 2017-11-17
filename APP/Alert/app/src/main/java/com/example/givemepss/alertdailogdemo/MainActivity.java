package com.example.givemepss.alertdailogdemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button customDialog;
    private View.OnClickListener buttonListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.custom_dialog:
                    customDialogEvent();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    public void initView(){
        customDialog = (Button) findViewById(R.id.custom_dialog);
        setButtonEvent();
    }

    public void setButtonEvent(){
        customDialog.setOnClickListener(buttonListener);
    }


    private void customDialogEvent() {
        final View item = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_layout, null);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.input_ur_name)
                .setView(item)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText editText = (EditText) item.findViewById(R.id.edit_text);
                    EditText editText1 = (EditText)item.findViewById(R.id.edit_text2);

                    String name = editText.getText().toString();
                    String teacher = editText1.getText().toString();

                    if(TextUtils.isEmpty(name)||TextUtils.isEmpty(teacher)){
                        Toast.makeText(getApplicationContext(), R.string.input_ur_name, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.sub) + name + "、" + getString(R.string.hi) + teacher, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", null)
            .show();
    }
}
