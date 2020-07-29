package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView cnfrmtext;
    private TextView activetext;
    private TextView recovertext;
    private TextView deceasedtext;
    private TextView cnfrm1text;
    private TextView active1text;
    private TextView recover1text;
    private TextView deceased1text;
    int confirmed,active,deceased,recovered,confirmed1,active1,deceased1,recovered1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cnfrmtext = (TextView)findViewById(R.id.confirmtext);
        activetext = (TextView)findViewById(R.id.activetext);
        recovertext = (TextView)findViewById(R.id.recovertext);
        deceasedtext = (TextView)findViewById(R.id.deathtext);
        cnfrm1text = (TextView)findViewById(R.id.confirm1);
        active1text = (TextView)findViewById(R.id.active1);
        recover1text = (TextView)findViewById(R.id.recover1);
        deceased1text = (TextView)findViewById(R.id.deceased1);

//        Code for All India Status
        final RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, "https://api.covid19india.org/state_district_wise.json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Iterator a = response.keys();
                    while(a.hasNext()) {
                        String keyx = (String) a.next();
                        JSONObject jobj1 = response.getJSONObject(keyx);
                        Iterator x = jobj1.keys();
                        String key = (String) x.next();
                        JSONObject jobj2 = jobj1.optJSONObject(key);
                        Iterator y = jobj2.keys();
                        while (y.hasNext()) {
                            String key2 = (String) y.next();
                            JSONObject jobj3 = jobj2.getJSONObject(key2);
                            confirmed1 += + jobj3.getInt("confirmed");
                            active1 += jobj3.getInt("active");
                            deceased1 += jobj3.getInt("deceased");
                            recovered1 += jobj3.getInt("recovered");
                        }
                    }
                    cnfrm1text.setText(String.valueOf(confirmed1));
                    active1text.setText(String.valueOf(active1));
                    recover1text.setText(String.valueOf(recovered1));
                    deceased1text.setText(String.valueOf(deceased1));


                }


                catch (JSONException e)
                {
                    e.printStackTrace();
                    Log.d("MY","Somethiong went wrong");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    Toast.makeText(null,"Please check your connection!",Toast.LENGTH_LONG);

                } else if (error instanceof ServerError) {
                    Toast.makeText(null,"The server could not be found.",Toast.LENGTH_LONG);
                    Log.d("MY","The server could not be found. Please try again after some time!!");

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(null,"The server could not be found.",Toast.LENGTH_LONG);
                    Log.d("MY","Cannot connect to Internet...Please check your connection!");

                } else if (error instanceof ParseError) {
                    Toast.makeText(null,"Parsing error!",Toast.LENGTH_LONG);
                    Log.d("MY","Parsing error! Please try again after some time!!");

                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(null,"Cannot connect to Internet",Toast.LENGTH_LONG);
                    Log.d("MY","Cannot connect to Internet...Please check your connection!");

                } else if (error instanceof TimeoutError) {
                    Toast.makeText(null,"Connection TimeOut!",Toast.LENGTH_LONG);
                    Log.d("MY","Connection TimeOut! Please check your internet connection.");


                }
            }
        });
        requestQueue.add(jsonRequest);




        Spinner spinner = findViewById(R.id.statelist);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.statelist, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
        ((TextView) parent.getChildAt(0)).setTextSize(25);
        String text = parent.getItemAtPosition(position).toString();
            data(parent.getItemAtPosition(position).toString());
        Toast.makeText(parent.getContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void data(final String s) {
        confirmed = active = recovered = deceased = 0;
            final RequestQueue requestQueue;
            requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, "https://api.covid19india.org/state_district_wise.json", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jobj1 = response.getJSONObject(s);
                        Iterator x = jobj1.keys();
                        String key = (String) x.next();
                        JSONObject jobj2 = jobj1.optJSONObject(key);
                        Iterator y = jobj2.keys();
                        while (y.hasNext()) {
                            String key2 = (String) y.next();
                            JSONObject jobj3 = jobj2.getJSONObject(key2);
                            confirmed = confirmed + jobj3.getInt("confirmed");
                            active += jobj3.getInt("active");
                            deceased += jobj3.getInt("deceased");
                            recovered += jobj3.getInt("recovered");

                        }

                        cnfrmtext.setText(String.valueOf(confirmed));
                        activetext.setText(String.valueOf(active));
                        deceasedtext.setText(String.valueOf(deceased));
                        recovertext.setText(String.valueOf(recovered));


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("MY", "Somethiong went wrong");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NetworkError) {
                        Log.d("MY", "Cannot connect to Internet...Please check your connection!");

                    } else if (error instanceof ServerError) {
                        Log.d("MY", "The server could not be found. Please try again after some time!!");

                    } else if (error instanceof AuthFailureError) {
                        Log.d("MY", "Cannot connect to Internet...Please check your connection!");

                    } else if (error instanceof ParseError) {
                        Log.d("MY", "Parsing error! Please try again after some time!!");

                    } else if (error instanceof NoConnectionError) {
                        Log.d("MY", "Cannot connect to Internet...Please check your connection!");

                    } else if (error instanceof TimeoutError) {
                        Log.d("MY", "Connection TimeOut! Please check your internet connection.");


                    }
                }
            });
            requestQueue.add(jsonRequest);

        }

}