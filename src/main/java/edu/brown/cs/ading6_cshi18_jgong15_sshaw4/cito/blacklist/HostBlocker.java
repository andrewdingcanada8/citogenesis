package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.blacklist;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.RootedSourcedMemGraph;

import java.net.MalformedURLException;
import java.net.URL;

class HostBlocker implements Rule {
  private String host;
  protected HostBlocker(String host) {
    this.host = host;
  }
  @Override
  public boolean verify(String url, Vertex<Source, String> prev, RootedSourcedMemGraph<Source, String> graph) {
    URL srcUrl;
    try {
      srcUrl = new URL(url);
    } catch (MalformedURLException e) {
      return false;
    }
    return !srcUrl.getHost().contains(host);
  }
}
