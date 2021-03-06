package com.honeste.honest_e;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String lid,email,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Generally Android Keyboard pops up when edittext is focused. This code prevents it.
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        SharedPreferences shared = getSharedPreferences("Honest-E", MODE_PRIVATE);
    if( shared != null)
    {
        String lid = shared.getString("lid","-1");
        if(!lid.equals("-1"))
        {
            Intent intent = new Intent(this,HomeUI.class);
            startActivity(intent);
        }

        }
    }
    public void OnLogin(View v)
    {

        // editext reference

        EditText etemail1 = (EditText)findViewById(R.id.main_email);
        EditText etpwd1 = (EditText)findViewById(R.id.main_pass);

        email = etemail1.getText().toString();
        pass = etpwd1.getText().toString();
        StrictMode.ThreadPolicy s=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(s);

        //Validation by means of incomplete items
        if (v.getId() == R.id.button4) {
            if (TextUtils.isEmpty(pass) || TextUtils.isEmpty(email)) {
                Toast.makeText(MainActivity.this, "None of the Field Should be empty", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //E-mail validation
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.matches(emailPattern))
        {
            //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }


        //connection
        try {

            CommonURL c=new CommonURL();
            String ip=c.getIP("User_Login.php");
            URL objUrl = new URL(ip);
            HttpURLConnection objUrlConnection = (HttpURLConnection)objUrl.openConnection();
            objUrlConnection.setRequestMethod("POST");
            objUrlConnection.setRequestProperty("Content-type","Application/json");
            objUrlConnection.setRequestProperty("Accept","Application/json");

            objUrlConnection.setDoOutput(true);
            objUrlConnection.setDoInput(true);

            objUrlConnection.connect();

            JSONObject objJson = new JSONObject();
            objJson.put("email",email);
            objJson.put("pwd",pass);

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
               lid = jsonobject2.getString("Email");
             //   String msg = jsonobject2.getString("Message");

            if(!(lid.equals("-1")))
                {
                    SharedPreferences shared = getSharedPreferences("Honest-E", MODE_PRIVATE);
                    SharedPreferences.Editor edit = shared.edit();
                    edit.putString("lid", lid);
                    edit.commit();
                    Toast.makeText(this,lid,Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(this,HomeUI.class);
                    startActivity(loginIntent);
              }
            else
                {
                    Toast.makeText(this,"Invalid Data", Toast.LENGTH_SHORT).show();
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
    public void OnSignUp(View view)
    {
        Intent signUpIntent = new Intent(this,RegistrationActivity.class);
        startActivity(signUpIntent);
    }
}
