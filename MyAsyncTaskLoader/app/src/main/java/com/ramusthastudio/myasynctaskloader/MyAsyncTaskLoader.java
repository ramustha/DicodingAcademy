package com.ramusthastudio.myasynctaskloader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.util.MalformedJsonException;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.ramusthastudio.myasynctaskloader.model.Result;
import cz.msebera.android.httpclient.Header;

import static com.ramusthastudio.myasynctaskloader.BuildConfig.SERVER_API;
import static com.ramusthastudio.myasynctaskloader.BuildConfig.SERVER_URL;

public class MyAsyncTaskLoader extends AsyncTaskLoader<Result> {
  private static final String TAG = "GetWeather";
  private static final String DEFAULT = "1642911,1650357,1627896";
  private static final String URL = SERVER_URL + "?id=" + DEFAULT + "&appid=" + SERVER_API;

  private static SyncHttpClient sHttpClient = new SyncHttpClient();

  private Result fResult;
  private boolean fHasResult = false;

  public MyAsyncTaskLoader(Context context) {
    super(context);
    onContentChanged();
  }

  @Override
  protected void onStartLoading() {
    if (takeContentChanged()) {
      forceLoad();
    } else if (fHasResult) {
      deliverResult(fResult);
    }
  }

  @Override
  public void deliverResult(final Result aResult) {
    fResult = aResult;
    fHasResult = true;
    super.deliverResult(aResult);
  }

  @Override
  protected void onReset() {
    super.onReset();
    onStopLoading();
    if (fHasResult) {
      onReleaseResources(fResult);
      fResult = null;
      fHasResult = false;
    }
  }

  @Override
  public Result loadInBackground() {
    return getCurrentWeather();
  }

  protected void onReleaseResources(Result aResult) { /*DO NOTHING*/ }

  private Result getCurrentWeather() {
    Log.d(TAG, "Running, getCurrentWeather: " + URL);

    sHttpClient.get(URL, new BaseJsonHttpResponseHandler<Result>() {
      @Override public void onStart() {
        super.onStart();
        setUseSynchronousMode(true);
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Result response) {
        Log.d(TAG, "Result " + response);

        if (response == null) {
          throw new NullPointerException("Weater cant be null");
        }

        fResult = response;
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Result errorResponse) {
        Log.e(TAG, "onFailure ", throwable);
      }

      @Override
      protected Result parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
        Log.d(TAG, "parseResponse " + rawJsonData + " isFailure " + isFailure);
        if (isFailure) {
          throw new MalformedJsonException("Error parse OpenWeather format");
        }
        return new Gson().fromJson(rawJsonData, Result.class);
      }
    });

    return fResult;
  }
}
