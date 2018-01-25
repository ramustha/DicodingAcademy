package com.ramusthastudio.mynotesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import static com.ramusthastudio.mynotesapp.DatabaseContract.CONTENT_URI;
import static com.ramusthastudio.mynotesapp.FormAddUpdateActivity.REQUEST_UPDATE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private RecyclerView fNotesListView;
  private ProgressBar fProgressBarView;
  private FloatingActionButton fFabAddView;
  private Cursor list;
  private NoteAdapter adapter;
  private final NoteHelper noteHelper = new NoteHelper(this);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle("Notes");
    }

    fNotesListView = findViewById(R.id.rv_notes);
    fProgressBarView = findViewById(R.id.progressbar);
    fFabAddView = findViewById(R.id.fab_add);

    fNotesListView.setLayoutManager(new LinearLayoutManager(this));
    fNotesListView.setHasFixedSize(true);

    fFabAddView.setOnClickListener(this);

    noteHelper.open();

    adapter = new NoteAdapter(this);
    adapter.setListNotes(list);
    fNotesListView.setAdapter(adapter);

    new LoadNoteAsync().execute();

  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.fab_add) {
      Intent intent = new Intent(MainActivity.this, FormAddUpdateActivity.class);
      startActivityForResult(intent, FormAddUpdateActivity.REQUEST_ADD);
    }
  }

  @SuppressLint("StaticFieldLeak")
  private class LoadNoteAsync extends AsyncTask<Void, Void, Cursor> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      fNotesListView.setVisibility(View.GONE);
      fProgressBarView.setVisibility(View.VISIBLE);
    }

    @Override
    protected Cursor doInBackground(Void... voids) {
      return getContentResolver().query(CONTENT_URI, null, null, null, null);
    }

    @Override
    protected void onPostExecute(Cursor notes) {
      super.onPostExecute(notes);
      fNotesListView.setVisibility(View.VISIBLE);
      fProgressBarView.setVisibility(View.GONE);

      list = notes;
      adapter.setListNotes(list);
      adapter.notifyDataSetChanged();

      if (list.getCount() == 0) {
        showSnackbarMessage("Tidak ada data saat ini");
      }
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == FormAddUpdateActivity.REQUEST_ADD) {
      if (resultCode == FormAddUpdateActivity.RESULT_ADD) {
        new LoadNoteAsync().execute();
        showSnackbarMessage("Satu item berhasil ditambahkan");
      }
    } else if (requestCode == REQUEST_UPDATE) {

      if (resultCode == FormAddUpdateActivity.RESULT_UPDATE) {
        new LoadNoteAsync().execute();
        showSnackbarMessage("Satu item berhasil diubah");
      } else if (resultCode == FormAddUpdateActivity.RESULT_DELETE) {
        new LoadNoteAsync().execute();
        showSnackbarMessage("Satu item berhasil dihapus");
      }
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    noteHelper.close();
  }

  private void showSnackbarMessage(String message) {
    Snackbar.make(fNotesListView, message, Snackbar.LENGTH_SHORT).show();
  }
}
