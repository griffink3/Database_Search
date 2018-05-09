package edu.brown.cs.gk16;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * This class exists to contain the dijkstra's algorithm.
 *
 * @param <V>
 *          object to be contained in the vertex
 */
public class Dijkstra<V> {

  private EdgeSetGraph<V> graph;

  /**
   * @param g
   *          graph upon which to perform dijkstra's
   */
  public Dijkstra(EdgeSetGraph<V> g) {
    graph = g;
  }

  /**
   * The implementation dijkstra's algorithm to find the shortest path in a
   * graph. Adds the edges if they're not already existent as it searches the
   * graph. Returns a map that maps a vertex to the previous vertex in the path.
   *
   * @param v1
   *          the first vertex
   * @param v2
   *          the final vertex
   * @return map mapping each vertex in the shortest path to the previous vertex
   *         in the path
   */
  @SuppressWarnings("unchecked")
  public Map<Vertex<V>, Vertex<V>> findShortestPath(Vertex<V> v1,
      Vertex<V> v2) {
    Map<Vertex<V>, Vertex<V>> previous = new HashMap<Vertex<V>, Vertex<V>>();
    PriorityQueue<Vertex<V>> nodes = new PriorityQueue<Vertex<V>>(graph.size(),
        (x, y) -> Double.compare(x.distance(), y.distance()));
    Set<Vertex<V>> visited = new HashSet<Vertex<V>>();
    v1.setDist(0);
    nodes.add(v1);
    graph = v1.createEdges(graph);
    while (!nodes.isEmpty()) {
      Vertex<V> closest = nodes.poll();
      visited.add(closest);
      graph = closest.createEdges(graph);
      if (closest.equals(v2) || closest.distance() == Double.MAX_VALUE) {
        return previous;
      }
      List<Edge<V>> list = closest.incidentEdges();
      for (Edge<V> edge : list) {
        Vertex<V> neighbor = edge.getToVertex();
        if (!visited.contains(neighbor)) {
          if (!nodes.contains(neighbor)) {
            neighbor.setDist(Double.MAX_VALUE);
          }
          Double alt = closest.distance() + edge.weight();
          if (alt <= neighbor.distance()) {
            if (nodes.contains(neighbor)) {
              nodes.remove(neighbor);
            }
            neighbor.setDist(alt);
            nodes.add(neighbor);
            previous.put(neighbor, closest);
          }
        }
      }
    }
    return previous;
  }

  /**
   * @return returns the graph object (useful if altered by dijkstra's)
   */
  public EdgeSetGraph<V> getGraph() {
    return graph;
  }
}
