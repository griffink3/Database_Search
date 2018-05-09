package edu.brown.cs.gk16;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.brown.cs.gk16.autocorrect.AcObject;
import edu.brown.cs.gk16.bacon.BaconObject;
import edu.brown.cs.gk16.stars.Star;
import edu.brown.cs.gk16.stars.StarsObject;

/**
 * This class represents an interpreter that generates a REPL to read in user
 * input. In its function eval, it then parses the commands given by the user
 * and interprets them according to other private functions listed in the class
 * (error-checking in the process).
 */
public class Interpreter {

  private StarsObject stars;
  private AcObject ac;
  private BaconObject bacon;
  private boolean starsCalled;

  /**
   * The interpreter has a star object which allows for functionality in
   * interpreting the commands stars, neighbors, and radius. To add further
   * functionality, one could create a new object that defines methods to
   * interpret new commands — and each new command could be added to the if else
   * loop in the eval function which is relatively extensible.
   */
  public Interpreter() {
    stars = new StarsObject(3);
    ac = new AcObject();
    bacon = new BaconObject();
    starsCalled = false;
    InputStreamReader inputStreamReader = new InputStreamReader(System.in);
    BufferedReader in = new BufferedReader(inputStreamReader);
    try {
      String str;
      while ((str = in.readLine()) != null) {
        eval(str);
      }
      in.close();
    } catch (Exception e) {
      return;
    }
  }

  private int eval(String str) {
    List<String> commands = tokenizeString(str);
    int len = commands.size();
    if (commands.get(0).equals("stars")) {
      if (len != 2) {
        return printIncorrectNumberOfArgumentsError();
      } else {
        // stars command
        return doStarsCommand(commands.get(1));
      }
    } else if (commands.get(0).equals("neighbors")) {
      if (parseNeighborsCommand(starsCalled, commands, stars).isEmpty()) {
        return -1;
      } else {
        return 0;
      }
    } else if (commands.get(0).equals("radius")) {
      if (parseRadiusCommand(starsCalled, commands, stars).isEmpty()) {
        return -1;
      } else {
        return 0;
      }
    } else if (commands.get(0).equals("corpus")) {
      if (len != 2) {
        return printIncorrectNumberOfArgumentsError();
      } else {
        return doCorpusCommand(commands.get(1));
      }
    } else if (commands.get(0).equals("prefix")
        || commands.get(0).equals("whitespace")
        || commands.get(0).equals("smart")) {
      if (parseToggleCommand(commands, ac).equals("")) {
        return -1;
      } else {
        return 0;
      }
    } else if (commands.get(0).equals("led") || commands.get(0).equals("n")) {
      return parseLedAndNCommand(commands, ac);
    } else if (commands.get(0).equals("ac")) {
      doAcCommand(str, ac);
      return 0;
    } else if (commands.get(0).equals("mdb")) {
      if (len != 2) {
        return printIncorrectNumberOfArgumentsError();
      } else {
        return doDatabaseCommand(commands.get(1));
      }
    } else if (commands.get(0).equals("connect")) {
      if (parseConnectCommand(commands, bacon).isEmpty()) {
        return -1;
      }
      return 0;
    } else {
      return printCommandNotRecognizedError();
    }
  }

  /**
   * Tokenizes the passed in string, by splitting on spaces (except for spaces
   * within quotes).
   *
   * @param str
   *          string to be tokenized
   * @return list of tokens as strings
   */
  public static List<String> tokenizeString(String str) {
    List<String> tokens = new ArrayList<String>();
    Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(str);
    while (m.find()) {
      tokens.add(m.group(1));
    }
    return tokens;
  }

