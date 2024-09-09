package com.elcha.uts.belajarsqlite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorit.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE tb_favorit (id INTEGER PRIMARY KEY AUTOINCREMENT, nama TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS tb_favorit";
        db.execSQL(sql);
        onCreate(db);
    }

    // Menyimpan data baru ke tabel tb_favorit
    public boolean simpan(String nama) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nama", nama);
        long result = db.insert("tb_favorit", null, values);
        return result != -1;
    }

    // Memperbarui data di tabel tb_favorit
    public boolean updateNama(String oldNama, String newNama) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nama", newNama);
        int result = db.update("tb_favorit", values, "nama = ?", new String[]{oldNama});
        return result > 0;
    }

    // Menghapus data dari tabel tb_favorit
    public boolean hapus(String nama) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("tb_favorit", "nama = ?", new String[]{nama});
        return result > 0;
    }

    // Menampilkan semua data dari tabel tb_favorit
    public List<String> tampilSemua() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT nama FROM tb_favorit";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
