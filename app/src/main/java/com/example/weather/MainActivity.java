package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    EditText editText;
    TextView  resultTextView;

    public class downloadTask extends AsyncTask<String , Void , String> {


        @Override
        protected String doInBackground(String... urls) {

            URL url;
            String results = "";
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;
                    results += current;
                    data = reader.read();

                }

                return results;

            } catch (Exception e) {

                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), "could not find the city,plz check your spelling );", Toast.LENGTH_LONG).show();

                return  null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                String tempInfo = jsonObject.getString("main");
                Log.i("weather info " , weatherInfo);
                JSONArray array = new JSONArray(weatherInfo);
                String mesaaage = "";

                for(int i = 0; i< array.length(); i++) {

                    JSONObject jsonpart = array.getJSONObject(i);
                    String  main = jsonpart.getString("main");
                    String description = jsonpart.getString("description");

                    if(!main.equals("") && !description.equals("")) {

                        mesaaage += main+ " : " +description + "\r\n" + "\r\n" + tempInfo;

                    }

                }

                if(!mesaaage.equals("")) {

                    resultTextView.setText(mesaaage);

                }



            } catch(Exception e) {

                e.printStackTrace();
                Toast.makeText(getApplicationContext() , "could not find the city,plz check your spelling );" , Toast.LENGTH_LONG).show();

            }

        }
    }

    public  void getWeather(View view ) {


        downloadTask task = new downloadTask();

        task.execute("https://openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=439d4b804bc8187953eb36d2a8c26a02");


        //the below to lines of code hides the keyboard when the find weatehr button is pressed i.e. when we searches the weather of any city.

        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken() , 0);

    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        resultTextView = findViewById(R.id.resutTextView);


    }



}
