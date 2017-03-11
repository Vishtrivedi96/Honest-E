package com.honeste.honest_e;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HomeUI extends AppCompatActivity {
    String JSON_String;
    FloatingActionButton fab;
    ListView listView;

    ArrayList<commonComplain> Complains;
    complainAdapter complainAdapter;

    String lid="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_ui);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        try {
            listView = (ListView) findViewById(R.id.ViewComplain);

            Complains = new ArrayList<commonComplain>();
            complainAdapter = new complainAdapter(this, Complains);

            StrictMode.ThreadPolicy s = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(s);
            try {

                CommonURL c = new CommonURL();
                String ip = c.getIP("List_All_Complaints.php");
                URL url = new URL(ip);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                JSONObject objJson = new JSONObject();
                TimeAndDate t1 = new TimeAndDate();
                String tdo = t1.getDateTime();
                objJson.put("time", tdo);
                DataOutputStream objDOS = new DataOutputStream(httpURLConnection.getOutputStream());
                objDOS.write(objJson.toString().getBytes());

                int code = httpURLConnection.getResponseCode();

                if (code == 200) {

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream())
                    );
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((JSON_String = bufferedReader.readLine()) != null) {
                        stringBuilder.append(JSON_String);
                    }

                    JSONArray jsonArray = new JSONArray(stringBuilder.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject = jsonArray.getJSONObject(i);
                        commonComplain commonComplains = new commonComplain();
                        commonComplains.setArea(jsonObject.getString("Area"));
                        commonComplains.setCategory(jsonObject.getString("Cat"));
                        commonComplains.setName(jsonObject.getString("Name"));
                        commonComplains.setDesc(jsonObject.getString("Cont"));
                        commonComplains.setHours(jsonObject.getString("Time"));
                        commonComplains.setCompid(Integer.parseInt(jsonObject.getString("ID")));
                        Complains.add(commonComplains);
                    }
                }

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            listView.setAdapter(complainAdapter);
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void onFloatBtn(View view)
    {
        Intent FABIntent = new Intent(this,NewComplaint.class);
        startActivity(FABIntent);
    }
}
