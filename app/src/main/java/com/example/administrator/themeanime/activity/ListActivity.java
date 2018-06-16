package com.example.administrator.themeanime.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.administrator.themeanime.adapter.AdapterImage;
import com.example.administrator.themeanime.adapter.GetFavoriteList;
import com.example.administrator.themeanime.adapter.ItemImage;
import com.example.administrator.themeanime.R;
import com.roger.catloadinglibrary.CatLoadingView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements AbsListView.OnScrollListener/*, AdapterView.OnItemClickListener*/ {
    public static final int CODE = 55;
    private ArrayList<ItemImage> listImage = new ArrayList<>();
    private GridView gridView;
    private AdapterImage adapterImage;
    private String url = "";
    private Toolbar toolbar;
    int pageCount = 1;
    boolean loadData = false;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main3);
        this.bundle = savedInstanceState;
        gridView = findViewById(R.id.gvRencest1);
        Intent intent = getIntent();
        url = intent.getStringExtra("URL");
        url = url + "&page=";
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().show();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = intent.getStringExtra("TITLE");
        toolbar.setTitle(title);
        gridView.setOnScrollListener(this);
        init();
    }

    private void init() {
        listImage = new ArrayList<>();
        adapterImage = new AdapterImage(this, listImage);
        gridView.setAdapter(adapterImage);
        pageCount = 1;
        if (isNetworkConnected()) {
            new LoadUrl().execute();
        } else {
            Toast.makeText(this, "Internet connection error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
            if (loadData == false) {
                loadData = true;
                pageCount++;
                if (pageCount > 0)
                    new LoadUrl().execute();
                else
                    Snackbar.make(gridView, "Out of data", Snackbar.LENGTH_SHORT).show();
            }
        }
    }


    private class LoadUrl extends AsyncTask<Void, Void, Void> {
        private CatLoadingView catLoadingView;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            catLoadingView = new CatLoadingView();
            catLoadingView.show(getSupportFragmentManager(), "Loading");
            catLoadingView.setCancelable(false);
            loadData = true;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(url + pageCount).get();
                Elements element1 = doc.select("div[id=container_page]").select("div[class=thumb-container-big ]");
                if (element1.size() < 30) {
                    pageCount = -99;
                }
                for (int i = 0; i < element1.size(); i++) {
                    String l = element1.get(i).select("div[class=thumb-container]").select("div[class=boxgrid]").select("img").attr("src");
                    if (!getAs(l)) {
                        if (getLike(l)) {
                            listImage.add(new ItemImage(l, true));
                        } else {
                            listImage.add(new ItemImage(l, false));
                        }
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
            adapterImage.notifyDataSetChanged();
            catLoadingView.dismiss();
            loadData = false;
        }

        private boolean getAs(String link) {
            for (int i = 0; i < listImage.size(); i++) {
                if (link.equals(listImage.get(i).getImg())) {
                    return true;
                }
            }
            return false;
        }

        private boolean getLike(String link) {
            GetFavoriteList getFavoriteList = new GetFavoriteList(ListActivity.this);
            ArrayList<ItemImage> itemImages = getFavoriteList.getArr();
            for (int i = 0; i < itemImages.size(); i++) {
                if (link.equals(itemImages.get(i).getImg())) {
                    return true;
                }
            }
            return false;
        }

    }


    //    @Override
//    protected void onResume() {
//        super.onResume();
//        if (isNetworkConnected()) {
//            new LoadUrl().execute();
//        } else {
//            Toast.makeText(this, "Internet connection error", Toast.LENGTH_SHORT).show();
//        }
//    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        }
        return true;
    }
}
