package com.example.hesham.popularmovies.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hesham.popularmovies.Objects.Trailer;
import com.example.hesham.popularmovies.R;

import java.util.List;

public class TrailerAdapter extends ArrayAdapter<Trailer>{

    public TrailerAdapter(Activity context, List<Trailer> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){

        Trailer trailer = getItem(position);

        if(convertView == null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.trailer_item, parent, false);

        }

        TextView nameTv = convertView.findViewById(R.id.trailer_name_tv);
        TextView typeTv = convertView.findViewById(R.id.teaser_tv);
        nameTv.setText(trailer.getName());
        typeTv.setText(trailer.getType());
        return convertView;









    }

}
