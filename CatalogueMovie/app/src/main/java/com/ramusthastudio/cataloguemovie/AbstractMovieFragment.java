package com.ramusthastudio.cataloguemovie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.firebase.jobdispatcher.JobParameters;
import com.ramusthastudio.cataloguemovie.common.EndlessRecyclerViewScrollListener;
import com.ramusthastudio.cataloguemovie.model.Moviedb;
import com.ramusthastudio.cataloguemovie.model.Result;
import java.util.ArrayList;

import static com.ramusthastudio.cataloguemovie.BuildConfig.LANGUAGE;
import static com.ramusthastudio.cataloguemovie.BuildConfig.SERVER_API;
import static com.ramusthastudio.cataloguemovie.BuildConfig.SERVER_URL;
import static com.ramusthastudio.cataloguemovie.MovieListAdapter.sDateFormat;

public abstract class AbstractMovieFragment extends Fragment implements Tasks.TaskListener<Moviedb> {
  protected static final String ARG_PARAM = "result";
  private SwipeRefreshLayout fSwipeRefreshView;
  private RecyclerView fMovieListView;
  private MovieListAdapter fMovieListAdapter;
  private StaggeredGridLayoutManager fGridLayoutManager;
  private EndlessRecyclerViewScrollListener fScrollListener;
  private Tasks<Moviedb> fTasks;
  private FloatingActionButton fToTopFab;
  private View fMovieEmpty;
  private String fPathUrl;

  public AbstractMovieFragment() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(final Context context) {
    super.onAttach(context);
    setHasOptionsMenu(true);

    if (getArguments() != null) {
      Result result = (Result) getArguments().getSerializable(ARG_PARAM);
      if (result != null) {
        Log.d(DetailFragment.class.getSimpleName(), result.toString());

        if (getActivity() != null) {
          Intent intent = new Intent(getActivity(), DetailActivity.class);
          intent.putExtra(ARG_PARAM, result);
          startActivity(intent);
        }
      }
    }

    fPathUrl = pathSource();
    fMovieListAdapter = new MovieListAdapter(context);
  }

  @Override
  public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

    fTasks = new Tasks<>(this);
    fTasks.start(fPathUrl);

    fSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        fToTopFab.hide();
        fMovieListAdapter.clear();
        fScrollListener.resetState();
        fScrollListener.setEnabled(true);
        fTasks.start(fPathUrl);
      }
    });

    fToTopFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        fToTopFab.hide();
        fMovieListView.smoothScrollToPosition(0);
      }
    });

    fScrollListener = new EndlessRecyclerViewScrollListener(fGridLayoutManager) {
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
    return view;
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
  }

  @Override
  public void onFailure(int statusCode, Throwable aThrowable, JobParameters aJobParameters) {
    Log.d(AbstractMovieFragment.class.getSimpleName(), String.format("status %s, couse %s", statusCode, aThrowable));
    fMovieListAdapter.setMovieList(new ArrayList<Result>());
    fSwipeRefreshView.setRefreshing(false);
    onEmptyMovie();
  }

  @Override public Class<Moviedb> toClass() { return Moviedb.class; }

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

  protected abstract String pathSource();
}
