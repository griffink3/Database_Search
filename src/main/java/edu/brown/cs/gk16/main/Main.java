package edu.brown.cs.gk16.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.gk16.Interpreter;
import edu.brown.cs.gk16.autocorrect.AcObject;
import edu.brown.cs.gk16.bacon.BaconObject;
import edu.brown.cs.gk16.stars.StarsObject;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * The Main class of our project. This is where execution begins.
 *
 * @author jj
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  private static final Gson GSON = new Gson();
  private static final Gui GUI = new Gui();
  private static boolean starsLoaded = false;
  private static boolean corpusLoaded = false;
  private static StarsObject stars;
  private static AcObject ac;
  private static BaconObject bacon;
  private static String actor1;
  private static String actor2;
  private static boolean connectCalled = false;

  /**
   * The initial method called when execution begins.
   *
   * @param args
   *          An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    Interpreter interpreter = new Interpreter();

  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    stars = new StarsObject(3);
    ac = new AcObject();
    bacon = new BaconObject();

    // Setup Spark Routes
    Spark.get("/stars", new FrontHandler(), freeMarker);
    Spark.post("/starsLoad", GUI.new StarsHandler(), freeMarker);
    Spark.post("/neighbors", GUI.new NeighborsHandler(), freeMarker);
    Spark.post("/radius", GUI.new RadiusHandler(), freeMarker);
    Spark.get("/autocorrect", GUI.new AcHandler(), freeMarker);
    Spark.post("/corpusLoad", GUI.new CorpusHandler(), freeMarker);
    Spark.post("/toggleSelect", GUI.new ToggleHandler(), freeMarker);
    Spark.post("/toggleInfo", GUI.new ToggleInfoHandler(), freeMarker);
    Spark.post("/suggestions", GUI.new GetSuggestionsHandler());
    Spark.get("/bacon", GUI.new BaconHandler(), freeMarker);
    Spark.post("/dbLoad", GUI.new DbHandler(), freeMarker);
    Spark.post("/baconConnect", GUI.new ConnectHandler(), freeMarker);
    Spark.post("/bacon/connectPath", GUI.new GetPathHandler());
    Spark.get("/bacon/actor/:name", GUI.new GetActorHandler(), freeMarker);
    Spark.get("/bacon/film/:name", GUI.new GetFilmHandler(), freeMarker);
    Spark.post("/bacon/putFilms", GUI.new PutActorFilmsHandler());
    Spark.post("/bacon/putActors", GUI.new PutFilmActorsHandler());

  }

  /**
   * Handle requests to the front page of our Stars website.
   *
   * @author jj
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables =
          ImmutableMap.of("title", "Stars: Query the database", "message", "");
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Display an error page when an exception occurs in the server.
   *
   * @author jj
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

}
