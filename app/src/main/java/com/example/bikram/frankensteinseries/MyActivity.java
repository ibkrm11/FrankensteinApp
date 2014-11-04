package com.example.bikram.frankensteinseries;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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


public class MyActivity extends Activity {
    private Context context;
    private boolean searchByTime;
    private String stage;
    private String timeday;
    private String actorName;
    private String eventName;
    private String type;

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
        Spinner spinner1 = (Spinner) findViewById(R.id.stageSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.array_stage, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                stage = parent.getItemAtPosition(pos).toString();
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
            actorName = ((EditText) findViewById(R.id.actors_name)).getText().toString();
            eventName = ((EditText) findViewById(R.id.event_name)).getText().toString();
            System.out.println("Event Details");
            System.out.println(actorName +" "+ eventName +"  "+ " "+ timeday + "  " +stage   );

            Intent i = new Intent(MyActivity.this, SearchResults.class);
            i.putExtra(SearchResults.EXTRA_ACTOR, actorName);
            i.putExtra(SearchResults.EXTRA_EVENT, eventName);
            i.putExtra(SearchResults.EXTRA_TIMEDAY, timeday);
            i.putExtra(SearchResults.EXTRA_STAGE, stage);
            i.putExtra(SearchResults.EXTRA_TYPE, type);

            startActivity(i);
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
}
