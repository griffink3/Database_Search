package edu.brown.cs.gk16.stars;

import java.util.ArrayList;

import edu.brown.cs.gk16.NodeObject;

/**
 * A star is a representation of a single star in the StarsObject conglomerate
 * of stars. Each star has an id, a name, and a point describing its location.
 * The class implements the nodeobject interface so that it can be stored in a
 * kdtree.
 */
public class Star implements NodeObject {

  private String iD;
  private String name;
  private ArrayList<Double> point;

  /**
   * @param newId
   *          the star's id
   * @param newName
   *          the star's name
   * @param x
   *          the star's x coordinate
   * @param y
   *          the star's y coordinate
   * @param z
   *          the star's z coordinate
   */
  public Star(String newId, String newName, double x, double y, double z) {
    iD = newId;
    name = newName;
    point = new ArrayList<Double>();
    point.add(x);
    point.add(y);
    point.add(z);
  }

  @Override
  public ArrayList<Double> getPoint() {
    return point;
  }

  @Override
  public String getId() {
    return iD;
  }

  /**
   * @return the star's name
   */
  public String getName() {
    return name;
  }

}
