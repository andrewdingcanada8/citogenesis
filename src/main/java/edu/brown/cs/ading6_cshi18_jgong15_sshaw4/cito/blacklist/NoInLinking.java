package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.blacklist;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.RootedSourcedMemGraph;

import java.net.MalformedURLException;
import java.net.URL;

public class NoInLinking implements Rule {
  //TODO: testing
  @Override
  public boolean verify(String url,
                        Vertex<Source, String> prev,
                        RootedSourcedMemGraph<Source, String> graph) {
    // extract base urls
    URL srcURL;
    URL dstURL;
    try {
      srcURL = new URL(prev.getVal().getURL());
      dstURL = new URL(url);
    } catch (MalformedURLException e) {
      System.out.println(url + " is a malformed URL");
      return false;
    }

    String srcHost = srcURL.getHost();
    String dstHost = dstURL.getHost();

    return !(srcHost.contains(dstHost) || dstHost.contains(srcHost));
  }
}
