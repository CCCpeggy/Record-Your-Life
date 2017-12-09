package com.ct.daan.recordingyourlife.Class;

public class Table{
    public String Name="123";
    public Class classes[];
    public int colorR=200;
    public int colorG=200;
    public int colorB=200;
    public boolean isOpen=false;
    public Table(int Class_count, String name){
        classes=new Class[Class_count];
        Name=name;
    }

}
