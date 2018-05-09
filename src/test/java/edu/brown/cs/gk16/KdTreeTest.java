package edu.brown.cs.gk16;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.brown.cs.gk16.stars.Star;

public class KdTreeTest {

  @Test
  public void testConcatenatePointsList() {
    // Creating 6 points, filling in 3 of them to test their values after
    // concatenation
    Star point = new Star("iD", "name", 0.0, 0.0, 0.0);
    Star point1 = new Star("iD", "name", 0.1, 0.0, 0.0);
    Star point2 = new Star("iD", "name", 0.2, 0.0, 0.0);
    Star point3 = new Star("iD", "name", 0.3, 0.0, 0.0);
    Star point4 = new Star("iD", "name", 0.4, 0.0, 0.0);
    Star point5 = new Star("iD", "name", 0.5, 0.0, 0.0);

    // Adding the points to two arraylists (not point3 which is the "pivot")
    ArrayList<NodeObject> firstList = new ArrayList<NodeObject>();
    ArrayList<NodeObject> secondList = new ArrayList<NodeObject>();
    firstList.add(point);
    firstList.add(point1);
    firstList.add(point2);
    secondList.add(point4);
    secondList.add(point5);

    // The resulting arraylist should have all the points in the correct order
    List<NodeObject> result = KdTree.concatenatePointsList(firstList, point3,
        secondList);
    assert (result.size() == 6);
    assert (result.get(0).getPoint().get(0) == 0.0);
    assert (result.get(3).getPoint().get(0) == 0.3);
    assert (result.get(5).getPoint().get(0) == 0.5);
  }

  @Test
  public void testSortPointsList() {
    // Creating 6 points with different values
    Star point = new Star("iD", "name", 0.0, 0.0, 0.0);
    Star point1 = new Star("iD", "name", 1.0, 0.0, 0.0);
    Star point2 = new Star("iD", "name", 2.0, 0.0, 0.0);
    Star point3 = new Star("iD", "name", 3.0, 0.0, 0.0);
    Star point4 = new Star("iD", "name", 4.0, 0.0, 0.0);
    Star point5 = new Star("iD", "name", 5.0, 0.0, 0.0);

    // Adding the points in random order to new arraylist - [4,2,3,5,1,0]
    ArrayList<NodeObject> list = new ArrayList<NodeObject>();
    list.add(point4);
    list.add(point2);
    list.add(point3);
    list.add(point5);
    list.add(point1);
    list.add(point);

    // Resulting list should be ascending order
    List<NodeObject> result = KdTree.sortPointsList(list, 0);
    assert (result.size() == 6);
    assert (result.get(0).getPoint().get(0) == 0.0);
    assert (result.get(1).getPoint().get(0) == 1.0);
    assert (result.get(2).getPoint().get(0) == 2.0);
    assert (result.get(3).getPoint().get(0) == 3.0);
    assert (result.get(4).getPoint().get(0) == 4.0);
    assert (result.get(5).getPoint().get(0) == 5.0);
  }

  @Test
  public void testNewKdTree() {
    KdTree tree = new KdTree(3);
    assert (tree.size() == 0); // Starts off as empty tree
    assert (tree != null);
    assert (tree.root() == null);
  }

  @Test
  public void testConstructTree() {
    // Constructing tree below
    KdTree tree = new KdTree(3);
    Star point = new Star("iD", "point", 0.0, 7.0, 3.0);
    Star point1 = new Star("iD", "point1", 1.0, 4.0, 6.0);
    Star point2 = new Star("iD", "point2", 2.0, 2.0, 2.0);
    Star point3 = new Star("iD", "point3", 3.0, 8.0, 1.0);
    Star point4 = new Star("iD", "point4", 4.0, 9.0, 5.0);
    Star point5 = new Star("iD", "point5", 5.0, 5.0, 9.0);

    ArrayList<NodeObject> list = new ArrayList<NodeObject>();
    list.add(point4);
    list.add(point2);
    list.add(point3);
    list.add(point5);
    list.add(point1);
    list.add(point);

    tree.addNodes(list);

    assert (tree.root() != null);

    // Checking for correct structure of tree
    assert (tree.root().getObject().getPoint().get(0) == 3.0);
    assert (tree.root().getLeft().getObject().getPoint().get(0) == 1.0);
    assert (tree.root().getLeft().getLeft().getObject().getPoint()
        .get(0) == 2.0);
    assert (tree.root().getLeft().getRight().getObject().getPoint()
        .get(0) == 0.0);
    assert (tree.root().getRight().getObject().getPoint().get(0) == 4.0);
    assert (tree.root().getRight().getLeft().getObject().getPoint()
        .get(0) == 5.0);

    // Checking left and right children of nodes
    assert (tree.root().getLeft().getLeft().isLeaf() == true);
    assert (tree.root().getLeft().getRight().isLeaf() == true);
    assert (tree.root().getRight().getLeft().isLeaf() == true);
    assert (tree.root().isLeaf() == false);
    assert (tree.root().getLeft().isLeaf() == false);
    assert (tree.root().getRight().isLeaf() == false);

    assert (tree.size() == 6);
  }

