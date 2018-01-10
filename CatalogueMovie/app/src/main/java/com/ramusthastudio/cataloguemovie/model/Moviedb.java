package com.ramusthastudio.cataloguemovie.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Moviedb {
  @SerializedName("page")
  private int page;
  @SerializedName("total_results")
  private int totalResults;
  @SerializedName("total_pages")
  private int totalPages;
  @SerializedName("results")
  private List<Result> results = null;

  public int getPage() { return page; }
  public int getTotalResults() { return totalResults; }
  public int getTotalPages() { return totalPages; }
  public List<Result> getResults() { return results; }

  @Override public String toString() {
    return "Search{" +
        "page=" + page +
        ", totalResults=" + totalResults +
        ", totalPages=" + totalPages +
        ", results=" + results +
        '}';
  }
}
