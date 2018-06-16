package com.example.administrator.themeanime.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.administrator.themeanime.adapter.AdapterSlideImage;
import com.example.administrator.themeanime.adapter.GetFavoriteList;
import com.example.administrator.themeanime.adapter.ItemImage;
import com.example.administrator.themeanime.custom.MyCheckBox;
import com.example.administrator.themeanime.R;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class SlideActivity extends AppCompatActivity implements FabSpeedDial.MenuListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private ArrayList<ItemImage> arrImage = new ArrayList<>();
    private AdapterSlideImage adapterSlideImage;
    private ViewPager viewPager;
    private int index;
    private Toolbar toolbar;
    private FabSpeedDial fabSpeedDial;
    private Button buttonShare;
    private MyCheckBox checkBox;
    private GetFavoriteList getFavoriteList;
    private boolean slide = false;
    private Timer timer = new Timer();
    public static final String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/THEME";
    public static final String PATH1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/THEMESHARE";
    private ArrayList<ItemImage> itemChange = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.slide_image);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            arrImage = (ArrayList<ItemImage>) bundle.getSerializable("List");
            index = bundle.getInt("Index");
        }
        getFavoriteList = new GetFavoriteList(this);

        toolbar = findViewById(R.id.toolbar1);
        fabSpeedDial = findViewById(R.id.floatButton);
        fabSpeedDial.setMenuListener(this);
        buttonShare = findViewById(R.id.btnShare1);
        checkBox = findViewById(R.id.btnLike);
        buttonShare.setOnClickListener(this);
        if (arrImage.get(index).isLike()) {
            checkBox.setSafeCheck(true, MyCheckBox.IGNORE);
        } else {
            checkBox.setSafeCheck(false, MyCheckBox.IGNORE);
        }
        checkBox.setOnCheckedChangeListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().show();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = findViewById(R.id.viewPager);
        adapterSlideImage = new AdapterSlideImage(arrImage, this);
        viewPager.setAdapter(adapterSlideImage);
        viewPager.setCurrentItem(index, true);
        viewPager.addOnPageChangeListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareMenu(NavigationMenu navigationMenu) {
        return true;
    }

    @Override
    public boolean onMenuItemSelected(MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.btnSetWall: {
                intent = new Intent(SlideActivity.this, SetWallPaperActivity.class);
                intent.putExtra("LINK", arrImage.get(viewPager.getCurrentItem()).getImg());
                startActivity(intent);
            }
            break;
            case R.id.btnShow: {
                if (!slide) {
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new MyTimeTask(), 2000, 4000);
                    slide = true;
                    checkBox.setVisibility(View.INVISIBLE);
                } else {
                    if (timer != null) {
                        timer.cancel();
                        slide = false;
                        checkBox.setVisibility(View.VISIBLE);
                    }
                }
            }
            break;
            case R.id.btnShare: {
                if (!isNetworkConnected()) {
                    Toast.makeText(this, "Internet connection error", Toast.LENGTH_SHORT).show();
                    return false;
                }
                String name = arrImage.get(viewPager.getCurrentItem()).getImg().substring(arrImage.get(viewPager.getCurrentItem()).getImg().lastIndexOf("/") + 1);
                saveImage(name, 0);
            }
            break;
            case R.id.btnSave: {
                if (!isNetworkConnected()) {
                    Toast.makeText(this, "Internet connection error", Toast.LENGTH_SHORT).show();
                    return false;
                }
                final String[] name = {arrImage.get(viewPager.getCurrentItem()).getImg().substring(arrImage.get(viewPager.getCurrentItem()).getImg().lastIndexOf("/") + 1)};
                File dir = new File(PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File f = new File(PATH, name[0]);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setMessage("File already exists. Do you want to reload?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Date currentTime = Calendar.getInstance().getTime();
                        name[0] = name[0].replace(".jpg", "").replaceAll(".png", "") + String.valueOf(currentTime.getTime()) + ".jpg";
                        saveImage(name[0], 1);
                    }
                }).setNegativeButton("No", null);
                if (f.exists()) {
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return false;
                }
                saveImage(name[0], 1);
            }
            break;
            case R.id.btnZoom: {
                intent = new Intent(SlideActivity.this, ZoomActivity.class);
                intent.putExtra("ImgZoom", arrImage.get(viewPager.getCurrentItem()).getImg());
                startActivity(intent);
            }
            break;
        }
        return true;
    }

    @Override
    public void onMenuClosed() {

    }

    private void saveImage(final String name, int d) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);

        String link = arrImage.get(viewPager.getCurrentItem()).getImg().replace("-350-", "-1920-");
        if (d == 1) {
            progressDialog.setMessage("Downloading photos...");
            progressDialog.show();
            Ion.with(this)
                    .load(link)
                    .progressDialog(progressDialog)
                    .progress(new ProgressCallback() {
                        @Override
                        public void onProgress(long downloaded, long total) {
                            int a = (int) ((int) downloaded / total);
                            progressDialog.setProgress(a);
                        }
                    })
                    .write(new File(PATH, name))
                    .setCallback(new FutureCallback<File>() {
                        @Override
                        public void onCompleted(Exception e, File file) {
                            if (e == null && file != null) {
                                progressDialog.setProgress(100);
                                progressDialog.cancel();
                                Snackbar.make(viewPager, "Save image succsess!", Snackbar.LENGTH_SHORT).show();
                            } else {
                                if(progressDialog.isShowing())
                                    progressDialog.cancel();
                                Snackbar.make(viewPager, "Save image error!", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            File file = new File(PATH1);
            if (!file.exists()) {
                file.mkdirs();
            }
            progressDialog.setMessage("Loading photos...");
            progressDialog.show();
            Ion.with(this)
                    .load(link)
                    .progressDialog(progressDialog)
                    .progress(new ProgressCallback() {
                        @Override
                        public void onProgress(long downloaded, long total) {
                            int a = (int) ((int) downloaded / total);
                            progressDialog.setProgress(a);
                        }
                    })
                    .write(new File(PATH1, name))
                    .setCallback(new FutureCallback<File>() {
                        @Override
                        public void onCompleted(Exception e, File file) {
                            if (e == null && file != null) {
                                progressDialog.setProgress(100);
                                progressDialog.cancel();
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                File file1 = new File(PATH1, name);

                                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file1));
                                intent.setType("image/*");
                                startActivity(Intent.createChooser(intent, "Share image..."));
                            } else {
                                if(progressDialog.isShowing())
                                    progressDialog.cancel();
                                Snackbar.make(viewPager, "Loading error!", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View view) {
        String name = arrImage.get(viewPager.getCurrentItem()).getImg().substring(arrImage.get(viewPager.getCurrentItem()).getImg().lastIndexOf("/") + 1);
        saveImage(name, 0);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            long i = getFavoriteList.insert(arrImage.get(viewPager.getCurrentItem()));
            if (i != -1) {
                Snackbar.make(checkBox, "Insert Favorite Success", Snackbar.LENGTH_SHORT).show();
                arrImage.get(viewPager.getCurrentItem()).setLike(true);
            } else {
                Snackbar.make(checkBox, "Insert Favorite Invalid", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            long i = getFavoriteList.delete(arrImage.get(viewPager.getCurrentItem()));
            if (i != -1) {
                Snackbar.make(checkBox, "Delete Favorite Success", Snackbar.LENGTH_SHORT).show();
                arrImage.get(viewPager.getCurrentItem()).setLike(false);
            } else {
                Snackbar.make(checkBox, "Delete Favorite Invalid", Snackbar.LENGTH_SHORT).show();
            }
        }
        adapterSlideImage.notifyDataSetChanged();
        ItemImage itemImage = arrImage.get(viewPager.getCurrentItem());
        for (int i = 0; i < itemChange.size(); i++) {
            if (itemImage == itemChange.get(i)) {
                itemChange.get(i).setLike(itemImage.isLike());
                return;
            }
        }
        itemChange.add(itemImage);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        checkBox.setOnCheckedChangeListener(null);
        if (arrImage.get(position).isLike()) {
            checkBox.setSafeCheck(true, MyCheckBox.IGNORE);
        } else {
            checkBox.setSafeCheck(false, MyCheckBox.IGNORE);
        }
        checkBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyTimeTask extends TimerTask {
        @Override
        public void run() {
            SlideActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int n = viewPager.getCurrentItem();
                    if (n < arrImage.size()) {
                        viewPager.setCurrentItem(n + 1);
                    } else {
                        Snackbar.make(buttonShare, "Came To The End Of The List", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        }
        return true;
    }
//
//
//    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
//        private String name;
//        private String count;
//
//        private Bitmap downloadImageBitmap(String sUrl) {
//            Bitmap bitmap = null;
//            try {
//                bitmap = BitmapFactory.decodeStream((InputStream) new URL(sUrl).getContent());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return bitmap;
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            name = params[0].substring(params[0].lastIndexOf("/") + 1);
//            this.count = params[1];
//            return downloadImageBitmap(params[0]);
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            shareImageI(result, name);
//        }
//    }

}
