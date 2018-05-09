package edu.brown.cs.gk16.autocorrect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.gk16.Interpreter;
import edu.brown.cs.gk16.Reader;
import edu.brown.cs.gk16.Trie;
import edu.brown.cs.gk16.TrieNode;

/**
 * This class is the representation of the autocorrect object. That means it
 * stores all the corpus information in a trie and all the autocorrect settings
 * and handles the autocorrect algorithms with the trie.
 */
public class AcObject {

  private ArrayList<String> filesUsed;
  private Trie trie;
  private HashMap<String, Integer> frequencies;
  private HashMap<String, Integer> bigramFreq;
  private HashMap<Integer, HashMap<String, Integer>> nGramFreq;
  private boolean prefix;
  private boolean whitespace;
  private boolean smart;
  private int led;
  private int n;

  /**
   * Takes in no parameters.
   */
  public AcObject() {
    filesUsed = new ArrayList<String>();
    trie = new Trie();
    frequencies = new HashMap<String, Integer>();
    bigramFreq = new HashMap<String, Integer>();
    nGramFreq = new HashMap<Integer, HashMap<String, Integer>>();
    prefix = false;
    whitespace = false;
    smart = false;
    led = 0;
    n = 0;
  }

  /**
   * Opens and reads a txt file containing words that form the data upon which
   * the autocorrect object makes and ranks suggestions. Stores the words in the
   * trie and the frequencies of unigrams/bigrams/ngrams in hashmaps.
   *
   * @param file
   *          the file containing text
   * @return 0 if no error occurs, -1 if an error occurs
   */
  public int addCorpus(String file) {
    for (int i = 0; i < filesUsed.size(); i++) {
      if (filesUsed.get(i).equals(file)) {
        System.out.println("ERROR: File was already uploaded");
        return -1;
      }
    }
    Reader fileReader = new Reader();
    List<String> input = fileReader.readFile(file);
    if (input.size() == 0) {
      return -1;
    } else {
      addWords(input, file, true);
    }
    System.out.println("corpus " + file + " added");
    return 0;
  }

  /**
   * Adds words from a corpus to the autocorrect object.
   *
   * @param input
   *          list of strings comprising the input
   * @param file
   *          the file from which the words were taken
   * @param lower
   *          details whether the input should be lowercase
   */
  public void addWords(List<String> input, String file, boolean lower) {
    String oldWord = "";
    String bigram = "";
    List<String> prevWords = new ArrayList<String>();
    for (String word : input) {
      String newWord = word;
      if (lower) {
        newWord = newWord.replaceAll("[^a-zA-Z]", "").toLowerCase();
      }
      trie.addNode(newWord);
      // We have a hashmap that maps the size of the gram to the hashmap
      // that stores the frequencies of those sized grams
      if (n > 0) {
        for (int i = 0; i < n; i++) {
          String nGram = "";
          for (int j = 0; j <= i; j++) {
            if (prevWords.size() > j) {
              nGram = prevWords.get(j) + " " + nGram;
            }
          }
          nGram = nGram.trim();
          if (!nGramFreq.containsKey(i)) {
            HashMap<String, Integer> freqMap = new HashMap<String, Integer>();
            nGramFreq.put(i, freqMap);
          }
          if (!nGramFreq.get(i).containsKey(nGram)) {
            nGramFreq.get(i).put(nGram, 1);
          } else {
            nGramFreq.get(i).put(nGram, nGramFreq.get(i).get(nGram) + 1);
          }
        }
        prevWords.add(0, newWord);
      }
      // Regular frequency storing
      bigram = oldWord + " " + newWord;
      if (!frequencies.containsKey(newWord)) {
        frequencies.put(newWord, 1);
      } else {
        frequencies.put(newWord, frequencies.get(newWord) + 1);
      }
      if (!bigramFreq.containsKey(bigram)) {
        bigramFreq.put(bigram, 1);
      } else {
        bigramFreq.put(bigram, bigramFreq.get(bigram) + 1);
      }
      oldWord = newWord;
    }
    filesUsed.add(file);
  }

