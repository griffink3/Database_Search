package edu.brown.cs.gk16;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.brown.cs.gk16.autocorrect.AcObject;
import edu.brown.cs.gk16.bacon.BaconObject;
import edu.brown.cs.gk16.stars.StarsObject;

public class InterpreterTest {

  @Test
  public void testTokenizeStringSplitsOnWhiteSpace() {
    String string = "This is a string";
    List<String> tokens = Interpreter.tokenizeString(string);
    assert (tokens.size() == 4);
    assert (tokens.get(0).equals("This"));
    assert (tokens.get(1).equals("is"));
    assert (tokens.get(2).equals("a"));
    assert (tokens.get(3).equals("string"));
  }

  @Test
  public void testTokenizeStringNoSplitBetweenQuotes() {
    String string = "This \"is a string\"";
    List<String> tokens = Interpreter.tokenizeString(string);
    assert (tokens.size() == 2);
    assert (tokens.get(0).equals("This"));
    assert (tokens.get(1).equals("\"is a string\""));
  }

  @Test
  public void testTokenizeStringIgnoresSingleQuote() {
    String string = "This \"is a string";
    List<String> tokens = Interpreter.tokenizeString(string);
    assert (tokens.size() == 4);
    assert (tokens.get(0).equals("This"));
    assert (tokens.get(1).equals("is"));
    assert (tokens.get(2).equals("a"));
    assert (tokens.get(3).equals("string"));
  }

  @Test
  public void testParseNeighborCommandNormalFunctionality() {
    // Creating a new stars object to pass into the function
    StarsObject stars = new StarsObject(3);

    List<String> commands = new ArrayList<String>();
    commands.add("neighbors");
    commands.add("3");
    commands.add("\"Sol\"");

    stars.addStars("data/stars/ten-star.csv");

    List<String> results =
        Interpreter.parseNeighborsCommand(true, commands, stars);
    assert (results.size() == 3);
    assert (results.get(0).equals("70667"));
    assert (results.get(1).equals("71454"));
    assert (results.get(2).equals("71457"));

    commands.remove(2);
    commands.add("1");
    commands.add("1");
    commands.add("1");

    results = Interpreter.parseNeighborsCommand(true, commands, stars);
    assert (results.size() == 3);
    assert (results.get(0).equals("0"));
    assert (results.get(1).equals("70667"));
    assert (results.get(2).equals("71454"));

  }

  @Test
  public void testParseNeigborsCommandNoStars() {
    // Creating a new stars object to pass into the function
    StarsObject stars = new StarsObject(3);

    List<String> commands = new ArrayList<String>();
    commands.add("neighbors");
    commands.add("3");
    commands.add("Sol");

    // Check that the proper errors are returned (empty list returned upon
    // error)
    List<String> results =
        Interpreter.parseNeighborsCommand(false, commands, stars);
    assert (results.size() == 0);
  }

  @Test
  public void testParseNeighborsCommandStarNameNoQuotes() {
    // Creating a new stars object to pass into the function
    StarsObject stars = new StarsObject(3);

    List<String> commands = new ArrayList<String>();
    commands.add("neighbors");
    commands.add("3");
    commands.add("Sol"); // Argument has no quotes

    stars.addStars("data/stars/ten-star.csv");

    List<String> results =
        Interpreter.parseNeighborsCommand(true, commands, stars);
    assert (results.size() == 0);
  }

  @Test
  public void testParseNeighborsCommandIncorrectNumberArguments() {
    // Creating a new stars object to pass into the function
    StarsObject stars = new StarsObject(3);

    List<String> commands = new ArrayList<String>();
    commands.add("neighbors");
    commands.add("3");
    commands.add("\"Sol\"");
    commands.add("another"); // Incorrect number of arguments (4)

    stars.addStars("data/stars/ten-star.csv");

    List<String> results =
        Interpreter.parseNeighborsCommand(true, commands, stars);
    assert (results.size() == 0);
  }

  @Test
  public void testParseNeighborsCommandNeighborsNotANumber() {
    // Creating a new stars object to pass into the function
    StarsObject stars = new StarsObject(3);

    List<String> commands = new ArrayList<String>();
    commands.add("neighbors");
    commands.add("not a number"); // Number of neighbors not a number
    commands.add("\"Sol\"");

    stars.addStars("data/stars/ten-star.csv");

    List<String> results =
        Interpreter.parseNeighborsCommand(true, commands, stars);
    assert (results.size() == 0);
  }

