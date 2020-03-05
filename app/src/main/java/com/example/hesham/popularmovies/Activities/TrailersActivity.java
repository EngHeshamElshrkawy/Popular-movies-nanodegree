package com.example.hesham.popularmovies.Activities;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hesham.popularmovies.Adapters.TrailerAdapter;
import com.example.hesham.popularmovies.Objects.Trailer;
import com.example.hesham.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TrailersActivity extends AppCompatActivity {

    ListView trailerLv;
    TrailerAdapter trailerAdapter;
    ArrayList<Trailer> trailersList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);
        trailerLv = findViewById(R.id.trailer_lv);

        Intent intent = getIntent();
        int id = intent.getExtras().getInt("id");
        //TODO: Replace THE "####" with your api key
        new TrailersTask().execute("https://api.themoviedb.org/3/movie/" + String.valueOf(id) + "/videos?api_key=" + "####" );
    }


    @SuppressLint("StaticFieldLeak")
    private class TrailersTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");


                }

                return buffer.toString();


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                ParseTrailers(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void ParseTrailers(String json) throws JSONException {
        JSONObject trailerObject = new JSONObject(json);
        JSONArray trailers = trailerObject.getJSONArray("results");
        for (int i = 0; i < trailers.length(); i++) {
            JSONObject trailer = trailers.getJSONObject(i);
            String name = trailer.getString("name");
            String key = trailer.getString("key");
            String type = trailer.getString("type");
            trailersList.add(new Trailer(key, name, type));
        }
        trailerAdapter = new TrailerAdapter(this, trailersList);
        trailerLv.setAdapter(trailerAdapter);
        trailerLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trailer trailer = (Trailer) trailerLv.getItemAtPosition(position);
                String key = trailer.getKey();
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + key));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }

            }
        });


    }
}
