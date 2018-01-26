package com.ramusthastudio.cataloguemovie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ramusthastudio.cataloguemovie.model.Result;
import com.ramusthastudio.cataloguemovie.repo.DatabaseContract;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ramusthastudio.cataloguemovie.DetailFragment.ARG_PARAM;
import static com.ramusthastudio.cataloguemovie.MovieListAdapter.sDateFormat;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.CONTENT_URI;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.BACKDROP;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.GENRE;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.POSTER;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.MOVIE_ID;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.OVERVIEW;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.POPULARITY;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.RATING;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.TITLE;

public class FavoriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
  private static final int LOADER_ID = 99;
  private SwipeRefreshLayout fSwipeRefreshView;
  private RecyclerView fMovieListView;
  private MovieListAdapter fMovieListAdapter;
  private StaggeredGridLayoutManager fGridLayoutManager;
  private FloatingActionButton fToTopFab;
  private View fMovieEmpty;

  public FavoriteFragment() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(final Context context) {
    super.onAttach(context);
    fMovieListAdapter = new MovieListAdapter(context);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_main, container, false);
    fGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

    fMovieEmpty = view.findViewById(R.id.movieEmpty);
    fSwipeRefreshView = view.findViewById(R.id.swipeListView);
    fMovieListView = view.findViewById(R.id.movieList);
    fToTopFab = view.findViewById(R.id.fab);
    fToTopFab.hide();

    fMovieListView.setHasFixedSize(true);
    fMovieListView.setLayoutManager(fGridLayoutManager);
    fMovieListView.setAdapter(fMovieListAdapter);

    fSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        fToTopFab.hide();
        fMovieListAdapter.clear();
        initLoader();
      }
    });

    fToTopFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        fToTopFab.hide();
        fMovieListView.smoothScrollToPosition(0);
      }
    });

    fMovieListAdapter.setOnClickListener(new MovieListAdapter.AdapterListener() {
      @Override
      public void onClick(Result aResult) {
        Log.d(DetailFragment.class.getSimpleName(), aResult.toString());

        if (getActivity() != null) {
          Intent intent = new Intent(getActivity(), DetailActivity.class);
          intent.putExtra(ARG_PARAM, aResult);
          startActivity(intent);
        }
      }

      @Override
      public void onClickShare(final Result aResult) {
        Log.d(AbstractMovieFragment.class.getSimpleName(), "Shared");
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, String.format("%s (%s) \n\n %s",
            aResult.getTitle(),
            sDateFormat.format(aResult.getReleaseDate()),
            aResult.getOverview()));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
      }
    });

    initLoader();
    return view;
  }

  @Override
  public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
    if (getContext() == null) {
      return null;
    }
    return new CursorLoader(getContext(), CONTENT_URI, null, null, null, null);
  }

  @Override
  public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
    fMovieListAdapter.setMovieList(toResult(data));
    fSwipeRefreshView.setRefreshing(false);

    if (data == null) {
      onEmptyMovie();
    } else {
      onShowMovie();
    }
  }

  @Override
  public void onLoaderReset(final Loader<Cursor> loader) {
    fMovieListAdapter.setMovieList(new ArrayList<Result>());
    fSwipeRefreshView.setRefreshing(false);
    onEmptyMovie();
  }

  private void initLoader() {
    if (getActivity() != null) {
      getActivity()
          .getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }
  }

  private void onEmptyMovie() {
    fMovieEmpty.setVisibility(View.VISIBLE);
    fMovieListView.setVisibility(View.GONE);
    fToTopFab.setVisibility(View.GONE);
  }

  private void onShowMovie() {
    fMovieEmpty.setVisibility(View.GONE);
    fMovieListView.setVisibility(View.VISIBLE);
    fToTopFab.setVisibility(View.VISIBLE);
  }

  public List<Result> toResult(Cursor aCursor) {
    List<Result> resultList = new ArrayList<>();
    while (aCursor.moveToNext()) {
      final int dbId = DatabaseContract.getColumnInt(aCursor, MOVIE_ID);
      final String title = DatabaseContract.getColumnString(aCursor, TITLE);
      final String poster = DatabaseContract.getColumnString(aCursor, POSTER);
      final String backdrop = DatabaseContract.getColumnString(aCursor, BACKDROP);
      final long releaseDate = DatabaseContract.getColumnLong(aCursor, RELEASE_DATE);
      final String genre = DatabaseContract.getColumnString(aCursor, GENRE);
      final double rating = DatabaseContract.getColumnDouble(aCursor, RATING);
      final String overview = DatabaseContract.getColumnString(aCursor, OVERVIEW);
      final float popularity = DatabaseContract.getColumnFloat(aCursor, POPULARITY);

      final String[] splited = genre.split(" ");
      List<Integer> b = new ArrayList<>();
      for (final String s : splited) {
        b.add(Integer.valueOf(s));
      }

      resultList.add(new Result(
          dbId,
          title,
          poster,
          backdrop,
          new Date(releaseDate),
          b,
          rating,
          overview,
          popularity
      ));
    }
    aCursor.close();

    return resultList;
  }

  public static FavoriteFragment newInstance() {
    FavoriteFragment fragment = new FavoriteFragment();
    fragment.setArguments(new Bundle());
    return fragment;
  }
}
