package com.ct.daan.recordingyourlife.MainPage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ct.daan.recordingyourlife.Diary.DiaryActivity;
import com.ct.daan.recordingyourlife.Event.EventActivity;
import com.ct.daan.recordingyourlife.Exam.ExamActivity;
import com.ct.daan.recordingyourlife.Note.NoteActivity;
import com.ct.daan.recordingyourlife.R;

/**
 * Created by info on 2017/12/8.
 */

public class MainPageActivity extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private MainPageActivity.OnFragmentInteractionListener mListener;

    public MainPageActivity() {
    }

    public static MainPageActivity newInstance(String param1, String param2) {
        MainPageActivity fragment = new MainPageActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.content_main, container, false);
        Button Calendar_btn=v.findViewById(R.id.calendar_btn);
        Button Note_btn=v.findViewById(R.id.note_btn);
        Button Diary_btn=v.findViewById(R.id.diary_btn);
        Button Test_btn=v.findViewById(R.id.test_btn);
        Button Table_btn=v.findViewById(R.id.table_btn);
        
        Calendar_btn.setOnClickListener(btn_listener);
        Note_btn.setOnClickListener(btn_listener);
        Diary_btn.setOnClickListener(btn_listener);
        Test_btn.setOnClickListener(btn_listener);
        Table_btn.setOnClickListener(btn_listener);
        return v;
    }
    Button.OnClickListener btn_listener= new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()){
                case R.id.calendar_btn:
                    intent=new Intent(getContext(),EventActivity.class);
                    startActivity(intent);
                    break;
                case R.id.note_btn:
                    intent=new Intent(getContext(),NoteActivity.class);
                    startActivity(intent);
                    break;
                case R.id.diary_btn:
                    intent=new Intent(getContext(),DiaryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.test_btn:
                    intent=new Intent(getContext(),ExamActivity.class);
                    startActivity(intent);
                    break;
                case R.id.table_btn:
                    /*intent=new Intent(getContext(),DiaryActivity.class);
                    startActivity(intent);*/
                    break;
            }
        }
    } ;

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainPageActivity.OnFragmentInteractionListener) {
            mListener = (MainPageActivity.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()+ " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
