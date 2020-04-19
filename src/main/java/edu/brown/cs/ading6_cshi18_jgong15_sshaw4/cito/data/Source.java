package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

public abstract class Source {

  // TODO: integrate date functionality
  // TODO: integrate text-checking fuctionality

  private int depth;

  public Source(int depth) {
    this.depth = depth;
  }

  public abstract double relevance(String keywords);

  public abstract String[] getLinks();

  public int getDepth() {
    return depth;
  }

  public void setDepth(int d) {
    depth = d;
  }

}
