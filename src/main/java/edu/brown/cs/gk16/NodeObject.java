package edu.brown.cs.gk16;

import java.util.ArrayList;

/**
 * Interface that represents an object that can be stored on a tree node.
 */
public interface NodeObject {

  /**
   * @return an arraylist representation of a point
   */
  ArrayList<Double> getPoint();

  /**
   * @return the node object id as a string
   */
  String getId();

}