  @Test
  public void testParseNeighborsCommandNegativeNeighbors() {
    // Creating a new stars object to pass into the function
    StarsObject stars = new StarsObject(3);

    List<String> commands = new ArrayList<String>();
    commands.add("neighbors");
    commands.add("-5"); // Number of neighbors not a number
    commands.add("\"Sol\"");

    stars.addStars("data/stars/ten-star.csv");

    List<String> results =
        Interpreter.parseNeighborsCommand(true, commands, stars);
    assert (results.size() == 0);
  }

  @Test
  public void testParseNeighborsCommandCoordinatessNotNumbers() {
    // Creating a new stars object to pass into the function
    StarsObject stars = new StarsObject(3);

    List<String> commands = new ArrayList<String>();
    commands.add("neighbors");
    commands.add("3"); // Number of neighbors not a number
    commands.add("word");
    commands.add("word");
    commands.add("word");

    stars.addStars("data/stars/ten-star.csv");

    List<String> results =
        Interpreter.parseNeighborsCommand(true, commands, stars);
    assert (results.size() == 0);
  }

  @Test
  public void testParseRadiusCommandNormalFunctionality() {
    // Creating a new stars object to pass into the function
    StarsObject stars = new StarsObject(3);

    List<String> commands = new ArrayList<String>();
    commands.add("radius");
    commands.add("2");
    commands.add("\"Sol\"");

    stars.addStars("data/stars/ten-star.csv");

    List<String> results =
        Interpreter.parseRadiusCommand(true, commands, stars);
    assert (results.size() == 4);
    assert (results.get(0).equals("70667"));
    assert (results.get(1).equals("71454"));
    assert (results.get(2).equals("71457"));
    assert (results.get(3).equals("87666"));

    commands.remove(2);
    commands.remove(1);
    commands.add("2.5");
    commands.add("0.5");
    commands.add("0.5");
    commands.add("0.5");

    results = Interpreter.parseRadiusCommand(true, commands, stars);
    assert (results.size() == 5);
    assert (results.get(0).equals("0"));
    assert (results.get(1).equals("70667"));
    assert (results.get(2).equals("71454"));
    assert (results.get(3).equals("71457"));
    assert (results.get(4).equals("87666"));
  }

  @Test
  public void testParseRadiusCommandNoStars() {
    // Creating a new stars object to pass into the function
    StarsObject stars = new StarsObject(3);

    List<String> commands = new ArrayList<String>();
    commands.add("radius");
    commands.add("3");
    commands.add("Sol");

    // Check that the proper errors are returned (empty list returned upon
    // error)
    List<String> results =
        Interpreter.parseRadiusCommand(false, commands, stars);
    assert (results.size() == 0);
  }

  @Test
  public void testParseRadiusCommandStarNameNoQuotes() {
    // Creating a new stars object to pass into the function
    StarsObject stars = new StarsObject(3);

    List<String> commands = new ArrayList<String>();
    commands.add("radius");
    commands.add("5");
    commands.add("Sol"); // Argument has no quotes

    stars.addStars("data/stars/ten-star.csv");

    List<String> results =
        Interpreter.parseNeighborsCommand(true, commands, stars);
    assert (results.size() == 0);
  }

  @Test
  public void testParseRadiusCommandIncorrectNumberArguments() {
    // Creating a new stars object to pass into the function
    StarsObject stars = new StarsObject(3);

    List<String> commands = new ArrayList<String>();
    commands.add("radius");
    commands.add("3");
    commands.add("\"Sol\"");
    commands.add("another"); // Incorrect number of arguments (4)

    stars.addStars("data/stars/ten-star.csv");

    List<String> results =
        Interpreter.parseRadiusCommand(true, commands, stars);
    assert (results.size() == 0);
  }

  @Test
  public void testParseRadiusCommandRadiusNotANumber() {
    // Creating a new stars object to pass into the function
    StarsObject stars = new StarsObject(3);

    List<String> commands = new ArrayList<String>();
    commands.add("radius");
    commands.add("not a number"); // Number of neighbors not a number
    commands.add("\"Sol\"");

    stars.addStars("data/stars/ten-star.csv");

    List<String> results =
        Interpreter.parseRadiusCommand(true, commands, stars);
    assert (results.size() == 0);
  }

  @Test
  public void testParseRadiusCommandNegativeRadius() {
    // Creating a new stars object to pass into the function
    StarsObject stars = new StarsObject(3);

    List<String> commands = new ArrayList<String>();
    commands.add("radius");
    commands.add("-5"); // Number of neighbors not a number
    commands.add("\"Sol\"");

    stars.addStars("data/stars/ten-star.csv");

    List<String> results =
        Interpreter.parseRadiusCommand(true, commands, stars);
    assert (results.size() == 0);
  }