  /**
   * Handles the autocorrect command- given the user input, finds and ranks all
   * the suggestions depending on whether prefix, whitespace, led, and smart are
   * on. Returns the suggestions as a list of strings with the first being the
   * best suggestion.
   *
   * @param words
   *          a string containing the user's input
   * @param lower
   *          details if input should be lower case
   * @return a list of strings containing the top five ranked suggestions, the
   *         first one being the best and the last being the worst
   */
  public List<String> autocorrect(String words, boolean lower) {
    List<String> output = new ArrayList<String>();
    if (filesUsed.isEmpty()) {
      // If no corpus loaded, return empty array
      output.add(words);
      return output;
    }
    // Check if user input ends with trailing whitespace
    if (!Character.isWhitespace(words.charAt(words.length() - 1))) {
      output.add(words);
      List<String> suggestions;
      String str = "";
      List<String> tokens;
      if (lower) {
        words = words.toLowerCase();
      }
      if (smart) {
        // Smart ranking takes into account punctuation
        String[] sentences = words.split(";:.,?!");
        tokens = Interpreter.tokenizeString(sentences[sentences.length - 1]);
      } else {
        if (lower) {
          words = words.replaceAll("[^a-zA-Z ]", " ");
        }
        tokens = Interpreter.tokenizeString(words);
      }
      String lastToken = tokens.get(tokens.size() - 1);
      if (trie.contains(lastToken)) {
        for (int i = 1; i < tokens.size() - 1; i++) {
          str = str + tokens.get(i) + " ";
        }
        str = str + lastToken;
        output.add(str);
      }
      if (tokens.size() == 2) {
        suggestions = getSuggestions(lastToken);
        suggestions = sortByUnigramFreq(suggestions);
      } else {
        suggestions = getSuggestions(lastToken);
        if (smart) {
          // Smart ranking using N gram frequencies
          List<String> prevWords = new ArrayList<String>();
          for (int i = 1; i < tokens.size() - 1; i++) {
            prevWords.add(0, tokens.get(i));
          }
          if (prevWords.size() < n) {
            suggestions =
                sortByNGramFreq(suggestions, prevWords, prevWords.size());
          } else {
            suggestions = sortByNGramFreq(suggestions, prevWords, n);
          }
        } else {
          // Otherwise normal ranking
          suggestions = sortByBigramAndUnigramFreq(suggestions,
              tokens.get(tokens.size() - 2));
        }
      }
      while (output.size() != 6 && !suggestions.isEmpty()) {
        if (!suggestions.get(0).equals(lastToken)) {
          str = "";
          if (tokens.size() > 2) {
            for (int i = 1; i < tokens.size() - 1; i++) {
              str = str + tokens.get(i) + " ";
            }
            str = str + suggestions.get(0);
          } else {
            str = suggestions.get(0);
          }
          output.add(str);
          suggestions.remove(0);
        } else {
          suggestions.remove(0);
        }
      }
    }
    return output;
  }

  /**
   * Given a word, returns a list of the possible autocorrect suggestions. The
   * suggestions depend on whether the user has turned on the prefix setting,
   * has turned on the whitespace setting, and has increased the led setting to
   * greater than 0.
   *
   * @param word
   *          the word inputted to get the autocorrect suggestions for
   * @return the list of suggestions
   */
  public List<String> getSuggestions(String word) {
    List<String> suggestions = new ArrayList<String>();
    if (prefix) {
      List<String> prefSuggestions = trie.findChildren(word);
      for (String suggestion : prefSuggestions) {
        suggestions.add(suggestion);
      }
    }
    if (led != 0) {
      List<String> ledSuggestions = getLedSuggestions(word);
      for (String suggestion : ledSuggestions) {
        if (!suggestions.contains(suggestion)) {
          suggestions.add(suggestion);
        }
      }
    }
    if (whitespace) {
      List<String> whitespaceSuggestions = splitWord(word);
      for (String suggestion : whitespaceSuggestions) {
        if (!suggestions.contains(suggestion)) {
          suggestions.add(suggestion);
        }
      }
    }
    return suggestions;
  }

