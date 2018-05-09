package edu.brown.cs.gk16.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.gk16.Interpreter;
import edu.brown.cs.gk16.autocorrect.AcObject;
import edu.brown.cs.gk16.bacon.BaconObject;
import edu.brown.cs.gk16.stars.StarsObject;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateViewRoute;

/**
 * This class serves to contain all the gui backend.
 */
public class Gui {

  private static final Gson GSON = new Gson();
  private boolean starsLoaded = false;
  private boolean corpusLoaded = false;
  private StarsObject stars;
  private AcObject ac;
  private BaconObject bacon;
  private String actor1;
  private String actor2;
  private boolean connectCalled = false;
  private List<String> films;
  private List<String> actors;

  /**
   * The class that contains all the spark handlers.
   */
  public Gui() {
    stars = new StarsObject(3);
    ac = new AcObject();
    bacon = new BaconObject();
  }

  /**
   * Handle requests to upload stars.
   */
  public class StarsHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String file = qm.value("fileName");
      String message = "";
      int error = stars.addStars(file);
      if (error < 0) {
        message = "Error occurred: please correct the file name - "
            + "it's possible this file has already been uploaded.";
      } else {
        message = "Read " + error + " stars from " + file;
      }
      Map<String, Object> variables = ImmutableMap.of("title",
          "Stars: Query the database", "message", message);
      starsLoaded = true;
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Handle requests to search for nearest neighbors.
   */
  public class NeighborsHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      String message = "";
      if (!starsLoaded) {
        message = "Error occurred: no stars have been loaded.";
      } else {
        QueryParamsMap qm = req.queryMap();
        String numNeighbors = qm.value("numNeighbors");
        String searchParam = qm.value("searchParam");
        List<String> commands = new ArrayList<String>();
        commands.add("neighbors");
        commands.add(numNeighbors);
        Matcher m =
            Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(searchParam);
        while (m.find()) {
          commands.add(m.group(1));
        }
        List<String> nearestNeighbors =
            Interpreter.parseNeighborsCommand(starsLoaded, commands, stars);
        if (nearestNeighbors.isEmpty()) {
          message = "Error occurred: fix input (or no stars found)";
        } else {
          message = "The " + numNeighbors + " closest neighbors to "
              + searchParam + " are ";
          for (String neighbor : nearestNeighbors) {
            if (!stars.getStarById(neighbor).getName().equals("")) {
              neighbor = stars.getStarById(neighbor).getName();
            }
            message = message + neighbor + ", ";
          }
        }
      }
      Map<String, Object> variables = ImmutableMap.of("title",
          "Stars: Query the database", "message", message);
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Handle requests to search for stars within a radius.
   */
  public class RadiusHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      String message = "";
      if (!starsLoaded) {
        message = "Error occurred: no stars have been loaded.";
      } else {
        QueryParamsMap qm = req.queryMap();
        String radius = qm.value("radius");
        String searchParam = qm.value("searchParam");
        List<String> commands = new ArrayList<String>();
        commands.add("neighbors");
        commands.add(radius);
        Matcher m =
            Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(searchParam);
        while (m.find()) {
          commands.add(m.group(1));
        }
        List<String> starsInRange =
            Interpreter.parseRadiusCommand(starsLoaded, commands, stars);
        if (starsInRange.isEmpty()) {
          message = "Error occurred: fix input (or no stars found)";
        } else {
          message =
              "The stars within " + radius + " from " + searchParam + " are ";
          for (String star : starsInRange) {
            if (!stars.getStarById(star).getName().equals("")) {
              star = stars.getStarById(star).getName();
            }
            message = message + star + ", ";
          }
        }
      }
      Map<String, Object> variables = ImmutableMap.of("title",
          "Stars: Query the database", "message", message);
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Handle requests to the front page of our autocorrect website.
   */
  public class AcHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Autocorrect: Text Prediction", "message", "");
      return new ModelAndView(variables, "ac_input.ftl");
    }
  }

  /**
   * Handles request to upload a corpus file.
   */
  public class CorpusHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String file = qm.value("fileName");
      String message = "";
      int error = ac.addCorpus(file);
      if (error < 0) {
        message = "Error occurred: please correct the file name - "
            + "it's possible this file has already been uploaded.";
      } else {
        message = "corpus " + file + " added";
      }
      Map<String, Object> variables = ImmutableMap.of("title",
          "Autocorrect: Text Prediction", "message", message);
      corpusLoaded = true;
      return new ModelAndView(variables, "ac_input.ftl");
    }
  }

  /**
   * Handles request to update autocorrect settings.
   */
  public class ToggleHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String prefix = qm.value("Prefix");
      String whitespace = qm.value("WhiteSpace");
      String smart = qm.value("Smart");
      String led = qm.value("led");
      String ngram = qm.value("nGram");
      String message = "";
      List<String> prefComm = new ArrayList<String>();
      prefComm.add("prefix");
      prefComm.add(prefix);
      Interpreter.parseToggleCommand(prefComm, ac);
      List<String> wsComm = new ArrayList<String>();
      wsComm.add("whitespace");
      wsComm.add(whitespace);
      Interpreter.parseToggleCommand(wsComm, ac);
      List<String> smartComm = new ArrayList<String>();
      smartComm.add("smart");
      smartComm.add(smart);
      Interpreter.parseToggleCommand(smartComm, ac);
      List<String> ledComm = new ArrayList<String>();
      ledComm.add("led");
      ledComm.add(led);
      int result1;
      if (led.equals("")) {
        result1 = 1;
      } else {
        result1 = Interpreter.parseLedAndNCommand(ledComm, ac);
      }
      List<String> nComm = new ArrayList<String>();
      nComm.add("n");
      nComm.add(ngram);
      int result2;
      if (ngram.equals("")) {
        result2 = 1;
      } else {
        result2 = Interpreter.parseLedAndNCommand(nComm, ac);
      }
      if (result1 < 0 || result2 < 0) {
        message = "Error occurred: make sure input is a positive integer";
      } else {
        message = "Prefix " + prefix + ", Whitespace " + whitespace + ", Smart "
            + smart + ", Led " + led + ", N-Gram " + ngram;
      }
      Map<String, Object> variables = ImmutableMap.of("title",
          "Autocorrect: Text Prediction", "message", message);
      return new ModelAndView(variables, "ac_input.ftl");
    }
  }

  /**
   * Handles request to get autocorrect settings info.
   */
  public class ToggleInfoHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String prefix = ac.getPrefix();
      String whitespace = ac.getWhiteSpace();
      String smart = ac.getSmart();
      String led = Integer.toString(ac.getLed());
      String ngram = Integer.toString(ac.getN());
      String message = "";
      message = prefix + ", " + whitespace + ", " + smart + ", led " + led
          + ", n-gram " + ngram;
      Map<String, Object> variables = ImmutableMap.of("title",
          "Autocorrect: Text Prediction", "message", message);
      return new ModelAndView(variables, "ac_input.ftl");
    }
  }

  /**
   * Handle requests to get autocorrect suggestions.
   */
  public class GetSuggestionsHandler implements Route {
    @Override
    public String handle(Request req, Response res) {

      QueryParamsMap qm = req.queryMap();
      String input = qm.value("input");

      List<String> suggestions = ac.autocorrect("ac " + input, false);
      if (suggestions.size() != 0) {
        suggestions.remove(0);
      }

      Map<String, Object> variables =
          ImmutableMap.of("suggestions", suggestions, "corpus", corpusLoaded);
      return GSON.toJson(variables);
    }
  }

  /**
   * Handle requests to the front page of our bacon website.
   */
  public class BaconHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      connectCalled = false;
      Map<String, Object> variables =
          ImmutableMap.of("title", "Bacon: Shortest Path", "message", "");
      return new ModelAndView(variables, "bacon_input.ftl");
    }
  }

  /**
   * Handles request to upload a database file.
   */
  public class DbHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      ac = new AcObject();
      QueryParamsMap qm = req.queryMap();
      String file = qm.value("fileName");
      String message = "";
      int error = bacon.addDb(file);
      if (error < 0) {
        message = "Error occurred: please correct the file path.";
      } else {
        message = "Database set to " + file;
      }
      List<String> allActors = bacon.getActors();
      ac.addWords(allActors, file, false);
      corpusLoaded = true;
      List<String> prefComm = new ArrayList<String>();
      prefComm.add("prefix");
      prefComm.add("on");
      Interpreter.parseToggleCommand(prefComm, ac);
      List<String> wsComm = new ArrayList<String>();
      wsComm.add("whitespace");
      wsComm.add("on");
      Interpreter.parseToggleCommand(wsComm, ac);
      List<String> ledComm = new ArrayList<String>();
      ledComm.add("led");
      ledComm.add("2");
      Interpreter.parseLedAndNCommand(ledComm, ac);
      Map<String, Object> variables =
          ImmutableMap.of("title", "Bacon: Shortest Path", "message", message);
      return new ModelAndView(variables, "bacon_input.ftl");
    }
  }

  /**
   * Handles request to connect two actors.
   */
  public class ConnectHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      actor1 = qm.value("autocorrect1");
      actor2 = qm.value("autocorrect2");
      connectCalled = true;
      Map<String, Object> variables =
          ImmutableMap.of("title", "Bacon: Shortest Path", "message", "");
      return new ModelAndView(variables, "bacon_connect.ftl");
    }
  }

  /**
   * Handle requests to get the shortest path for the connect command.
   */
  public class GetPathHandler implements Route {
    @Override
    public String handle(Request req, Response res) {

      List<List<String>> path = bacon.connectPieces(actor1, actor2);

      Map<String, Object> variables =
          ImmutableMap.of("path", path, "connectCalled", connectCalled);
      return GSON.toJson(variables);
    }
  }

  /**
   * Handles request to get an actor page.
   */
  public class GetActorHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String name = req.params(":name");
      StringBuffer s = new StringBuffer(name);
      for (int i = 0; i < s.length(); i++) {
        if (s.charAt(i) == '-') {
          s.setCharAt(i, '\'');
        } else if (s.charAt(i) == '_') {
          s.setCharAt(i, ' ');
        } else if (s.charAt(i) == '=') {
          s.setCharAt(i, '\"');
        } else if (s.charAt(i) == '+') {
          s.setCharAt(i, '.');
        }
      }
      name = s.toString();
      films = bacon.queryTableList(name, "film", "actor");
      Map<String, Object> variables = ImmutableMap.of("title",
          "Bacon: Shortest Path", "message", "", "actor", name);
      return new ModelAndView(variables, "bacon_actor.ftl");
    }
  }

  /**
   * Handle requests to get a film page.
   */
  public class GetFilmHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String name = req.params(":name");
      StringBuffer s = new StringBuffer(name);
      for (int i = 0; i < s.length(); i++) {
        if (s.charAt(i) == '-') {
          s.setCharAt(i, '\'');
        } else if (s.charAt(i) == '_') {
          s.setCharAt(i, ' ');
        } else if (s.charAt(i) == '=') {
          s.setCharAt(i, '\"');
        } else if (s.charAt(i) == '+') {
          s.setCharAt(i, '.');
        }
      }
      name = s.toString();
      actors = bacon.queryTableList(name, "actor", "film");
      Map<String, Object> variables =
          ImmutableMap.of("title", "Bacon: Shortest Path", "message", "",
              "film", name, "actors", actors);
      return new ModelAndView(variables, "bacon_film.ftl");
    }
  }

  /**
   * Handle putting the films on the actor page.
   */
  public class PutActorFilmsHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Map<String, Object> variables =
          ImmutableMap.of("films", films, "actor_film", 1);
      return GSON.toJson(variables);
    }
  }

  /**
   * Handle putting the actors on the film page.
   */
  public class PutFilmActorsHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Map<String, Object> variables =
          ImmutableMap.of("actors", actors, "actor_film", 2);
      return GSON.toJson(variables);
    }
  }

}
