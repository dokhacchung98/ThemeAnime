package com.example.administrator.themeanime.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.administrator.themeanime.R;
import com.example.administrator.themeanime.activity.SlideActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 25/12/2017.
 */

public class AdapterImage extends ArrayAdapter<ItemImage> {
    private LayoutInflater layoutInflater;
    private ArrayList<ItemImage> strings;
    private Context mainActivity;
    private ImageView imageView;
    private Context context;

    public AdapterImage(@NonNull Context context, @NonNull ArrayList<ItemImage> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        strings = objects;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        mainActivity = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.item_list, parent, false);
        imageView = convertView.findViewById(R.id.pop);
        Picasso.with(context).load(strings.get(position).getImg()).error(android.R.drawable.ic_menu_report_image).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSlideImage(position, strings);

            }
        });
        return convertView;
    }


    public void gotoSlideImage(int index, ArrayList<ItemImage> arrayList) {
        Intent intent = new Intent(context, SlideActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("List", arrayList);
        bundle.putInt("Index", index);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
