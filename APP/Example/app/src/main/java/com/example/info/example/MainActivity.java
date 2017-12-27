package com.example.info.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] array = new String[] {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
        Log.v("123","1");
        List<String> list = new ArrayList<String>(Arrays.asList(array));
        Log.v("123","1");
        GridView grid = (GridView) findViewById(R.id.gridview);
        Log.v("123","1");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.list_item,R.id.text1, list);
        Log.v("123","1");
        grid.setAdapter(adapter);
        Log.v("123","1");
    }
}
