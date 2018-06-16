package com.example.administrator.themeanime.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.administrator.themeanime.adapter.AdapterImage;
import com.example.administrator.themeanime.adapter.ItemImage;
import com.example.administrator.themeanime.R;
import com.example.administrator.themeanime.activity.MenuActivity;
import com.example.administrator.themeanime.adapter.GetFavoriteList;
import com.roger.catloadinglibrary.CatLoadingView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Administrator on 25/12/2017.
 */

public class Fragment_Rencest extends Fragment {

    private MenuActivity menuActivity;
    private ArrayList<ItemImage> listImage;
    private GridView gridView;
    private AdapterImage adapterImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recent, container, false);
        menuActivity = (MenuActivity) getActivity();
        gridView = view.findViewById(R.id.gvRencest);
        if (menuActivity.isInternet()) {
            new LoadUrl().execute();
        } else {
            Toast.makeText(menuActivity, "Internet connection error", Toast.LENGTH_SHORT).show();
        }
        return view;
    }


    private class LoadUrl extends AsyncTask<Void, Void, Void> {
        //private ProgressDialog builder;
        private CatLoadingView catLoadingView;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            builder = new ProgressDialog(menuActivity);
//            builder.setMessage("Loading...");
//            builder.setCancelable(false);
//            builder.show();
            catLoadingView = new CatLoadingView();
            catLoadingView.show(getActivity().getSupportFragmentManager(), "Loading");
            catLoadingView.setCancelable(false);
            listImage = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect("https://wall.alphacoders.com/by_gallery.php?id=3785").get();
                Elements element1 = doc.select("div[id=container_page]").select("div[class=thumb-container-big ]");
                for (int i = 0; i < element1.size(); i++) {
                    String l = element1.get(i).select("div[class=thumb-container]").select("div[class=boxgrid]").select("img").attr("src");
                    if (getLike(l)) {
                        listImage.add(new ItemImage(l, true));
                    } else {
                        listImage.add(new ItemImage(l, false));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapterImage = new AdapterImage(menuActivity, listImage);
            gridView.setAdapter(adapterImage);
            // builder.cancel();
            catLoadingView.dismiss();
        }
    }

    private boolean getLike(String link) {
        GetFavoriteList getFavoriteList = new GetFavoriteList(menuActivity);
        ArrayList<ItemImage> itemImages = getFavoriteList.getArr();
        for (int i = 0; i < itemImages.size(); i++) {
            if (link.equals(itemImages.get(i).getImg())) {
                return true;
            }
        }
        return false;
    }
}