  @Test
  public void testConstructComplicatedTree() {
    // Constructing tree below
    KdTree tree = new KdTree(3);
    Star point = new Star("0", "point", 0.0, 0.0, 0.0);
    Star point1 = new Star("1", "point1", 282.43485, 0.00449, 5.36884);
    Star point2 = new Star("2", "point2", 43.04329, 0.00285, -15.24144);
    Star point3 = new Star("3", "point3", 277.11358, 0.02422, 223.27753);
    Star point4 = new Star("3759", "point4", 7.26388, 1.55643, 0.68697);
    Star point5 = new Star("70667", "point5", -0.47175, -0.36132, -1.15037);
    Star point6 = new Star("71454", "point5", -0.50359, -0.42128, -1.1767);
    Star point7 = new Star("71457", "point5", -0.50362, -0.42139, -1.17665);
    Star point8 = new Star("87666", "point5", -0.01729, -1.81533, 0.14824);
    Star point9 = new Star("118721", "point5", -2.28262, 0.64697, 0.29354);

    ArrayList<NodeObject> list = new ArrayList<NodeObject>();
    list.add(point4);
    list.add(point2);
    list.add(point3);
    list.add(point5);
    list.add(point1);
    list.add(point);
    list.add(point6);
    list.add(point7);
    list.add(point8);
    list.add(point9);

    tree.addNodes(list);

    assert (tree.root() != null);

    // Checking for correct structure of tree
    assert (tree.root().getObject().getId().equals("0"));
    assert (tree.root().getLeft().getObject().getId().equals("71454"));
    assert (tree.root().getLeft().getLeft().getObject().getId()
        .equals("87666"));
    assert (tree.root().getLeft().getLeft().getLeft().getObject().getId()
        .equals("71457"));
    assert (tree.root().getLeft().getRight().getObject().getId()
        .equals("118721"));
    assert (tree.root().getLeft().getRight().getLeft().getObject().getId()
        .equals("70667"));
    assert (tree.root().getRight().getObject().getId().equals("3"));
    assert (tree.root().getRight().getLeft().getObject().getId().equals("1"));
    assert (tree.root().getRight().getLeft().getLeft().getObject().getId()
        .equals("2"));
    assert (tree.root().getRight().getRight().getObject().getId()
        .equals("3759"));

    assert (tree.size() == 10);
  }

  @Test
  public void testAddNewNodes() {
    // Testing adding new nodes to an already constructed tree

    // Constructing tree below
    KdTree tree = new KdTree(3);
    Star point = new Star("iD", "point", 0.0, 7.0, 3.0);
    Star point1 = new Star("iD", "point1", 1.0, 4.0, 6.0);
    Star point2 = new Star("iD", "point2", 2.0, 2.0, 2.0);
    Star point3 = new Star("iD", "point3", 3.0, 8.0, 1.0);
    Star point4 = new Star("iD", "point4", 4.0, 9.0, 5.0);
    Star point5 = new Star("iD", "point5", 5.0, 5.0, 9.0);

    ArrayList<NodeObject> list = new ArrayList<NodeObject>();
    list.add(point4);
    list.add(point2);
    list.add(point3);
    list.add(point5);
    list.add(point1);
    list.add(point);

    tree.addNodes(list);

    // Adding new nodes
    Star point6 = new Star("iD", "point6", 6.0, 0.0, 10.0);
    Star point7 = new Star("iD", "point7", 7.0, 11.0, 4.0);
    Star point8 = new Star("iD", "point8", 8.0, 1.0, 7.0);
    ArrayList<NodeObject> list1 = new ArrayList<NodeObject>();
    list1.add(point6);
    list1.add(point7);
    list1.add(point8);

    tree.addNodes(list1); // Should freshly construct tree with all nodes

    // Checking for correct tree structure
    assert (tree.root().getObject().getPoint().get(0) == 4.0);
    assert (tree.root().getLeft().getObject().getPoint().get(0) == 0.0);
    assert (tree.root().getLeft().getLeft().getObject().getPoint()
        .get(0) == 1.0);
    assert (tree.root().getLeft().getLeft().getLeft().getObject().getPoint()
        .get(0) == 2.0);
    assert (tree.root().getLeft().getRight().getObject().getPoint()
        .get(0) == 3.0);
    assert (tree.root().getRight().getObject().getPoint().get(0) == 5.0);
    assert (tree.root().getRight().getLeft().getObject().getPoint()
        .get(0) == 6.0);
    assert (tree.root().getRight().getRight().getObject().getPoint()
        .get(0) == 7.0);
    assert (tree.root().getRight().getLeft().getLeft().getObject().getPoint()
        .get(0) == 8.0);

    assert (tree.size() == 9);
  }

  @Test
  public void testAddSingleNewNode() {
    // Testing adding a single new node to an already constructed tree

    // Constructing tree below
    KdTree tree = new KdTree(3);
    Star point = new Star("iD", "point", 0.0, 7.0, 3.0);
    Star point1 = new Star("iD", "point1", 1.0, 4.0, 6.0);
    Star point2 = new Star("iD", "point2", 2.0, 2.0, 2.0);
    Star point3 = new Star("iD", "point3", 3.0, 8.0, 1.0);
    Star point4 = new Star("iD", "point4", 4.0, 9.0, 5.0);
    Star point5 = new Star("iD", "point5", 5.0, 5.0, 9.0);

    ArrayList<NodeObject> list = new ArrayList<NodeObject>();
    list.add(point4);
    list.add(point2);
    list.add(point3);
    list.add(point5);
    list.add(point1);
    list.add(point);

    tree.addNodes(list);

    // Adding a new node
    Star point6 = new Star("iD", "point6", 6.0, 10.0, 0.0);

    tree.addNode(point6);

    // Check that it's in the correct location
    assert (tree.root().getRight().getRight().getObject().getPoint()
        .get(0) == 6.0);
    assert (tree.size() == 7);
  }
}
