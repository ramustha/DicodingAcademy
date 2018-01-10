package com.ramusthastudio.cataloguemovie;

import android.accounts.NetworkErrorException;
import android.os.Bundle;
import android.util.Log;
import com.firebase.jobdispatcher.JobParameters;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.ramusthastudio.cataloguemovie.common.CustomGson;
import cz.msebera.android.httpclient.Header;

import static com.ramusthastudio.cataloguemovie.MovieService.EXTRA_JOB_SERVICE;

public final class Tasks<T> {
  private static final String TAG = Tasks.class.getSimpleName();
  private static final AsyncHttpClient sHttpClient = new AsyncHttpClient();
  private final TaskListener<T> fListener;

  public Tasks(TaskListener<T> aListener) {
    fListener = aListener;
  }

  interface TaskListener<T> {
    void onSuccess(T aResponse, JobParameters aJobParameters);
    void onFailure(int statusCode, Throwable aThrowable, JobParameters aJobParameters);
    Class<T> toClass();
  }

  public void start(String aUrl) {
    Log.d(TAG, "Running");

    sHttpClient.get(aUrl, new BaseJsonHttpResponseHandler<T>() {

      @Override
      public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, T response) {
        Log.d(TAG, "Moviedb " + response);

        if (response == null) {
          throw new NullPointerException("Moviedb cant be null");
        }

        if (fListener != null) {
          fListener.onSuccess(response, null);
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, T errorResponse) {
        Log.e(TAG, "onFailure ", throwable);
        fListener.onFailure(statusCode, throwable, null);
      }

      @Override
      protected T parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
        Log.d(TAG, "parseResponse " + rawJsonData + " isFailure " + isFailure);
        if (isFailure) {
          throw new NetworkErrorException("Failure");
        }
        return CustomGson.instance().fromJson(rawJsonData, fListener.toClass());
      }
    });
  }

  public void start(final JobParameters aJobParameters) {
    Log.d(TAG, "Running");

    Bundle extra = aJobParameters.getExtras();
    if (extra == null || extra.getString(EXTRA_JOB_SERVICE) == null) {
      fListener.onFailure(500, new NullPointerException("Extra cant be null"), aJobParameters);
      return;
    }

    sHttpClient.get(extra.getString(EXTRA_JOB_SERVICE), new BaseJsonHttpResponseHandler<T>() {

      @Override
      public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, T response) {
        Log.d(TAG, "Moviedb " + response);

        if (response == null) {
          throw new NullPointerException("Moviedb cant be null");
        }

        if (fListener != null) {
          fListener.onSuccess(response, aJobParameters);
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, T errorResponse) {
        Log.e(TAG, "onFailure ", throwable);
        fListener.onFailure(statusCode, throwable, aJobParameters);
      }

      @Override
      protected T parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
        Log.d(TAG, "parseResponse " + rawJsonData + " isFailure " + isFailure);
        return CustomGson.instance().fromJson(rawJsonData, fListener.toClass());
      }
    });
  }
}
