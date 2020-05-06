package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.gui;

import com.google.common.collect.ImmutableMap;
import spark.*;

import java.util.Map;

public class MainHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) throws Exception {
    Map<String, Object> variables = ImmutableMap.of("title",
            "Citogenesis");
    return new ModelAndView(variables, "main.ftl");
  }
}
