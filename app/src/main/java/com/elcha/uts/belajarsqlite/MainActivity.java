package com.elcha.uts.belajarsqlite;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lvFavorit;
    FloatingActionButton fabTambah;
    DatabaseHandler db;
    List<String> listFavorit;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvFavorit = findViewById(R.id.lv_favorit);
        fabTambah = findViewById(R.id.fab_tambah);
        db = new DatabaseHandler(this);

        fabTambah.setOnClickListener(v -> bukaDialogTambah());

        lvFavorit.setOnItemClickListener((parent, view, position, id) -> {
            String data = listFavorit.get(position);
            bukaDialogUpdateHapus(data);
        });

        // Mengambil data dari database dan menginisialisasi adapter
        listFavorit = db.tampilSemua();
        adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_list_item_1,
            listFavorit);

        // Menghubungkan adapter ke ListView
        lvFavorit.setAdapter(adapter);
    }

    private void bukaDialogTambah() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Menu Favorit");

        View dialogView = LayoutInflater.from(this)
            .inflate(R.layout.frm_tambah, null);
        builder.setView(dialogView);

        EditText etNama = dialogView.findViewById(R.id.et_nama);
        Button btnSimpan = dialogView.findViewById(R.id.btn_simpan);

        AlertDialog dialog = builder.create();

        btnSimpan.setOnClickListener(v -> {
            // Menyimpan data baru ke database
            boolean success = simpanData(etNama.getText().toString());

            if (success) {
                // Memperbarui data pada adapter
                listFavorit.clear();
                listFavorit.addAll(db.tampilSemua());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            } else {
                // Handle jika simpan gagal
                etNama.setError("Gagal menyimpan data");
            }
        });

        dialog.show();
    }

    private void bukaDialogUpdateHapus(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update atau Hapus Menu Favorit");

        View dialogView = LayoutInflater.from(this)
            .inflate(R.layout.frm_update, null);  // Gunakan layout yang benar
        builder.setView(dialogView);

        EditText etNama = dialogView.findViewById(R.id.et_nama);
        Button btnUpdate = dialogView.findViewById(R.id.btn_update);
        Button btnHapus = dialogView.findViewById(R.id.btn_hapus);

        // Menampilkan data yang akan di-update di EditText
        etNama.setText(data);

        AlertDialog dialog = builder.create();

        btnUpdate.setOnClickListener(v -> {
            // Update data di database
            boolean updated = db.updateNama(data, etNama.getText().toString());

            if (updated) {
                // Memperbarui data pada adapter
                listFavorit.clear();
                listFavorit.addAll(db.tampilSemua());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            } else {
                // Handle jika update gagal
                etNama.setError("Update gagal");
            }
        });

        btnHapus.setOnClickListener(v -> {
            // Hapus data dari database
            boolean deleted = db.hapus(data);

            if (deleted) {
                // Memperbarui data pada adapter
                listFavorit.clear();
                listFavorit.addAll(db.tampilSemua());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            } else {
                // Handle jika hapus gagal
                etNama.setError("Hapus gagal");
            }
        });

        dialog.show();
    }

    private boolean simpanData(String nama) {
        return db.simpan(nama);
    }
}

