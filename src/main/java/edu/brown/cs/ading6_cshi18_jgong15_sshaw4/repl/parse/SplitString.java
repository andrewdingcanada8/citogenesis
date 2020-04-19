package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse;

/**
 * Represents two halves of a String.
 */
public class SplitString {
  private String start;
  private String rest;

  /**
   * Creates a SplitString by splitting an input String at a given index.
   * @param whole the string to split
   * @param splitIndex the index at which to split it
   * @throws ParseException if string index bounds are exceeded
   */
  public SplitString(String whole, int splitIndex) throws ParseException {
    if (splitIndex < 0 || splitIndex > whole.length()) {
      throw new ParseException("splitIndex " + splitIndex + " out of bounds (string "
              + "length " + whole.length() + ")");
    }
    this.start = whole.substring(0, splitIndex);
    this.rest = whole.substring(splitIndex);
  }

  /**
   * Gets the part of the String that has been split off the front.
   * @return the front part of the String
   */
  public String getStart() {
    return start;
  }

  /**
   * Gets the remaining part of the String after splitting off the front part.
   * @return the remaining part of the String
   */
  public String getRest() {
    return rest;
  }
}
