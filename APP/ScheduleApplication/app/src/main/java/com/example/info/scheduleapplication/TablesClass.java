package com.example.info.scheduleapplication;

import android.graphics.Color;

/**
 * Created by info on 2017/11/30.
 */

public class TablesClass {
    public Table[] Tables;
    TablesClass(int TableClass_count){
        Tables=new Table[TableClass_count];
    }
}

class Table{
    public String Name="123";
    public Class classes[];
    public int colorR=200;
    public int colorG=200;
    public int colorB=200;
    public boolean isOpen=false;
    Table(int Class_count,String name){
        classes=new Class[Class_count];
        Name=name;
    }

}
class Class{
    public String start_time;
    public String Subject;
    Class(String time,String subject){
        start_time=time;
        Subject=subject;
    }
}