package com.ramusthastudio.cataloguemovie.fragment;

import android.os.Bundle;
import com.ramusthastudio.cataloguemovie.model.Result;

import static com.ramusthastudio.cataloguemovie.BuildConfig.LANGUAGE;
import static com.ramusthastudio.cataloguemovie.BuildConfig.SERVER_API;
import static com.ramusthastudio.cataloguemovie.BuildConfig.SERVER_URL;

public class NowPlayingMovieFragment extends AbstractMovieFragment {

  @Override
  protected String pathSource() {
    return SERVER_URL + "/movie/now_playing?api_key=" + SERVER_API
        + "&language=" + LANGUAGE;
  }

  public static AbstractMovieFragment newInstance() {
    AbstractMovieFragment fragment = new NowPlayingMovieFragment();
    fragment.setArguments(new Bundle());
    return fragment;
  }

  public static AbstractMovieFragment newInstance(Result aResult) {
    AbstractMovieFragment fragment = new NowPlayingMovieFragment();
    Bundle args = new Bundle();
    args.putSerializable(ARG_PARAM, aResult);
    fragment.setArguments(args);
    return fragment;
  }
}
