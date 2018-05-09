package edu.brown.cs.gk16.autocorrect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class AcObjectTest {

  @Test
  public void testGetSuggestionsPrefixOn() {
    // Creating new ac object and loading dictionary
    AcObject ac = new AcObject();
    ac.addCorpus("data/autocorrect/dictionary.txt");

    // Turn on prefix setting
    ac.setPrefix(true);

    // Get suggestions
    List<String> suggestions = ac.getSuggestions("serin");
    assert (suggestions.size() == 6);
    assert (suggestions.contains("serine"));
    assert (suggestions.contains("serines"));
    assert (suggestions.contains("sering"));
    assert (suggestions.contains("seringa"));
    assert (suggestions.contains("seringas"));
    assert (suggestions.contains("serins"));
  }

  @Test
  public void testGetSuggestionsLedOn() {
    // Creating new ac object and loading file with definitive suggestions
    AcObject ac = new AcObject();
    ac.addCorpus("data/autocorrect/same_frequencies.txt");

    // Turn on led setting
    ac.setLed(2);

    // Get suggestions
    List<String> suggestions = ac.getSuggestions("ba");
    assert (suggestions.size() == 3);
    assert (suggestions.contains("ab"));
    assert (suggestions.contains("back"));
    assert (suggestions.contains("eat"));
  }

  @Test
  public void testGetSuggestionsWhitespaceOn() {
    // Creating new ac object and loading dictionary
    AcObject ac = new AcObject();
    ac.addCorpus("data/autocorrect/dictionary.txt");

    // Turn on whitespace setting
    ac.setWhiteSpace(true);

    // Get suggestions
    List<String> suggestions = ac.getSuggestions("likewise");
    assert (suggestions.size() == 1);
    assert (suggestions.contains("like wise"));
  }

  @Test
  public void testGetSuggestionsMultipleSettingsOn() {
    // Creating new ac object and loading dictionary
    AcObject ac = new AcObject();
    ac.addCorpus("data/autocorrect/dictionary.txt");

    // Turn on whitespace setting
    ac.setWhiteSpace(true);

    // Turn on prefix setting
    ac.setPrefix(true);

    // Get suggestions
    List<String> suggestions = ac.getSuggestions("abode");
    assert (suggestions.size() == 4);
    assert (suggestions.contains("ab ode"));
    assert (suggestions.contains("abo de"));
    assert (suggestions.contains("abodes"));
    assert (suggestions.contains("aboded"));
  }

  @Test
  public void testGetSuggestionsNoSettingsOn() {
    // Creating new ac object and loading dictionary
    AcObject ac = new AcObject();
    ac.addCorpus("data/autocorrect/dictionary.txt");

    // Get suggestions
    List<String> suggestions = ac.getSuggestions("likewise");
    assert (suggestions != null);
    assert (suggestions.size() == 0);
  }

  @Test
  public void testSortByBigramAndUnigramFreqNormal() {
    // Creating new ac object and loading corpus with definitive ordered bigram
    // frequencies
    AcObject ac = new AcObject();
    ac.addCorpus("data/autocorrect/bigram_frequencies.txt");
    List<String> words = new ArrayList<String>();

    // Adding suggestions in random order
    words.add("milk");
    words.add("drink");
    words.add("dog");
    words.add("phone");
    words.add("barber");

    List<String> suggestions = ac.sortByBigramAndUnigramFreq(words, "the");
    assert (suggestions.size() == 5);
    assert (suggestions.get(0).equals("dog"));
    assert (suggestions.get(1).equals("milk"));
    assert (suggestions.get(2).equals("barber"));
    assert (suggestions.get(3).equals("drink"));
    assert (suggestions.get(4).equals("phone"));
  }

  @Test
  public void testSortByBigramAndUnigramFreqSameBigramFreq() {
    // Creating new ac object and loading corpus with equal bigram
    // frequencies
    AcObject ac = new AcObject();
    ac.addCorpus("data/autocorrect/bigram_same_frequencies.txt");
    List<String> words = new ArrayList<String>();

    // Adding suggestions in random order
    words.add("cat");
    words.add("drink");
    words.add("dog");
    words.add("ball");

    // Drink, cat, and ball should be ordered by unigram frequencies
    List<String> suggestions = ac.sortByBigramAndUnigramFreq(words, "her");
    assert (suggestions.size() == 4);
    assert (suggestions.get(0).equals("dog"));
    assert (suggestions.get(1).equals("ball"));
    assert (suggestions.get(2).equals("drink"));
    assert (suggestions.get(3).equals("cat"));
  }

  @Test
  public void testSortByBigramAndUnigramFreqSameUnigramFreq() {
    // Creating new ac object and loading corpus with equal bigram
    // frequencies
    AcObject ac = new AcObject();
    ac.addCorpus("data/autocorrect/unigram_same_frequencies.txt");
    List<String> words = new ArrayList<String>();

    // Adding suggestions in random order
    words.add("cat");
    words.add("drink");
    words.add("dog");
    words.add("ball");
    words.add("sword");

    // Drink, cat, and ball should be ordered alphabetically
    List<String> suggestions = ac.sortByBigramAndUnigramFreq(words, "her");
    assert (suggestions.size() == 5);
    assert (suggestions.get(0).equals("dog"));
    assert (suggestions.get(1).equals("sword"));
    assert (suggestions.get(2).equals("ball"));
    assert (suggestions.get(3).equals("cat"));
    assert (suggestions.get(4).equals("drink"));
  }

  @Test
  public void testSortByBigramAndUnigramFreqNoSuggestions() {
    // Creating new ac object and loading corpus with bigram frequencies
    AcObject ac = new AcObject();
    ac.addCorpus("data/autocorrect/bigram_frequencies.txt");
    List<String> words = new ArrayList<String>();

    // Drink, cat, and ball should be ordered by unigram frequencies
    List<String> suggestions = ac.sortByBigramAndUnigramFreq(words, "the");
    assert (suggestions != null);
    assert (suggestions.size() == 0);
  }

  @Test
  public void testSortByUnigramFreqNormal() {
    // Creating new ac object and loading corpus with definitive ordered unigram
    // frequencies
    AcObject ac = new AcObject();
    ac.addCorpus("data/autocorrect/word_frequencies.txt");
    List<String> unigrams = new ArrayList<String>();

    // Adding unigrams in random order
    unigrams.add("aahed");
    unigrams.add("aal");
    unigrams.add("aa");
    unigrams.add("aahs");
    unigrams.add("aah");
    unigrams.add("aahing");

    List<String> suggestions = ac.sortByUnigramFreq(unigrams);
    assert (suggestions.size() == 6);
    assert (suggestions.get(0).equals("aa"));
    assert (suggestions.get(1).equals("aah"));
    assert (suggestions.get(2).equals("aahed"));
    assert (suggestions.get(3).equals("aahing"));
    assert (suggestions.get(4).equals("aahs"));
    assert (suggestions.get(5).equals("aal"));
  }

  @Test
  public void testSortByUnigramFreqSameFreq() {
    // Creating new ac object and loading corpus with equal unigram
    // frequencies
    AcObject ac = new AcObject();
    ac.addCorpus("data/autocorrect/same_frequencies.txt");
    List<String> unigrams = new ArrayList<String>();

    // Adding unigrams in random order
    unigrams.add("cone");
    unigrams.add("eat");
    unigrams.add("dive");
    unigrams.add("ab");
    unigrams.add("back");

    List<String> suggestions = ac.sortByUnigramFreq(unigrams);
    assert (suggestions.size() == 5);
    assert (suggestions.get(0).equals("eat"));
    assert (suggestions.get(1).equals("ab"));
    assert (suggestions.get(2).equals("back"));
    assert (suggestions.get(3).equals("cone"));
    assert (suggestions.get(4).equals("dive"));
  }

  @Test
  public void testSortByUnigramFreqNoSuggestions() {
    // Creating new ac object and loading corpus with equal unigram
    // frequencies
    AcObject ac = new AcObject();
    ac.addCorpus("data/autocorrect/word_frequencies.txt");
    List<String> unigrams = new ArrayList<String>();

    List<String> suggestions = ac.sortByUnigramFreq(unigrams);
    assert (suggestions != null);
    assert (suggestions.size() == 0);
  }

  @Test
  public void testGetLedWordsNormal() {
    // Creating new ac object and loading file with known led suggestions
    AcObject ac = new AcObject();
    ac.addCorpus("data/autocorrect/led_distances.txt");

    ac.setLed(2);

    HashMap<Integer, List<String>> suggestions = ac.getLedWords("ba");
    assert (suggestions.size() == 2);
    assert (suggestions.containsKey(2));
    assert (suggestions.containsKey(1));
    assert (suggestions.get(2).contains("ab"));
    assert (suggestions.get(2).contains("back"));
    assert (suggestions.get(2).contains("eat"));
    assert (suggestions.get(1).contains("bad"));
  }

  @Test
  public void testGetLedWordsNoChange() {
    // Creating new ac object and loading file with known led suggestions
    AcObject ac = new AcObject();
    ac.addCorpus("data/autocorrect/led_distances.txt");

    ac.setLed(1);

    HashMap<Integer, List<String>> suggestions = ac.getLedWords("bad");
    assert (suggestions.containsKey(0));
    assert (suggestions.get(0).contains("bad"));
  }

  @Test
  public void testGetLedWordsLedOff() {
    // Creating new ac object and loading file with known led suggestions
    AcObject ac = new AcObject();
    ac.addCorpus("data/autocorrect/led_distances.txt");

    HashMap<Integer, List<String>> suggestions = ac.getLedWords("ba");
    assert (suggestions != null);
    assert (suggestions.isEmpty());
  }

  @Test
  public void testSplitWordNormal() {
    // Creating new ac object and loading entire dictionary
    AcObject ac = new AcObject();
    ac.addCorpus("data/autocorrect/dictionary.txt");

    List<String> possSplits = ac.splitWord("watermelon");
    assert (possSplits.size() == 1);
    assert (possSplits.get(0).equals("water melon"));

    possSplits = ac.splitWord("abalone");
    assert (possSplits.size() == 2);
    assert (possSplits.get(0).equals("ab alone"));
    assert (possSplits.get(1).equals("aba lone"));
  }

  @Test
  public void testSplitWordNoSplit() {
    // Creating new ac object and loading entire dictionary
    AcObject ac = new AcObject();
    ac.addCorpus("data/autocorrect/dictionary.txt");

    List<String> possSplits = ac.splitWord("asdfoigajkawe");
    assert (possSplits.size() == 0);
  }

  @Test
  public void testSortByNGramFreq() {
    // Creating new ac object and loading entire dictionary
    AcObject ac = new AcObject();
    ac.setN(4);
    ac.addCorpus("data/autocorrect/ngram_frequencies.txt");

    List<String> suggestions = new ArrayList<String>();
    suggestions.add("pie");
    suggestions.add("food");
    suggestions.add("to");
    suggestions.add("that");

    List<String> prevWords = new ArrayList<String>();
    prevWords.add("love");
    prevWords.add("would");
    prevWords.add("i");

    List<String> rankedSuggestions = ac.sortByNGramFreq(suggestions, prevWords,
        4);
    assert (rankedSuggestions.size() == 4);
    assert (rankedSuggestions.get(0).equals("to"));
    assert (rankedSuggestions.get(1).equals("that"));
    assert (rankedSuggestions.get(2).equals("pie"));
    assert (rankedSuggestions.get(3).equals("food"));
  }

  @Test
  public void testSortByNGramFreqNoNGramsStored() {
    // Creating new ac object and loading entire dictionary
    AcObject ac = new AcObject();
    ac.addCorpus("data/autocorrect/ngram_frequencies.txt");
    ac.setN(4); // n set to 4 after loading corpus

    List<String> suggestions = new ArrayList<String>();
    suggestions.add("pie");
    suggestions.add("food");
    suggestions.add("to");
    suggestions.add("that");

    List<String> prevWords = new ArrayList<String>();
    prevWords.add("love");
    prevWords.add("would");
    prevWords.add("i");

    // Should default to regular bigram/unigram rankings
    List<String> rankedSuggestions = ac.sortByNGramFreq(suggestions, prevWords,
        4);
    assert (rankedSuggestions.size() == 4);
    assert (rankedSuggestions.get(0).equals("that"));
    assert (rankedSuggestions.get(1).equals("pie"));
    assert (rankedSuggestions.get(2).equals("to"));
    assert (rankedSuggestions.get(3).equals("food"));
  }

  @Test
  public void testSortByNGramFreqNoSuggestions() {
    // Creating new ac object and loading entire dictionary
    AcObject ac = new AcObject();
    ac.setN(4);
    ac.addCorpus("data/autocorrect/ngram_frequencies.txt");

    List<String> suggestions = new ArrayList<String>();

    List<String> prevWords = new ArrayList<String>();
    prevWords.add("love");
    prevWords.add("would");
    prevWords.add("i");

    List<String> rankedSuggestions = ac.sortByNGramFreq(suggestions, prevWords,
        4);
    assert (rankedSuggestions != null);
    assert (rankedSuggestions.size() == 0);
  }

}
