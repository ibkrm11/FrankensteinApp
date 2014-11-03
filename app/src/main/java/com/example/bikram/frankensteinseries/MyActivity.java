package com.example.bikram.frankensteinseries;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {
    private Context context;
    private boolean searchByTime;
    private String stage;
    private String timeday;
    private String actorName;
    private String eventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        context = this.getApplicationContext();

        // Spinner to select day of week
        Spinner spinner = (Spinner) findViewById(R.id.daySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.day_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                timeday = parent.getItemAtPosition(pos).toString().toLowerCase();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                timeday = "";
            }
        });


        // Spinner to select stage
        Spinner spinner1 = (Spinner) findViewById(R.id.stageSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.array_stage, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                stage = parent.getItemAtPosition(pos).toString().toLowerCase();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                stage = "";
            }
        });

        // Search for the field
        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {
            actorName = ((EditText) findViewById(R.id.actors_name)).getText().toString().toLowerCase();
            eventName = ((EditText) findViewById(R.id.event_name)).getText().toString().toLowerCase();
            System.out.println("Event Details");
            System.out.println(actorName +" "+ eventName +"  "+ " "+ timeday + "  " +stage   );
            Log.d("test connection","" + isConnected());
            postData();
        }
    });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public void postData() {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramUsername = params[0];
                String paramPassword = params[1];

                System.out.println("*** doInBackground ** paramUsername " + paramUsername + " paramPassword :" + paramPassword);

                HttpClient client = new DefaultHttpClient();

                // In a POST request, we don't pass the values in the URL.
                //Therefore we use only the web page URL as the parameter of the HttpPost argument
                //HttpPost httpPost = new HttpPost("http://139.147.33.170:8000/index");

                try {
                    // UrlEncodedFormEntity is an entity composed of a list of url-encoded pairs.
                    //This is typically useful while sending an HTTP POST request.

                    // setEntity() hands the entity (here it is urlEncodedFormEntity) to the request.

                    HttpGet request = new HttpGet();
                    request.setURI(new URI("http://139.147.33.170:8000/search/people/?types=[actor]&name=Adam"));

                    try {
                        // HttpResponse is an interface just like HttpPost.
                        //Therefore we can't initialize them
                        HttpResponse response = client.execute(request);

                        // According to the JAVA API, InputStream constructor do nothing.
                        //So we can't initialize InputStream although it is not an interface
                        InputStream inputStream = response.getEntity().getContent();

                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                        StringBuilder stringBuilder = new StringBuilder();

                        String bufferedStrChunk = null;

                        String str = "";

                        while((bufferedStrChunk = bufferedReader.readLine()) != null){
                            //Log.d("in loop", stringBuilder.toString());
                            //stringBuilder.append(bufferedStrChunk);
                            str += bufferedStrChunk + '\n';
                            if (str.length() > 2000/* && bufferedStrChunk.contains("<!DOCTYPE html>")*/)
                                break;
                        }

                        Log.e("",str);

                        Log.d("any return?", stringBuilder.toString());

                        return stringBuilder.toString();

                    }
                    catch (ClientProtocolException cpe) {
                        cpe.printStackTrace();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (URISyntaxException urie) {
                    urie.printStackTrace();
                }

                return null;
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute("/search", "/people");
        Log.d("?","?");
    }
}
