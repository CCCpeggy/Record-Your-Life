package com.example.info.baseadapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    //ListView 要顯示的內容
    public String[][] data = {
            {"1","臺北市\nTaipei City\nSky Dragon Country"},
            {"2","信義區\nTaipei City\nSky Dragon Country"},
            {"1","新北市\nTaipei City\nSky Dragon Country"},
            {"2","板橋區\nTaipei City\nSky Dragon Country"},
            {"1","桃園市\nTaipei City\nSky Dragon Country"},
            {"2","桃園區\nTaipei City\nSky Dragon Country"},
            {"1","臺中市\nTaipei City\nSky Dragon Country"},
            {"2","西屯區\nTaipei City\nSky Dragon Country"},
            {"1","臺南市\nTaipei City\nSky Dragon Country"},
            {"2","安平區\nTaipei City\nSky Dragon Country"},
            {"2","新營區\nTaipei City\nSky Dragon Country"},
            {"1","高雄市\nTaipei City\nSky Dragon Country"},
            {"2","苓雅區\nTaipei City\nSky Dragon Country"},
            {"2","鳳山區\nTaipei City\nSky Dragon Country"}
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listview = (ListView) findViewById(R.id.listview);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewAdapter adapter = new ViewAdapter(data,inflater);
        listview.setAdapter(adapter);

    }
}

