package edu.brown.cs.gk16;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.brown.cs.gk16.Trie;

public class TrieTest {

  @Test
  public void testNewTrie() {
    Trie trie = new Trie();
    assert (trie != null);
    assert (trie.size() == 0);
    assert (trie.root() != null);
  }

  @Test
  public void testAddNodes() {
    Trie trie = new Trie();

    List<String> words = new ArrayList<String>();
    words.add("mean");
    words.add("aah");
    words.add("love");
    words.add("aahing");
    words.add("soothe");
    words.add("cool");

    for (String word : words) {
      trie.addNode(word);
    }

    assert (trie.size() == 6);
  }

  @Test
  public void testSearch() {
    Trie trie = new Trie();

    List<String> words = new ArrayList<String>();
    words.add("aa");
    words.add("boot");
    words.add("can");
    words.add("very");
    words.add("aahs");
    words.add("so");

    for (String word : words) {
      trie.addNode(word);
    }

    // Testing that search returns the node containing the word
    assert (trie.search("aa").getChars().equals("aa"));
    assert (trie.search("so").getChars().equals("so"));
    assert (trie.search("very").getChars().equals("very"));
    assert (trie.search("happy") == null);
  }

  @Test
  public void testFindChildren() {
    Trie trie = new Trie();

    List<String> words = new ArrayList<String>();
    words.add("aa");
    words.add("aah");
    words.add("aahed");
    words.add("aahing");
    words.add("aahs");
    words.add("aal");

    for (String word : words) {
      trie.addNode(word);
    }

    List<String> children = trie.findChildren("aa");

    // Should return all of the words but aa (aa is not a suggestion for aa)
    assert (children.size() == 5);
    assert (children.contains("aah"));
    assert (children.contains("aahed"));
    assert (children.contains("aahing"));
    assert (children.contains("aahs"));
    assert (children.contains("aal"));
  }

}
