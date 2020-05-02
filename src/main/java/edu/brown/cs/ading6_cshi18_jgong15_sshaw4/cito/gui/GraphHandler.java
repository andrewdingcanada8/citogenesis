package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.gui;

import com.google.common.collect.ImmutableMap;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import java.util.Map;

public class GraphHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) throws Exception {
    Map<String, Object> variables = ImmutableMap.of("title",
            "Cito: Graph");
    return new ModelAndView(variables, "graph.ftl");
  }
}
