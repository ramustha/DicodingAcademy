package com.ramusthastudio.favoritemovie;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import static com.ramusthastudio.favoritemovie.provider.DatabaseContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor>, MovieListAdapter.AdapterListener {
  private static final int LOADER_ID = 99;
  private SwipeRefreshLayout fSwipeRefreshView;
  private ListView fMovieListView;
  private MovieListAdapter fMovieListAdapter;
  private View fMovieEmpty;
  private String fPathUrl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final Toolbar toolbar = findViewById(R.id.toolbar);

    setSupportActionBar(toolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    fMovieEmpty = findViewById(R.id.movieEmpty);
    fSwipeRefreshView = findViewById(R.id.swipeListView);
    fMovieListView = findViewById(R.id.movieList);

    fMovieListAdapter = new MovieListAdapter(this, null, this);
    fMovieListView.setAdapter(fMovieListAdapter);


    fSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        initLoader();
      }
    });
  }

  @Override
  public void onClick(final Uri aUri) {
    Intent intent = new Intent(this, DetailMovieActivity.class);
    intent.setData(aUri);
    startActivity(intent);
  }

  @Override
  public void onClickDelete(final Uri aUri) {
    getContentResolver().delete(aUri, null, null);
  }

  @Override
  public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
    return new CursorLoader(this, CONTENT_URI, null, null, null, null);
  }

  @Override
  public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
    fMovieListAdapter.swapCursor(data);
    fSwipeRefreshView.setRefreshing(false);

    if (fMovieListAdapter.getCount() == 0) {
      onEmptyMovie();
    } else {
      onShowMovie();
    }
  }

  @Override
  public void onLoaderReset(final Loader<Cursor> loader) {
    fMovieListAdapter.swapCursor(null);
    fSwipeRefreshView.setRefreshing(false);
    onEmptyMovie();
  }

  @Override
  public void onResume() {
    super.onResume();
    getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
  }

  private void initLoader() {
    getSupportLoaderManager().initLoader(LOADER_ID, null, this);
  }

  private void onEmptyMovie() {
    fMovieEmpty.setVisibility(View.VISIBLE);
    fMovieListView.setVisibility(View.GONE);
  }

  private void onShowMovie() {
    fMovieEmpty.setVisibility(View.GONE);
    fMovieListView.setVisibility(View.VISIBLE);
  }
}
