package it.cinema.cinemaCentral;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import it.cinema.cinemaCentral.R;

public class Search extends ActionBarActivity {


    EditText edit;
    Button search;
    JSONObject j;
    JSONArray s;
    ListView listView;
    static String[] values;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(myToolbar);
        search =(Button)findViewById(R.id.searchButton);
        listView = (ListView) findViewById(R.id.movie_list);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movieName = ((EditText) findViewById(R.id.edit_text)).getText().toString().replace(" ", "+");
                //new readJson().execute("http://www.omdbapi.com/?t="+movieName+"&y=&plot=short&r=json");
                new readJson().execute("http://www.omdbapi.com/?s="+movieName+"&r=json");
            }
        });

        /**
         * Search movies and add to listview send user to movie detail page on click
         *   Make movie Detail class able to process requests from Search class and Home class
         *
         *   Sample code in  Ashish Search Result class
         */

    }
    private class readJson extends AsyncTask<String,Void,JSONObject> {
        protected JSONObject doInBackground(String... surl) {
            j = new JSONObject();
            InputStream in = null;
            String result = "";
            JSONObject jsonObject = null;


            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(surl[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                return null;
            }

            // Read response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                in.close();
                result = sb.toString();
            } catch (Exception e) {
                return null;
            }

            // Convert string to object
            try {
                jsonObject = new JSONObject(result);
            } catch (JSONException e) {
                return null;
            }
            j = jsonObject;
            return jsonObject;

        }

        protected void onPostExecute(JSONObject page) {

            ((EditText) (findViewById(R.id.edit_text))).setText("");
            String x = "";
            try {
                s = j.getJSONArray("Search");
                for (int i = 0; i < s.length(); i++) {
                    x += s.getJSONObject(i).getString("Title") + "(" + s.getJSONObject(i).getString("Year") + ")";
                    if (i != s.length()) x += "\n";
                }
                values = x.split("\n");
                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, Search.values);

                // Assign adapter to ListView
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        // ListView Clicked item index
                        int itemPosition = position;

                        // ListView Clicked item value
                        String itemValue = (String) listView.getItemAtPosition(position);
                        itemValue = itemValue.substring(0,itemValue.length()-6).replace(" ","+");
                        //Toast.makeText(getApplicationContext(),itemValue,Toast.LENGTH_SHORT).show();
                        new executeJson().execute("http://www.omdbapi.com/?t="+itemValue+"&y=&plot=short&r=json");
                    }

                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private class executeJson extends AsyncTask<String,Void,JSONObject> {
        protected JSONObject doInBackground(String... surl) {
            j = new JSONObject();
            InputStream in = null;
            String result = "";
            JSONObject jsonObject = null;


            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(surl[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                return null;
            }

            // Read response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                in.close();
                result = sb.toString();
            } catch (Exception e) {
                return null;
            }

            // Convert string to object
            try {
                jsonObject = new JSONObject(result);
            } catch (JSONException e) {
                return null;
            }
            j = jsonObject;
            return jsonObject;

        }

        protected void onPostExecute(JSONObject page) {
            Intent intent = new Intent(Search.this, MovieDetails.class);
            intent.putExtra("json",j.toString());
            startActivity(intent);

        }
    }




}