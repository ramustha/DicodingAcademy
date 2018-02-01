package com.ramusthastudio.cataloguemovie.fragment;

import android.os.Bundle;
import com.ramusthastudio.cataloguemovie.R;
import com.ramusthastudio.cataloguemovie.model.Result;

import static com.ramusthastudio.cataloguemovie.BuildConfig.LANGUAGE;
import static com.ramusthastudio.cataloguemovie.BuildConfig.SERVER_API;
import static com.ramusthastudio.cataloguemovie.BuildConfig.SERVER_URL;
import static com.ramusthastudio.cataloguemovie.fragment.DetailMovieFragment.NOTIF_PARAM;

public class NowPlayingMovieFragment extends AbstractMovieFragment {

  @Override
  protected int title() {
    return R.string.menu_now_playing;
  }

  @Override
  protected String sourcePath() {
    return SERVER_URL + "/movie/now_playing?api_key=" + SERVER_API
        + "&language=" + LANGUAGE;
  }

  public static AbstractMovieFragment newInstance() {
    return new NowPlayingMovieFragment();
  }

  public static AbstractMovieFragment newInstance(Result aResult) {
    return newInstance(aResult , false);
  }

  public static AbstractMovieFragment newInstance(Result aResult, boolean aIsFromNotif) {
    AbstractMovieFragment fragment = new NowPlayingMovieFragment();
    Bundle args = new Bundle();
    args.putBoolean(NOTIF_PARAM, aIsFromNotif);
    args.putSerializable(ARG_PARAM, aResult);
    fragment.setArguments(args);
    return fragment;
  }
}
