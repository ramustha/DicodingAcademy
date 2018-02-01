package com.ramusthastudio.cataloguemovie.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.firebase.jobdispatcher.JobParameters;
import com.ramusthastudio.cataloguemovie.MoviesActivity;
import com.ramusthastudio.cataloguemovie.R;
import com.ramusthastudio.cataloguemovie.common.EndlessRecyclerViewScrollListener;
import com.ramusthastudio.cataloguemovie.model.Moviedb;
import com.ramusthastudio.cataloguemovie.model.Result;
import com.ramusthastudio.cataloguemovie.service.MovieListAdapter;
import com.ramusthastudio.cataloguemovie.service.Tasks;
import java.util.List;

import static com.ramusthastudio.cataloguemovie.BuildConfig.LANGUAGE;
import static com.ramusthastudio.cataloguemovie.BuildConfig.SERVER_API;
import static com.ramusthastudio.cataloguemovie.BuildConfig.SERVER_URL;
import static com.ramusthastudio.cataloguemovie.fragment.DetailMovieFragment.NOTIF_PARAM;
import static com.ramusthastudio.cataloguemovie.service.MovieListAdapter.sDateFormat;

public abstract class AbstractMovieFragment extends Fragment
    implements NavigationView.OnNavigationItemSelectedListener, Tasks.TaskListener<Moviedb> {
  public static final String ARG_PARAM = "result";
  private static final String ADAPTER_STATE = "adapter_state";
  private DrawerLayout fDrawerLayout;
  private NavigationView fNavigationView;
  private ActionBarDrawerToggle fDrawerToggle;
  private Toolbar fToolbar;
  private SwipeRefreshLayout fSwipeRefreshView;
  private RecyclerView fMovieListView;
  private MovieListAdapter fMovieListAdapter;
  private StaggeredGridLayoutManager fGridLayoutManager;
  private EndlessRecyclerViewScrollListener fScrollListener;
  private Tasks<Moviedb> fTasks;
  private FloatingActionButton fToTopFab;
  private View fMovieEmpty;
  private List<Result> fCurrentMovieList;
  private String fPathUrl;
  private int fCurrentPage = 0;

  public AbstractMovieFragment() {
    // Required empty public constructor
  }

  @Override public Class<Moviedb> toClass() { return Moviedb.class; }

  @Override
  public void onAttach(final Context context) {
    super.onAttach(context);
    fPathUrl = sourcePath();
  }

  @Override
  public void onSaveInstanceState(@NonNull final Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(ADAPTER_STATE, fGridLayoutManager.onSaveInstanceState());
    fCurrentMovieList = fMovieListAdapter.getMovieList();
    fCurrentPage = fScrollListener.getCurrentPage();
  }

  @Override
  public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
      fToolbar.setTitle(title());
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
      if (fTasks == null) {
        fTasks = new Tasks<>(this);
        fTasks.start(fPathUrl);
      }
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
  public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
    inflater.inflate(R.menu.general_search, menu);

    final SearchView searchActionMenu = (SearchView) menu.findItem(R.id.menu_search).getActionView();
    final LinearLayout ln = searchActionMenu.findViewById(R.id.search_edit_frame);
    ((LinearLayout.LayoutParams) ln.getLayoutParams()).leftMargin = 0;
    searchActionMenu.setQueryHint(getString(R.string.menu_search_movie_hint));
    searchActionMenu.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(final String query) {
        Log.d(AbstractMovieFragment.class.getSimpleName(), query);
        fTasks.start(SERVER_URL + "/search/movie?api_key=" + SERVER_API + "&language=" + LANGUAGE + "&sort_by=popularity.desc&query=" + query);

        fMovieListAdapter.clear();
        fScrollListener.resetState();
        fScrollListener.setEnabled(false);

        return query != null;
      }
      @Override public boolean onQueryTextChange(final String newText) { return false; }
    });
    searchActionMenu.setOnCloseListener(new SearchView.OnCloseListener() {
      @Override
      public boolean onClose() {
        initAdapter();
        return false;
      }
    });
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
  public void onResume() {
    super.onResume();
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
  public void onStartTask() {
    fSwipeRefreshView.setRefreshing(true);
  }

  @Override
  public void onSuccess(Moviedb aResponse, JobParameters aJobParameters) {
    Log.d(AbstractMovieFragment.class.getSimpleName(), aResponse.toString());
    fMovieListAdapter.setMovieList(aResponse.getResults());
    fSwipeRefreshView.setRefreshing(false);

    if (aResponse.getResults() == null || aResponse.getResults().isEmpty()) {
      onEmptyMovie();
    } else {
      onShowMovie();
    }

    fromNotif();
  }

  @Override
  public void onFailure(int statusCode, Throwable aThrowable, JobParameters aJobParameters) {
    Log.d(AbstractMovieFragment.class.getSimpleName(), String.format("status %s, couse %s", statusCode, aThrowable));
    fMovieListAdapter.setMovieList(null);
    fSwipeRefreshView.setRefreshing(false);
    onEmptyMovie();
  }

  private void fromNotif() {
    if (getArguments() != null) {
      final boolean isNotif = getArguments().getBoolean(NOTIF_PARAM);
      if (isNotif) {
        if (getActivity() != null) {
          MoviesActivity activity = (MoviesActivity) getActivity();
          final Result resultFromNotif = (Result) getArguments().getSerializable(ARG_PARAM);
          fCurrentMovieList = fMovieListAdapter.getMovieList();

          activity.showDetailFragment(
              DetailMovieFragment.newInstance(resultFromNotif),
              DetailMovieFragment.class.getSimpleName());
          getArguments().clear();
        }
      }
    }
  }

  private void setupListener() {
    fSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        initAdapter();
      }
    });

    fToTopFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        fToTopFab.hide();
        fMovieListView.smoothScrollToPosition(0);
      }
    });

    fScrollListener = new EndlessRecyclerViewScrollListener(fGridLayoutManager, fCurrentPage) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        Log.d(AbstractMovieFragment.class.getSimpleName(), String.format("page %s, total items count %s", page, totalItemsCount));
        fTasks.start(fPathUrl + "&page=" + page);
      }
      @Override public void onScrollYGreaterThanZero() { fToTopFab.show(); }
      @Override public void onScrollYLessThanZero() { fToTopFab.hide(); }

    };
    fMovieListView.addOnScrollListener(fScrollListener);

    fMovieListAdapter.setOnClickListener(new MovieListAdapter.AdapterListener() {
      @Override
      public void onClick(Result aResult) {
        Log.d(DetailMovieFragment.class.getSimpleName(), aResult.toString());
        if (getActivity() != null) {
          MoviesActivity activity = (MoviesActivity) getActivity();
          fCurrentMovieList = fMovieListAdapter.getMovieList();
          activity.showDetailFragment(
              DetailMovieFragment.newInstance(aResult),
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

  private void initAdapter() {
    fToTopFab.hide();
    fMovieListAdapter.clear();
    fScrollListener.resetState();
    fScrollListener.setEnabled(true);
    fTasks.start(fPathUrl);
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

  protected abstract int title();
  protected abstract String sourcePath();
}