  /**
   * Given a list of autocorrect suggestions, ranks and sorts the list based on
   * the ngram probability. The integer n denotes the size of the gram (i.e. if
   * n = 2, sorts based on bigram probability). Recursively, then sorts on n -1
   * gram probability for suggestions with equal n probability.
   *
   * @param suggestions
   *          a list of strings representing the autocorrect suggestions
   * @param prevWords
   *          the previous words in the form of a list of strings - NOTE: the
   *          size of the list must be at least size n
   * @param nsize
   *          the size of the ngrams to sort by
   * @return the ranked list of suggestions (the first having the highest ngram
   *         probability)
   */
  public List<String> sortByNGramFreq(List<String> suggestions,
      List<String> prevWords, int nsize) {
    if (nsize == 1) {
      return sortByBigramAndUnigramFreq(suggestions, prevWords.get(0));
    } else if (nsize == 0) {
      return sortByUnigramFreq(suggestions);
    }
    List<String> sorted = new ArrayList<String>();
    if (nGramFreq.containsKey(nsize)) {
      HashMap<String, Integer> gramFreq = nGramFreq.get(nsize);
      HashMap<Integer, List<String>> freqAsKeys =
          new HashMap<Integer, List<String>>();
      ArrayList<Integer> frequenciesToSort = new ArrayList<Integer>();
      List<String> printSugg;
      for (String aSuggestion : suggestions) {
        String suggestion = aSuggestion.split(" ")[0];
        String nGram = "";
        for (int i = 0; i < nsize; i++) {
          nGram = prevWords.get(i) + " " + nGram;
        }
        nGram = nGram + suggestion;
        int freq;
        if (gramFreq.containsKey(nGram)) {
          freq = gramFreq.get(nGram);
        } else {
          freq = 0;
        }
        if (freqAsKeys.containsKey(freq)) {
          freqAsKeys.get(freq).add(aSuggestion);
        } else {
          printSugg = new ArrayList<String>();
          printSugg.add(aSuggestion);
          frequenciesToSort.add(freq);
          freqAsKeys.put(freq, printSugg);
        }
      }
      Collections.sort(frequenciesToSort, Collections.reverseOrder()); // sort
      for (int freq : frequenciesToSort) {
        printSugg = freqAsKeys.get(freq);
        printSugg = sortByNGramFreq(printSugg, prevWords, nsize - 1);
        sorted.addAll(printSugg);
      }
    } else {
      sorted = sortByNGramFreq(suggestions, prevWords, nsize - 1);
    }
    return sorted;
  }

