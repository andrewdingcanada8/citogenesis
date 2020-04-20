package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

public abstract class Source {

  // TODO: integrate date functionality
  // TODO: integrate text-checking fuctionality
  

  public abstract String getHTML();

  public abstract String getURL();

  /**
   * Return all hrefs.
   * @return hrefs
   */
  public abstract String[] getLinks();

  /**
   * Returns plaintext body content.
   * @return plaintext
   */
  public abstract String getContent();




  // original HTML
  // content - Body text - run relevance comparison
  // source's URLdepth
  // hrefs


  // outsource relevance to another algorithm-only class
}
