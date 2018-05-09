package edu.brown.cs.gk16.bacon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import edu.brown.cs.gk16.Actor;
import edu.brown.cs.gk16.Dijkstra;
import edu.brown.cs.gk16.Edge;
import edu.brown.cs.gk16.EdgeSetGraph;
import edu.brown.cs.gk16.Interpreter;
import edu.brown.cs.gk16.Vertex;

/**
 * This class is the representation of the autocorrect object. That means it
 * stores all the corpus information in a trie and all the autocorrect settings
 * and handles the autocorrect algorithms with the trie.
 */
public class BaconObject {

  private Connection conn;
  private EdgeSetGraph<String> graph;
  private Map<String, Integer> idToNum;
  private Set<Vertex<String>> passesAdded;
  private LoadingCache<String, String> cacheActorName;
  private LoadingCache<String, String> cacheFilmName;
  private LoadingCache<String, Double> cacheFilmCount;

  /**
   * Takes in no parameters.
   */
  public BaconObject() {
    conn = null;
    graph = new EdgeSetGraph<String>();
    idToNum = new HashMap<String, Integer>();
    passesAdded = new HashSet<Vertex<String>>();
    // Creating a cache for the actor names from id
    CacheLoader<String, String> loader;
    loader = new CacheLoader<String, String>() {
      @Override
      public String load(String key) {
        return queryTable(key, "name", "id", "actor");
      }
    };
    cacheActorName = CacheBuilder.newBuilder().build(loader);
    loader = new CacheLoader<String, String>() {
      @Override
      public String load(String key) {
        return queryTable(key, "name", "id", "film");
      }
    };
    cacheFilmName = CacheBuilder.newBuilder().build(loader);
    CacheLoader<String, Double> loader1 = new CacheLoader<String, Double>() {
      @Override
      public Double load(String key) {
        return 1.0 / Double
            .parseDouble(queryTable(key, "COUNT(actor)", "film", "actor_film"));
      }
    };
    cacheFilmCount = CacheBuilder.newBuilder().build(loader1);
  }

  /**
   * Opens and reads a txt file containing words that form the data upon which
   * the autocorrect object makes and ranks suggestions. Stores the words in the
   * trie and the frequencies of unigrams/bigrams/ngrams in hashmaps.
   *
   * @param file
   *          the file containing text
   * @return 0 if no error occurs, -1 if an error occurs
   */
  public int addDb(String file) {
    try {
      Class.forName("org.sqlite.JDBC");
      String urlToDb = "jdbc:sqlite:" + file;
      Connection currentConn = DriverManager.getConnection(urlToDb);
      Statement stat = currentConn.createStatement();
      stat.executeUpdate("PRAGMA foreign_keys = ON;");
      conn = currentConn;
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR: Error uploading database");
      return -1;
    } catch (SQLException e) {
      System.out.println("ERROR: Invalid database pathway specified");
      return -1;
    }
    graph.clear();
    idToNum.clear();
    passesAdded.clear();
    cacheActorName.invalidateAll();
    cacheFilmName.invalidateAll();
    cacheFilmCount.invalidateAll();
    addAllVertices();
    System.out.println("db set to " + file);
    return 0;
  }

  private void addAllVertices() {
    PreparedStatement prep;
    try {
      prep = conn.prepareStatement("SELECT id FROM actor;");
      ResultSet rs = prep.executeQuery();
      while (rs.next()) {
        String id = rs.getString(1);
        int num = graph.getVertNum();
        Actor<String> actor =
            new Actor<String>(id, num, this, cacheActorName, cacheFilmCount);
        graph.insertVertex(actor, num);
        idToNum.put(id, num);
      }
      rs.close();
      prep.close();
    } catch (SQLException e) {
      return;
    }
    return;
  }

