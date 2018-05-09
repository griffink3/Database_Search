package edu.brown.cs.gk16.stars;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.brown.cs.gk16.KdNode;
import edu.brown.cs.gk16.KdTree;
import edu.brown.cs.gk16.NodeObject;

/**
 * This class is the representation of a collection of stars existing in a
 * kdtree that can be queried 1) to find the nearest k neighbors given a star or
 * point and 2) to find all the stars in a radius around a given star or point.
 * Both algorithms are housed in this class - as well as the csv reading related
 * to uploading the stars.
 */
public class StarsObject {

  private KdTree starsTree;
  private int size;
  private int dim;
  private ArrayList<String> filesUsed;
  private HashMap<String, Star> starsMap;
  private HashMap<String, Star> starIds;

  /**
   * @param k
   *          the dimensions of the stars (which will typically be 3)
   */
  public StarsObject(int k) {
    starsTree = new KdTree(k);
    dim = k;
    size = 0;
    filesUsed = new ArrayList<String>();
    starsMap = new HashMap<String, Star>();
    starIds = new HashMap<String, Star>();
  }

  /**
   * Opens and reads a csv file containing a list of stars, parsing the various
   * arguments in the process, and then adds them to the kdtree containing all
   * the stars.
   *
   * @param file
   *          the file containing the list of stars
   * @return the number of stars loaded if no error occurs, -1 if an error
   *         occurs
   */
  public int addStars(String file) {
    for (int i = 0; i < filesUsed.size(); i++) {
      if (filesUsed.get(i).equals(file)) {
        System.out.println("ERROR: File was already uploaded");
        return -1;
      }
    }
    BufferedReader br = null;
    String line = "";
    String separator = ",";
    int numRead = 0;
    List<NodeObject> stars = new ArrayList<NodeObject>();
    try {
      br = new BufferedReader(new FileReader(file));
      // Skip over header line
      line = br.readLine();
      if (line != null) {
        line = br.readLine();
        while (line != null) {
          // use comma as separator
          String[] star = line.split(separator);
          if (star.length != 5) {
            return printNumberOfFieldsError();
          }
          if (!checkDouble(star[2]) || !checkDouble(star[3])
              || !checkDouble(star[4])) {
            return -1;
          }
          // If a star with the same id is already in the kdtree, it shouldn't
          // be added again
          if (!starIds.containsKey(star[0])) {
            Star newStar = new Star(star[0], star[1], makeDouble(star[2]),
                makeDouble(star[3]), makeDouble(star[4]));
            stars.add(newStar);
            starIds.put(star[0], newStar);
            starsMap.put(star[1], newStar);
            numRead++;
          }
          line = br.readLine();
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println("ERROR: File not found");
      return -1;
    } catch (IOException e) {
      System.out
          .println("ERROR: IO Exception occurred upon opening - check file");
      return -1;
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          System.out.println(
              "ERROR: IO Exception occurred upon closing - check file");
          return -1;
        }
      }
    }
    filesUsed.add(file);
    starsTree.addNodes(stars);
    size = size + numRead;
    System.out.println("Read " + numRead + " stars from " + file);
    return numRead;
  }

  private static int printNumberOfFieldsError() {
    System.out
        .println("ERROR: Points in the CSV file do not have the correct number "
            + "of fields");
    return -1;
  }

  private static void printNotADoubleError() {
    System.out.println("ERROR: Coordinates must be numbers");
  }

  /**
   * Checks if the passed in string can be parsed into a double using the
   * Double.parseDouble function.
   *
   * @param str
   *          string to be parsed
   * @return boolean detailing if the string can be made into a double
   */
  public static boolean checkDouble(String str) {
    try {
      Double.parseDouble(str);
    } catch (NumberFormatException e) {
      printNotADoubleError();
      return false;
    }
    return true;
  }

  /**
   * Parses the string into a double - only strings that can be parsed into a
   * double should be passed in (i.e. use in conjunction with checkDouble).
   *
   * @param str
   *          string to be parsed
   * @return the string in double form
   */
  public static double makeDouble(String str) {
    double k = Double.parseDouble(str);
    return k;
  }

