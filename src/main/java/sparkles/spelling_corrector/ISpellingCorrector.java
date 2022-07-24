package sparkles.spelling_corrector;

import java.util.List;

public interface ISpellingCorrector {
    void putWord(String word);
    String correct(String word);
    boolean containsWord(String word);

    List<String> wordEdits(String word);
}
