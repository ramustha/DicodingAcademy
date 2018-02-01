package com.ramusthastudio.cataloguemovie.model;

import android.graphics.Bitmap;
import java.io.Serializable;
import java.util.Date;

public class Widget implements Serializable {
  private int id;
  private String title;
  private double voteAverage;
  private Bitmap backdropPath;
  private Date releaseDate;

  public Widget(final int aId,
      final String aTitle,
      final double aVoteAverage,
      final Bitmap aBackdropPath,
      final Date aReleaseDate) {
    id = aId;
    title = aTitle;
    voteAverage = aVoteAverage;
    backdropPath = aBackdropPath;
    releaseDate = aReleaseDate;
  }

  public int getId() { return id; }
  public String getTitle() { return title; }
  public double getVoteAverage() { return voteAverage; }
  public Bitmap getBackdropPath() { return backdropPath; }
  public Date getReleaseDate() { return releaseDate; }
}