  /**
   * Checks the starsMap hashmap to see if the star (by star name) has been
   * already loaded into the StarsObject - all stars already loaded will be
   * contained in the hashmap.
   *
   * @param star
   *          the star of interest
   * @return boolean detailing if the star has been read in already
   */
  public boolean isStarLoaded(String star) {
    return starsMap.containsKey(star);
  }

  /**
   * Returns the star given the star name if the star has already been loaded -
   * if the star hasn't been loaded, returns null.
   *
   * @param name
   *          the name of the star to get
   * @return the star object corresponding to the star name
   */
  public Star getStar(String name) {
    if (starsMap.containsKey(name)) {
      return starsMap.get(name);
    } else {
      return null;
    }
  }

  /**
   * Returns the star given the star iD if the star has already been loaded - if
   * the star hasn't been loaded, returns null.
   *
   * @param iD
   *          the iD of the star to get
   * @return the star object corresponding to the star iD
   */
  public Star getStarById(String iD) {
    if (starIds.containsKey(iD)) {
      return starIds.get(iD);
    } else {
      return null;
    }
  }

  /**
   * Searches for the nearest k (delineated by the integer neighbors) neighbors
   * in the kd tree to the point given by point and returns them in a sorted
   * list of their ids as strings (in order of nearest to furthest). Uses the
   * function searchSubTree as a recursive helper that recurses down each
   * subtree.
   *
   * Returns an empty list if the number of neighbors specified is 0 or if the
   * point given is null
   *
   * @param neighbors
   *          the number of neighbors to search for
   * @param point
   *          a point whose neighbors to search for
   * @return a sorted list of the nearest k neighbors
   */
  public List<String> nearestNeighbor(int neighbors, List<Double> point) {
    if (neighbors == 0 || point == null) {
      List<String> empty = new ArrayList<String>();
      return empty;
    }
    HashMap<KdNode, Double> nearestStars = new HashMap<KdNode, Double>();
    if (starsTree.hasRoot()) {
      nearestStars = searchSubTree(starsTree.root(), point, nearestStars,
          neighbors);
    }
    return sortListByDistance(nearestStars);
  }

  private HashMap<KdNode, Double> searchSubTree(KdNode node, List<Double> point,
      HashMap<KdNode, Double> bests, int neighbors) {
    if (node.isLeaf()) {
      return putInList(node, point, bests, neighbors);
    } else {
      int coord = node.depth() % dim;
      int left = 0;
      if (node.getObject().getPoint().get(coord) > point.get(coord)) {
        if (node.hasLeft()) {
          bests = searchSubTree(node.getLeft(), point, bests, neighbors);
          left = 1;
        }
      } else {
        if (node.hasRight()) {
          bests = searchSubTree(node.getRight(), point, bests, neighbors);
        }
      }
      bests = putInList(node, point, bests, neighbors);
      if (shouldSearchOther(bests, node, coord, point, neighbors)) {
        if (left == 1) {
          if (node.hasRight()) {
            bests = searchSubTree(node.getRight(), point, bests, neighbors);
          }
        } else {
          if (node.hasLeft()) {
            bests = searchSubTree(node.getLeft(), point, bests, neighbors);
          }
        }
      }
      return bests;
    }
  }

  private HashMap<KdNode, Double> putInList(KdNode node, List<Double> point,
      HashMap<KdNode, Double> nodes, int neighbors) {
    double dist = distance(node, point);
    if (nodes.size() < neighbors) {
      nodes.put(node, dist);
    } else {
      KdNode farthest = findFarthest(nodes);
      if (nodes.get(farthest) > dist) {
        nodes.remove(farthest);
        nodes.put(node, dist);
      }
    }
    return nodes;
  }

  private boolean shouldSearchOther(HashMap<KdNode, Double> nodes, KdNode node,
      int coord, List<Double> point, int neighbors) {
    double farthest = nodes.get(findFarthest(nodes));
    if (node.getObject().getPoint().get(coord) - point.get(coord) < farthest
        || nodes.size() < neighbors) {
      return true;
    }
    return false;
  }

