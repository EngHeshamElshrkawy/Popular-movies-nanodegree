package com.example.hesham.popularmovies.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hesham.popularmovies.Objects.Review;
import com.example.hesham.popularmovies.R;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {

    public  ReviewAdapter(Activity context, List<Review> objects){
        super(context, 0,objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        Review review = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, parent, false);
        }
        assert review != null;
        TextView author = convertView.findViewById(R.id.author_tv);
        TextView content = convertView.findViewById(R.id.content_tv);
        author.setText(review.getAuthor());
        content.setText(review.getContent());
        return convertView;
    }

}
