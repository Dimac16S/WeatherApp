package com.example.dimac.whatstheweather;

import android.content.Context;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button buttonView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    String editTextContent;


    public void buttonPressed(View view) {
        editTextContent = editText.getText().toString();
        if(editTextContent.equals("")){
            Toast.makeText(getApplicationContext(), "You did not enter a city", Toast.LENGTH_SHORT).show();
        }else {
            jsonBodyRequest(editTextContent);
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public void jsonBodyRequest(String cityName){
        String url = "https://api.weatherapi.com/v1/current.json?key=44b9ad6775384b95aac73621221307&q=" + cityName + "&aqi=no";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
//                    JSONObject jsonObject = response.getJSONObject("main");
//                    JSONArray jsonArray = response.getJSONArray("weather");
//                    JSONObject object = jsonArray.getJSONObject(0);
//                    String temp = String.valueOf(jsonObject.getDouble("temp"));
//                    String description = object.getString("description");
//                    String city = response.getString("name");
                    JSONObject jsonObjectOne = response.getJSONObject("location");
                    JSONObject jsonObjectTwo = response.getJSONObject("current");
                    JSONObject jsonObjectThree = jsonObjectTwo.getJSONObject("condition");
                    String temp = String.valueOf(jsonObjectTwo.getDouble("temp_c"));
                    String description = jsonObjectThree.getString("text");
                    String city = jsonObjectOne.getString("name");

                    textView2.setText(city);
                    textView3.setText(temp);
                    textView4.setText(description);

                    textView2.setVisibility(View.VISIBLE);
                    textView3.setVisibility(View.VISIBLE);
                    textView4.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(jsonObjectRequest);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        buttonView = findViewById(R.id.buttonView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);



        //enter is pressed
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                editTextContent = editText.getText().toString();
                if (editTextContent.equals("")) {
                    Toast.makeText(getApplicationContext(), "You did not enter a city", Toast.LENGTH_SHORT).show();
                } else {
                    jsonBodyRequest(editTextContent);
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
                return true;
            }
        });
    }
}