package edu.brown.cs.gk16;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.common.cache.LoadingCache;

import edu.brown.cs.gk16.bacon.BaconObject;

/**
 * A subclass of the graph vertex superclass, the actor class represents a
 * vertex in the bacon object graph. In addition to the functionality of a graph
 * vertex, the actor subclass also has a method to create its outgoing edges in
 * the graph.
 *
 * @param <V>
 *          object to be contained in the actor
 */
public class Actor<V> extends Vertex<V> {

  private String actorId;
  private static BaconObject bacon;
  private boolean edgesCreated;
  private String name;
  private static LoadingCache<String, String> actorCache;
  private static LoadingCache<String, Double> filmCountCache;

  /**
   * Takes in the vertex element (can be null) and a number - this number should
   * be unique — like the vertex super class. Also takes in the graph and bacon
   * object which will contain this instance of actor.
   *
   * @param v
   *          object to be contained in the vertex
   * @param number
   *          unique vertex number
   * @param b
   *          the BaconObject that contains this actor
   * @param c
   *          the loading cache for actor ids to actor names
   * @param f
   *          the loading cache for film ids to actor count
   */
  public Actor(V v, int number, BaconObject b, LoadingCache<String, String> c,
      LoadingCache<String, Double> f) {
    super(v, number);
    actorId = (String) super.element();
    edgesCreated = false;
    bacon = b;
    name = null;
    actorCache = c;
    filmCountCache = f;
  }

  /**
   * Creates the incident edges (outgoing edges) from this actor based the given
   * database. To elaborate, an edge should be added whenever a co-actor's first
   * name begins with the same letter as this actor's last name, so gets the
   * possible recipients to which the actor specified by the given actorId could
   * given the bacon to. Queries the database for the list of movies that the
   * actor acted in and for each co-actor, determines if they meet the specified
   * criteria (first name must begin with same letter as the donor's last name).
   * Creates an edge in the graph for each possible handoff.
   *
   * @param graph
   *          graph to add the new incident edges to
   */
  @Override
  public EdgeSetGraph<V> createEdges(EdgeSetGraph<V> graph) {
    if (!edgesCreated) {
      edgesCreated = true;
      PreparedStatement prep;
      name = name();
      try {
        prep = bacon.getConn().prepareStatement("SELECT * FROM actor_film "
            + "AS a, actor_film AS b WHERE b.actor = ? AND a.film = b.film;");
        prep.setString(1, actorId);
        ResultSet rs = prep.executeQuery();
        String poss;
        String film;
        while (rs.next()) {
          poss = rs.getString(1);
          if (!poss.equals(actorId)) {
            Vertex<V> v2 = graph.getVert(bacon.getVertNum(poss));
            film = rs.getString(2);
            if (BaconObject.checkFirstLetters(name, v2.name())) {
              graph.insertEdge(graph.getVert(bacon.getVertNum(actorId)), v2,
                  filmCountCache.getUnchecked(film), film);
            }
          }
        }
        rs.close();
        prep.close();
      } catch (SQLException e) {
        return graph;
      }
    }
    return graph;
  }

  /**
   * @return the name of the actor
   */
  @Override
  public String name() {
    if (name == null) {
      // name = bacon.getCacheActorName(actorId);
      name = actorCache.getUnchecked(actorId);
    }
    return name;
  }

  /**
   * @return the actor cache
   */
  public static LoadingCache<String, String> actorCache() {
    return actorCache;
  }

  /**
   * @return the film count cache
   */
  public static LoadingCache<String, Double> filmCountCache() {
    return filmCountCache;
  }

}
