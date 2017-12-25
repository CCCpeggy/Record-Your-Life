package com.ct.daan.recordingyourlife.MainPage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ct.daan.recordingyourlife.Schedule.ScheduleActivity;

//首頁Tab更新

public class PagerAdapter extends FragmentStatePagerAdapter {
     int mNoOfTabs;
    public PagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                MainPageActivity tab1=new MainPageActivity();
                return tab1;
            case 1:
                ScheduleActivity tab2=new ScheduleActivity();
                return tab2;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
