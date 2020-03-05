package com.example.hesham.popularmovies.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hesham.popularmovies.Activities.MainActivity;
import com.example.hesham.popularmovies.Helpers.CircleTransform;
import com.example.hesham.popularmovies.Objects.Movie;
import com.example.hesham.popularmovies.Helpers.Utils;
import com.example.hesham.popularmovies.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {
    public MovieAdapter(Activity context, List<Movie> objects) {

        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
        }
        assert movie != null;
        TextView textView = convertView.findViewById(R.id.discover_name);
        ImageView moviePoster = convertView.findViewById(R.id.discover_movie_image);
        textView.setText(movie.getName());

        Utils.loadImages(movie.getImage(), moviePoster);
        return convertView;

    }





}
