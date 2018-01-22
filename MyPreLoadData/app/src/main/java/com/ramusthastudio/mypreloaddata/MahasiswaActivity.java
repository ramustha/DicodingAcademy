package com.ramusthastudio.mypreloaddata;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.List;

public class MahasiswaActivity extends AppCompatActivity {
  private RecyclerView recyclerView;
  private MahasiswaAdapter mahasiswaAdapter;
  private MahasiswaHelper mahasiswaHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mahasiswa);

    recyclerView = findViewById(R.id.recyclerview);

    mahasiswaHelper = new MahasiswaHelper(this);
    mahasiswaAdapter = new MahasiswaAdapter(this);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    recyclerView.setAdapter(mahasiswaAdapter);

    mahasiswaHelper.open();

    // Ambil semua data mahasiswa di database
    List<MahasiswaModel> mahasiswaModels = mahasiswaHelper.getAllData();

    mahasiswaHelper.close();

    mahasiswaAdapter.addItem(mahasiswaModels);
  }
}
