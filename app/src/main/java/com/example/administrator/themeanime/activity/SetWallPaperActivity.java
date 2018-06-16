package com.example.administrator.themeanime.activity;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.administrator.themeanime.R;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.InputStream;
import java.net.URL;

public class SetWallPaperActivity extends AppCompatActivity implements View.OnClickListener {

    private CropImageView cropImage;
    private Toolbar toolbar;
    private FloatingActionButton btnOk;
    private FloatingActionButton btnVertical;
    private FloatingActionButton btnHorizontal;
    private Bitmap bitmap1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main5_crop);
        toolbar = findViewById(R.id.toolbar4);
        btnHorizontal = findViewById(R.id.btnHorizontal);
        btnOk = findViewById(R.id.btnOk);
        btnVertical = findViewById(R.id.btnVetical);
        btnVertical.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnHorizontal.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        cropImage = findViewById(R.id.cropimg);
        Intent intent = getIntent();
        String link = intent.getStringExtra("LINK");
        if (isNetworkConnected()) {
            LoadBitmap loadBitmap = new LoadBitmap();
            loadBitmap.execute(link);
        } else {
            Toast.makeText(this, "Internet connection error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnHorizontal: {
                //cropImage.setCropRect(new Rect(0, 0, 200, 400));
                cropImage.setAspectRatio(16, 9);
                cropImage.setFixedAspectRatio(true);
            }
            break;
            case R.id.btnVetical: {
                // cropImage.setCropRect(new Rect(0, 0, 400, 200));
                cropImage.setAspectRatio(9, 16);
                cropImage.setFixedAspectRatio(true);
            }
            break;
            case R.id.btnOk: {
                bitmap1 = cropImage.getCroppedImage();
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(SetWallPaperActivity.this);
                try {
                    wallpaperManager.setBitmap(bitmap1);
                    Toast.makeText(this, "Set As Wallpaper Success", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Set As Wallpaper Invaild", Toast.LENGTH_SHORT).show();
                }
            }
            break;

        }
    }

    private class LoadBitmap extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            cropImage.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String link = strings[0];
            link = link.replace("-350-", "-1920-");
            return loadBitMap(link);
        }

        private Bitmap loadBitMap(String link) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(link).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        }
        return true;
    }
}
