package com.ramusthastudio.cataloguemovie.fragment;

import android.os.Bundle;
import com.ramusthastudio.cataloguemovie.model.Result;

import static com.ramusthastudio.cataloguemovie.BuildConfig.LANGUAGE;
import static com.ramusthastudio.cataloguemovie.BuildConfig.SERVER_API;
import static com.ramusthastudio.cataloguemovie.BuildConfig.SERVER_URL;

public class TopRatedMovieFragment extends AbstractMovieFragment {

  @Override
  protected String pathSource() {
    return SERVER_URL + "/movie/top_rated?api_key=" + SERVER_API
        + "&language=" + LANGUAGE;
  }

  public static AbstractMovieFragment newInstance() {
    AbstractMovieFragment fragment = new TopRatedMovieFragment();
    fragment.setArguments(new Bundle());
    return fragment;
  }

  public static AbstractMovieFragment newInstance(Result aResult) {
    AbstractMovieFragment fragment = new TopRatedMovieFragment();
    Bundle args = new Bundle();
    args.putSerializable(ARG_PARAM, aResult);
    fragment.setArguments(args);
    return fragment;
  }
}
