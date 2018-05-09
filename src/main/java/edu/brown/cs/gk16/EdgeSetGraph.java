package edu.brown.cs.gk16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class defines a Graph that tracks its edges through the use of an edge
 * set.
 *
 * @param <V>
 *          object to be contained in the graph
 */
public class EdgeSetGraph<V> {

  private Map<Edge<V>, Edge<V>> edges;

  private Map<Integer, Vertex<V>> vertices;

  private Set<Edge<V>> allEdges;

  private LinkedList<Integer> availableNums;

  /**
   * No parameters. Constructor for graph where the edgeset and the set of
   * vertices are initialized.
   */
  public EdgeSetGraph() {
    edges = new HashMap<Edge<V>, Edge<V>>();
    vertices = new HashMap<Integer, Vertex<V>>();
    allEdges = new HashSet<Edge<V>>();
    availableNums = new LinkedList<Integer>();
  }

  /**
   * Returns an iterator holding all the Vertices of the graph.
   *
   * @return an Iterator containing the vertices of the Graph.
   */
  public Iterator<Vertex<V>> vertices() {
    return vertices.values().iterator();
  }

  /**
   * Returns an iterator holding all the edges of the graph.
   *
   * @return an Iterator containing the edges of the Graph.
   */
  public Iterator<Edge<V>> edges() {
    return allEdges.iterator();
  }

  /**
   * Inserts a new Vertex into the Graph.
   *
   * @param vertElement
   *          the element to be added to the graph as a vertex
   * @return the vertex that was just inserted
   */
  public Vertex<V> insertVertex(V vertElement) {
    int vertNum = 0;
    if (availableNums.size() == 0) {
      vertNum = vertices.size();
    } else {
      vertNum = availableNums.remove();
    }
    Vertex<V> vertex = new Vertex<V>(vertElement, vertNum);
    vertices.put(vertNum, vertex);
    return vertex;
  }

  /**
   * Inserts a new Vertex (already instantiated) into the Graph.
   *
   * @param vert
   *          vertex to insert into the graph
   * @param num
   *          the unique vertex number for this vertex
   */
  public void insertVertex(Vertex<V> vert, int num) {
    vertices.put(num, vert);
  }

  /**
   * Gets the next available vert number.
   *
   * @return the next available vert number
   */
  public int getVertNum() {
    if (availableNums.size() == 0) {
      return vertices.size();
    } else {
      return availableNums.remove();
    }
  }

  /**
   * Inserts a new Edge into your Graph.
   *
   * @param v1
   *          The first vertex of the edge connection.
   * @param v2
   *          The second vertex of the edge connection.
   * @param edgeWeight
   *          the weight of the newly inserted edge
   * @param edgeElement
   *          The weight of the newly inserted edge.
   * @return the edge that was just inserted
   */
  public Edge<V> insertEdge(Vertex<V> v1, Vertex<V> v2, double edgeWeight,
      String edgeElement) {
    Edge<V> edge = new Edge<V>(edgeElement, edgeWeight);
    edge.setFromVertex(v1);
    edge.setToVertex(v2);
    if (edges.containsKey(edge) && edges.get(edge).weight() < edgeWeight) {
      return edges.get(edge);
    } else {
      v1.removeEdge(edges.get(edge));
      v1.addEdge(edge);
      edges.put(edge, edge);
      allEdges.add(edge);
      return edge;
    }
  }

  /**
   * Removes a Vertex from your graph. Looks through all of the edges, and
   * removes it if it is adjacent to the vertex to be removed. In addition,
   * updates set of vertices.
   *
   * @param vert
   *          The Vertex to remove.
   * @return The element of the removed Vertex.
   */
  public V removeVertex(Vertex<V> vert) {
    Iterator<Edge<V>> itEdges = edges();
    // Using an arraylist in addition to the iterator to avoid
    // any ConcurrentModificationExceptions - this occurs if we try to
    // remove elements from a collection while we iterate over the collection
    ArrayList<Edge<V>> edgesList = new ArrayList<Edge<V>>();
    while (itEdges.hasNext()) {
      edgesList.add(itEdges.next());
    }
    for (int i = 0; i < edgesList.size(); i++) {
      if (edgesList.get(i).getFromVertex().equals(vert)
          || edgesList.get(i).getToVertex().equals(vert)) {
        edges.remove(edgesList.get(i));
        allEdges.remove(edgesList.get(i));
      }
    }
    vertices.remove(vert.vertNum());
    availableNums.add(vert.vertNum());
    return vert.element();
  }

