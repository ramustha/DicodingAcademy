package com.ramusthastudio.cataloguemovie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ramusthastudio.cataloguemovie.model.Result;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailFragment extends DialogFragment {
  private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("E, dd-MM-yyyy", Locale.getDefault());
  private ImageView fItemImageView;
  private TextView fItemTitleView;
  private TextView fItemDateView;
  private TextView fItemGenreView;
  private TextView fItemRatingView;
  private TextView fItemDescView;
  private Result fResult;
  private Button fCloseBtn;

  public DetailFragment() {
    // Required empty public constructor
  }

  public void setResult(Result aResult) {
    fResult = aResult;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (fResult != null) {
      getDialog().setTitle(fResult.getTitle());
    }
  }
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_detail, container, false);

    fItemImageView = view.findViewById(R.id.detailImage);
    fItemTitleView = view.findViewById(R.id.detailTitle);
    fItemDateView = view.findViewById(R.id.detailDate);
    fItemGenreView = view.findViewById(R.id.detailGenre);
    fItemRatingView = view.findViewById(R.id.detailRating);
    fItemDescView = view.findViewById(R.id.detailDesc);
    fCloseBtn = view.findViewById(R.id.closeBtn);

    if (fResult != null) {
      if (getContext() != null) {
        Glide
            .with(getContext())
            .load(BuildConfig.IMAGE_URL + "/w342" + fResult.getBackdropPath())
            .into(fItemImageView)
            .clearOnDetach();

        fItemTitleView.setText(fResult.getTitle());

        if (fResult.getReleaseDate() != null) {
          fItemDateView.setText(sDateFormat.format(fResult.getReleaseDate()));
        } else {
          fItemDateView.setText("Unknown");
        }

        StringBuilder b = new StringBuilder();
        for (int genre : fResult.getGenreIds()) {
          b.append(Genre.displayName(genre)).append(" ");
        }
        fItemGenreView.setText(b.toString());
        fItemRatingView.setText(String.valueOf(fResult.getVoteAverage()));
        fItemDescView.setText(String.valueOf(fResult.getOverview()));
      }
    }

    fCloseBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getDialog().cancel();
      }
    });

    return view;
  }
}
