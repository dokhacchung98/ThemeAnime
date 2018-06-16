package com.example.administrator.themeanime.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.administrator.themeanime.adapter.AdapterImage;
import com.example.administrator.themeanime.adapter.ItemImage;
import com.example.administrator.themeanime.R;
import com.example.administrator.themeanime.activity.MenuActivity;
import com.example.administrator.themeanime.adapter.GetFavoriteList;

import java.util.ArrayList;

/**
 * Created by Administrator on 25/12/2017.
 */

public class Fragment_Favorite extends Fragment {

    private MenuActivity menuActivity;
    private GetFavoriteList getFavoriteList;
    private ArrayList<ItemImage> itemImages;
    private AdapterImage adapterImage;
    private GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        menuActivity = (MenuActivity) getActivity();
        getFavoriteList = new GetFavoriteList(menuActivity);
        View view = inflater.inflate(R.layout.layout_recent, container, false);
        gridView = view.findViewById(R.id.gvRencest);
        itemImages = getFavoriteList.getArr();
        adapterImage = new AdapterImage(menuActivity, itemImages);
        gridView.setAdapter(adapterImage);
        return view;
    }


}
