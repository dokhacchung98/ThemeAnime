package com.example.administrator.themeanime.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.administrator.themeanime.fragment.Fragment_About;
import com.example.administrator.themeanime.fragment.Fragment_Favorite;
import com.example.administrator.themeanime.fragment.Fragment_List;
import com.example.administrator.themeanime.fragment.Fragment_Rencest;
import com.example.administrator.themeanime.R;


public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragmentTemp = null;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private android.support.design.widget.AppBarLayout action;
    private Fragment_Rencest fragment_rencest;
    private Fragment_About fragment_about;
    private Fragment_Favorite fragment_favorite;
    private Fragment_List fragment_list;
    public static final String[] PERMISSION_LIST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SET_WALLPAPER,
            Manifest.permission.SET_WALLPAPER_HINTS
    };
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                Intent intent = new Intent(getApplication(), PermissionActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        }
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permisson : PERMISSION_LIST) {
                if (checkSelfPermission(permisson) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(PERMISSION_LIST, 0);
                    return;
                }
            }
        }
        init();

    }

    private void init() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        builder = new AlertDialog.Builder(MenuActivity.this);
        builder.setMessage("Do you want exit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).setNegativeButton("No", null);
        isNetworkConnected();
        alertDialog = builder.create();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        action = findViewById(R.id.action);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.getMenu().getItem(0).setChecked(true);
        openDrawerLayout();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            alertDialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuRecent: {
                fragment_rencest = new Fragment_Rencest();
                switchFragment(fragment_rencest);
            }
            break;
            case R.id.menuList: {
                openList();
            }
            break;
            case R.id.menuFavoite: {
                fragment_favorite = new Fragment_Favorite();
                switchFragment(fragment_favorite);
            }
            break;
            case R.id.menuAbout: {
                fragment_about = new Fragment_About();
                switchFragment(fragment_about);
            }
            break;
        }
        item.setChecked(true);
        drawerLayout.closeDrawers();
        return true;
    }

    public void openList() {
        fragment_list = new Fragment_List();
        switchFragment(fragment_list);
    }

    public void switchFragment(Fragment fragment) {
        fragmentTemp = fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.frPanel, fragment);
        fragmentTransaction.commit();
    }

    public void openDrawerLayout() {
        //drawerLayout.openDrawer(GravityCompat.START);
        fragment_rencest = new Fragment_Rencest();
        fragmentTemp = fragment_rencest;
        switchFragment(fragment_rencest);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fragmentTemp == fragment_favorite) {
            fragment_favorite = new Fragment_Favorite();
            switchFragment(fragment_favorite);
        } else if (fragmentTemp == fragment_rencest) {
            fragment_rencest = new Fragment_Rencest();
            switchFragment(fragment_rencest);
        }
    }

    public void isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {
            Toast.makeText(this, "Internet connection error", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public boolean isInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        }
        return true;
    }
}
