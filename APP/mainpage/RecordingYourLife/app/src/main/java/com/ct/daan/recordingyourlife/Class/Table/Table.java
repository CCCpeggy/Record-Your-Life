package com.ct.daan.recordingyourlife.Class.Table;


public class Table{
    public String Name="123";
    public Class[] classes;
    public int color=0;
    public boolean isOpen=false;
    public Table(int Class_count, String name){
        classes=new Class[Class_count];
        Name=name;
    }
    public Table(int Class_count, String name,int color){
        classes=new Class[Class_count];
        Name=name;
        this.color=color;
    }

}
