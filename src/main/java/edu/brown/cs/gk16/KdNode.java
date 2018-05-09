package edu.brown.cs.gk16;

/**
 * The representation of a node on a tree which stores left and right children,
 * a node object, and a depth.
 */
public class KdNode {

  private KdNode left;
  private KdNode right;
  private NodeObject object;
  private int depth;

  /**
   * @param leftChild
   *          the left child of the node
   * @param rightChild
   *          the right child of the node
   * @param newObject
   *          the object to be stored in the node
   * @param currDepth
   *          the depth of the node
   */
  public KdNode(KdNode leftChild, KdNode rightChild, NodeObject newObject,
      int currDepth) {
    left = leftChild;
    right = rightChild;
    object = newObject;
    depth = currDepth;
  }

  /**
   * @return the object stored in the node
   */
  public NodeObject getObject() {
    return object;
  }

  /**
   * @return a boolean detailing if the node has a left child
   */
  public boolean hasLeft() {
    if (left == null) {
      return false;
    }
    return true;
  }

  /**
   * @return a boolean detailing if the node has a right child
   */
  public boolean hasRight() {
    if (right == null) {
      return false;
    }
    return true;
  }

  /**
   * @return the left child of the node
   */
  public KdNode getLeft() {
    return left;
  }

  /**
   * @return the right child of the node
   */
  public KdNode getRight() {
    return right;
  }

  /**
   * @return the depth of the node
   */
  public int depth() {
    return depth;
  }

  /**
   * @return boolean detailing if the node is a leaf
   */
  public boolean isLeaf() {
    if (hasLeft() || hasRight()) {
      return false;
    }
    return true;
  }

}
