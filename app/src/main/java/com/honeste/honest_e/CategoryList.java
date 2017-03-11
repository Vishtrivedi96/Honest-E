package com.honeste.honest_e;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by abhis on 10-Mar-17.
 */

public class CategoryList {
    String JSON_String;
    ArrayList<String> catgory;

    public ArrayList<String> getCatgory() {

        StrictMode.ThreadPolicy s=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(s);
        catgory = new ArrayList<>();
        try {
            CommonURL c = new CommonURL();
            String ip = c.getIP("Complaint_Category.php");
            URL url = new URL(ip);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();
            int code = httpURLConnection.getResponseCode();
            if (code == 200) {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream())
                );
                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_String = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_String);
                }

                JSONArray mJsonArray = new JSONArray(stringBuilder.toString());
                JSONObject cat = new JSONObject();
                for (int i = 0; i < mJsonArray.length(); i++) {
                    cat = mJsonArray.getJSONObject(i);
                    catgory.add(cat.getString("cat"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //  Toast.makeText(NewComplaint.this,e.toString(), Toast.LENGTH_SHORT).show();
        }
        return catgory;
    }
    }


