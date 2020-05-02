package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.gui;

import com.google.common.collect.ImmutableMap;
import spark.*;

import java.io.File;
import java.util.Map;
import java.util.Scanner;

public class AnnotateHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) throws Exception {
    QueryParamsMap qm = request.queryMap();
    String pageURL = request.params(":pageURL");
    System.out.println(pageURL);

    Map<String, Object> variables = ImmutableMap.of("title",
            "Cito: Annotate", "results", pageURL);
    return new ModelAndView(variables, "annotate.ftl");
  }
}
