package edu.brown.cs.gk16;

import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of a KdTree which is a data structure that organizes
 * points in k-dimensional space.
 */
public class KdTree {

  private int size;
  private int dim;
  private KdNode head;
  private List<NodeObject> currPoints;

  /**
   * Each KdTree knows about it size, dimensions of its points, head, and
   * current nodes.
   *
   * @param k
   *          the number of dimensions of the KdTree
   */
  public KdTree(int k) {
    size = 0;
    dim = k;
    head = null;
    currPoints = new ArrayList<NodeObject>();
  }

  /**
   * With the help of the helper function constructSubTree, addNewNodes builds
   * the kd tree, alternating the splitting hyperplane at each level if the tree
   * hasn't been constructed yet. However, if there are already points in the
   * tree, the function reconstructs the tree with the new points.
   *
   * NOTE: Freshly constructing the tree when adding new sets of points
   * maintains its balanced state - advantageous for queries list nearest
   * neighbor search and radius search.
   *
   * Returns without doing anything if no points passed in
   *
   * @param points
   *          the list of nodeobjects containing the points to be inserted into
   *          the tree
   */
  public void addNodes(List<NodeObject> points) {
    if (points == null) {
      return;
    } else if (size == 0) {
      // Constructing tree
      currPoints = points;
      size = points.size();
      head = constructSubTree(points, 0);
    } else {
      // Tree already exists and we're adding new nodes
      List<NodeObject> currentPoints = concatenatePointsList(currPoints, null,
          points);
      currPoints = currentPoints;
      size = currPoints.size();
      head = constructSubTree(currPoints, 0);
    }
  }

  private KdNode constructSubTree(List<NodeObject> points, int depth) {
    if (points.size() == 1) {
      KdNode kdNode = new KdNode(null, null, points.get(0), depth);
      return kdNode;
    } else if (points.size() == 0) {
      return null;
    }
    int coord = depth % dim;
    List<NodeObject> sortedPoints = sortPointsList(points, coord);
    int middleIndex = (int) Math.floor((double) points.size() / 2);
    NodeObject median = sortedPoints.get(middleIndex);
    List<NodeObject> lesserPoints = new ArrayList<NodeObject>(
        sortedPoints.subList(0, middleIndex));
    List<NodeObject> greaterPoints = new ArrayList<NodeObject>(
        sortedPoints.subList(middleIndex + 1, points.size()));
    KdNode kdNode = new KdNode(constructSubTree(lesserPoints, depth + 1),
        constructSubTree(greaterPoints, depth + 1), median, depth);
    return kdNode;
  }

  /**
   * Returns the passed in list of points (in the form of node objects that
   * contain arraylists of coordinates) sorted in ascending order by the
   * coordinate specified.
   *
   * Returns null if no points passed in or incorrect coordinate — however no
   * error checking for if the coordinate is beyond the bounds of the points
   *
   * @param points
   *          list of points
   * @param coord
   *          coordinate of points upon which to sort
   * @return sorted list
   */
  public static List<NodeObject> sortPointsList(List<NodeObject> points,
      int coord) {
    if (points == null || coord < 0) {
      return null;
    }
    if (points.size() <= 1) {
      return points;
    }
    int middle = (int) Math.floor((double) points.size() / 2);
    NodeObject pivot = points.get(middle);
    List<NodeObject> less = new ArrayList<NodeObject>();
    List<NodeObject> greater = new ArrayList<NodeObject>();
    for (int i = 0; i < points.size(); i++) {
      if (i != middle) {
        if (points.get(i).getPoint().get(coord) <= pivot.getPoint()
            .get(coord)) {
          less.add(points.get(i));
        } else {
          greater.add(points.get(i));
        }
      }
    }
    return concatenatePointsList(sortPointsList(less, coord), pivot,
        sortPointsList(greater, coord));
  }

  /**
   * Concatenates a list of points, a single point, and another list (in that
   * order) into a single list. If any of the arguments passed in is null, the
   * function just returns the non-null arguments concatenated. If all the
   * passed in arguments are null, the function will return an empty arraylist.
   *
   * @param list1
   *          list of points
   * @param point
   *          single point
   * @param list2
   *          another list of points
   * @return concatenated list
   */
  public static List<NodeObject> concatenatePointsList(List<NodeObject> list1,
      NodeObject point, List<NodeObject> list2) {
    List<NodeObject> result = new ArrayList<NodeObject>();
    if (list1 != null) {
      result.addAll(list1);
    }
    if (point != null) {
      result.add(point);
    }
    if (list2 != null) {
      result.addAll(list2);
    }
    return result;
  }

  /**
   * @return the size of the KdTree
   */
  public int size() {
    return size;
  }

  /**
   * @return boolean detailing if the KdTree has a head node
   */
  public boolean hasRoot() {
    if (head != null) {
      return true;
    }
    return false;
  }

  /**
   * @return the head of the KdTree
   */
  public KdNode root() {
    return head;
  }

  /**
   * Adds a new node to the tree by calling the addNodes method on a list
   * composed of a single element - maintains balanced state.
   *
   * Returns without doing anything if no point is passed in
   *
   * @param object
   *          a single object to be added to the tree
   */
  public void addNode(NodeObject object) {
    if (object == null) {
      return;
    }
    List<NodeObject> singlePoint = new ArrayList<NodeObject>();
    singlePoint.add(object);
    addNodes(singlePoint);
  }

  /**
   * @return a list of the current nodes in the KdTree
   */
  public List<NodeObject> kdNodes() {
    return currPoints;
  }

}
