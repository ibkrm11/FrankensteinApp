package com.example.bikram.frankensteinseries;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Bikram on 11/3/2014.
 */
public class SearchResults extends ListActivity{

    public static final String EXTRA_TYPE = "com.android.bikram.franensteinseries.type";
    public static final String EXTRA_RESULT = "com.android.bikram.franensteinseries.result";

    private String type;
    private String result;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.result);
        result = getIntent().getStringExtra(EXTRA_RESULT);
        type = getIntent().getStringExtra(EXTRA_TYPE);

        ArrayList<String> results;

        title = "";

        if (type.equals("actor") || type.equals("role")) {
            title = "  Role   |   Event   |   Stage   |   Time";
        } else if (type.equals("crew")) {
            title = "Role   |   Stage";
        } else {
            title = "No Result";
        }

        results = parseJson(result);

        Log.d("????", results.toString());
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

    public ArrayList<String> parseJson(String s){
        ArrayList<String> arr = new ArrayList<String>();
        arr.add(title);
        s = s.replace("[[","\n");
        s = s.replace('"', ' ');
        s = s.replace('{',' ');
        s = s.replace('}',' ');
        s.trim();
        String[] actor_desc= s.split("]], ");

        for(int i=0;i < actor_desc.length; i++){
            //System.out.println(actor_desc[i]);
            actor_desc[i] = actor_desc[i].replace(']','\n');
            actor_desc[i] = actor_desc[i].replace(", [","");

            System.out.println(actor_desc[i]);
            System.out.println("----------------------------");
            arr.add( actor_desc[i]);
        }

        return arr;
    }
}
