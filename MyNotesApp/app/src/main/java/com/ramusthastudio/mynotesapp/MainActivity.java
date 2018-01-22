package com.ramusthastudio.mynotesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import java.util.LinkedList;
import java.util.List;

import static com.ramusthastudio.mynotesapp.FormAddUpdateActivity.REQUEST_UPDATE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private RecyclerView fNotesListView;
  private ProgressBar fProgressBarView;
  private FloatingActionButton fFabAddView;
  private LinkedList<Note> list;
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

    list = new LinkedList<>();

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
  private class LoadNoteAsync extends AsyncTask<Void, Void, List<Note>> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      fNotesListView.setVisibility(View.GONE);
      fProgressBarView.setVisibility(View.VISIBLE);

      if (list.size() > 0) {
        list.clear();
      }
    }

    @Override
    protected List<Note> doInBackground(Void... voids) {
      return noteHelper.query();
    }

    @Override
    protected void onPostExecute(List<Note> notes) {
      super.onPostExecute(notes);
      fNotesListView.setVisibility(View.VISIBLE);
      fProgressBarView.setVisibility(View.GONE);

      list.addAll(notes);
      adapter.setListNotes(list);
      adapter.notifyDataSetChanged();

      if (list.size() == 0) {
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
        // fNotesListView.getLayoutManager().smoothScrollToPosition(fNotesListView, new RecyclerView.State(), 0);
      }
    } else if (requestCode == REQUEST_UPDATE) {

      if (resultCode == FormAddUpdateActivity.RESULT_UPDATE) {
        new LoadNoteAsync().execute();
        showSnackbarMessage("Satu item berhasil diubah");
        // int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION, 0);
        // fNotesListView.getLayoutManager().smoothScrollToPosition(fNotesListView, new RecyclerView.State(), position);
      } else if (resultCode == FormAddUpdateActivity.RESULT_DELETE) {
        int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION, 0);
        list.remove(position);
        adapter.setListNotes(list);
        adapter.notifyDataSetChanged();
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