  /**
   * Parses (and error-checks) the arguments passed into the neighbors command
   * and executes the k-nearest search.
   *
   * @param starsLoaded
   *          boolean detailing whether a stars file has been loaded yet
   * @param commands
   *          arraylist containing the neighbors command and its arguments
   * @param starsOb
   *          the starsObject to be manipulated
   * @return list of the k nearest neighbors as string iDs
   */
  public static List<String> parseNeighborsCommand(boolean starsLoaded,
      List<String> commands, StarsObject starsOb) {
    int length = commands.size();
    List<String> empty = new ArrayList<String>();
    if (!starsLoaded) {
      printNoStarsError();
      return empty;
    } else if (length == 3 || length == 5) {
      int numNeighbors = checkInteger(commands.get(1));
      if (numNeighbors < 0) {
        printNotAPositiveIntegerError();
        return empty;
      }
      if (length == 3) {
        if (!checkStringHasQuotes(commands.get(2))) {
          printNoQuotesError();
          return empty;
        } else {
          return doNeighborsCommand(numNeighbors,
              commands.get(2).substring(1, commands.get(2).length() - 1),
              starsOb);
        }
      } else {
        if (!StarsObject.checkDouble(commands.get(2))
            || !StarsObject.checkDouble(commands.get(3))
            || !StarsObject.checkDouble(commands.get(4))) {
          return empty;
        } else {
          double x = StarsObject.makeDouble(commands.get(2));
          double y = StarsObject.makeDouble(commands.get(3));
          double z = StarsObject.makeDouble(commands.get(4));
          return doNeighborsCommand(numNeighbors, x, y, z, starsOb);
        }
      }
    } else {
      printIncorrectNumberOfArgumentsError();
      return empty;
    }
  }

  /**
   * Parses (and error-checks) the arguments passed into the radius command and
   * executes the range search.
   *
   * @param starsLoaded
   *          boolean detailing whether a stars file has been loaded yet
   * @param commands
   *          arraylist containing the radius command and its arguments
   * @param starsOb
   *          the starsObject to be manipulated
   * @return list of the stars in range as string iDs
   */
  public static List<String> parseRadiusCommand(boolean starsLoaded,
      List<String> commands, StarsObject starsOb) {
    int length = commands.size();
    List<String> empty = new ArrayList<String>();
    if (!starsLoaded) {
      printNoStarsError();
      return empty;
    } else if (length == 3 || length == 5) {
      double radius;
      if (!StarsObject.checkDouble(commands.get(1))
          || StarsObject.makeDouble(commands.get(1)) < 0) {
        printRadiusNotPositiveNumberError();
        return empty;
      } else {
        radius = StarsObject.makeDouble(commands.get(1));
      }
      if (length == 3) {
        if (!checkStringHasQuotes(commands.get(2))) {
          printNoQuotesError();
          return empty;
        } else {
          return doRadiusCommand(radius,
              commands.get(2).substring(1, commands.get(2).length() - 1),
              starsOb);
        }
      } else {
        if (!StarsObject.checkDouble(commands.get(2))
            || !StarsObject.checkDouble(commands.get(3))
            || !StarsObject.checkDouble(commands.get(4))) {
          return empty;
        } else {
          double x = StarsObject.makeDouble(commands.get(2));
          double y = StarsObject.makeDouble(commands.get(3));
          double z = StarsObject.makeDouble(commands.get(4));
          return doRadiusCommand(radius, x, y, z, starsOb);
        }
      }
    } else {
      printIncorrectNumberOfArgumentsError();
      return empty;
    }
  }

