package edu.brown.cs.gk16;

import java.util.List;

import org.junit.Test;

import edu.brown.cs.gk16.bacon.BaconObject;

public class DijkstraTest {

  /**
   * Testing the djikstra's algorithm through the bacon object's connect
   * command.
   */

  @Test
  public void testFindShortestPath() {
    // Creating new bacon object and loading database
    BaconObject bacon = new BaconObject();
    bacon.addDb("data/bacon/smallbacon.sqlite3");
    EdgeSetGraph<String> graph = bacon.getGraph();
    String actor1 = "Will Smith";
    String actor2 = "Stephen Root";
    List<String> output = bacon.connect(actor1, actor2);
    assert (output.size() == 1);
    assert (output.get(0).equals("Will Smith -> Stephen Root : Jersey Girl"));
  }

  @Test
  public void testFindShortestPathLonger() {
    // Creating new bacon object and loading database
    BaconObject bacon = new BaconObject();
    bacon.addDb("data/bacon/smallbacon.sqlite3");
    String actor1 = "Arnold Schwarzenegger";
    String actor2 = "Willow Smith";
    List<String> output = bacon.connect(actor1, actor2);
    assert (output.get(0).equals(
        "Arnold Schwarzenegger -> Steve Coogan : Around the World in 80 Days"));
    assert (output.get(1).equals("Steve Coogan -> Cate Blanchett : Hot Fuzz"));
    assert (output.get(2).equals("Cate Blanchett -> Betty White : Ponyo"));
    assert (output.get(3).equals("Betty White -> Willow Smith : The Lorax"));
  }

  @Test
  public void testFindShortestPathNone() {
    // Creating new bacon object and loading database
    BaconObject bacon = new BaconObject();
    bacon.addDb("data/bacon/smallbacon.sqlite3");
    String actor1 = "Jeff Bridges"; // The actor id of Jeff Bridges
    String actor2 = "John Travolta"; // The actor id of John Travolta
    List<String> output = bacon.connect(actor1, actor2);
    assert (output.size() == 1);
    assert (output.get(0).equals("Jeff Bridges -/- John Travolta"));
  }

}
