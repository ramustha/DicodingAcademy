package com.ramusthastudio.cataloguemovie.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Result implements Serializable {
  @SerializedName("vote_count")
  private int voteCount;
  @SerializedName("id")
  private int id;
  @SerializedName("video")
  private boolean video;
  @SerializedName("vote_average")
  private double voteAverage;
  @SerializedName("title")
  private String title;
  @SerializedName("popularity")
  private float popularity;
  @SerializedName("poster_path")
  private String posterPath;
  @SerializedName("original_language")
  private String originalLanguage;
  @SerializedName("original_title")
  private String originalTitle;
  @SerializedName("genre_ids")
  private List<Integer> genreIds = null;
  @SerializedName("backdrop_path")
  private String backdropPath;
  @SerializedName("adult")
  private boolean adult;
  @SerializedName("overview")
  private String overview;
  @SerializedName("release_date")
  private Date releaseDate;

  public Result(
      final int aId,
      final String aTitle,
      final String aPoster,
      final String aBackdrop,
      final Date aReleaseDate,
      final List<Integer> aGenreIds,
      final double aRating,
      final String aOverview,
      final float aPopularity) {
    id = aId;
    title = aTitle;
    posterPath = aPoster;
    backdropPath = aBackdrop;
    releaseDate = aReleaseDate;
    genreIds = aGenreIds;
    voteAverage = aRating;
    overview = aOverview;
    popularity = aPopularity;
  }

  public int getVoteCount() { return voteCount; }
  public int getId() { return id; }
  public boolean isVideo() { return video; }
  public double getVoteAverage() { return voteAverage; }
  public String getTitle() { return title; }
  public float getPopularity() { return popularity; }
  public String getPosterPath() { return posterPath; }
  public String getOriginalLanguage() { return originalLanguage; }
  public String getOriginalTitle() { return originalTitle; }
  public List<Integer> getGenreIds() { return genreIds; }
  public String getBackdropPath() { return backdropPath; }
  public boolean isAdult() { return adult; }
  public String getOverview() { return overview; }
  public Date getReleaseDate() { return releaseDate; }

  @Override public String toString() {
    return "Result{" +
        "voteCount=" + voteCount +
        ", id=" + id +
        ", video=" + video +
        ", voteAverage=" + voteAverage +
        ", title='" + title + '\'' +
        ", popularity=" + popularity +
        ", posterPath='" + posterPath + '\'' +
        ", originalLanguage='" + originalLanguage + '\'' +
        ", originalTitle='" + originalTitle + '\'' +
        ", genreIds=" + genreIds +
        ", backdropPath='" + backdropPath + '\'' +
        ", adult=" + adult +
        ", overview='" + overview + '\'' +
        ", releaseDate='" + releaseDate + '\'' +
        '}';
  }
}
