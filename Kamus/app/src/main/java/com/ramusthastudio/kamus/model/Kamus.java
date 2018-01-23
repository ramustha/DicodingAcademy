package com.ramusthastudio.kamus.model;

public class Kamus {
  private int id;
  private String word;
  private String description;

  public Kamus(final String aWord, final String aDescription) {
    word = aWord;
    description = aDescription;
  }

  public Kamus(final int aId, final String aWord, final String aDescription) {
    id = aId;
    word = aWord;
    description = aDescription;
  }

  public int getId() { return id; }
  public String getWord() { return word; }
  public String getDescription() { return description; }

  public Kamus setId(final int aId) {
    id = aId;
    return this;
  }
  public Kamus setWord(final String aWord) {
    word = aWord;
    return this;
  }
  public Kamus setDescription(final String aDescription) {
    description = aDescription;
    return this;
  }

  @Override
  public String toString() {
    return word;
  }
}
