package edu.brown.cs.gk16.stars;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class StarsObjectTest {

  @Test
  public void testAddStarsNormalFunctionality() {
    // Creating stars object
    StarsObject stars = new StarsObject(3);
    assert (stars.addStars("data/stars/one-star.csv") > -1);
    assert (stars.isStarLoaded("Lonely Star"));
    assert (stars.getStar("Lonely Star") != null);
    assert (stars.getStarById("1") != null);
  }

  @Test
  public void testAddStarsNoDuplicateFiles() {
    // Creating stars object
    StarsObject stars = new StarsObject(3);
    assert (stars.addStars("data/stars/one-star.csv") > -1);
    assert (stars.addStars("data/stars/one-star.csv") == -1);
  }

  @Test
  public void testAddStarsInvalidFile() {
    // Creating stars object
    StarsObject stars = new StarsObject(3);
    assert (stars.addStars("invalid/file/path") == -1);
  }

  @Test
  public void testAddStarsNoDuplicatesById() {
    // Creating stars object
    StarsObject stars = new StarsObject(3);
    assert (stars.addStars("data/stars/duplicate-star-ids.csv") > -1);
    assert (stars.isStarLoaded("Lonely Star"));
    assert (!stars.isStarLoaded("Different Star"));
  }

  @Test
  public void testAddStarsIncorrectFileFormat() {
    // Creating stars object
    StarsObject stars = new StarsObject(3);
    assert (stars.addStars("data/stars/incorrect-format.csv") == -1);
    assert (stars
        .addStars("data/stars/incorrect-format-not-enough-fields.csv") == -1);
  }

  @Test
  public void testFindSingleNearestNeighbor() {
    // Creating stars object and adding stars
    StarsObject stars = new StarsObject(3);
    stars.addStars("data/stars/ten-star.csv");

    // Searching for nearest neighbor to 1,1,1
    List<Double> point = new ArrayList<Double>();
    point.add(1.0);
    point.add(1.0);
    point.add(1.0);

    List<String> nearestStars = stars.nearestNeighbor(1, point);
    assert (nearestStars.size() == 1);
    assert (nearestStars.get(0).equals("0"));
  }

  @Test
  public void testFindThreeNearestNeighbors() {
    // Creating stars object and adding stars
    StarsObject stars = new StarsObject(3);
    stars.addStars("data/stars/ten-star.csv");

    // Searching for nearest neighbors to 1,1,1
    List<Double> point = new ArrayList<Double>();
    point.add(1.0);
    point.add(1.0);
    point.add(1.0);

    List<String> nearestStars = stars.nearestNeighbor(3, point);
    assert (nearestStars.size() == 3);
    assert (nearestStars.get(0).equals("0"));
    assert (nearestStars.get(1).equals("70667"));
    assert (nearestStars.get(2).equals("71454"));
  }

  @Test
  public void testFindSevenNearestNeighbors() {
    // Creating stars object and adding stars
    StarsObject stars = new StarsObject(3);
    stars.addStars("data/stars/ten-star.csv");

    // Searching for nearest neighbors to 1,1,1
    List<Double> point = new ArrayList<Double>();
    point.add(1.0);
    point.add(1.0);
    point.add(1.0);

    List<String> nearestStars = stars.nearestNeighbor(7, point);
    assert (nearestStars.size() == 7);
    assert (nearestStars.get(0).equals("0"));
    assert (nearestStars.get(1).equals("70667"));
    assert (nearestStars.get(2).equals("71454"));
    assert (nearestStars.get(3).equals("71457"));
    assert (nearestStars.get(4).equals("87666"));
    assert (nearestStars.get(5).equals("118721"));
    assert (nearestStars.get(6).equals("3759"));
  }

  @Test
  public void testFindAllNearestNeighbors() {
    // Creating stars object and adding stars
    StarsObject stars = new StarsObject(3);
    stars.addStars("data/stars/ten-star.csv");

    // Searching for nearest neighbors to 1,1,1
    List<Double> point = new ArrayList<Double>();
    point.add(1.0);
    point.add(1.0);
    point.add(1.0);

    List<String> nearestStars = stars.nearestNeighbor(10, point);
    assert (nearestStars.size() == 10);
    assert (nearestStars.get(0).equals("0"));
    assert (nearestStars.get(1).equals("70667"));
    assert (nearestStars.get(2).equals("71454"));
    assert (nearestStars.get(3).equals("71457"));
    assert (nearestStars.get(4).equals("87666"));
    assert (nearestStars.get(5).equals("118721"));
    assert (nearestStars.get(6).equals("3759"));
    assert (nearestStars.get(7).equals("2"));
    assert (nearestStars.get(8).equals("1"));
    assert (nearestStars.get(9).equals("3"));
  }

  @Test
  public void testFindNearestNeighborStarsSameLocation() {
    // Creating stars object and adding stars
    StarsObject stars = new StarsObject(3);
    stars.addStars("data/stars/stars-same-location.csv");

    // Searching for nearest neighbors to 1,1,1
    List<Double> point = new ArrayList<Double>();
    point.add(1.0);
    point.add(1.0);
    point.add(1.0);

    List<String> nearestStars = stars.nearestNeighbor(1, point);
    assert (nearestStars.size() == 1);
    assert (nearestStars.get(0).equals("1") || nearestStars.get(0).equals("2"));
  }

  @Test
  public void testFindNearestNeighborsZeroNeighborsOrNullPoint() {
    // Creating stars object and adding stars
    StarsObject stars = new StarsObject(3);
    stars.addStars("data/stars/ten-star.csv");

    // Searching for nearest neighbors to 1,1,1
    List<Double> point = new ArrayList<Double>();
    point.add(1.0);
    point.add(1.0);
    point.add(1.0);

    List<String> nearestStars = stars.nearestNeighbor(0, point);
    assert (nearestStars.size() == 0);
    nearestStars = stars.nearestNeighbor(1, null);
    assert (nearestStars.size() == 0);
  }

  @Test
  public void testFindNearestNeighborEmptyTree() {
    // Creating stars object and adding stars
    StarsObject stars = new StarsObject(3);

    // Searching for nearest neighbors to 1,1,1
    List<Double> point = new ArrayList<Double>();
    point.add(1.0);
    point.add(1.0);
    point.add(1.0);

    List<String> nearestStars = stars.nearestNeighbor(1, point);
    assert (nearestStars.size() == 0);
  }

  @Test
  public void testRangeSearchWithOneStarInRange() {
    // Creating stars object and adding stars
    StarsObject stars = new StarsObject(3);
    stars.addStars("data/stars/ten-star.csv");

    List<Double> point = new ArrayList<Double>();
    point.add(1.0);
    point.add(1.0);
    point.add(1.0);

    List<String> starsInRange = stars.rangeSearch(2.0, point);
    assert (starsInRange.size() == 1);
    assert (starsInRange.get(0).equals("0"));
  }

  @Test
  public void testRangeSearchWithSixStarsInRange() {
    // Creating stars object and adding stars
    StarsObject stars = new StarsObject(3);
    stars.addStars("data/stars/ten-star.csv");

    List<Double> point = new ArrayList<Double>();
    point.add(1.0);
    point.add(1.0);
    point.add(1.0);

    List<String> starsInRange = stars.rangeSearch(4.0, point);
    assert (starsInRange.size() == 6);
    assert (starsInRange.get(0).equals("0"));
    assert (starsInRange.get(1).equals("70667"));
    assert (starsInRange.get(2).equals("71454"));
    assert (starsInRange.get(3).equals("71457"));
    assert (starsInRange.get(4).equals("87666"));
    assert (starsInRange.get(5).equals("118721"));
  }

  @Test
  public void testRangeSearchWillAllStarsInRange() {
    // Creating stars object and adding stars
    StarsObject stars = new StarsObject(3);
    stars.addStars("data/stars/ten-star.csv");

    List<Double> point = new ArrayList<Double>();
    point.add(1.0);
    point.add(1.0);
    point.add(1.0);

    List<String> starsInRange = stars.rangeSearch(400.0, point);
    assert (starsInRange.size() == 10);
    assert (starsInRange.get(0).equals("0"));
    assert (starsInRange.get(1).equals("70667"));
    assert (starsInRange.get(2).equals("71454"));
    assert (starsInRange.get(3).equals("71457"));
    assert (starsInRange.get(4).equals("87666"));
    assert (starsInRange.get(5).equals("118721"));
    assert (starsInRange.get(6).equals("3759"));
    assert (starsInRange.get(7).equals("2"));
    assert (starsInRange.get(8).equals("1"));
    assert (starsInRange.get(9).equals("3"));
  }

  @Test
  public void testRangeSearchStarsSameLocation() {
    // Creating stars object and adding stars
    StarsObject stars = new StarsObject(3);
    stars.addStars("data/stars/stars-same-location.csv");

    List<Double> point = new ArrayList<Double>();
    point.add(1.0);
    point.add(1.0);
    point.add(1.0);

    List<String> starsInRange = stars.rangeSearch(0.5, point);
    assert (starsInRange.size() == 2);
  }

  @Test
  public void testRangeSearchStarDistanceEqualToRadius() {
    // Creating stars object and adding stars
    StarsObject stars = new StarsObject(3);
    stars.addStars("data/stars/stars-same-location.csv");

    List<Double> point = new ArrayList<Double>();
    point.add(1.0);
    point.add(1.0);
    point.add(1.0);

    List<String> starsInRange = stars.rangeSearch(1, point);
    assert (starsInRange.size() == 3);
    assert (starsInRange.get(2).equals("3"));
  }

  @Test
  public void testRangeSearchZeroRadiussOrNullPoint() {
    // Creating stars object and adding stars
    StarsObject stars = new StarsObject(3);
    stars.addStars("data/stars/ten-star.csv");

    List<Double> point = new ArrayList<Double>();
    point.add(1.0);
    point.add(1.0);
    point.add(1.0);

    List<String> starsInRange = stars.rangeSearch(0, point);
    assert (starsInRange.size() == 0);
    starsInRange = stars.rangeSearch(1, null);
    assert (starsInRange.size() == 0);
  }

  @Test
  public void testRangeSearchEmptyTree() {
    // Creating stars object and adding stars
    StarsObject stars = new StarsObject(3);

    List<Double> point = new ArrayList<Double>();
    point.add(1.0);
    point.add(1.0);
    point.add(1.0);

    List<String> starsInRange = stars.rangeSearch(1, point);
    assert (starsInRange.size() == 0);
  }

}
