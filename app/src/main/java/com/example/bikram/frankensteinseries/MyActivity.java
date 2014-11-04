package com.example.bikram.frankensteinseries;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;


public class MyActivity extends Activity implements AsynResponse {
    private Context context;
    private String stage;
    private String timeday;
    private String actorName;
    private String eventName;
    private String type;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        context = this.getApplicationContext();

        //spinner to select the type of character
        Spinner spinner3 = (Spinner) findViewById(R.id.personType);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.array_type, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                type = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                type = "";
            }
        });

        // Spinner to select day of week
        Spinner spinner = (Spinner) findViewById(R.id.daySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.day_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                timeday = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                timeday = "";
            }
        });


        // Spinner to select stage
//        Spinner spinner1 = (Spinner) findViewById(R.id.stageSpinner);
//        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
//                R.array.array_stage, android.R.layout.simple_spinner_item);
//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner1.setAdapter(adapter1);
//        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int pos, long id) {
//                stage = parent.getItemAtPosition(pos).toString();
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) {
//                stage = "";
//            }
//        });

        // Search for the field
        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {
            actorName = ((EditText) findViewById(R.id.actors_name)).getText().toString();
            eventName = ((EditText) findViewById(R.id.event_name)).getText().toString();

            postData(type, actorName);
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


    public String postData(String type, String name) {

        if (type.length() <= 0 || name.length() <= 0) {
            return null;
        } else {
            SenGetReqAsyncTask asyncTask = new SenGetReqAsyncTask(this);

            String time = "";
            String time_wed = "4/22/2015";
            String time_thr = "4/23/2015";
            String time_fri = "4/24/2015";
            String time_sat = "4/25/2015";
            String date_early, date_late;
            if (timeday.equals("Wednesday")) {
                 date_early = time_wed;
                 date_late = time_thr;
            } else if (timeday.equals("Thursday")) {
                 date_early = time_thr;
                 date_late = time_fri;
            } else if (timeday.equals("Friday")) {
                 date_early = time_fri;
                 date_late = time_sat;
            } else {
                date_early = "";
                date_late = "";
            }

            time += "startDate=" + date_early + "&endDate=" + date_late;

            Log.d("time", time);

            asyncTask.execute(type, name, date_early, date_late);
        }
        return "";
    }

    @Override
    public void processFinish(String output){
        result = output;
        Intent i = new Intent(MyActivity.this, SearchResults.class);
        i.putExtra(SearchResults.EXTRA_RESULT, result);
        i.putExtra(SearchResults.EXTRA_TYPE, type);

        startActivity(i);
    }

    private class SenGetReqAsyncTask extends AsyncTask<String, Void, String> {

        AsynResponse delegate=null;

        public SenGetReqAsyncTask(AsynResponse as){
            delegate = as;
        }

        @Override
        protected void onPostExecute(String result) {
            delegate.processFinish(result);
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String name = params[1];
            String starttime = params[2];
            String endtime = params[3];

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            String hostName = "http://139.147.24.15:8000";
            String urlName = hostName += "/search/people/?android=true";

            try {
                if (name.length() > 0) {
                    urlName += "&types=[" + type + "]&name=" + URLEncoder.encode(name, "UTF-8").replace("+", "%20");
                    if (starttime.length() > 0 && endtime.length() > 0) {
                        urlName += "&startDate="
                                + URLEncoder.encode(starttime, "UTF-8").replace("+", "%20")
                                + "&endDate="
                                + URLEncoder.encode(endtime, "UTF-8").replace("+", "%20");
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("url", urlName);
            try {
                request.setURI(new URI(urlName));
                try {
                    // HttpResponse is an interface just like HttpPost.
                    // Therefore we can't initialize them
                    HttpResponse response = client.execute(request);

                    // According to the JAVA API, InputStream constructor do nothing.
                    //So we can't initialize InputStream although it is not an interface
                    InputStream inputStream = response.getEntity().getContent();

                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder stringBuilder = new StringBuilder();

                    String bufferedStrChunk = null;

                    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                        stringBuilder.append(bufferedStrChunk + '\n');
                        if (bufferedStrChunk.contains("</html>")) {
                            throw new RuntimeException("get html file instead of json");
                        }
                    }
                    return stringBuilder.toString();
                } catch (ClientProtocolException cpe) {
                    cpe.printStackTrace();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

            } catch (URISyntaxException urie) {
                urie.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