  @Test
  public void testParseRadiusCommandCoordinatessNotNumbers() {
    // Creating a new stars object to pass into the function
    StarsObject stars = new StarsObject(3);

    List<String> commands = new ArrayList<String>();
    commands.add("radius");
    commands.add("3"); // Number of neighbors not a number
    commands.add("word");
    commands.add("word");
    commands.add("word");

    stars.addStars("data/stars/ten-star.csv");

    List<String> results =
        Interpreter.parseRadiusCommand(true, commands, stars);
    assert (results.size() == 0);
  }

  @Test
  public void testParseToggleCommand() {
    // Testing normal functionality for parseToggleCommand function
    AcObject ac = new AcObject();
    List<String> prefix = new ArrayList<String>();
    prefix.add("prefix");
    List<String> whitespace = new ArrayList<String>();
    whitespace.add("whitespace");
    List<String> smart = new ArrayList<String>();
    smart.add("smart");
    assert (Interpreter.parseToggleCommand(prefix, ac).equals("prefix off"));
    assert (Interpreter.parseToggleCommand(whitespace, ac)
        .equals("whitespace off"));
    assert (Interpreter.parseToggleCommand(smart, ac).equals("smart off"));
    prefix.add("on");
    whitespace.add("on");
    smart.add("on");
    Interpreter.parseToggleCommand(prefix, ac);
    Interpreter.parseToggleCommand(whitespace, ac);
    Interpreter.parseToggleCommand(smart, ac);
    assert (ac.getPrefix().equals("prefix on"));
    assert (ac.getWhiteSpace().equals("whitespace on"));
    assert (ac.getSmart().equals("smart on"));
  }

  @Test
  public void testParseToggleCommandIncorrectNumberArguments() {
    AcObject ac = new AcObject();
    List<String> prefix = new ArrayList<String>();
    prefix.add("prefix");
    prefix.add("on");
    prefix.add("another");
    assert (Interpreter.parseToggleCommand(prefix, ac).equals(""));
  }

  @Test
  public void testParseToggleCommandInvalidArguments() {
    AcObject ac = new AcObject();
    List<String> prefix = new ArrayList<String>();
    prefix.add("notprefix");
    prefix.add("on");
    assert (Interpreter.parseToggleCommand(prefix, ac).equals(""));
  }

  @Test
  public void testParseLedAndNCommand() {
    // Testing normal functionality for parseLedAndNCommand function
    AcObject ac = new AcObject();
    List<String> led = new ArrayList<String>();
    led.add("led");
    List<String> ngram = new ArrayList<String>();
    ngram.add("n");
    assert (Interpreter.parseLedAndNCommand(led, ac) == 0);
    assert (Interpreter.parseLedAndNCommand(ngram, ac) == 0);
    led.add("3");
    ngram.add("2");
    Interpreter.parseLedAndNCommand(led, ac);
    Interpreter.parseLedAndNCommand(ngram, ac);
    assert (ac.getLed() == 3);
    assert (ac.getN() == 2);
  }

  @Test
  public void testParseLedAndNCommandIncorrectNumberArguments() {
    AcObject ac = new AcObject();
    List<String> led = new ArrayList<String>();
    led.add("led");
    led.add("3");
    led.add("another");
    assert (Interpreter.parseLedAndNCommand(led, ac) == -1);
  }

  @Test
  public void testParseLedAndNCommandInvalidArgument() {
    AcObject ac = new AcObject();
    List<String> led = new ArrayList<String>();
    led.add("led");
    led.add("-2");
    assert (Interpreter.parseLedAndNCommand(led, ac) == -1);
  }

  @Test
  public void testParseConnectCommandIncorrectNumeberArguments() {
    BaconObject bacon = new BaconObject();
    List<String> commands = new ArrayList<String>();
    commands.add("connect");
    commands.add("\"Michael\"");
    assert (Interpreter.parseConnectCommand(commands, bacon).isEmpty());
    commands.add("\"Last\"");
    commands.add("\"Another\"");
    assert (Interpreter.parseConnectCommand(commands, bacon).isEmpty());
  }

  @Test
  public void testParseConnectCommandArgumentsWithoutQuotes() {
    BaconObject bacon = new BaconObject();
    List<String> commands = new ArrayList<String>();
    commands.add("connect");
    commands.add("\"Michael\"");
    commands.add("last");
    assert (Interpreter.parseConnectCommand(commands, bacon).isEmpty());
  }
}
