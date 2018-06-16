package com.example.administrator.themeanime.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.administrator.themeanime.R;

/**
 * Created by Admin on 1/11/2018.
 */

public class PermissionActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permision);
        button = findViewById(R.id.btnTry);
        button.setOnClickListener(this);
    }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    return;
                }
            }
            init();
        }

    private void init() {
        Intent intent = new Intent(PermissionActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permisson : MenuActivity.PERMISSION_LIST) {
                if (checkSelfPermission(permisson) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(MenuActivity.PERMISSION_LIST, 0);
                    return;
                }
            }
        }
        init();
    }
}
