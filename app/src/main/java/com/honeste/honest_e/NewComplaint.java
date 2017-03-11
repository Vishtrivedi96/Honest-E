//with async

package com.honeste.honest_e;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class NewComplaint extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //TextView textView;
    Spinner SpinCat,s1;
    AreaArrayList a1;
    String id;
    String category,area;
    ArrayList<String> catgory,areaAL;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_complaint);

        SharedPreferences s=getSharedPreferences("Honest-E",MODE_PRIVATE);
        id = s.getString("lid","-1");
        CategoryList ctglist = new CategoryList();
        catgory = new ArrayList<String>();
        //catgory.add("jhgliuy");
        //catgory.add("uiyuiy");
        catgory = ctglist.getCatgory();
        SpinCat = (Spinner)findViewById(R.id.cat_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,catgory);
        SpinCat.setAdapter(arrayAdapter);
        SpinCat.setOnItemSelectedListener(this);

        s1 = (Spinner)findViewById(R.id.comp_area);
        a1 = new AreaArrayList();
        areaAL = a1.getAreaList();
        ArrayAdapter<String> arrayAdapterArea = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,areaAL);
        s1.setAdapter(arrayAdapterArea);
        s1.setOnItemSelectedListener(this);


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }



    public  void OnPost(View v)
    {
        EditText subcat = (EditText)findViewById(R.id.etsubcategory);
        EditText complaintdes = (EditText)findViewById(R.id.etcomplaintdescription);
        EditText location = (EditText)findViewById(R.id.etcomplaintlocation);

        String SubCat = subcat.getText().toString();
        String ComplaintDes = complaintdes.getText().toString();
        String Location = location.getText().toString();
        String stats = "0" ;
        //String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        StrictMode.ThreadPolicy s=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(s);
        try {
            CommonURL c=new CommonURL();
            String ip=c.getIP("Submit_Complaint.php");
            URL objUrl = new URL(ip);
            HttpURLConnection objUrlConnection = (HttpURLConnection)objUrl.openConnection();
            objUrlConnection.setRequestMethod("POST");
            objUrlConnection.setRequestProperty("Content-type","Application/json");
            objUrlConnection.setRequestProperty("Accept","Application/json");

            objUrlConnection.setDoOutput(true);
            objUrlConnection.setDoInput(true);

            objUrlConnection.connect();

            JSONObject objJson = new JSONObject();
            objJson.put("content",ComplaintDes);
            objJson.put("add",Location);
            objJson.put("lid",id);
            objJson.put("category",category);
            objJson.put("stats",stats);
            TimeAndDate t1 = new TimeAndDate();
            objJson.put("time",t1.getDateTime());
            objJson.put("Area",area);

            DataOutputStream objDOS = new DataOutputStream(objUrlConnection.getOutputStream());
            objDOS.write(objJson.toString().getBytes());
            int respcode = objUrlConnection.getResponseCode();
            if (respcode==200)
            {
                BufferedReader bfrdr = new BufferedReader(new InputStreamReader(objUrlConnection.getInputStream()));
                String line = "";
                StringBuilder response = new StringBuilder();
                while ((line = bfrdr.readLine()) != null)
                {
                    response.append(line);
                }
                JSONObject jsonobject2 = new JSONObject(response.toString());
                String lid1 = jsonobject2.getString("id");
                String msg = jsonobject2.getString("Message");
                //Toast.makeText(this,lid + " " + msg, Toast.LENGTH_SHORT).show();
                if(lid1.equals("1"))
                {
                    Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this,HomeUI.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(this,"failed", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, "insertion failed", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
           Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        TextView spinnerText = (TextView) SpinCat.getChildAt(0);
        TextView spinnerText1 = (TextView) s1.getChildAt(0);

        switch(adapterView.getId()){
            case R.id.cat_spinner :
                spinnerText.setTextColor(Color.WHITE);
                String  s = adapterView.getItemAtPosition(i).toString();
                category = s;
                break;
            case R.id.comp_area :
                spinnerText1.setTextColor(Color.WHITE);
                String abx  = adapterView.getItemAtPosition(i).toString();
                area = abx;
                break;
        }    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

