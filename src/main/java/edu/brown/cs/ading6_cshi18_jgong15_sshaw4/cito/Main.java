package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.WebSourceSocket;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.gui.AnnotateHandler;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.gui.SearchHandler;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run.REPL;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.*;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

public final class Main {
  private static final int DEFAULT_PORT = 4567;
  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }
  public static void main(String[]args) {
    new Main(args).run();
  }
  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }
    // Instantiate new REPL
    REPL repl = new REPL(new PrintWriter(System.out), CitoWorld.getInstance());
    repl.run();
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n", templates);
      System.exit(1);
    }

    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();
    Spark.webSocket("/socket-process", WebSourceSocket.class);

    // Setup Spark Routes
    Spark.get("/socket-demo", new SocketDemoHandler(), freeMarker);
    Spark.get("/search", new SearchHandler(), freeMarker);
    Spark.get("/annotate/wiki/:pageURL", new AnnotateHandler(), freeMarker);

  }

  private class SocketDemoHandler implements TemplateViewRoute {

    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Demo");
      return new ModelAndView(variables, "socket-demo.ftl");
    }
  }

  /**
   * Display an error page when an exception occurs in the server.
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