  /**
   * Removes an Edge from your Graph.
   *
   * @param edge
   *          The Edge to remove.
   * @return The weight of the removed Edge.
   */
  public String removeEdge(Edge<V> edge) {
    Vertex<V> v1 = edge.getFromVertex();
    v1.removeEdge(edge);
    edges.remove(edge);
    allEdges.remove(edge);
    return edge.element();
  }

  /**
   * Returns the edge that connects the two vertices. Uses the areAdjacent
   * method. Queries the set of Edges using an imposter edge to check for the
   * existence of an edge with endpoints v1 and v2.
   *
   * @param v1
   *          The first vertex that may be connected.
   * @param v2
   *          The second vertex that may be connected.
   * @return The edge that connects the first and second vertices.
   */
  public Edge<V> connectingEdge(Vertex<V> v1, Vertex<V> v2) {
    // Creating two fake edges with arbitrary weights that represent the
    // possible combination of end vertices that an edge connecting the two
    // given points could have
    Edge<V> fakeEdge1 = new Edge<V>(null, 0);
    fakeEdge1.setFromVertex(v1);
    fakeEdge1.setToVertex(v2);
    Edge<V> fakeEdge2 = new Edge<V>(null, 0);
    fakeEdge2.setFromVertex(v2);
    fakeEdge2.setToVertex(v1);
    // Then we check that such an edge exists in our edge set
    if (!edges.containsKey(fakeEdge1) && !edges.containsKey(fakeEdge2)) {
      return null;
    } else {
      // If the edge exists, we get it from the hash map and return it
      if (edges.containsKey(fakeEdge1)) {
        return edges.get(fakeEdge1);
      } else {
        return edges.get(fakeEdge2);
      }
    }
  }

  /**
   * Returns an Iterator over all the Edges that are connected to this Vertex.
   *
   * @param vert
   *          The vertex to find the incident edges on.
   * @return Returns an Iterator holding the incident edges on v.
   */
  public Iterator<Edge<V>> incidentEdges(Vertex<V> vert) {
    ArrayList<Edge<V>> incidentEdges = new ArrayList<Edge<V>>();
    Iterator<Edge<V>> itEdges = edges();
    while (itEdges.hasNext()) {
      Edge<V> next = itEdges.next();
      if (next.getFromVertex().equals(vert)
          || next.getToVertex().equals(vert)) {
        incidentEdges.add(next);
      }
    }
    return incidentEdges.iterator();
  }

  /**
   * Returns the Vertex that is on the other side of the given Edge opposite of
   * Vertex vert. If the edge is not incident on vert, then returns null.
   *
   * @param vert
   *          The first vertex on Edge e.
   * @param edge
   *          The edge connecting Vertex v and the unknown opposite Vertex.
   * @return The opposite Vertex of v across Edge e.
   */
  public Vertex<V> opposite(Vertex<V> vert, Edge<V> edge) {
    // The vertex must be either the edge's to or from vertex to be connected
    if (!edge.getFromVertex().equals(vert)
        && !edge.getToVertex().equals(vert)) {
      return null;
    } else if (edge.getFromVertex().equals(vert)) {
      return edge.getToVertex();
    } else {
      return edge.getFromVertex();
    }
  }

  /**
   * Returns the two Vertex's as a list that the Edge edge is incident upon.
   *
   * @param edge
   *          The edge to find the connecting Vertex's on.
   * @return a list of Vertex's holding the two connecting vertices.
   */
  public List<Vertex<V>> endVertices(Edge<V> edge) {
    ArrayList<Vertex<V>> endVertices = new ArrayList<Vertex<V>>();
    // Every edge in the graph should be incident on two vertices, since when
    // we remove a vertex, we remove all of its incident edges
    endVertices.add(edge.getFromVertex());
    endVertices.add(edge.getToVertex());
    return endVertices;
  }

  /**
   * Returns true if there exists an Edge that connects Vertex v1 and Vertex v2.
   *
   * @param v1
   *          The first Vertex to test adjacency.
   * @param v2
   *          The second Vertex to test adjacency.
   * @return Returns true if the vertices are adjacent.
   */
  public boolean areAdjacent(Vertex<V> v1, Vertex<V> v2) {
    if (connectingEdge(v1, v2) == null) {
      return false;
    }
    return true;
  }

  /**
   * Clears all the vertices and edges from the graph.
   */
  public void clear() {
    // We empty all our data structures
    edges.clear();
    vertices.clear();
    allEdges.clear();
    availableNums.clear();
  }

  /**
   * @return the number of vertices in the graph
   */
  public int size() {
    return vertices.size();
  }

  /**
   * @param vertNum
   *          the vertex number of the vertex to get
   * @return the vertex of the vertNum
   */
  public Vertex<V> getVert(int vertNum) {
    return vertices.get(vertNum);
  }

}
