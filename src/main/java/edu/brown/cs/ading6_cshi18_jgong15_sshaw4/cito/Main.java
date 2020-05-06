package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.gui.AnnotateHandler;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.gui.GraphHandler;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.gui.MainHandler;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.gui.SearchHandler;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.sockets.WikiCitationSocket;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run.REPL;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public final class Main {
  private static boolean isMocking;
  private static WireMockServer mockServer;
  private static boolean isVerbose = true;

  private static final int DEFAULT_SPARK_PORT = 4567;
  private static final int DEFAULT_MOCK_PORT = 8089;
  private String[] args;


  private Main(String[] args) {
    this.args = args;
  }

  public static void main(String[] args) {
    new Main(args).run();
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(DEFAULT_SPARK_PORT);
    parser.accepts("mock");
    parser.accepts("mockport").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_MOCK_PORT);
    parser.accepts("verbose");
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    isVerbose = options.has("verbose");

    if (options.has("mock")) {
      // start mockserver
      isMocking = true;
      mockServer = new WireMockServer(options().port((int) options.valueOf("mockport")));
      mockServer.start();
      MockServerUtils.setUpMockServer(mockServer.port());
    } else {
      isMocking = false;
    }

    // Instantiate new REPL
    REPL repl = new REPL(new PrintWriter(System.out), CitoWorld.getInstance());
    repl.run();

    if (isMocking) {
      mockServer.stop();
    }
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
    Spark.webSocket("/citation-socket", WikiCitationSocket.class);
//    Spark.webSocket("/socket-process", DemoSocket.class);

    // Setup Spark Routes
//    Spark.get("/socket-demo", new SocketDemoHandler(), freeMarker);
    Spark.get("/main", new MainHandler(), freeMarker);
    Spark.get("/search", new SearchHandler(), freeMarker);
    Spark.get("/wiki/:pageURL", new AnnotateHandler(), freeMarker);
    Spark.get("/graph", new GraphHandler(), freeMarker);
//    Spark.get("/graph/wiki/:pageURL", new GraphHandler(), freeMarker);
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

  public static boolean isMocking() {
    return isMocking;
  }

  public static String getMockUrl() {
    return mockServer.baseUrl();
  }

  public static boolean isVerbose() {
    return isVerbose;
  }
}
