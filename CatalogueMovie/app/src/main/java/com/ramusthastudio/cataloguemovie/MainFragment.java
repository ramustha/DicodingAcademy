package com.ramusthastudio.cataloguemovie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.firebase.jobdispatcher.JobParameters;
import com.ramusthastudio.cataloguemovie.common.EndlessRecyclerViewScrollListener;
import com.ramusthastudio.cataloguemovie.common.TextFilter;
import com.ramusthastudio.cataloguemovie.model.Moviedb;
import com.ramusthastudio.cataloguemovie.model.Result;
import java.util.ArrayList;

import static com.ramusthastudio.cataloguemovie.BuildConfig.LANGUAGE;
import static com.ramusthastudio.cataloguemovie.BuildConfig.SERVER_API;
import static com.ramusthastudio.cataloguemovie.BuildConfig.SERVER_URL;
import static com.ramusthastudio.cataloguemovie.MovieListAdapter.sDateFormat;

public class MainFragment extends Fragment implements View.OnClickListener, Tasks.TaskListener<Moviedb> {
  public static final String ARG_PARAM = "result";
  private EditText fSearchText;
  private Button fSearchBtn;
  private SwipeRefreshLayout fSwipeRefreshView;
  private RecyclerView fMovieListView;
  private MovieListAdapter fMovieListAdapter;
  private StaggeredGridLayoutManager fGridLayoutManager;
  private EndlessRecyclerViewScrollListener fScrollListener;
  private Tasks<Moviedb> fTasks;

  public MainFragment() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(final Context context) {
    super.onAttach(context);
    if (getArguments() != null) {
      Result result = (Result) getArguments().getSerializable(ARG_PARAM);
      if (result != null) {
        Log.d(DetailFragment.class.getSimpleName(), result.toString());

        DetailFragment fragment = new DetailFragment();
        fragment.setResult(result);
        FragmentManager fragmentManager = getChildFragmentManager();
        fragment.show(fragmentManager, DetailFragment.class.getSimpleName());
      }
    }

    fMovieListAdapter = new MovieListAdapter(context);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_main, container, false);
    fGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

    fSearchText = view.findViewById(R.id.searchText);
    fSearchBtn = view.findViewById(R.id.searchBtn);
    fSwipeRefreshView = view.findViewById(R.id.swipeListView);
    fMovieListView = view.findViewById(R.id.movieList);

    fSearchBtn.setOnClickListener(this);
    fSearchText.getText().setFilters(new InputFilter[] {new TextFilter()});

    fMovieListView.setHasFixedSize(true);
    fMovieListView.setLayoutManager(fGridLayoutManager);

    fMovieListView.setAdapter(fMovieListAdapter);

    fTasks = new Tasks<>(this);
    fTasks.start(SERVER_URL + "/discover/movie?api_key=" + SERVER_API + "&language=" + LANGUAGE + "&sort_by=popularity.desc");
    fSwipeRefreshView.setRefreshing(true);

    fSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        fSearchText.setText(null);
        fMovieListAdapter.clear();
        fScrollListener.resetState();
        fScrollListener.setEnabled(true);
        fTasks.start(SERVER_URL + "/discover/movie?api_key=" + SERVER_API + "&language=" + LANGUAGE + "&sort_by=popularity.desc");
      }
    });

    fScrollListener = new EndlessRecyclerViewScrollListener(fGridLayoutManager) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        Log.d(MainFragment.class.getSimpleName(), String.format("page %s, total items count %s", page, totalItemsCount));
        fTasks.start(SERVER_URL + "/discover/movie?api_key=" + SERVER_API + "&language=" + LANGUAGE + "&sort_by=popularity.desc&page=" + page);
      }
    };
    fMovieListView.addOnScrollListener(fScrollListener);

    fMovieListAdapter.setOnClickListener(new MovieListAdapter.AdapterListener() {
      @Override
      public void onClick(Result aResult) {
        Log.d(DetailFragment.class.getSimpleName(), aResult.toString());

        DetailFragment fragment = new DetailFragment();
        fragment.setResult(aResult);
        FragmentManager fragmentManager = getChildFragmentManager();
        fragment.show(fragmentManager, DetailFragment.class.getSimpleName());
      }

      @Override
      public void onClickShare(final Result aResult) {
        Log.d("MainFragment", "Shared");
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
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.searchBtn:
        Editable searchText = fSearchText.getText();
        if (searchText.toString().isEmpty()) {
          if (getContext() != null) {
            fSearchText.setError(getContext().getString(R.string.blank_field_search));
          }
        } else {
          Log.d(MainFragment.class.getSimpleName(), searchText.toString());

          fTasks.start(SERVER_URL + "/search/movie?api_key=" + SERVER_API + "&language=" + LANGUAGE + "&sort_by=popularity.desc&query=" + searchText.toString());
          fSwipeRefreshView.setRefreshing(true);
          fMovieListAdapter.clear();
          fScrollListener.resetState();
          fScrollListener.setEnabled(false);
        }
        break;
    }
  }

  @Override
  public void onSuccess(Moviedb aResponse, JobParameters aJobParameters) {
    Log.d(MainFragment.class.getSimpleName(), aResponse.toString());
    fMovieListAdapter.setMovieList(aResponse.getResults());
    fSwipeRefreshView.setRefreshing(false);
  }

  @Override
  public void onFailure(int statusCode, Throwable aThrowable, JobParameters aJobParameters) {
    Log.d(MainFragment.class.getSimpleName(), String.format("status %s, couse %s", statusCode, aThrowable));
    fMovieListAdapter.setMovieList(new ArrayList<Result>());
    fSwipeRefreshView.setRefreshing(false);
  }

  @Override public Class<Moviedb> toClass() { return Moviedb.class; }

  public static MainFragment newInstance() {
    MainFragment fragment = new MainFragment();
    fragment.setArguments(new Bundle());
    return fragment;
  }

  public static MainFragment newInstance(Result aResult) {
    MainFragment fragment = new MainFragment();
    Bundle args = new Bundle();
    args.putSerializable(ARG_PARAM, aResult);
    fragment.setArguments(args);
    return fragment;
  }
}
