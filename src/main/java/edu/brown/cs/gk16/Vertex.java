package edu.brown.cs.gk16;

import java.util.ArrayList;
import java.util.List;

/**
 * The representation of a graph vertex. It stores an element, a unique vertex
 * number, a distance value, and the incident edges (edges going away).
 *
 * @param <V>
 *          object to be contained in the vertex
 */
public class Vertex<V> {

  private V object;
  private int num;
  private double dist;
  private List<Edge<V>> edges;

  /**
   * Takes in the vertex element (can be null) and a number - this number should
   * be unique.
   *
   * @param v
   *          object to be contained in the vertex
   * @param number
   *          unique vertex number
   */
  public Vertex(V v, int number) {
    object = v;
    num = number;
    dist = 0;
    edges = new ArrayList<Edge<V>>();
  }

  /**
   * @return the contained element
   */
  public V element() {
    return object;
  }

  /**
   * @param v
   *          element to which to set the contained element
   */
  public void setElement(V v) {
    object = v;
  }

  /**
   * @return the unique vertex number
   */
  public int vertNum() {
    return num;
  }

  /**
   * @param number
   *          int to set to be the unique vertex number
   */
  public void setVertNum(int number) {
    num = number;
  }

  /**
   * @return the stored distance value
   */
  public double distance() {
    return dist;
  }

  /**
   * @param distance
   *          value to which to set the distance value
   */
  public void setDist(double distance) {
    dist = distance;
  }

  /**
   * @param e
   *          incident edge to add to the incident edge list
   */
  public void addEdge(Edge<V> e) {
    if (!edges.contains(e)) {
      edges.add(e);
    }
  }

  /**
   * @param e
   *          incident edge to remove from the incident edge list
   */
  public void removeEdge(Edge<V> e) {
    edges.remove(e);
  }

  /**
   * @return a list of the edges incident (going away) to this vertex
   */
  public List<Edge<V>> incidentEdges() {
    return edges;
  }

  /**
   * Wrapper for subclass method.
   *
   * @param graph
   *          edgeset graph to be passed in
   * @return graph
   */
  public EdgeSetGraph<V> createEdges(EdgeSetGraph<V> graph) {
    return graph;
  }

  /**
   * Wrapper for subclass method.
   *
   * @return the vertex name
   */
  public String name() {
    return "";
  }

}
