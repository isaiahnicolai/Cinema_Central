package it.cinema.cinemaCentral;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;

public class MovieDetails extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.details_toolbar);
        setSupportActionBar(myToolbar);

        try {
           //JSONObject j = HomeActivity.j;
            JSONObject j = new JSONObject(getIntent().getStringExtra("json"));

            /**
             * Need to figure out why passing through intent doesn't work
             * Use movie class after full implemented and Parcelable
             */

            //new JSONObject(getIntent().getStringExtra("product"));
            //JSONArray j = new JSONArray(getIntent().getStringExtra("product"));

            /**
             * Downloads poster and parses JSON object for movie values to display in textView
             */

            new ImageDownloader(((ImageView) (findViewById(R.id.imageView)))).execute(j.getString("Poster"));
            ((TextView) findViewById(R.id.textView)).setText("MOVIE TITLE : " +
                            j.getString("Title") + "\n" +
                            "DIRECTOR : " +
                            j.getString("Director") +
                            "\n" + "TYPE : " + j.getString("Type") + "(" + j.getString("Genre") + ")"
                            + "\n" + "RELEASED : " + j.getString("Released")
                            + "\n" + "IMDB RATING : " + j.getString("imdbRating") + "(" + j.getString("imdbVotes") + " votes)"
                            + "\n" + "RUN TIME : " + j.getString("Runtime")
                            + "\n" + "RATED : " + j.getString("Rated")
                            + "\n" + "PLOT : " + j.getString("Plot")
            );

        } catch (Exception e) {


                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        //String imgUrl =
        //new ImageDownloader(imageView).execute(imgUrl);
    }



        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_movie_details, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
        class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;

            public ImageDownloader(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            protected Bitmap doInBackground(String... urls) {
                String url = urls[0];
                Bitmap mIcon = null;
                try {
                    InputStream in = new java.net.URL(url).openStream();
                    mIcon = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
                return mIcon;
            }

            protected void onPostExecute(Bitmap result) {
                bmImage.setImageBitmap(result);
            }
        }



}
