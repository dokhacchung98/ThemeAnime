package com.example.administrator.themeanime.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.themeanime.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 26/12/2017.
 */

public class AdapterSlideImage extends PagerAdapter {
    private ArrayList<ItemImage> arrImage;
    private Context mainActivity;
    private LayoutInflater layoutInflater;
    private ImageView imageView;

    public AdapterSlideImage(ArrayList<ItemImage> arrImage, Context mainActivity) {
        this.arrImage = arrImage;
        this.mainActivity = mainActivity;
        layoutInflater = LayoutInflater.from(mainActivity);
    }

    @Override
    public int getCount() {
        return arrImage.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.item_slide, container, false);
        imageView = view.findViewById(R.id.imgSlide);
        Picasso.with(mainActivity).load(arrImage.get(position).getImg()).error(android.R.drawable.ic_menu_report_image).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