  /**
   * Executes the connect command by searching for the shortest path between the
   * two actors specified by the strings actor1 and actor2 for which bacon to be
   * handed off.
   *
   * If the actors aren't found in the database, returns an empty list.
   *
   * @param actor1
   *          the name of the donor actor
   * @param actor2
   *          the name of the final recipient actor
   * @return list of strings detailing the individual handoffs in the shortest
   *         paths
   */
  public List<String> connect(String actor1, String actor2) {
    List<String> output = new ArrayList<String>();
    if (conn == null) {
      System.out.println("ERROR: No database has been uploaded");
      return output;
    }
    String id1 = queryTable(actor1, "id", "name", "actor");
    String id2 = queryTable(actor2, "id", "name", "actor");
    if (id1.equals("") || id2.equals("")) {
      System.out.println("ERROR: Invalid actor names");
      return output;
    }
    Dijkstra<String> d = new Dijkstra<String>(graph);
    Vertex<String> v1 = graph.getVert(idToNum.get(id1));
    Vertex<String> v2 = graph.getVert(idToNum.get(id2));
    Map<Vertex<String>, Vertex<String>> prevs = d.findShortestPath(v1, v2);
    graph = d.getGraph();
    cacheActorName = Actor.actorCache();
    cacheFilmCount = Actor.filmCountCache();
    Vertex<String> curr = v2;
    Vertex<String> prev = prevs.get(curr);
    while (prev != null) {
      Edge<String> e = graph.connectingEdge(prev, curr);
      StringBuilder str = new StringBuilder();
      str.append(prev.name());
      str.append(" -> ");
      str.append(curr.name());
      str.append(" : ");
      str.append(cacheFilmName.getUnchecked(e.element()));
      output.add(0, str.toString());
      if (prev.equals(v1)) {
        prev = null;
      } else {
        curr = prev;
        prev = prevs.get(curr);
      }
    }
    if (output.size() == 0) {
      StringBuilder str = new StringBuilder();
      str.append(actor1);
      str.append(" -/- ");
      str.append(actor2);
      output.add(str.toString());
    }
    return output;
  }

  /**
   * Executes the connect command by searching for the shortest path between the
   * two actors specified by the strings actor1 and actor2 for which bacon to be
   * handed off. Instead of returning the shortest path as a list however,
   * returns it as a list of lists of strings containing the donor, the
   * recipient, and the film in which they acted together (in that order).
   *
   * If the actors aren't found in the database, returns an empty list. If
   * there's no connection between the actors, returns a list of one list that
   * has just the two actor names.
   *
   * @param actor1
   *          the name of the donor actor
   * @param actor2
   *          the name of the final recipient actor
   * @return list of lists of strings detailing the shortest path
   */
  public List<List<String>> connectPieces(String actor1, String actor2) {
    List<List<String>> output = new ArrayList<List<String>>();
    if (conn == null) {
      System.out.println("ERROR: No database has been uploaded");
      return output;
    }
    String id1 = queryTable(actor1, "id", "name", "actor");
    String id2 = queryTable(actor2, "id", "name", "actor");
    if (id1.equals("") || id2.equals("")) {
      System.out.println("ERROR: Invalid actor names");
      return output;
    }
    Dijkstra<String> d = new Dijkstra<String>(graph);
    Vertex<String> v1 = graph.getVert(idToNum.get(id1));
    Vertex<String> v2 = graph.getVert(idToNum.get(id2));
    Map<Vertex<String>, Vertex<String>> prevs = d.findShortestPath(v1, v2);
    graph = d.getGraph();
    cacheActorName = Actor.actorCache();
    cacheFilmCount = Actor.filmCountCache();
    Vertex<String> curr = v2;
    Vertex<String> prev = prevs.get(curr);
    while (prev != null) {
      Edge<String> e = graph.connectingEdge(prev, curr);
      List<String> connect = new ArrayList<String>();
      connect.add(prev.name());
      connect.add(curr.name());
      connect.add(cacheFilmName.getUnchecked(e.element()));
      output.add(0, connect);
      if (prev.equals(v1)) {
        prev = null;
      } else {
        curr = prev;
        prev = prevs.get(curr);
      }
    }
    if (output.size() == 0) {
      List<String> connect = new ArrayList<String>();
      connect.add(actor1);
      connect.add(actor2);
      output.add(connect);
    }
    return output;
  }

