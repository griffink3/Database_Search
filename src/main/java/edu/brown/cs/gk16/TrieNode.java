package edu.brown.cs.gk16;

import java.util.HashMap;

/**
 * The representation of a node on a trie which stores its children in a hashmap
 * that uses characters as keys. Also stores a string and a boolean that details
 * whether the string is a completed word.
 */
public class TrieNode {

  private HashMap<Character, TrieNode> children;
  private boolean isEnd;
  private String chars;
  private TrieNode parent;

  /**
   * @param characters
   *          the string to be stored in the node
   * @param nodeParent
   *          the node's parent node
   */
  public TrieNode(String characters, TrieNode nodeParent) {
    children = new HashMap<Character, TrieNode>();
    isEnd = false;
    chars = characters;
    parent = nodeParent;
  }

  /**
   * @param end
   *          boolean detailing whether the contained string is a completed word
   */
  public void setEnd(boolean end) {
    isEnd = end;
  }

  /**
   * @return returns the boolean detailing whether the contained string is a
   *         completed word
   */
  public boolean isEnd() {
    return isEnd;
  }

  /**
   * @param c
   *          character that serves as the key to the child node
   * @param node
   *          child node to be added to the children hashmap
   */
  public void addChild(char c, TrieNode node) {
    children.put(c, node);
    return;
  }

  /**
   * @return TrieNode representing the node's parent
   */
  public TrieNode getParent() {
    return parent;
  }

  /**
   * @return hashmap containing the node's children
   */
  public HashMap<Character, TrieNode> getChildren() {
    return children;
  }

  /**
   * @return the string contained in the node
   */
  public String getChars() {
    return chars;
  }

  /**
   * @return the last character of the contained string
   */
  public char getChar() {
    return chars.charAt(chars.length() - 1);
  }

}