  /**
   * Given a list of autocorrect suggestions, ranks and sorts the list based on
   * the bigram probability. In other words, if prevWord is not null (i.e. the
   * user inputted more than one word) then the words are first ranked by bigram
   * probability, how frequently they appear after the prevWord, and then
   * unigram probability and then alphabetically.
   *
   * @param suggestions
   *          a list of strings representing the autocorrect suggestions (bigram
   *          suggestions must be already loaded from a corpus and thus in the
   *          bigramFreq hashmap)
   * @param prevWord
   *          a word representing the second to last inputted word, cannot be
   *          null
   * @return the ranked list of suggestions (the first having the highest bigram
   *         probability)
   */
  public List<String> sortByBigramAndUnigramFreq(List<String> suggestions,
      String prevWord) {
    List<String> sorted = new ArrayList<String>();
    // rank by bigram
    HashMap<Integer, List<String>> freqAsKeys =
        new HashMap<Integer, List<String>>();
    ArrayList<Integer> frequenciesToSort = new ArrayList<Integer>();
    String bigram;
    List<String> printSugg;
    for (String aSuggestion : suggestions) {
      // creating the bigram for each suggestion and then add it to a new
      // arraylist
      String suggestion = aSuggestion.split(" ")[0];
      bigram = prevWord + " " + suggestion;
      int freq;
      if (bigramFreq.containsKey(bigram)) {
        freq = bigramFreq.get(bigram);
      } else {
        freq = 0;
      }
      if (freqAsKeys.containsKey(freq)) {
        freqAsKeys.get(freq).add(aSuggestion);
      } else {
        printSugg = new ArrayList<String>();
        printSugg.add(aSuggestion);
        frequenciesToSort.add(freq);
        freqAsKeys.put(freq, printSugg);
      }
    }
    Collections.sort(frequenciesToSort, Collections.reverseOrder()); // sort
    for (int freq : frequenciesToSort) {
      printSugg = freqAsKeys.get(freq);
      printSugg = sortByUnigramFreq(printSugg);
      sorted.addAll(printSugg);
    }
    return sorted;
  }

  /**
   * Given a list of suggestions, ranks and sorts the list based on the
   * frequency of the unigrams (the first token of each element in the list to
   * account for white space suggestions) in order of most common to least
   * common. If any suggestions have the same unigram frequency, they're ranked
   * and sorted alphabetically.
   *
   * @param suggestions
   *          list of suggestions to be sorted (suggestions must be already
   *          loaded from a corpus and thus in the frequencies hashmap)
   * @return the sorted list of suggestions
   */
  public List<String> sortByUnigramFreq(List<String> suggestions) {
    List<String> sorted = new ArrayList<String>();
    while (!suggestions.isEmpty()) {
      int most = 0;
      List<String> words = new ArrayList<String>();
      String token;
      for (String suggestion : suggestions) {
        token = suggestion.split(" ")[0];
        if (frequencies.get(token) > most) {
          words.removeAll(words);
          words.add(suggestion);
          most = frequencies.get(token);
        } else if (frequencies.get(token) == most) {
          words.add(suggestion);
        }
      }
      if (!words.isEmpty()) {
        Collections.sort(words); // Sorting list alphabetically
        for (String word : words) {
          suggestions.remove(word);
          sorted.add(word);
        }
      }
    }
    return sorted;
  }

  /**
   * Finds all the word suggestions in the given trie whose levenshtein edit
   * distance from the given word is less than or equal to the given distance
   * criteria.
   *
   * @param word
   *          the word to calculate the led from
   * @return a list of strings containing all the led suggestions
   */
  public List<String> getLedSuggestions(String word) {
    HashMap<Integer, List<String>> words = getLedWords(word);
    List<String> suggestions = new ArrayList<String>();
    for (List<String> list : words.values()) {
      for (String suggestion : list) {
        suggestions.add(suggestion);
      }
    }
    return suggestions;
  }

  /**
   * Finds and returns all the words contained in the trie whose levenshtein
   * edit distance from the given word is less than or equal to the current led
   * value. Returns words in a hashmap where the keys are led distances.
   *
   * @param word
   *          the word from which to calculate the levenshtein edit distance
   * @return a hashmap containing all the words in the trie with the correct led
   *         distances
   */
  public HashMap<Integer, List<String>> getLedWords(String word) {
    HashMap<Integer, List<String>> words = new HashMap<Integer, List<String>>();
    int size = word.length();
    List<Integer> row = new ArrayList<Integer>();
    for (int i = 0; i <= size; i++) {
      row.add(i, i);
    }
    for (Map.Entry<Character, TrieNode> entry : trie.root().getChildren()
        .entrySet()) {
      TrieNode node = entry.getValue();
      words.putAll(recurseLed(node, node.getChar(), word, row, words));
    }
    return words;
  }

