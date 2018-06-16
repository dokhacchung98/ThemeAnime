package com.example.administrator.themeanime.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.themeanime.adapter.AdapterListView;
import com.example.administrator.themeanime.adapter.ItemListView;
import com.example.administrator.themeanime.R;
import com.example.administrator.themeanime.activity.MenuActivity;
import com.roger.catloadinglibrary.CatLoadingView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Administrator on 25/12/2017.
 */

public class Fragment_List extends Fragment implements  SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, android.widget.SearchView.OnQueryTextListener {

    private MenuActivity menuActivity;
    private ArrayList<ItemListView> itemListViews;
    private AdapterListView adapterListView;
    private ListView listView;

    public AdapterListView getAdapterListView() {
        return adapterListView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_list, container, false);
        menuActivity = (MenuActivity) getActivity();
        setHasOptionsMenu(true);
        listView = view.findViewById(R.id.list);
        if (menuActivity.isInternet()) {
            new Loadding().execute();
        } else {
            Toast.makeText(menuActivity, "Internet connection error", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.btnSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getItemSearch(String newText) {
        ArrayList<ItemListView> listViews = new ArrayList<>();
        for (ItemListView listView : itemListViews) {
            if (listView.getName().toLowerCase().startsWith(newText.toLowerCase())) {
                listViews.add(listView);
            }
        }
        adapterListView = new AdapterListView(menuActivity, listViews);
        listView.setAdapter(adapterListView);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            resetArr();
            return false;
        }
        ArrayList<ItemListView> filteredValues = new ArrayList<>();
        for (ItemListView value : itemListViews) {
            if (value.getName().toLowerCase().startsWith(newText.toLowerCase())) {
                filteredValues.add(value);
            }
        }
        adapterListView = new AdapterListView(menuActivity, filteredValues);
        listView.setAdapter(adapterListView);
        return false;
    }

    private void resetArr() {
        adapterListView = new AdapterListView(menuActivity, itemListViews);
        listView.setAdapter(adapterListView);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return false;
    }

    private class Loadding extends AsyncTask<Void, Void, Void> {
        private CatLoadingView catLoadingView;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            itemListViews = new ArrayList<>();
            catLoadingView = new CatLoadingView();
            catLoadingView.show(getActivity().getSupportFragmentManager(), "Loading");
            catLoadingView.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document document = Jsoup.connect("https://wall.alphacoders.com/galleries.php?id=84472").get();
                Elements elements = document.select("ul[id=ul_top]").select("li[id=3785]").select("ul").select("li");
                for (int i = 0; i < elements.size(); i++) {
                    if (i != 3) {
                        Element elements1 = elements.get(i);
                        String name = elements1.select("a").first().text().replace("» ", "");
                        name = name.substring(name.indexOf(")") + 1);
                        String link = "https://wall.alphacoders.com/" + elements1.select("a").first().attr("href");
                        itemListViews.add(new ItemListView(name, link));
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
            adapterListView = new AdapterListView(menuActivity, itemListViews);
            listView.setAdapter(adapterListView);
            catLoadingView.dismiss();
        }

    }

}
