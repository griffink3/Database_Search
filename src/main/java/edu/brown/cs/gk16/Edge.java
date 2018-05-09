package edu.brown.cs.gk16;

/**
 * The representation of a graph edge. It stores a weight and the two incident
 * vertices.
 *
 * @param <V>
 *          object to be contained in edge
 */
public class Edge<V> {

  private double weight;
  private Vertex<V> fromVertex;
  private Vertex<V> toVertex;
  private String element;

  /**
   * Just takes in the weight of the edge.
   *
   * @param v
   *          the element to be contained in this edge
   * @param i
   *          the weight of the edge
   */
  public Edge(String v, double i) {
    weight = i;
    element = v;
  }

  /**
   * @return the weight of the edge
   */
  public double weight() {
    return weight;
  }

  /**
   * @param i
   *          int to set weight
   */
  public void setWeight(int i) {
    weight = i;
  }

  /**
   * @return the string stored in the edge
   */
  public String element() {
    return element;
  }

  /**
   * @param v
   *          the element to be contained in the edge
   */
  public void setElement(String v) {
    element = v;
  }

  /**
   * @return the fromVertex
   */
  public Vertex<V> getFromVertex() {
    return fromVertex;
  }

  /**
   * @return the toVertex
   */
  public Vertex<V> getToVertex() {
    return toVertex;
  }

  /**
   * @param v
   *          vertex to which to set the fromVertex
   */
  public void setFromVertex(Vertex<V> v) {
    fromVertex = v;
  }

  /**
   * @param v
   *          vertex to which to set the toVertex
   */
  public void setToVertex(Vertex<V> v) {
    toVertex = v;
  }

  /**
   * Overrides the hashcode function so that edges with the same vertices can be
   * used as equal keys in a hashmap.
   */
  @Override
  public int hashCode() {
    return toVertex.vertNum();
  }

  /**
   * @param other
   *          Object to compare with the current edge
   */
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Edge)) {
      return false;
    } else {
      @SuppressWarnings("unchecked")
      Edge<V> e = (Edge<V>) other;
      return e.getFromVertex().equals(fromVertex)
          && e.getToVertex().equals(toVertex);
    }
  }

}
