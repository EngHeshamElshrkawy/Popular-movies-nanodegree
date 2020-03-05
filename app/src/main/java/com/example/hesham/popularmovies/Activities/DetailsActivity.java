package com.example.hesham.popularmovies.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hesham.popularmovies.Adapters.CastAdapter;
import com.example.hesham.popularmovies.Adapters.MovieAdapter;
import com.example.hesham.popularmovies.Adapters.ReviewAdapter;
import com.example.hesham.popularmovies.Helpers.AppDatabase;
import com.example.hesham.popularmovies.Helpers.AppExecutors;
import com.example.hesham.popularmovies.Helpers.Utils;
import com.example.hesham.popularmovies.Objects.Cast;
import com.example.hesham.popularmovies.Objects.Movie;
import com.example.hesham.popularmovies.Objects.Review;
import com.example.hesham.popularmovies.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {
    ImageView mPoster;
    TextView mPlot;
    TextView mReleased;
    String POSTER_PATH = "http://image.tmdb.org/t/p/w500/";
    String reviewResults;
    String castResults;
    String recommendationResults;
    String runtimeResults;
    String name;
    String plot;
    String image;
    String released;

    int id;
    ArrayList<Review> reviewList = new ArrayList<>();
    ArrayList<Cast> castList = new ArrayList<>();
    ArrayList<Movie> recommendationsList = new ArrayList<>();
    MovieAdapter recommendationsAdapter;
    ReviewAdapter reviewAdapter;
    CastAdapter castAdapter;
    ListView reviewLv;
    RatingBar ratingBar;
    TextView runTimeTv;
    GridView castLv;
    GridView recommendGv;
    TextView detailsName;

    double rating;
    int end;
    boolean isFavourite;
    private AppDatabase mDb;
    String imageDir;
    String castDir;
    Button favouriteButton;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public boolean isMoviedFavourite;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Objects.requireNonNull(getSupportActionBar()).hide();
        mPoster = findViewById(R.id.details_poster);
        mPlot = findViewById(R.id.details_plot);
        mReleased = findViewById(R.id.details_released);
        reviewLv = findViewById(R.id.review_lv);
        castLv = findViewById(R.id.cast);
        detailsName = findViewById(R.id.details_name);
        ratingBar = findViewById(R.id.ratingBar);
        runTimeTv = findViewById(R.id.run_time);
        recommendGv = findViewById(R.id.recommendations_gv);
        favouriteButton = findViewById(R.id.favourite_button);


        Bundle data = getIntent().getExtras();
        isFavourite = data.getBoolean("isFavourite", false);
        name = data.getString("name");
        plot = data.getString("plot");
        image = data.getString("image");
        rating = data.getDouble("rating");
        id = data.getInt("id");
        released = data.getString("released");


        if (!isFavourite) {
            populateUIOnline(name, plot, image, released, id);

        } else {
            reviewResults = data.getString("reviews");
            castResults = data.getString("cast");
            recommendationResults = data.getString("recommendations");
            runtimeResults = data.getString("runtime");
            imageDir = data.getString("imageDir");
            castDir = data.getString("castDir");




            try {
                populateUIOffline(name, plot, released, id, reviewResults, castResults, recommendationResults, runtimeResults, imageDir);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ratingBar.setRating(Float.parseFloat(String.valueOf(rating / 2)));
        mDb = AppDatabase.getInstance(getApplicationContext());



        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        isMoviedFavourite = prefs.getBoolean(String.valueOf(id), false);
        if (isMoviedFavourite) {
            favouriteButton.setBackgroundResource(R.drawable.favourite);
        }else{
            favouriteButton.setBackgroundResource(R.drawable.not_favourite);

        }



    }

    public void populateUIOffline(String name,String plot,String released,int id ,String reviewResults,String castResults,String recommendationResults, String runtimeResults, String imageDir) throws JSONException {
        try {
            File f=new File(imageDir, String.valueOf(id) +".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            mPoster.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        mPlot.setText(plot);
        detailsName.setText(name);
        mReleased.setText(released);
        runTimeTv.setText(runtimeResults);
        ratingBar.setRating(Float.parseFloat(String.valueOf(rating / 2)));

        ParseCast(castResults);
        ParseRecommendations(recommendationResults);
        ParseReviews(reviewResults);
    }

    public void populateUIOnline(String name, String plot, String image, String released, int id) {

        mPlot.setText(plot);
        detailsName.setText(name);
        mReleased.setText(released);
        Picasso.get().load(image).placeholder(R.drawable.place_holder_animated).into(mPoster);
        new ReviewsTask().execute("https://api.themoviedb.org/3/movie/" + String.valueOf(id) + "/reviews?api_key=85d8b7fc49300344300331020d65d764");
        new MovieDetails().execute("https://api.themoviedb.org/3/movie/" + String.valueOf(id) + "?api_key=85d8b7fc49300344300331020d65d764");
        new CastDetails().execute("https://api.themoviedb.org/3/movie/" + String.valueOf(id) + "/credits?api_key=85d8b7fc49300344300331020d65d764");
        new RecommendationsDetails().execute("https://api.themoviedb.org/3/movie/" + String.valueOf(id) + "/recommendations?api_key=85d8b7fc49300344300331020d65d764");

    }

    @SuppressLint("StaticFieldLeak")
    private class ReviewsTask extends AsyncTask<String, String, String> {

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
            reviewResults = result;
            try {
                ParseReviews(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void ParseReviews(String json) throws JSONException {
        JSONObject reviewsObject = new JSONObject(json);
        JSONArray reviews = reviewsObject.getJSONArray("results");
        end = 1;
        if (end > reviews.length()) {
            end = reviews.length();
        }
        for (int i = 0; i < end; i++) {
            JSONObject trailer = reviews.getJSONObject(i);
            String author = trailer.getString("author");
            String content = trailer.getString("content");
            String url = trailer.getString("url");
            reviewList.add(new Review(author, content, url));

            Log.v("TAG", "GGGG");
        }
        reviewAdapter = new ReviewAdapter(this, reviewList);
        reviewLv.setAdapter(reviewAdapter);
        reviewLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Review review = (Review) reviewLv.getItemAtPosition(position);
                String url = review.getUrl();
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(webIntent);

            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class MovieDetails extends AsyncTask<String, String, String> {

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
                ParseDetails(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @SuppressLint("DefaultLocale")
    public void ParseDetails(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        int runtime = jsonObject.getInt("runtime");
        int hours = (runtime / 60);
        int minutes = runtime % 60;
        runtimeResults = String.format("%d:%02d:00", hours, minutes);
        runTimeTv.setText(runtimeResults);
        runTimeTv.setVisibility(View.VISIBLE);
    }

    @SuppressLint("StaticFieldLeak")
    private class CastDetails extends AsyncTask<String, String, String> {

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
            castResults = result;
            try {
                ParseCast(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @SuppressLint("DefaultLocale")
    public void ParseCast(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray casts = jsonObject.getJSONArray("cast");
        end = 3;
        if (casts.length() < end) {
            end = casts.length();
        }
        for (int i = 0; i < end; i++) {
            JSONObject cast = casts.getJSONObject(i);
            String name = cast.getString("name");
            String profile = "http://image.tmdb.org/t/p/w185/" + cast.getString("profile_path");
            castList.add(new Cast(name, profile, imageDir));
        }

        castAdapter = new CastAdapter(this, castList);
        castLv.setAdapter(castAdapter);
    }

    @SuppressLint("StaticFieldLeak")
    private class RecommendationsDetails extends AsyncTask<String, String, String> {

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
            recommendationResults = result;
            try {
                ParseRecommendations(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @SuppressLint("DefaultLocale")
    public void ParseRecommendations(String json) throws JSONException {
        JSONObject response = new JSONObject(json);
        JSONArray movies = response.getJSONArray("results");
        end = 3;
        if (movies.length() < 3) {
            end = movies.length();
        }
        for (int i = 0; i < end; i++) {
            JSONObject movie = movies.getJSONObject(i);
            String name = movie.getString("title");
            String plot = movie.getString("overview");
            String image = POSTER_PATH + movie.getString("poster_path");
            double rating = movie.getDouble("vote_average");
            int id = movie.getInt("id");
            String object = String.valueOf(movie);
            String released = movie.getString("release_date");
            recommendationsList.add(new Movie(name, plot, image, rating, id, released, object, imageDir));
        }

        recommendationsAdapter = new MovieAdapter(this, recommendationsList);
        recommendGv.setAdapter(recommendationsAdapter);
        try {
            if (Utils.isConnected()){
                recommendGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Movie movie = (Movie) recommendGv.getItemAtPosition(position);
                        Intent details = new Intent(getApplicationContext(), DetailsActivity.class);
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
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }

    public void backButton(View view) {
        finish();
    }

    public void playTrailers(View view) {
        try {
            if(Utils.isConnected()){
                Intent openTrailers = new Intent(this, TrailersActivity.class);
                openTrailers.putExtra("id", id);
                startActivity(openTrailers);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }

    public String imageToDir(Bitmap bitmap, String dir){

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File myPath=new File(directory,dir + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public void Favorited(View view) {
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) mPoster.getDrawable());
        Bitmap bmp = bitmapDrawable.getBitmap();
        final Movie movie = new Movie(id, name, plot, image, released, rating, reviewResults, castResults, recommendationResults, runtimeResults,
                imageToDir(bmp, String.valueOf(id)));
        if(!isMoviedFavourite){
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().insertMovie(movie);

                }
            });

            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putBoolean(String.valueOf(id), true);
            editor.apply();

            favouriteButton.setBackgroundResource(R.drawable.favourite);

        }else{
            //delete it from the datebase
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().deleteByUserId(id);

                }
            });
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putBoolean(String.valueOf(id), false);
            editor.apply();
            favouriteButton.setBackgroundResource(R.drawable.not_favourite);
            finish();
        }


    }

















}