  private KdNode findFarthest(HashMap<KdNode, Double> nodes) {
    double farthest = -1;
    KdNode farthestNode = null;
    for (KdNode star : nodes.keySet()) {
      if (nodes.get(star) > farthest) {
        farthest = nodes.get(star);
        farthestNode = star;
      }
    }
    return farthestNode;
  }

  private List<String> sortListByDistance(HashMap<KdNode, Double> nodes) {
    List<String> sortedList = new ArrayList<String>();
    while (!nodes.isEmpty()) {
      KdNode farthest = findFarthest(nodes);
      sortedList.add(0, farthest.getObject().getId());
      nodes.remove(farthest);
    }
    return sortedList;
  }

  /**
   * Used by both the nearest neighbors and range search algorithms, returns the
   * distance between the point stored in a KdNode and the point of interest in
   * the form of a double.
   *
   * returns 0 if the node or the point are null
   *
   * @param node
   *          KdNode whose point is used to calculate the distance from the
   *          point of interest
   * @param point
   *          the point of interest
   * @return the distance as a double
   */
  private double distance(KdNode node, List<Double> point) {
    if (node == null || point == null) {
      return 0;
    }
    int pointSize = point.size();
    List<Double> point1 = node.getObject().getPoint();
    double sum = 0;
    for (int i = 0; i < pointSize; i++) {
      sum = sum
          + ((point.get(i) - point1.get(i)) * (point.get(i) - point1.get(i)));
    }
    return Math.sqrt(sum);
  }

  /**
   * Searches for all the stars in the kd tree that are within the range of
   * radius from the given point. Then returns them in a sorted list of their
   * ids as strings (in order of nearest to furthest). Uses the function
   * searchRangeSubTree as a recursive helper that recurses down each subtree.
   *
   * Returns an empty list if the radius specified is 0 or if the point given is
   * null
   *
   * @param radius
   *          radial distance within which to find stars
   * @param point
   *          the point around which the radius is centered
   * @return a sorted list of the stars within range
   */
  public List<String> rangeSearch(double radius, List<Double> point) {
    if (radius == 0 || point == null) {
      List<String> empty = new ArrayList<String>();
      return empty;
    }
    HashMap<KdNode, Double> starsInRange = new HashMap<KdNode, Double>();
    if (starsTree.hasRoot()) {
      starsInRange = rangeSearchSubTree(starsTree.root(), point, radius,
          starsInRange);
    }
    return sortListByDistance(starsInRange);
  }

  private HashMap<KdNode, Double> rangeSearchSubTree(KdNode node,
      List<Double> point, double radius, HashMap<KdNode, Double> starsInRange) {
    if (node.isLeaf()) {
      return putInRangeList(node, point, radius, starsInRange);
    } else {
      int coord = node.depth() % dim;
      int left = 0;
      if (node.getObject().getPoint().get(coord) > point.get(coord)) {
        if (node.hasLeft()) {
          starsInRange = rangeSearchSubTree(node.getLeft(), point, radius,
              starsInRange);
          left = 1;
        }
      } else {
        if (node.hasRight()) {
          starsInRange = rangeSearchSubTree(node.getRight(), point, radius,
              starsInRange);
        }
      }
      starsInRange = putInRangeList(node, point, radius, starsInRange);
      if (Math.abs(
          node.getObject().getPoint().get(coord) - point.get(coord)) < radius) {
        if (left == 1) {
          if (node.hasRight()) {
            starsInRange = rangeSearchSubTree(node.getRight(), point, radius,
                starsInRange);
          }
        } else {
          if (node.hasLeft()) {
            starsInRange = rangeSearchSubTree(node.getLeft(), point, radius,
                starsInRange);
          }
        }
      }
      return starsInRange;
    }
  }

  private HashMap<KdNode, Double> putInRangeList(KdNode node,
      List<Double> point, double radius, HashMap<KdNode, Double> starsInRange) {
    double dist = distance(node, point);
    if (dist <= radius) {
      starsInRange.put(node, dist);
    }
    return starsInRange;
  }

}
