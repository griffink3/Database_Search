package edu.brown.cs.gk16;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class GraphTest {

  /**
   * A simple test for insertVertex, that adds 3 vertices and then checks to
   * make sure they were added by accessing them through the vertices iterator.
   */
  public void testInsertVertex() {
    // insert vertices
    EdgeSetGraph<String> graph = new EdgeSetGraph<String>();
    Vertex<String> a = graph.insertVertex("A");
    Vertex<String> b = graph.insertVertex("B");
    Vertex<String> c = graph.insertVertex("C");

    // use the vertex iterator to get a list of the vertices in the actual
    // graph
    List<Vertex<String>> actualVertices = new ArrayList<Vertex<String>>();
    Iterator<Vertex<String>> it = graph.vertices();
    while (it.hasNext()) {
      actualVertices.add(it.next());
    }

    // assert that the graph state is consistent with what is expected
    assert (actualVertices.size() == 3);
    assert (actualVertices.contains(a));
    assert (actualVertices.contains(b));
    assert (actualVertices.contains(c));
  }

  /**
   * A simple test for insertEdges that adds 3 vertices, adds two edges to the
   * graph and then asserts that both edges were in fact added using the edge
   * iterator as well as checks to make sure their from and to vertices were set
   * correctly.
   */
  @Test
  public void testInsertEdges() {
    EdgeSetGraph<String> graph = new EdgeSetGraph<String>();
    Vertex<String> a = graph.insertVertex("A");
    Vertex<String> b = graph.insertVertex("B");
    Vertex<String> c = graph.insertVertex("C");

    // use the edge iterator to get a list of the edges in the actual graph.
    Edge<String> ab = graph.insertEdge(a, b, 1, null);
    Edge<String> bc = graph.insertEdge(b, c, 2, null);

    // use the edge iterator to get a list of the edges in the actual graph.
    List<Edge<String>> actualEdges = new ArrayList<Edge<String>>();
    Iterator<Edge<String>> it = graph.edges();
    while (it.hasNext()) {
      actualEdges.add(it.next());
    }

    // assert that the graph state is consistent with what is expected.
    assert (actualEdges.size() == 2);
    assert (actualEdges.contains(ab));
    assert (actualEdges.contains(bc));
  }

  /**
   * To test the functionality of the remove vertex method, in removing one
   * vertex. In addition to checking the vertex is no longer in the graph, we
   * must also check that all its incident edges are removed.
   */
  @Test
  public void testRemoveVertex() {
    EdgeSetGraph<String> graph = new EdgeSetGraph<String>();
    Vertex<String> a = graph.insertVertex("A");
    Vertex<String> b = graph.insertVertex("B");
    Vertex<String> c = graph.insertVertex("C");

    Edge<String> ab = graph.insertEdge(a, b, 1, null);
    Edge<String> bc = graph.insertEdge(b, c, 2, null);

    graph.removeVertex(a);

    List<Vertex<String>> actualVertices = new ArrayList<Vertex<String>>();
    Iterator<Vertex<String>> it1 = graph.vertices();
    while (it1.hasNext()) {
      actualVertices.add(it1.next());
    }
    List<Edge<String>> actualEdges = new ArrayList<Edge<String>>();
    Iterator<Edge<String>> it2 = graph.edges();
    while (it2.hasNext()) {
      actualEdges.add(it2.next());
    }

    assert (actualVertices.size() == 2);
    assert (!actualVertices.contains(a));
    assert (actualVertices.contains(b));
    assert (actualVertices.contains(c));

    assert (actualEdges.size() == 1);
    assert (!actualEdges.contains(ab));
    assert (actualEdges.contains(bc));
  }

  /**
   * To test the functionality of the remove vertex method in removing multiple
   * vertices and then adding more. Again we must check for the removal of
   * incident edges.
   */
  @Test
  public void testAddAndRemoveVerticesThenAdd() {
    EdgeSetGraph<String> graph = new EdgeSetGraph<String>();
    Vertex<String> a = graph.insertVertex("A");
    Vertex<String> b = graph.insertVertex("B");
    Vertex<String> c = graph.insertVertex("C");
    Vertex<String> d = graph.insertVertex("D");
    Vertex<String> e = graph.insertVertex("E");

    Edge<String> ab = graph.insertEdge(a, b, 1, null);
    Edge<String> bc = graph.insertEdge(b, c, 2, null);
    Edge<String> bd = graph.insertEdge(b, d, 4, null);
    Edge<String> be = graph.insertEdge(b, e, 7, null);
    Edge<String> de = graph.insertEdge(d, e, 2, null);

    graph.removeVertex(a);
    graph.removeVertex(b); // Now de should be only edge that remains
    graph.removeVertex(d); // Now no edges should remain

    Vertex<String> f = graph.insertVertex("F");
    Vertex<String> g = graph.insertVertex("G");
    Edge<String> cf = graph.insertEdge(c, f, 3, null);
    Edge<String> fg = graph.insertEdge(f, g, 5, null);

    List<Vertex<String>> actualVertices = new ArrayList<Vertex<String>>();
    Iterator<Vertex<String>> it1 = graph.vertices();
    while (it1.hasNext()) {
      actualVertices.add(it1.next());
    }
    List<Edge<String>> actualEdges = new ArrayList<Edge<String>>();
    Iterator<Edge<String>> it2 = graph.edges();
    while (it2.hasNext()) {
      actualEdges.add(it2.next());
    }

    assert (actualVertices.size() == 4);
    assert (!actualVertices.contains(a));
    assert (!actualVertices.contains(b));
    assert (!actualVertices.contains(d));
    assert (actualVertices.contains(c));
    assert (actualVertices.contains(e));
    assert (actualVertices.contains(f));
    assert (actualVertices.contains(g));

    assert (actualEdges.size() == 2);
    assert (!actualEdges.contains(ab));
    assert (!actualEdges.contains(bc));
    assert (!actualEdges.contains(bd));
    assert (!actualEdges.contains(be));
    assert (!actualEdges.contains(de));
    assert (actualEdges.contains(cf));
    assert (actualEdges.contains(fg));
  }

  /**
   * To test the functionality of the remove edge method. This method should
   * just remove the edge, doing nothing to the vertices in the graph. First, we
   * insert three edges then remove one - checking that this edge is in fact
   * removed. Then we remove one of the vertices which removes another edge and
   * add two more edges. After removing one of those edges and the one previous
   * edge, we check that there is in fact only one edge left and only one vertex
   * removed.
   */
  @Test
  public void testRemoveEdge() {
    EdgeSetGraph<String> graph = new EdgeSetGraph<String>();
    Vertex<String> a = graph.insertVertex("A");
    Vertex<String> b = graph.insertVertex("B");
    Vertex<String> c = graph.insertVertex("C");

    Edge<String> ab = graph.insertEdge(a, b, 10, null);
    Edge<String> bc = graph.insertEdge(b, c, 2, null);
    Edge<String> ac = graph.insertEdge(a, c, 7, null);

    graph.removeEdge(ab);

    List<Vertex<String>> actualVertices = new ArrayList<Vertex<String>>();
    Iterator<Vertex<String>> it1 = graph.vertices();
    while (it1.hasNext()) {
      actualVertices.add(it1.next());
    }
    List<Edge<String>> actualEdges = new ArrayList<Edge<String>>();
    Iterator<Edge<String>> it2 = graph.edges();
    while (it2.hasNext()) {
      actualEdges.add(it2.next());
    }

    assert (actualVertices.size() == 3);
    assert (actualEdges.size() == 2);
    assert (!actualEdges.contains(ab));

    graph.removeVertex(b); // ac should be the only edge remaining
    Vertex<String> d = graph.insertVertex("D");
    Edge<String> ad = graph.insertEdge(a, d, 9, null);
    Edge<String> cd = graph.insertEdge(c, d, 2, null);

    graph.removeEdge(ac);
    graph.removeEdge(ad);

    List<Vertex<String>> actualVertices1 = new ArrayList<Vertex<String>>();
    Iterator<Vertex<String>> it3 = graph.vertices();
    while (it3.hasNext()) {
      actualVertices1.add(it3.next());
    }
    List<Edge<String>> actualEdges1 = new ArrayList<Edge<String>>();
    Iterator<Edge<String>> it4 = graph.edges();
    while (it4.hasNext()) {
      actualEdges1.add(it4.next());
    }

    assert (actualVertices1.size() == 3);
    assert (actualVertices1.contains(a));
    assert (actualVertices1.contains(c));
    assert (actualVertices1.contains(d));
    assert (!actualVertices1.contains(b));

    assert (actualEdges1.size() == 1);
    assert (actualEdges1.contains(cd));
    assert (!actualEdges1.contains(ab));
    assert (!actualEdges1.contains(bc));
    assert (!actualEdges1.contains(ac));
    assert (!actualEdges1.contains(ad));
  }

  /**
   * To test the functionality of the connectingEdge method. We test here that
   * if there exists an edge between two vertices in the graph, the
   * connectingEdge method will return the edge given the two vertices.
   */
  @Test
  public void testConnectingEdge() {
    EdgeSetGraph<String> graph = new EdgeSetGraph<String>();
    Vertex<String> a = graph.insertVertex("A");
    Vertex<String> b = graph.insertVertex("B");
    Vertex<String> c = graph.insertVertex("C");

    Edge<String> ab = graph.insertEdge(a, b, 22, null);
    Edge<String> bc = graph.insertEdge(b, c, 2, null);

    assert (graph.connectingEdge(a, b).equals(ab));
    assert (graph.connectingEdge(b, c).equals(bc));

    graph.removeEdge(ab);
    Edge<String> ac = graph.insertEdge(a, c, 3, null);

    assert (graph.connectingEdge(a, c).equals(ac));
  }

  /**
   * To test the functionality of the incidentEdges method. We test here that if
   * there exists an edge between two vertices in the graph, the connectingEdge
   * method will return the edge given the two vertices. We ensure that the
   * method returns the right iterator even after removing edges or adjacent
   * vertices.
   */
  @Test
  public void testIncidentEdges() {
    EdgeSetGraph<String> graph = new EdgeSetGraph<String>();
    Vertex<String> a = graph.insertVertex("A");
    Vertex<String> b = graph.insertVertex("B");
    Vertex<String> c = graph.insertVertex("C");
    Vertex<String> d = graph.insertVertex("D");
    Vertex<String> e = graph.insertVertex("E");

    Edge<String> ab = graph.insertEdge(a, b, 1, null);
    Edge<String> bc = graph.insertEdge(b, c, 2, null);
    Edge<String> bd = graph.insertEdge(b, d, 4, null);
    Edge<String> be = graph.insertEdge(b, e, 7, null);
    Edge<String> de = graph.insertEdge(d, e, 2, null);

    List<Edge<String>> incidentEdges = new ArrayList<Edge<String>>();
    Iterator<Edge<String>> it = graph.incidentEdges(b);
    while (it.hasNext()) {
      incidentEdges.add(it.next());
    }

    assert (incidentEdges.size() == 4);
    assert (incidentEdges.contains(ab));
    assert (incidentEdges.contains(bc));
    assert (incidentEdges.contains(bd));
    assert (incidentEdges.contains(be));
    assert (!incidentEdges.contains(de));

    graph.removeEdge(bc);
    graph.removeVertex(d); // This should also remove the edge bd

    List<Edge<String>> incidentEdges1 = new ArrayList<Edge<String>>();
    Iterator<Edge<String>> it1 = graph.incidentEdges(b);
    while (it1.hasNext()) {
      incidentEdges1.add(it1.next());
    }

    assert (incidentEdges1.size() == 2);
    assert (!incidentEdges1.contains(bc));
    assert (!incidentEdges1.contains(bd));
  }

  /**
   * To test the functionality of the opposite method. We test here that the
   * method in fact returns the vertex opposite the given vertex on the given
   * edge. We make sure to test that no matter which vertex of the two on an
   * edge is given, the opposite one is returned.
   */
  @Test
  public void testOpposite() {
    EdgeSetGraph<String> graph = new EdgeSetGraph<String>();
    Vertex<String> a = graph.insertVertex("A");
    Vertex<String> b = graph.insertVertex("B");
    Vertex<String> d = graph.insertVertex("D");
    Vertex<String> e = graph.insertVertex("E");

    Edge<String> ab = graph.insertEdge(a, b, 1, null);
    Edge<String> bd = graph.insertEdge(b, d, 4, null);
    Edge<String> be = graph.insertEdge(b, e, 7, null);
    Edge<String> de = graph.insertEdge(d, e, 2, null);

    assert (graph.opposite(b, ab).equals(a));
    assert (graph.opposite(a, ab).equals(b));
    assert (graph.opposite(d, de).equals(e));
    assert (graph.opposite(e, de).equals(d));

    graph.removeEdge(bd);
    graph.removeVertex(d);

    assert (graph.opposite(b, be).equals(e));
    assert (graph.opposite(e, be).equals(b));
  }

  /**
   * To test the functionality of the endVertices method. We test that given an
   * edge, the method returns the two vertices connected by the edge (there
   * should always be two vertices).
   */
  @Test
  public void testEndVertices() {
    EdgeSetGraph<String> graph = new EdgeSetGraph<String>();
    Vertex<String> a = graph.insertVertex("A");
    Vertex<String> b = graph.insertVertex("B");
    Vertex<String> c = graph.insertVertex("C");
    Vertex<String> d = graph.insertVertex("D");
    Vertex<String> e = graph.insertVertex("E");

    Edge<String> ab = graph.insertEdge(a, b, 1, null);
    Edge<String> bc = graph.insertEdge(b, c, 4, null);

    assert (graph.endVertices(ab).contains(a));
    assert (graph.endVertices(ab).contains(b));
    assert (!graph.endVertices(ab).contains(c));

    graph.removeVertex(b);
    Edge<String> ac = graph.insertEdge(a, c, 77, null);

    assert (graph.endVertices(ac).contains(a));
    assert (!graph.endVertices(ac).contains(b));
    assert (graph.endVertices(ac).contains(c));
  }

  /**
   * To test the functionality of the areAdjacent method. We test that given two
   * vertices, the method returns whether they are connected by an edge. Here,
   * we make sure that if we remove an edge that connected two vertices,
   * areAdjacent will now return false if given those vertices.
   */
  @Test
  public void testAreAdjacent() {
    EdgeSetGraph<String> graph = new EdgeSetGraph<String>();
    Vertex<String> a = graph.insertVertex("A");
    Vertex<String> b = graph.insertVertex("B");
    Vertex<String> c = graph.insertVertex("C");
    Vertex<String> d = graph.insertVertex("D");
    Vertex<String> e = graph.insertVertex("E");

    Edge<String> ab = graph.insertEdge(a, b, 1, null);
    Edge<String> ac = graph.insertEdge(a, c, 4, null);

    assert (graph.areAdjacent(a, c));
    assert (graph.areAdjacent(c, a));
    assert (graph.areAdjacent(a, b));
    assert (graph.areAdjacent(b, a));
    assert (!graph.areAdjacent(b, c));
    assert (!graph.areAdjacent(c, b));

    graph.removeEdge(ac);

    assert (!graph.areAdjacent(a, c));
    assert (!graph.areAdjacent(c, a));
  }

  /**
   * To test the functionality of clear() method. We simply test that there are
   * no more vertices and edges in the graph after we call the method - and then
   * we can continue to add vertices and edges afterwards with normal
   * functionality.
   */
  @Test
  public void testClear() {
    EdgeSetGraph<String> graph = new EdgeSetGraph<String>();
    Vertex<String> a = graph.insertVertex("A");
    Vertex<String> b = graph.insertVertex("B");
    Vertex<String> c = graph.insertVertex("C");

    Edge<String> ab = graph.insertEdge(a, b, 1, null);
    Edge<String> ac = graph.insertEdge(a, c, 4, null);

    graph.clear();

    List<Vertex<String>> actualVertices = new ArrayList<Vertex<String>>();
    Iterator<Vertex<String>> it1 = graph.vertices();
    while (it1.hasNext()) {
      actualVertices.add(it1.next());
    }
    List<Edge<String>> actualEdges = new ArrayList<Edge<String>>();
    Iterator<Edge<String>> it2 = graph.edges();
    while (it2.hasNext()) {
      actualEdges.add(it2.next());
    }

    assert (actualVertices.size() == 0);
    assert (actualEdges.size() == 0);

    Vertex<String> d = graph.insertVertex("D");
    Vertex<String> e = graph.insertVertex("E");
    Vertex<String> f = graph.insertVertex("F");

    Edge<String> de = graph.insertEdge(d, e, 1, null);
    graph.removeVertex(f);

    List<Vertex<String>> actualVertices1 = new ArrayList<Vertex<String>>();
    Iterator<Vertex<String>> it3 = graph.vertices();
    while (it3.hasNext()) {
      actualVertices1.add(it3.next());
    }
    List<Edge<String>> actualEdges1 = new ArrayList<Edge<String>>();
    Iterator<Edge<String>> it4 = graph.edges();
    while (it4.hasNext()) {
      actualEdges1.add(it4.next());
    }

    assert (actualVertices1.size() == 2);
    assert (actualVertices1.contains(d));
    assert (actualVertices1.contains(e));

    assert (actualEdges1.size() == 1);
    assert (actualEdges1.contains(de));
  }

}
