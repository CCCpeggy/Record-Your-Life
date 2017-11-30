package com.example.info.scheduleapplication;

import java.util.jar.Attributes;

/**
 * Created by info on 2017/11/30.
 */

public class ScheduleClass {
    item[] items;
    ScheduleClass(int item_count){
        items=new item[item_count];
    }
}
class item{
    String Name;
    String Time;
    item(String name,String time){
        Name=name;
        Time=time;
    }
}
