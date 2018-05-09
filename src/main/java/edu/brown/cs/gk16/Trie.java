package edu.brown.cs.gk16;

import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of a Trie which is an ordered tree data structure used to
 * store a dynamic set with the keys being strings.
 */
public class Trie {

  private TrieNode root;
  private int size;
  private List<String> words;

  /**
   * Each Trie maintains a root which doesn't store any string, its size (the
   * size is the number of words stored in the Trie - not the number of nodes),
   * and a list of all the full words stored in the trie.
   */
  public Trie() {
    root = new TrieNode("", null);
    size = 0;
    words = new ArrayList<String>();

  }

  /**
   * Adds a word to the Trie by recursively searching the subtree
   * (recurseSubTree helper function) - at each new depth, compares the next
   * letter (left to right) of the word which depends on charIndex.
   *
   * @param word
   *          word to be added to the trie
   */
  public void addNode(String word) {
    if (word == null) {
      return;
    } else {
      recurseSubTree(root, word, 0);
    }
  }

  private void recurseSubTree(TrieNode node, String word, int charIndex) {
    if (word.length() == charIndex) {
      // End of the word
      node.setEnd(true);
      size++;
      words.add(word);
      return;
    } else {
      char c = word.charAt(charIndex);
      TrieNode next;
      if (!node.getChildren().containsKey(c)) {
        next = new TrieNode(word.substring(0, charIndex + 1), node);
        node.addChild(c, next);
      } else {
        next = node.getChildren().get(c);
      }
      recurseSubTree(next, word, charIndex + 1);
    }
  }

  /**
   * Returns a boolean detailing if the trie contains the given word.
   *
   * @param word
   *          word to be searched for in the trie
   * @return true if word is in the tree, false otherwise
   */
  public boolean contains(String word) {
    TrieNode node = search(word);
    if (node == null || !node.isEnd()) {
      return false;
    }
    return true;
  }

  /**
   * Searches for the node that contains the given word. If the word is a prefix
   * instead of a complete word, the function will return the node that contains
   * the prefix. If the word is not in the trie, the function returns null.
   *
   * @param word
   *          word to be searched for in the trie
   * @return TrieNode containing the word if it's in the tree, null otherwise
   */
  public TrieNode search(String word) {
    return searchSubTree(root, word, 0);
  }

  private TrieNode searchSubTree(TrieNode node, String word, int charIndex) {
    if (word.length() == charIndex) {
      return node;
    } else {
      char c = word.charAt(charIndex);
      TrieNode next;
      if (!node.getChildren().containsKey(c)) {
        return null;
      } else {
        next = node.getChildren().get(c);
      }
      return searchSubTree(next, word, charIndex + 1);
    }
  }

  /**
   * Returns all the possible completed words (as a list of strings) that exist
   * in the trie given a prefix. First searches for the prefix node using search
   * and then uses the addChildren helper to recurse down the tree looking for
   * children that are completed words.
   *
   * @param prefix
   *          prefix to completed words to be returned
   * @return list of strings that represent the possible completions of the
   *         prefix
   */
  public List<String> findChildren(String prefix) {
    List<String> children = new ArrayList<String>();
    TrieNode node = search(prefix);
    if (node == null) {
      return children;
    } else {
      return addChildren(children, node);
    }
  }

  private List<String> addChildren(List<String> children, TrieNode node) {
    for (TrieNode child : node.getChildren().values()) {
      if (child.isEnd()) {
        children.add(child.getChars());
      }
      if (!child.getChildren().isEmpty()) {
        children = addChildren(children, child);
      }
    }
    return children;
  }

  /**
   * @return the size of the Trie
   */
  public int size() {
    return size;
  }

  /**
   * @return the root of the Trie
   */
  public TrieNode root() {
    return root;
  }

  /**
   * @return the arraylist containing all the words stored in the trie
   */
  public List<String> getWords() {
    return words;
  }

  @Override
  public String toString() {
    String str = "";
    return printSubTree(root, str);
  }

  private String printSubTree(TrieNode node, String str) {
    if (!node.equals(root)) {
      str = str + "Node: " + node.getChars() + "\n";
    }
    if (!node.getChildren().isEmpty()) {
      for (TrieNode child : node.getChildren().values()) {
        str = str + "Child: " + child.getChars() + "\n";
      }
      for (TrieNode child : node.getChildren().values()) {
        str = printSubTree(child, str);
      }
    }
    return str;
  }

}
