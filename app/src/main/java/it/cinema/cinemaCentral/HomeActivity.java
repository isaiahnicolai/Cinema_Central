package it.cinema.cinemaCentral;



import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


import it.cinema.coverflow.components.ui.containers.FeatureCoverFlow;

import static android.support.v7.app.ActionBar.*;


public class HomeActivity extends AppCompatActivity {

    //implements ConnectionCallbacks, OnConnectionFailedListener {

    private FeatureCoverFlow mCoverFlow;
    private CoverFlowAdapter mAdapter;
    private ArrayList<MovieEntity> mData = new ArrayList<>(0);
    TextView test;
    //private TextSwitcher mTitle;
    public JSONObject j;
    Button search;
    //GoogleApiClient mGoogleApiClient;
   // protected Location mLastLocation;
    //private AddressResultReceiver mResultReceiver;
    Boolean mAddressRequested;

    static String[] values;
    JSONArray s;
    private String mAddressOutput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(myToolbar);


        /**
         * Location testing
         * not done working
         *
         this.mAddressRequested = true;
         this.buildGoogleApiClient();
         mGoogleApiClient.connect();
         // this.startIntentService();
         test = (TextView)findViewById(R.id.testView);
         **/

        search = (Button) findViewById(R.id.searchButtonHome);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Search.class);
                startActivity(intent);
            }
        });

/**
 * Adds posters to the coverflow adapter to display on main screen
 * Possibly make dynamic based on user location
 */

        mData.add(new MovieEntity(R.drawable.poster1, R.string.title1));
        mData.add(new MovieEntity(R.drawable.poster2, R.string.title2));
        mData.add(new MovieEntity(R.drawable.poster3, R.string.title3));


        /**
         * Text switcher that display the name of the movie in
         * the cover flow removed but can be readded
         */

        /*
        mTitle = (TextSwitcher) findViewById(R.id.title);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
                TextView textView = (TextView) inflater.inflate(R.layout.item_title, null);
                return textView;
            }
        });
        */
        // Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);//Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
        //mTitle.setInAnimation(in);
        //mTitle.setOutAnimation(out);

        mAdapter = new CoverFlowAdapter(this);
        mAdapter.setData(mData);
        mCoverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);
        mCoverFlow.setAdapter(mAdapter);


        /**
         * Handles clicks on movie posters in the coverflow adapter
         */
        mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                try {

                    /**
                     * Currently hardcoded because of JSON exception
                     */
                    // String movieName = getResources().getString(mData.get(position).titleResId);
                    //movieName.replace(" ", "+");
                    //new readJson().execute("http://www.omdbapi.com/?t="+movieName+"&y=&plot=short&r=json");


                    // new readJson().execute("http://www.omdbapi.com/?s="+movieName+"&r=json");

                    int title = mData.get(position).titleResId;
                    switch (title) {
                        case R.string.title1:
                            new readJson().execute(" http://www.omdbapi.com/?t=hunger+games&y=2015&plot=short&r=json");
                            break;
                        case R.string.title2:
                            new readJson().execute("http://www.omdbapi.com/?t=captain+america&y=2016&plot=short&r=json");
                            break;
                        case R.string.title3:
                            new readJson().execute("http://www.omdbapi.com/?t=spectre&y=2015&plot=short&r=json");
                            break;
                    }

                } catch (Exception e) {

                }
            }
        });

        /*
        mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle.setText(getResources().getString(mData.get(position).titleResId));
            }

            @Override
            public void onScrolling() {
                mTitle.setText("");
            }
        });

        */


    }


    /**
     * Location code
     *
     *

    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }


    @Override
    public void onConnected(Bundle bundle) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                Toast.makeText(this, R.string.no_geocoder_available,
                        Toast.LENGTH_LONG).show();
                return;
            }

            if (mAddressRequested) {
                startIntentService();
            }

        }

    }
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData){

            // Display the address string
            // or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            test.setText(mAddressOutput);
            //displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                Toast.makeText(getApplicationContext(), getString(R.string.address_found), Toast.LENGTH_LONG).show();
            }

        }
    }





    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    **/



    /**
     * JSON request to OMDB to retrieve information about movie poster that was selected
     */
    private class readJson extends AsyncTask<String,Void,JSONObject>{
       @Override
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
            j= jsonObject;
            return jsonObject;

        }
        @Override
        protected void onPostExecute(JSONObject page)
        {

            /**
             * Handles the intent for sending JSON object to Movie details class
             *
             */

            try{
            if(j !=null) {

                    Intent intent = new Intent(HomeActivity.this, MovieDetails.class);
                    //intent.putExtra("movie",movie);
                    intent.putExtra("json",j.toString());
                    startActivity(intent);

            }else{
                throw new Exception("error");
            }
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(), "some error occured : " + e.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coverflow_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_search:
               Intent intent = new Intent(HomeActivity.this,Search.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    /**
     * Builds Google Play service
     *
    protected synchronized void buildGoogleApiClient() {
         mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener( this)
                .addApi(LocationServices.API)
                .build();
    }
     **/

}

