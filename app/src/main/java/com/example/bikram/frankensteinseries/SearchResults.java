package com.example.bikram.frankensteinseries;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ActionBar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

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
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bikram on 11/3/2014.
 */
public class SearchResults extends ListActivity{

    public static final String EXTRA_ACTOR = "com.android.bikram.frankensteinseries.actor";

    public static final String EXTRA_EVENT = "com.android.bikram.frankensteinseries.event";
    public static final String EXTRA_TIMEDAY = "com.android.bikram.frankensteinseries.timeday";
    public static final String EXTRA_STAGE = "com.android.bikram.frankensteinseries.stage";
    public static final String EXTRA_TYPE = "com.android.bikram.franensteinseries.type";

    private String actor;
    private String event;
    private String timeday;
    private String stage;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        actor = getIntent().getStringExtra(EXTRA_ACTOR);
        event  = getIntent().getStringExtra(EXTRA_EVENT);
        timeday = getIntent().getStringExtra(EXTRA_TIMEDAY);
        stage = getIntent().getStringExtra(EXTRA_STAGE);
        type = getIntent().getStringExtra(EXTRA_TYPE);

        postData(type, actor);

       /* ArrayList<String> results = new ArrayList<String>();
        results.add(actor);
        results.add(event);
        results.add(timeday);
        results.add(stage);*/

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, results);
        setListAdapter(adapter);

        adapter.notifyDataSetChanged();*/






    }
    /**
     * Creates options menu
     * @param menu
     * @return whether menu was created or not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    /**
     * Handles selection of items from options menu
     * @param item
     * @return whether option selection was successful or not
     */
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

    public List<String> postData(String type, String name) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String type = params[0];
                String name = params[1];

                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                String hostName = "http://139.147.24.15:8000";
                String urlName = hostName += "/search/people/?android=true";
                if (name.length() > 0) {
                    try {
                        urlName += "&types=[" + type + "]&name=" + URLEncoder.encode(name, "UTF-8").replace("+", "%20");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("url",urlName);
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
        if (type.length() <= 0) {
            return null;
        } else {
            SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
            sendPostReqAsyncTask.execute(type, name);
        }
        return new ArrayList<String>();
    }

}
