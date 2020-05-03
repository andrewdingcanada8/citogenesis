package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.common.collect.ImmutableMap;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.gui.AnnotateHandler;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.gui.SearchHandler;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.sockets.WikiCitationSocket;
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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public final class Main {
  private static boolean isMocking;
  private static WireMockServer mockServer;

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
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    if (options.has("mock")) {
      // start mockserver
      isMocking = true;
      mockServer = new WireMockServer(options().port((int) options.valueOf("mockport")));
      mockServer.start();
      setUpStubs();
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

  /**
   * Load in expected requests into the MockServer.
   */
  private static void setUpStubs() {
    configureFor("localhost", mockServer.port());
    stubFor(get(urlMatching(".*")).willReturn(aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "text/plain")
        .withBody("testing12")));

  }

  public static boolean isMocking() {
    return isMocking;
  }

  public static String getMockUrl() {
    return mockServer.baseUrl();
  }
}
