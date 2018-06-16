package com.example.administrator.themeanime.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 27/12/2017.
 */

public class GetFavoriteList {
    private Context context;
    private ArrayList<ItemImage> itemImages = new ArrayList<>();
    private String Path = Environment.getDataDirectory().getPath()
            + "/data/com.example.administrator.themeanime/databases/";
    private String Name = "data.sqlite";
    private SQLiteDatabase sqLiteDatabase;

    public GetFavoriteList(Context context) {
        this.context = context;
        copyFileToDevice();
    }

    private void copyFileToDevice() {
        File file = new File(Path + Name);
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            parentFile.mkdirs();
            try {
                InputStream inputStream = context.getAssets().open(Name);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int count = inputStream.read(b);
                while (count != -1) {
                    fileOutputStream.write(b, 0, count);
                    count = inputStream.read(b);
                }
                inputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openedDatabase() {
        sqLiteDatabase = context.openOrCreateDatabase(Name, Context.MODE_PRIVATE, null);
    }

    private void closeDatabase() {
        sqLiteDatabase.close();
    }


    public ArrayList<ItemImage> getArr() {
        openedDatabase();
        Cursor cursor = sqLiteDatabase.query("FavoriteList", null, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String img = cursor.getString(cursor.getColumnIndex("img"));
            int check = cursor.getInt(cursor.getColumnIndex("like"));
            boolean like = false;
            if (check == 1) {
                like = true;
            }
            itemImages.add(new ItemImage(img, like));
            cursor.moveToNext();
        }
        closeDatabase();
        return itemImages;
    }


    public long insert(ItemImage item) {
        openedDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("img", item.getImg());
        contentValues.put("like", 1);
        long id = sqLiteDatabase.insert("FavoriteList", null, contentValues);
        closeDatabase();
        return id;
    }

    public int delete(ItemImage itemImage) {
        openedDatabase();
        String where = "img = ? ";
        String[] whereAgrs = {itemImage.getImg() + ""};
        int rows = sqLiteDatabase.delete("FavoriteList", where, whereAgrs);
        closeDatabase();
        return rows;
    }
}
