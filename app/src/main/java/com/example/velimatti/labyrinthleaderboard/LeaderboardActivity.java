package com.example.velimatti.labyrinthleaderboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private Integer league = 0;
    private String lname;
    private TextView txt2;
    private EditText txtTop;
    private Spinner spn1;
    private ArrayList<String> stringData;

    private ArrayList<LabyrinthRun> labyrinthRun = new ArrayList<LabyrinthRun>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            league = savedInstanceState.getInt("LEAGUE");
        }
        setContentView(R.layout.activity_leaderboard);

        //otetaan itentin mukana tuleva liigan arvo talteen
        Intent intent = getIntent();
        league = intent.getIntExtra("LEAGUE", 0);

        //otetaan liigan nimi erilliseen muuttujaan
        switch (league) {
            case 1:
                lname = getString(R.string.l1);
                break;
            case 2:
                lname = getString(R.string.l2);
                break;
            case 3:
                lname = getString(R.string.l3);
                break;
            case 4:
                lname = getString(R.string.l4);
                break;
            default:
                lname = "Error";
        }

        spn1 = (Spinner) findViewById(R.id.spn1);
        spn1.setSelection(3);
        spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                labyrinthRun = new ArrayList<LabyrinthRun>();
                getRunDataFromServer();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtTop = (EditText) findViewById(R.id.txtTop);
        txtTop.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                labyrinthRun = new ArrayList<LabyrinthRun>();
                getRunDataFromServer();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                View view = getCurrentFocus();
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return false;
            }
        });

        txt2 = (TextView) findViewById(R.id.txt2);
        txt2.setText(lname);
        txt2.setFocusableInTouchMode(true);
        txt2.requestFocus();

        //if (savedInstanceState == null)
        //    getRunDataFromServer();
    }

    public void refresh(View v) {
        labyrinthRun = new ArrayList<LabyrinthRun>();
        getRunDataFromServer();
    }

    public void getRunDataFromServer() {
        // Haetaan Labyrintti data palvelimelta
        // Instantiate the RequestQueue.
        String urlString = "http://api.pathofexile.com/ladders/"+lname+"?limit="+txtTop.getText().toString()+"&type=labyrinth&difficulty="+(spn1.getSelectedItemPosition()+1);
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText( this, response, Toast.LENGTH_SHORT).show();
                        parseJsonAndConstructArrayList( response );
                        showData();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void parseJsonAndConstructArrayList(String response) {
        // rakennetaan LabyrinthRun array response -datan pohjalta
        try {
            JSONObject rootObject = new JSONObject( response );
            JSONArray wikiArray = rootObject.getJSONArray("entries");
            for( int i=0; i<wikiArray.length(); i++ ){
                JSONObject o = wikiArray.getJSONObject(i);
                Integer rank = o.getInt("rank");
                Integer time = o.getInt("time");
                // character name on character objectin sisällä
                JSONObject c = o.getJSONObject("character");
                String name = c.getString("name");
                String ascendancy = c.getString("class");
                labyrinthRun.add(new LabyrinthRun( rank, time, name, ascendancy ));
            }
        }
        catch (JSONException e ){

        }
    }

    public void showData(){
        // Laitetaan data listViewiin...
        List<LabyrinthRun> data = labyrinthRun;
        stringData = new ArrayList<String>();
        for( LabyrinthRun f : data ){
            Integer min = f.time/60;
            Integer sec = f.time%60;
            stringData.add( "Rank: " + f.rank.toString() + ", Time: " + min.toString() + ":" + sec.toString()  + ", Class: " + f.ascendancy + "\nName: " + f.name);
        }
        ArrayAdapter<String> wikiSummaryAdapter =
                new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, stringData );
        ListView myListView = (ListView)findViewById(R.id.listView);
        myListView.setAdapter( wikiSummaryAdapter );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //otetaan liiga ja data talteen
        outState.putInt( "LEAGUE", league );
        outState.putStringArrayList("STRINGDATA", stringData);
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        //palautetetaan liiga
        league = savedInstanceState.getInt("LEAGUE");

        //palautetaan data ja laitetaan se näkyville
        stringData = savedInstanceState.getStringArrayList("STRINGDATA");
        ArrayAdapter<String> wikiSummaryAdapter =
                new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, stringData );
        ListView myListView = (ListView)findViewById(R.id.listView);
        myListView.setAdapter( wikiSummaryAdapter );
    }

}
