package searchEngine;
import java.io.IOException;

public interface ISpellCorrector {
	void useDictionary(String dictionaryFileName) throws IOException;
	String suggestSimilarWord(String inputWord);
}
