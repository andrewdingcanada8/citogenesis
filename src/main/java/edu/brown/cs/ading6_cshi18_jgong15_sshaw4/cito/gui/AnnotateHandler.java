package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.gui;

import com.google.common.collect.ImmutableMap;
import spark.*;

import java.util.Map;

public class AnnotateHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) throws Exception {
    QueryParamsMap qm = request.queryMap();
    String pageURL = request.params(":pageURL");
    System.out.println(pageURL); //TODO: Delete later

    Map<String, Object> variables = ImmutableMap.of("title",
            "Cito: Annotate");
    return new ModelAndView(variables, "annotate.ftl");
  }
}
