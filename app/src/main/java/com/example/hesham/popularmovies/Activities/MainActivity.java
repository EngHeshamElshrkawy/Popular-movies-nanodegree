package com.example.hesham.popularmovies.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.hesham.popularmovies.Adapters.MovieAdapter;
import com.example.hesham.popularmovies.Helpers.AppDatabase;
import com.example.hesham.popularmovies.Helpers.AppExecutors;
import com.example.hesham.popularmovies.Helpers.Utils;
import com.example.hesham.popularmovies.Objects.Movie;
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
import java.util.List;


public class MainActivity extends AppCompatActivity {
    //TODO: Replace THE "####" with your api key
    String apiKey = "?api_key=" + "#####" + "&page=";
    ProgressDialog pd;
    public MovieAdapter adapter;
    GridView listView;
    String POSTER_PATH = "http://image.tmdb.org/t/p/w500/";
    ArrayList<Movie> moviesList = new ArrayList<>();
    TextView noInternet;
    boolean isTopRated;
    boolean isPopular;
    boolean isFavourite;
    int PAGE_NUMBER = 1;
    String TOP_RATED = "top_rated";
    String POPULAR = "popular";
    String apiUrl = "http://api.themoviedb.org/3/movie/";
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.discover_view);
        noInternet = findViewById(R.id.no_internet_tv);
        mDb = AppDatabase.getInstance(getApplicationContext());
        getData(apiUrl + TOP_RATED + apiKey + String.valueOf(PAGE_NUMBER));
        isTopRated = true;
        isPopular = false;
        isFavourite = false;


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isFavourite){
            PopulateFavourite();
        }
    }


    public void getData(String link) {

        try {
            if(Utils.isConnected()){
                new MovieJSON().execute(link);
            }else{
                noInternet.setVisibility(View.VISIBLE);
                noInternet.setText("No internet connection");
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }


    @SuppressLint("StaticFieldLeak")
    private class MovieJSON extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
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
            if (pd.isShowing()) {
                pd.dismiss();
            }
            parseResponse(result);


        }
    }

    public void parseResponse(String result) {

        try {
            JSONObject response = new JSONObject(result);
            JSONArray movies = response.getJSONArray("results");
            for (int i = 0; i < movies.length(); i++) {
                JSONObject movie = movies.getJSONObject(i);
                String name = movie.getString("title");
                String plot = movie.getString("overview");
                String image = POSTER_PATH + movie.getString("poster_path");
                double rating = movie.getDouble("vote_average");
                int id = movie.getInt("id");
                String object = String.valueOf(movie);
                String released = movie.getString("release_date");
                moviesList.add(new Movie(name, plot, image, rating, id, released, object));
            }
            adapter = new MovieAdapter(this, moviesList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {Movie movie = (Movie) listView.getItemAtPosition(position);
                    Intent details = new Intent(MainActivity.this, DetailsActivity.class);
                    details.putExtra("isFavourite", false);
                    details.putExtra("name", movie.getName());
                    details.putExtra("plot", movie.getPlot());
                    details.putExtra("image", movie.getImage());
                    details.putExtra("rating", movie.getRating());
                    details.putExtra("id", movie.getId());
                    details.putExtra("released", movie.getReleased());
                    startActivity(details);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem top = menu.findItem(R.id.top_rated);
        MenuItem popular = menu.findItem(R.id.popular);
        MenuItem favourite = menu.findItem(R.id.favourite);
        try {
            if(!Utils.isConnected()){
                top.setEnabled(false);
                popular.setEnabled(false);
                favourite.setEnabled(false);
                isPopular = false;
                isTopRated = false;
                isFavourite = true;
                PopulateFavourite();
            }else{
                top.setEnabled(!isTopRated);
                popular.setEnabled(!isPopular);
                favourite.setEnabled(!isFavourite);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.top_rated:
                adapter.clear();
                getData(apiUrl + TOP_RATED + apiKey + String.valueOf(PAGE_NUMBER));
                isTopRated = true;
                isPopular = false;
                isFavourite = false;
                return true;
            case R.id.popular:
                adapter.clear();
                getData(apiUrl + POPULAR + apiKey + String.valueOf(PAGE_NUMBER));
                isTopRated = false;
                isPopular = true;
                isFavourite = false;
                return true;
            case R.id.favourite:
                adapter.clear();
                listView.setAdapter(adapter);
                PopulateFavourite();
                isTopRated = false;
                isPopular = false;
                isFavourite = true;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void PopulateFavourite() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Movie> movies = mDb.movieDao().loadAllMovies();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new MovieAdapter(MainActivity.this, movies);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Movie movie = (Movie) listView.getItemAtPosition(position);
                                Intent details = new Intent(MainActivity.this, DetailsActivity.class);
                                details.putExtra("isFavourite", true);
                                details.putExtra("name", movie.getName());
                                details.putExtra("plot", movie.getPlot());
                                details.putExtra("image", movie.getImage());
                                details.putExtra("rating", movie.getRating());
                                details.putExtra("id", movie.getId());
                                details.putExtra("released", movie.getReleased());
                                details.putExtra("reviews", movie.getReviews());
                                details.putExtra("cast", movie.getCasts());
                                details.putExtra("recommendations", movie.getRecommendations());
                                details.putExtra("runtime", movie.getRuntime());
                                details.putExtra("imageDir", movie.getImageDir());
                                startActivity(details);

                            }
                        });
                    }
                });

            }
        });


        noInternet.setVisibility(View.INVISIBLE);

    }

}
