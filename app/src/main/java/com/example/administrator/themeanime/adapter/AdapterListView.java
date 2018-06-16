package com.example.administrator.themeanime.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.administrator.themeanime.R;
import com.example.administrator.themeanime.activity.ListActivity;
import com.example.administrator.themeanime.activity.MenuActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 26/12/2017.
 */

public class AdapterListView extends ArrayAdapter<ItemListView> {
    private LayoutInflater layoutInflater;
    private ArrayList<ItemListView> itemListViews;
    private TextView textView;
    private Context context;

    public AdapterListView(@NonNull Context context, @NonNull ArrayList<ItemListView> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        layoutInflater = LayoutInflater.from(context);
        this.itemListViews = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.item_listview, parent, false);
        textView = convertView.findViewById(R.id.txtReview);
        final ItemListView itemListView = itemListViews.get(position);
        textView.setText(itemListView.getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListActivity.class);
                intent.putExtra("URL", itemListView.getUrl());
                intent.putExtra("TITLE", itemListView.getName());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
