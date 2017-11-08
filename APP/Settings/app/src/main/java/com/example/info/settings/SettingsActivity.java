package com.example.info.settings;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.icu.util.Calendar;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static android.R.attr.key;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    public static Context context;

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                //Log.e("值變動" ,((ListPreference) preference).toString() +value.toString());
                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);


            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        context=this;
/*
        SharedPreferences spref=this.getPreferences(MODE_PRIVATE);
        Intent intent=getIntent();
        Bundle extra=intent.getExtras();
        try{
            if(!extra.isEmpty() && extra.getBoolean("REMINDER_CLICK")){
               /String Reminder_Date=extra.getString("REMINDER_DATE");
                Boolean IsRepeat_Switch=extra.getBoolean("REMINDER_ISREPEAT");
                String Repeat_Type_List=extra.getString("REMINDER_REPEATTYPE");
                //Log.e("傳入值",String.format("%s=%s,%s=%b,%s=%s","Reminder_Date",Reminder_Date,"IsRepeat_Switch",IsRepeat_Switch,"Repeat_Type_List",Repeat_Type_List));

                spref.edit()
                        .putString("REMINDER_DATE",Reminder_Date)
                        .putBoolean("REMINDER_ISREPEAT",IsRepeat_Switch)
                        .putString("REMINDER_REPEATTYPE",Repeat_Type_List)
                        .commit();
                Log.e("傳入值",String.format("%s=%s,%s=%b,%s=%s","REMINDER_DATE",spref.getString("REMINDER_DATE",""),"IsRepeat_Switch",spref.getBoolean("REMINDER_ISREPEAT",false),"Repeat_Type_List",spref.getString("REMINDER_REPEATTYPE","")));
                extra.clear();
                extra=null;
                intent=null;

            }
        }catch (Exception e){

        }
        Log.e("傳入值",String.format("%s=%s,%s=%b,%s=%s","REMINDER_DATE",spref.getString("REMINDER_DATE",""),"IsRepeat_Switch",spref.getBoolean("REMINDER_ISREPEAT",false),"Repeat_Type_List",spref.getString("REMINDER_REPEATTYPE","")));
*/
    }


    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || ReminderPreferenceFragment.class.getName().equals(fragmentName);
    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ReminderPreferenceFragment extends PreferenceFragment {

        com.example.info.settings.DatePreference Reminder_Date;
        SwitchPreference IsRepeat_Switch;
        ListPreference Repeat_Type_List;
        SharedPreferences spref;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_reminder);
            setHasOptionsMenu(true);

            Reminder_Date=(com.example.info.settings.DatePreference)findPreference("Reminder_Date");
            IsRepeat_Switch=(SwitchPreference)findPreference("IsRepeat_Switch");
            Repeat_Type_List=(ListPreference)findPreference("Repeat_Type_List");
            bindPreferenceSummaryToValue(Repeat_Type_List);

            spref = SettingsActivity.getSharedPreferences(context);
            boolean Click=spref.getBoolean("REMINDER_CLICK",false);
            boolean IsRepeat=spref.getBoolean("REMINDER_ISREPEAT",false);
            String RepeatType=spref.getString("REMINDER_REPEATTYPE","0");
            String Date=spref.getString("REMINDER_DATE",getTodayDate());
            Log.v("取得 SharedPreferences ",String.format("%b,%b,%s,%s",Click,IsRepeat,RepeatType,Date));
            Reminder_Date.setText(Date);
            IsRepeat_Switch.setDefaultValue(IsRepeat);
            Repeat_Type_List.setValue(RepeatType);

        }

        /*
        @Override
        public boolean onKeyDown(int KeyCode, KeyEvent event){
            if(KeyCode == KeyEvent.KEYCODE_BACK){
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                SharedPreferences.Editor editor = spref.edit();
                editor.putBoolean("REMINDER_CLICK",true)
                        .putBoolean("REMINDER_ISREPEAT",IsRepeat_Switch.isChecked())
                        .putString("REMINDER_REPEATTYPE", Repeat_Type_List.getValue())
                        .putString("REMINDER_DATE",Reminder_Date.getText())
                        .commit();

                Log.v("Enter傳回設定值",String.format("%s=%s,%s=%s,%s=%s","REMINDER_DATE",Reminder_Date.getText(),"REMINDER_ISREPEAT",IsRepeat_Switch.isChecked(),"REMINDER_REPEATTYPE", Repeat_Type_List.getValue()));

            }
            return true;
        }*/


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                SharedPreferences.Editor editor = spref.edit();
                editor.putBoolean("REMINDER_CLICK",true)
                        .putBoolean("REMINDER_ISREPEAT",IsRepeat_Switch.isChecked())
                        .putString("REMINDER_REPEATTYPE", Repeat_Type_List.getValue())
                        .putString("REMINDER_DATE",Reminder_Date.getText())
                        .commit();

                Log.v("傳回設定值",String.format("%s=%s,%s=%s,%s=%s","REMINDER_DATE",Reminder_Date.getText(),"REMINDER_ISREPEAT",IsRepeat_Switch.isChecked(),"REMINDER_REPEATTYPE", Repeat_Type_List.getValue()));

                startActivity(intent);
                //Log.e("IsRepeat_Switch","Data_Changed");

                return true;
            }
            return super.onOptionsItemSelected(item);
        }
        public  static  SharedPreferences getSharedPreferences(Context ctxt){
            return ctxt.getSharedPreferences("test",MODE_PRIVATE);
        }

        public String getTodayDate(){
            Calendar cal=Calendar.getInstance();
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
            return sdf.format(cal.getTime());
        }

    }

    public  static  SharedPreferences getSharedPreferences(Context ctxt){
        return ctxt.getSharedPreferences("test",MODE_PRIVATE);
    }


}