  /**
   * Parses (and error-checks) the arguments passed into the commands that
   * toggle certain setting for autocorrect. All such commands should be
   * followed by no arguments or by "on"/"off".
   *
   * @param commands
   *          arraylist containing the toggle command (and its argument)
   * @param ac
   *          the AcObject to be manipulated
   * @return String detailing toggle setting (or error occurred)
   */
  public static String parseToggleCommand(List<String> commands, AcObject ac) {
    if (commands.size() == 2) {
      int result = checkToggleArgument(commands.get(1));
      if (result < 0) {
        return "";
      } else {
        if (commands.get(0).equals("prefix")) {
          if (result == 1) {
            ac.setPrefix(true);
          } else {
            ac.setPrefix(false);
          }
        } else if (commands.get(0).equals("whitespace")) {
          if (result == 1) {
            ac.setWhiteSpace(true);
          } else {
            ac.setWhiteSpace(false);
          }
        } else if (commands.get(0).equals("smart")) {
          if (result == 1) {
            ac.setSmart(true);
          } else {
            ac.setSmart(false);
          }
        } else {
          printCommandNotRecognizedError();
          return "";
        }
      }
      return "done";
    } else if (commands.size() == 1) {
      if (commands.get(0).equals("prefix")) {
        System.out.println(ac.getPrefix());
        return ac.getPrefix();
      } else if (commands.get(0).equals("whitespace")) {
        System.out.println(ac.getWhiteSpace());
        return ac.getWhiteSpace();
      } else if (commands.get(0).equals("smart")) {
        System.out.println(ac.getSmart());
        return ac.getSmart();
      } else {
        printCommandNotRecognizedError();
        return "";
      }
    } else {
      printIncorrectNumberOfArgumentsError();
      return "";
    }
  }

  /**
   * Parses (and error-checks) the led command that sets the levenshtein edit
   * distance for the autocorrect object or the n command that sets the size of
   * the n gram to be stored.
   *
   * @param commands
   *          arraylist containing the led command (and its argument)
   * @param ac
   *          the AcObject to be manipulated
   * @return int detailing led or n setting (or error occurred)
   */
  public static int parseLedAndNCommand(List<String> commands, AcObject ac) {
    if (commands.size() == 2) {
      int val = checkInteger(commands.get(1));
      if (val < 0) {
        return printNotAPositiveIntegerError();
      } else {
        if (commands.get(0).equals("led")) {
          ac.setLed(val);
        } else {
          ac.setN(val);
        }
        return 0;
      }
    } else if (commands.size() == 1) {
      if (commands.get(0).equals("led")) {
        System.out.println("led " + ac.getLed());
        return ac.getLed();
      } else {
        System.out.println("n " + ac.getN());
        return ac.getN();
      }
    } else {
      return printIncorrectNumberOfArgumentsError();
    }
  }

  /**
   * Parses (and error-checks) the connect command that finds the shortest path
   * between two actors given a database file.
   *
   * @param commands
   *          arraylist containing the led command (and its argument)
   * @param baconOb
   *          the BaconObject to be manipulated
   * @return list of strings detailing the shortest path
   */
  public static List<String> parseConnectCommand(List<String> commands,
      BaconObject baconOb) {
    List<String> empty = new ArrayList<String>();
    if (commands.size() != 3) {
      printIncorrectNumberOfArgumentsError();
      return empty;
    } else {
      if (checkStringHasQuotes(commands.get(1))
          && checkStringHasQuotes(commands.get(2))) {
        return doConnectCommand(
            commands.get(1).substring(1, commands.get(1).length() - 1),
            commands.get(2).substring(1, commands.get(2).length() - 1),
            baconOb);
      } else {
        printNoQuotesError();
        return empty;
      }
    }
  }

  /**
   * Checks if the string passed in is a representation of a valid integer.
   *
   * @param string
   *          representation of integer
   * @return integer or -1 if not an integer
   */
  private static int checkInteger(String str) {
    int k;
    try {
      k = Integer.parseInt(str);
    } catch (NumberFormatException e) {
      return -1;
    }
    return k;
  }

  /**
   * Checks if the string passed in is a valid toggle argument - meaning if it's
   * "on" or "off".
   *
   * @param string
   *          the argument to the toggle command that should be "on" or "off"
   * @return 1 if argument is "on", 0 if argument is "off", or -1 if not valid
   */
  private static int checkToggleArgument(String str) {
    if (str.equals("on")) {
      return 1;
    } else if (str.equals("off")) {
      return 0;
    } else {
      System.out.println("ERROR: Argument must be on or off");
      return -1;
    }
  }

  private static boolean checkStringHasQuotes(String string) {
    if (!string.startsWith("\"")) {
      return false;
    } else if (!string.endsWith("\"")) {
      return false;
    }
    return true;
  }

