package com.example.hesham.popularmovies.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hesham.popularmovies.Objects.Cast;
import com.example.hesham.popularmovies.Helpers.CircleTransform;
import com.example.hesham.popularmovies.Helpers.Utils;
import com.example.hesham.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class CastAdapter extends ArrayAdapter<Cast> {

    public CastAdapter(Activity context, List<Cast> objects) {

        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Cast cast = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cast_item, parent, false);
        }
        assert cast != null;
        ImageView castImage = convertView.findViewById(R.id.cast_image);
        TextView castName = convertView.findViewById(R.id.cast_name);
        castName.setText(cast.getName());

        try {
            if (Utils.isConnected()){
                Picasso.get().load(cast.getImage()).into(Utils.picassoImageTarget(getContext(), "imageDir", cast.getName() + ".jpg"));
                Picasso.get().load(cast.getImage()).transform(new CircleTransform()).error(R.drawable.place_holder_animated).into(castImage);
            }else{
                File f = new File(cast.getImageDir(),cast.getName() +".jpg");
                Picasso.get().load(f).transform(new CircleTransform()).error(R.drawable.actor_place_holder).into(castImage);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return convertView;

    }






}
