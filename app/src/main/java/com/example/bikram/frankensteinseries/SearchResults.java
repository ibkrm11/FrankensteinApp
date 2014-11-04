package com.example.bikram.frankensteinseries;

import android.app.ListActivity;
import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

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

    private String actor;
    private String event;
    private String timeday;
    private String stage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        actor = getIntent().getStringExtra(EXTRA_ACTOR);
        event  = getIntent().getStringExtra(EXTRA_EVENT);
        timeday = getIntent().getStringExtra(EXTRA_TIMEDAY);
        stage = getIntent().getStringExtra(EXTRA_STAGE);

        ArrayList<String> results = new ArrayList<String>();
        results.add(actor);
        results.add(event);
        results.add(timeday);
        results.add(stage);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, results);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();

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

}
