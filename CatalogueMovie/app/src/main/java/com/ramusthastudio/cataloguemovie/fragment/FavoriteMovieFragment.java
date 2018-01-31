package com.ramusthastudio.cataloguemovie.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.ramusthastudio.cataloguemovie.MoviesActivity;
import com.ramusthastudio.cataloguemovie.R;
import com.ramusthastudio.cataloguemovie.model.Result;
import com.ramusthastudio.cataloguemovie.repo.DatabaseContract;
import com.ramusthastudio.cataloguemovie.service.MovieListAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.CONTENT_URI;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.BACKDROP;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.GENRE;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.OVERVIEW;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.POPULARITY;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.POSTER;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.RATING;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.TITLE;
import static com.ramusthastudio.cataloguemovie.service.MovieListAdapter.sDateFormat;

public class FavoriteMovieFragment extends Fragment
    implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {
  private static final String ADAPTER_STATE = "adapter_state";
  private static final int LOADER_ID = 98;
  private DrawerLayout fDrawerLayout;
  private NavigationView fNavigationView;
  private ActionBarDrawerToggle fDrawerToggle;
  private Toolbar fToolbar;
  private SwipeRefreshLayout fSwipeRefreshView;
  private RecyclerView fMovieListView;
  private MovieListAdapter fMovieListAdapter;
  private StaggeredGridLayoutManager fGridLayoutManager;
  private FloatingActionButton fToTopFab;
  private View fMovieEmpty;
  private List<Result> fCurrentMovieList;

  public FavoriteMovieFragment() {
    // Required empty public constructor
  }

  @Override
  public void onSaveInstanceState(@NonNull final Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(ADAPTER_STATE, fGridLayoutManager.onSaveInstanceState());
    fCurrentMovieList = fMovieListAdapter.getMovieList();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
    setHasOptionsMenu(true);
    setRetainInstance(true);

    fToolbar = view.findViewById(R.id.toolbar);
    fDrawerLayout = view.findViewById(R.id.drawer_layout);
    fNavigationView = view.findViewById(R.id.nav_view);
    fMovieEmpty = view.findViewById(R.id.movieEmpty);
    fSwipeRefreshView = view.findViewById(R.id.swipeListView);
    fMovieListView = view.findViewById(R.id.movieList);
    fToTopFab = view.findViewById(R.id.fab);
    fToTopFab.hide();

    fNavigationView.setItemIconTintList(null);
    fNavigationView.setNavigationItemSelectedListener(this);

    if (getActivity() != null) {
      MoviesActivity activity = (MoviesActivity) getActivity();
      activity.setSupportActionBar(fToolbar);
      fToolbar.setTitle(getString(R.string.menu_favorite));
    }
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    if (getResources().getBoolean(R.bool.is_landscape)) {
      fGridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
    } else {
      fGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }
    fMovieListAdapter = new MovieListAdapter(getContext());

    if (savedInstanceState != null) {
      final Parcelable adapterState = savedInstanceState.getParcelable(ADAPTER_STATE);
      fGridLayoutManager.onRestoreInstanceState(adapterState);
    } else {
      initLoader();
    }

    if (fCurrentMovieList != null) {
      fMovieListAdapter.setMovieList(fCurrentMovieList);
      onShowMovie();
    }

    fMovieListView.setHasFixedSize(true);
    fMovieListView.setLayoutManager(fGridLayoutManager);
    fMovieListView.setAdapter(fMovieListAdapter);
    setupListener();
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
    MoviesActivity activity = (MoviesActivity) getActivity();
    if (activity != null) {

      int id = item.getItemId();
      switch (id) {
        case R.id.now_playing:
          activity.replaceFragment(NowPlayingMovieFragment.newInstance(), NowPlayingMovieFragment.class.getSimpleName());
          break;
        case R.id.upcoming:
          activity.replaceFragment(UpcomingMovieFragment.newInstance(), UpcomingMovieFragment.class.getSimpleName());
          break;
        case R.id.popular:
          activity.replaceFragment(PopularMovieFragment.newInstance(), PopularMovieFragment.class.getSimpleName());
          break;
        case R.id.top_rated:
          activity.replaceFragment(TopRatedMovieFragment.newInstance(), TopRatedMovieFragment.class.getSimpleName());
          break;
        case R.id.favorite:
          activity.replaceFragment(FavoriteMovieFragment.newInstance(), FavoriteMovieFragment.class.getSimpleName());
          break;
        case R.id.change_theme:
          activity.changeThemePref();
          break;
        case R.id.change_language:
          startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
          break;
      }

      if (getView() != null) {
        DrawerLayout drawer = getView().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
      }
      return true;
    }
    return false;
  }

  @Override
  public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
    if (getContext() == null) {
      return null;
    }
    return new CursorLoader(getContext(), CONTENT_URI, null, null, null, null);
  }

  @Override
  public void onResume() {
    super.onResume();
    resetLoader();
    fDrawerToggle = new ActionBarDrawerToggle(
        getActivity(), fDrawerLayout, fToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    fDrawerLayout.addDrawerListener(fDrawerToggle);
    fDrawerToggle.syncState();
  }

  @Override
  public void onPause() {
    super.onPause();
    fDrawerLayout.removeDrawerListener(fDrawerToggle);
  }

  @Override
  public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
    fMovieListAdapter.clear();
    fMovieListAdapter.setMovieList(toResult(data));
    fSwipeRefreshView.setRefreshing(false);

    if (fMovieListAdapter.getItemCount() == 0) {
      onEmptyMovie();
    } else {
      onShowMovie();
    }
    data.close();
  }

  @Override
  public void onLoaderReset(final Loader<Cursor> loader) {
    fMovieListAdapter.clear();
    fSwipeRefreshView.setRefreshing(false);
    onEmptyMovie();
  }

  private void initLoader() {
    if (getActivity() != null) {
      getActivity()
          .getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }
  }

  private void resetLoader() {
    if (getActivity() != null) {
      getActivity()
          .getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
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

  private void setupListener() {
    fSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        fToTopFab.hide();
        fSwipeRefreshView.setEnabled(false);
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
        Log.d(DetailMovieFragment.class.getSimpleName(), aResult.toString());
        if (getActivity() != null) {
          MoviesActivity activity = (MoviesActivity) getActivity();
          fCurrentMovieList = fMovieListAdapter.getMovieList();
          activity.showDetailFragment(
              DetailMovieFragment.newInstance(aResult),
              DetailMovieFragment.class.getSimpleName(),
              DetailMovieFragment.class.getSimpleName());
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
  }

  public List<Result> toResult(Cursor aCursor) {
    List<Result> resultList = new ArrayList<>();
    while (aCursor.moveToNext()) {
      final int dbId = DatabaseContract.getColumnInt(aCursor, _ID);
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
    return resultList;
  }

  public static FavoriteMovieFragment newInstance() {
    FavoriteMovieFragment fragment = new FavoriteMovieFragment();
    fragment.setArguments(new Bundle());
    return fragment;
  }
}