  private Map<Integer, List<String>> recurseLed(TrieNode node, char letter,
      String word, List<Integer> prevRow, Map<Integer, List<String>> words) {
    int columns = prevRow.size();
    int insertCost;
    int deleteCost;
    int subCost;
    List<Integer> currRow = new ArrayList<Integer>();
    currRow.add(0, prevRow.get(0) + 1);
    for (int i = 1; i < columns; i++) {
      insertCost = currRow.get(i - 1) + 1;
      deleteCost = prevRow.get(i) + 1;
      if (word.charAt(i - 1) != letter) {
        subCost = prevRow.get(i - 1) + 1;
      } else {
        subCost = prevRow.get(i - 1);
      }
      currRow.add(i, Math.min(insertCost, Math.min(deleteCost, subCost)));
    }
    if (currRow.get(currRow.size() - 1) <= led && node.isEnd()) {
      int last = currRow.get(currRow.size() - 1);
      if (words.containsKey(last)) {
        words.get(last).add(node.getChars());
      } else {
        List<String> newList = new ArrayList<String>();
        newList.add(node.getChars());
        words.put(last, newList);
      }
    }
    Object obj = Collections.min(currRow);
    Integer i = new Integer((int) obj);
    if (i.intValue() <= led) {
      for (Map.Entry<Character, TrieNode> entry : node.getChildren()
          .entrySet()) {
        words.putAll(recurseLed(entry.getValue(), entry.getValue().getChar(),
            word, currRow, words));
      }
    }
    return words;
  }

  /**
   * Takes in a string and determines if the string can be split into two
   * different valid words (check if the words are contained in the tree).
   * Represents any possible split as a string with a space in between the
   * complete words (will only have two words) and then returns all possible
   * splits as a list of strings.
   *
   * @param word
   *          word to be determined if splittable
   * @return list of strings representing any possible splits
   */
  public List<String> splitWord(String word) {
    List<String> possSplits = new ArrayList<String>();
    for (int i = 1; i < word.length(); i++) {
      String str1 = word.substring(0, i);
      String str2 = word.substring(i, word.length());
      if (trie.contains(str1) && trie.contains(str2)) {
        possSplits.add(str1 + " " + str2);
      }
    }
    return possSplits;
  }

  /**
   * @param setting
   *          boolean detailing what to set the prefix setting to
   */
  public void setPrefix(boolean setting) {
    prefix = setting;
  }

  /**
   * @return string detailing if prefix setting is on
   */
  public String getPrefix() {
    if (prefix) {
      return "prefix on";
    } else {
      return "prefix off";
    }
  }

  /**
   * @param setting
   *          boolean detailing what to set the white space setting to
   */
  public void setWhiteSpace(boolean setting) {
    whitespace = setting;
  }

  /**
   * @return string detailing if whitespace setting is on
   */
  public String getWhiteSpace() {
    if (whitespace) {
      return "whitespace on";
    } else {
      return "whitespace off";
    }
  }

  /**
   * @param setting
   *          boolean detailing what to set the smart setting to
   */
  public void setSmart(boolean setting) {
    smart = setting;
  }

  /**
   * @return string detailing if smart setting is on
   */
  public String getSmart() {
    if (smart) {
      return "smart on";
    } else {
      return "smart off";
    }
  }

  /**
   * @param setting
   *          integer detailing what to set the led distance to
   */
  public void setLed(int setting) {
    led = setting;
  }

  /**
   * @return integer detailing what the led distance is set to
   */
  public int getLed() {
    return led;
  }

  /**
   * @param setting
   *          integer detailing size of "n grams" to store when reading in
   *          corpus
   */
  public void setN(int setting) {
    n = setting;
  }

  /**
   * @return integer detailing what the size of the "n grams" currently being
   *         stored during corpus input
   */
  public int getN() {
    return n;
  }

  @Override
  public String toString() {
    String str = "";
    for (String bigram : bigramFreq.keySet()) {
      str = str + bigram + "\n";
    }
    return str;
  }
}
