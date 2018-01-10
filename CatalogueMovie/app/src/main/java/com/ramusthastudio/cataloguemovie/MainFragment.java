package com.ramusthastudio.cataloguemovie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.firebase.jobdispatcher.JobParameters;
import com.ramusthastudio.cataloguemovie.common.EndlessRecyclerViewScrollListener;
import com.ramusthastudio.cataloguemovie.common.TextFilter;
import com.ramusthastudio.cataloguemovie.model.Moviedb;
import com.ramusthastudio.cataloguemovie.model.Result;

import static com.ramusthastudio.cataloguemovie.BuildConfig.LANGUAGE;
import static com.ramusthastudio.cataloguemovie.BuildConfig.SERVER_API;
import static com.ramusthastudio.cataloguemovie.BuildConfig.SERVER_URL;

public class MainFragment extends Fragment implements View.OnClickListener, Tasks.TaskListener<Moviedb> {
  private static final String ARG_PARAM = "param";
  private EditText fSearchText;
  private Button fSearchBtn;
  private SwipeRefreshLayout fSwipeRefreshView;
  private RecyclerView fMovieListView;
  private MovieListAdapter fMovieListAdapter;
  private GridLayoutManager fGridLayoutManager;
  private EndlessRecyclerViewScrollListener fScrollListener;
  private Tasks<Moviedb> fTasks;

  public MainFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      String param = getArguments().getString(ARG_PARAM);
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_main, container, false);
    fGridLayoutManager = new GridLayoutManager(getContext(), 2);

    fSearchText = view.findViewById(R.id.searchText);
    fSearchBtn = view.findViewById(R.id.searchBtn);
    fSwipeRefreshView = view.findViewById(R.id.swipeListView);
    fMovieListView = view.findViewById(R.id.movieList);

    fSearchBtn.setOnClickListener(this);
    fSearchText.getText().setFilters(new InputFilter[] {new TextFilter()});

    fMovieListView.setHasFixedSize(true);
    fMovieListView.setLayoutManager(fGridLayoutManager);

    fMovieListAdapter = new MovieListAdapter(getContext());
    fMovieListView.setAdapter(fMovieListAdapter);

    fTasks = new Tasks<>(this);
    fTasks.start(SERVER_URL + "/discover/movie?api_key=" + SERVER_API + "&language=" + LANGUAGE + "&sort_by=popularity.desc");
    fSwipeRefreshView.setRefreshing(true);

    fSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
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
    });
    return view;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.searchBtn:
        Editable searchText = fSearchText.getText();
        Log.d(MainFragment.class.getSimpleName(), searchText.toString());

        fTasks.start(SERVER_URL + "/search/movie?api_key=" + SERVER_API + "&language=" + LANGUAGE + "&sort_by=popularity.desc&query=" + searchText.toString());
        fSwipeRefreshView.setRefreshing(true);
        fMovieListAdapter.clear();
        fScrollListener.resetState();
        fScrollListener.setEnabled(false);
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
    fSwipeRefreshView.setRefreshing(false);
  }

  @Override public Class<Moviedb> toClass() { return Moviedb.class; }

  public static MainFragment newInstance(String param1) {
    MainFragment fragment = new MainFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM, param1);
    fragment.setArguments(args);
    return fragment;
  }
}
