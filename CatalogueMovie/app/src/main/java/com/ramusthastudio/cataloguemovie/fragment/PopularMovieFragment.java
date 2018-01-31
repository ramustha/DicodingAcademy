package com.ramusthastudio.cataloguemovie.fragment;

import android.os.Bundle;
import com.ramusthastudio.cataloguemovie.R;
import com.ramusthastudio.cataloguemovie.model.Result;

import static com.ramusthastudio.cataloguemovie.BuildConfig.LANGUAGE;
import static com.ramusthastudio.cataloguemovie.BuildConfig.SERVER_API;
import static com.ramusthastudio.cataloguemovie.BuildConfig.SERVER_URL;

public class PopularMovieFragment extends AbstractMovieFragment {

  @Override
  protected int title() {
    return R.string.menu_popular;
  }

  @Override
  protected String sourcePath() {
    return SERVER_URL + "/movie/popular?api_key=" + SERVER_API
        + "&language=" + LANGUAGE;
  }

  public static AbstractMovieFragment newInstance() {
    AbstractMovieFragment fragment = new PopularMovieFragment();
    fragment.setArguments(new Bundle());
    return fragment;
  }

  public static AbstractMovieFragment newInstance(Result aResult) {
    AbstractMovieFragment fragment = new PopularMovieFragment();
    Bundle args = new Bundle();
    args.putSerializable(ARG_PARAM, aResult);
    fragment.setArguments(args);
    return fragment;
  }
}
