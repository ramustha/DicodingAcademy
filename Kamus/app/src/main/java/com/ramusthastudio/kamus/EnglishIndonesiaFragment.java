package com.ramusthastudio.kamus;

import com.ramusthastudio.kamus.model.Kamus;
import com.ramusthastudio.kamus.repository.RepositoryService;
import java.util.List;

public final class EnglishIndonesiaFragment extends AbstractFragment {
  public EnglishIndonesiaFragment() {}

  public static AbstractFragment instance() {
    return new EnglishIndonesiaFragment();
  }

  @Override List<Kamus> queryByWord(final String aQuery) {
    return RepositoryService.getEnglishIndonesiaByWord(aQuery);
  }
  @Override List<Kamus> queryByWordLike(final String aQuery) {
    return RepositoryService.getEnglishIndonesiaByWordLike(aQuery);
  }
}
