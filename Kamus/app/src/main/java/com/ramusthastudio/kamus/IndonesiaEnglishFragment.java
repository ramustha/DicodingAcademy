package com.ramusthastudio.kamus;

import com.ramusthastudio.kamus.model.Kamus;
import com.ramusthastudio.kamus.repository.RepositoryService;
import java.util.List;

public final class IndonesiaEnglishFragment extends AbstractFragment {
  public IndonesiaEnglishFragment() {}

  public static AbstractFragment instance() {
    return new IndonesiaEnglishFragment();
  }

  @Override List<Kamus> queryByWord(final String aQuery) {
    return RepositoryService.getIndonesiaEnglishByWord(aQuery);
  }
  @Override List<Kamus> queryByWordLike(final String aQuery) {
    return RepositoryService.getIndonesiaEnglishByWordLike(aQuery);
  }
}
