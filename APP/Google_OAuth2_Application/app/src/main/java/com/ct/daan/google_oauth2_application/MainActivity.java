package com.ct.daan.google_oauth2_application;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Context mContext;
    String TAG="Google";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext=MainActivity.this;

        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();

        ArrayList SCOPES = new ArrayList();
        SCOPES.add("https://spreadsheets.google.com/feeds");

        //SerServiceAcountId: clientID value should be similar to @developer.gserviceaccount.com They basically expect the email_address value from the console api credentials
        GoogleCredential credential = null;
        try {
            credential = new GoogleCredential.Builder().setTransport(httpTransport).setJsonFactory(jsonFactory)
                    .setServiceAccountId("test-842@iconic-hue-190505.iam.gserviceaccount.com")
                    .setServiceAccountPrivateKeyFromP12File(getTempPkc12File())
                    .setServiceAccountScopes(SCOPES)
                    .build();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            credential.refreshToken();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String accessToken = credential.getAccessToken();
        Log.i(TAG, "accessToken: "+accessToken);
    }

    private File getTempPkc12File() throws IOException {
        InputStream pkc12Stream = mContext.getAssets().open("DataBase-224d9fba7c01.p12");//記得將檔名改成剛才存進Assets資料夾裡的P12檔檔名
        File tempPkc12File = File.createTempFile("P12File", "p12");
        OutputStream tempFileStream = new FileOutputStream(tempPkc12File);

        int read = 0;
        byte[] bytes = new byte[1024];
        while ((read = pkc12Stream.read(bytes)) != -1) {
            tempFileStream.write(bytes, 0, read);
        }
        return tempPkc12File;

    }

    /*private void example(){
        List<SpreadsheetEntry> spreadsheets = null;

        SpreadsheetService mSpreadSheetService = new SpreadsheetService("欲存取的資料表名稱");
        mSpreadSheetService.setProtocolVersion(SpreadsheetService.Versions.V3);
        try

        {
            //2015/05月已停止使用
//                mSpreadSheetService.setUserCredentials("xxx@gmail.com", "密碼");

            HttpTransport httpTransport = new NetHttpTransport();
            JacksonFactory jsonFactory = new JacksonFactory();

            ArrayList SCOPES = new ArrayList();
            SCOPES.add("https://spreadsheets.google.com/feeds");

            //SerServiceAcountId: clientID value should be similar to @developer.gserviceaccount.com They basically expect the email_address value from the console api credentials
            GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport).setJsonFactory(jsonFactory)
                    .setServiceAccountId("test-842@iconic-hue-190505.iam.gserviceaccount.com")
                    .setServiceAccountPrivateKeyFromP12File(getTempPkc12File())
                    .setServiceAccountUser("peggy19925@gmail.com")
                    .setServiceAccountScopes(SCOPES)
                    .build();


            String accessToken = credential.getAccessToken();
            Log.i(TAG, "accessToken: " + accessToken);
            mSpreadSheetService.setOAuthCredentials(credential);

            URL url = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");

            SpreadsheetFeed spreadFeed = mSpreadSheetService.getFeed(url, SpreadsheetFeed.class);
            spreadsheets = spreadFeed.getEntries();
        }catch(Exception e){

        }
    }*/


}
