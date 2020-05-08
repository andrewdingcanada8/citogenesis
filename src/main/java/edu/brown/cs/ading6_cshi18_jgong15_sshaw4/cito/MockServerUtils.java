package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public final class MockServerUtils {

  public static final String PAGES_SERVER_PATH = "http://localhost:63342/term-project-ading6-cshi18-jgong15-sshaw4-1/dummies/";
  public static final String PAGES_DIR_PATH = "dummies/";

  private MockServerUtils() {
  }

  public static void setUpMockServer(int port) {
    configureFor("localhost", port);
    File folder = new File(PAGES_DIR_PATH);
    try {
      Files.walk(Paths.get(PAGES_DIR_PATH))
          .filter(Files::isRegularFile)
          .forEach(p -> loadPage(p.toString()));
    } catch (IOException e) {
      System.out.println("Error while walking on " + PAGES_DIR_PATH + ": " + e.getMessage());
    }
  }

  private static void loadPage(String filepath) {
    File input = new File(filepath);
    Document doc;
    try {
      doc = Jsoup.parse(input, "UTF-8", PAGES_SERVER_PATH);
    } catch (IOException e) {
      System.err.println("Error loading mock page " + filepath + ": " + e.getMessage());
      return;
    }
    Elements els = doc.getElementsByAttribute("data-timestamp");
    String timeStr = els.get(0).attr("data-timestamp").replace("\"", "");
    if (timeStr.equals("")) {
      return;
    }
    stubFor(get(urlEqualTo("/?url="
        + doc.baseUri() + filepath.replace("dummies/", "")
        + "&fl=timestamp&output=json&limit=1")).willReturn(aResponse()
        .withHeader("Content-type", "application/json")
        .withBody("[[\"timestamp\"],\n"
            + "[\"" + timeStr + "\"]]\n")));
  }
}