  /**
   * Queries the database for an attribute value.
   *
   * @param name
   *          the value to set the parameter for which to search
   * @param getAttr
   *          the attribute of which to search for its value
   * @param parameter
   *          attribute to which to set as the parameter
   * @param table
   *          the table to query
   * @return the attribute value
   */
  public String queryTable(String name, String getAttr, String parameter,
      String table) {
    PreparedStatement prep;
    String attr = "";
    try {
      prep = conn.prepareStatement("SELECT " + getAttr + " FROM " + table
          + " WHERE " + parameter + " = ?;");
      prep.setString(1, name);
      ResultSet rs = prep.executeQuery();
      if (rs.next()) {
        attr = rs.getString(1);
      }
      rs.close();
      prep.close();
    } catch (SQLException e) {
      return "";
    }
    return attr;
  }

  /**
   * Queries the database for a list of films that an actor acts in or a list of
   * actors that act in a film.
   *
   * @param name
   *          the name of the actor or film
   * @param get
   *          "actor" or "film"
   * @param from
   *          "actor" or "film" (who has name)
   * @return the list of names of qualifying films or actors
   */
  public List<String> queryTableList(String name, String get, String from) {
    PreparedStatement prep;
    String id = queryTable(name, "id", "name", from);
    List<String> attr = new ArrayList<String>();
    try {
      prep = conn.prepareStatement(
          "SELECT " + get + " FROM actor_film WHERE " + from + " = ?;");
      prep.setString(1, id);
      ResultSet rs = prep.executeQuery();
      while (rs.next()) {
        attr.add(queryTable(rs.getString(1), "name", "id", get));
      }
      rs.close();
      prep.close();
    } catch (SQLException e) {
      return attr;
    }
    return attr;
  }

  /**
   * Given two strings representing two full names, returns true if the last
   * name of the first person begins with the same letter as the first name of
   * the second person. Otherwise, returns false. Takes into account suffixes
   * composed of Jr. Sr. or nicknames in quotes.
   *
   * @param name1
   *          The first full name.
   * @param name2
   *          The second full name.
   * @return Boolean detailing if the start of the first last name matches the
   *         start of the second first name
   */
  public static boolean checkFirstLetters(String name1, String name2) {
    if (name1.equals("") || name2.equals("")) {
      return false;
    }
    List<String> tokens1 = Interpreter.tokenizeString(name1);
    List<String> tokens2 = Interpreter.tokenizeString(name2);
    int index = tokens1.size() - 1;
    String lastName = tokens1.get(index);
    while (checkLastName(lastName)) {
      index--;
      if (index >= 0) {
        lastName = tokens1.get(index);
      } else {
        break;
      }
    }
    String firstName = tokens2.get(0);
    if (lastName.charAt(0) == firstName.charAt(0)) {
      return true;
    }
    return false;
  }

  private static boolean checkLastName(String name) {
    if (name.contains("Jr.")) {
      return true;
    } else if (name.contains("Sr.")) {
      return true;
    } else if (name.contains("\"")) {
      return true;
    }
    return false;
  }

  /**
   * @param id
   *          the id whose vertex to get
   * @return the vertex corresponding to the given id
   */
  public Vertex<String> getVertex(String id) {
    return graph.getVert(idToNum.get(id));
  }

  /**
   * @param id
   *          the id of the vertex whose vertex num to get
   * @return the num of the vertex
   */
  public int getVertNum(String id) {
    return idToNum.get(id);
  }

  /**
   * @return the open connection
   */
  public Connection getConn() {
    return conn;
  }

  /**
   * @return the graph contained in this bacon object
   */
  public EdgeSetGraph<String> getGraph() {
    return graph;
  }

  /**
   * Gets all the actors from the database as a list of strings. If no database
   * has been loaded yet, returns an empty list.
   *
   * @return all the actors in the datbase as a list
   */
  public List<String> getActors() {
    List<String> actors = new ArrayList<String>();
    if (conn == null) {
      return actors;
    } else {
      PreparedStatement prep;
      try {
        prep = conn.prepareStatement("SELECT name FROM actor;");
        ResultSet rs = prep.executeQuery();
        while (rs.next()) {
          String actor = rs.getString(1);
          String[] tokens = actor.split(" ");
          for (String token : tokens) {
            actors.add(token);
          }
        }
        rs.close();
        prep.close();
      } catch (SQLException e) {
        return actors;
      }
      return actors;
    }
  }

}
