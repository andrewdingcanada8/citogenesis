package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters.url;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.RootedSourcedMemGraph;

import java.net.MalformedURLException;
import java.net.URL;

public class NoInLinking implements URLRule {
  //TODO: testing
  @Override
  public boolean verify(String url,
                        String prev,
                        RootedSourcedMemGraph<Source, String> graph) {
    // extract base urls
    URL srcURL;
    URL dstURL;
    try {
      srcURL = new URL(prev);
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