  private static int printIncorrectNumberOfArgumentsError() {
    System.out.println(
        "ERROR: Incorrect number of arguments given following the command");
    return -1;
  }

  private static int printNotAPositiveIntegerError() {
    System.out.println("ERROR: The argument has to be a positive integer");
    return -1;
  }

  private static int printNoQuotesError() {
    System.out.println(
        "ERROR: The name of the argument should be surrounded by quotes");
    return -1;
  }

  private static int printNoStarsError() {
    System.out.println("ERROR: No stars have been loaded - use stars command");
    return -1;
  }

  private static int printRadiusNotPositiveNumberError() {
    System.out.println("ERROR: Radius must be a positive number");
    return -1;
  }

  private static int printNotValidStarError() {
    System.out.println("ERROR: Star has not been loaded yet");
    return -1;
  }

  private static int printCommandNotRecognizedError() {
    System.out
        .println("ERROR: That was not recognizable command, please try again");
    return -1;
  }

  private int doStarsCommand(String file) {
    starsCalled = true;
    return stars.addStars(file);
  }

  private static List<String> doNeighborsCommand(int neighbors, String star,
      StarsObject starsOb) {
    List<String> empty = new ArrayList<String>();
    if (!starsOb.isStarLoaded(star)) {
      printNotValidStarError();
      return empty;
    } else {
      List<Double> point = starsOb.getStar(star).getPoint();
      List<String> nearestNeighbors =
          starsOb.nearestNeighbor(neighbors + 1, point);
      nearestNeighbors.remove(0);
      for (int i = 0; i < nearestNeighbors.size(); i++) {
        System.out.println(nearestNeighbors.get(i));
      }
      return nearestNeighbors;
    }
  }

  private static List<String> doNeighborsCommand(int neighbors, double x,
      double y, double z, StarsObject starsOb) {
    List<Double> point = new ArrayList<Double>();
    point.add(x);
    point.add(y);
    point.add(z);
    List<String> nearestNeighbors = starsOb.nearestNeighbor(neighbors, point);
    for (int i = 0; i < nearestNeighbors.size(); i++) {
      System.out.println(nearestNeighbors.get(i));
    }
    return nearestNeighbors;
  }

  private static List<String> doRadiusCommand(double radius, String star,
      StarsObject starsOb) {
    List<String> empty = new ArrayList<String>();
    if (!starsOb.isStarLoaded(star)) {
      printNotValidStarError();
      return empty;
    } else {
      Star currStar = starsOb.getStar(star);
      List<Double> point = currStar.getPoint();
      List<String> starsInRange = starsOb.rangeSearch(radius, point);
      starsInRange.remove(0);
      for (int i = 0; i < starsInRange.size(); i++) {
        if (!starsInRange.equals(currStar.getId())) {
          System.out.println(starsInRange.get(i));
        }
      }
      return starsInRange;
    }
  }

  private static List<String> doRadiusCommand(double radius, double x, double y,
      double z, StarsObject starsOb) {
    List<Double> point = new ArrayList<Double>();
    point.add(x);
    point.add(y);
    point.add(z);
    List<String> starsInRange = starsOb.rangeSearch(radius, point);
    for (int i = 0; i < starsInRange.size(); i++) {
      System.out.println(starsInRange.get(i));
    }
    return starsInRange;
  }

  private int doCorpusCommand(String file) {
    return ac.addCorpus(file);
  }

  private int doAcCommand(String words, AcObject auto) {
    List<String> output = auto.autocorrect(words, true);
    for (String line : output) {
      System.out.println(line);
    }
    return 0;
  }

  private int doDatabaseCommand(String file) {
    return bacon.addDb(file);
  }

  private static List<String> doConnectCommand(String actor1, String actor2,
      BaconObject baconOb) {
    List<String> output = baconOb.connect(actor1, actor2);
    for (String line : output) {
      System.out.println(line);
    }
    return output;
  }

}
