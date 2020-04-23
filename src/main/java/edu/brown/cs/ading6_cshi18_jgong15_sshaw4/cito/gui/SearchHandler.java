package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.gui;

import com.google.common.collect.ImmutableMap;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

public class SearchHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) throws Exception {
    String htmlString = new String();
    Scanner in = null;
    in = new Scanner(new File("data/cito/samplePage.txt"));
    while (in.hasNextLine()) {
      // Regex to make file consistent.
      String nextLine = in.nextLine();
      htmlString = htmlString.concat(nextLine);
    }
    in.close();
    assert htmlString != null;
    System.out.println(htmlString);

    Map<String, Object> variables = ImmutableMap.of("title",
            "Cito: Search", "html", htmlString, "content", htmlString);
    return new ModelAndView(variables, "main.ftl");
  }
}
