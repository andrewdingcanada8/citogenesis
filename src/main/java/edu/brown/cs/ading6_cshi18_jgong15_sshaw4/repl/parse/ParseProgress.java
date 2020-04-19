package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse;


import java.util.ArrayList;
import java.util.List;

/**
 * Represents an in-progress parsing of a string.
 */
public class ParseProgress {
  private List<TypedString> tokens;
  private String remaining;

  /**
   * Creates a ParseProgess to represent a string that has not yet started being parsed.
   * @param start the string to start parsing
   */
  public ParseProgress(String start) {
    this(new ArrayList<>(), start);
  }

  /**
   * Creates a ParseProgress given already-parsed tokens and the remaining unparsed part.
   * @param tokens all the tokens that have already been parsed from this string
   * @param remaining the part of the string left to parse
   */
  public ParseProgress(List<TypedString> tokens, String remaining) {
    this.tokens = tokens;
    this.remaining = remaining.trim();
  }

  /**
   * Indicates whether the string has finished being parsed.
   * @return true if the string has finished being parsed, false if it's still in progress
   */
  public boolean isDone() {
    return remaining.equals("");
  }

  /**
   * Gets the parsed tokens of this string, along with their types.
   * @return the parsed tokens
   */
  public List<TypedString> getTokens() {
    return tokens;
  }

  /**
   * Gets the remaining unparsed part of the string.
   * @return the unparsed part of the string
   */
  public String getRemaining() {
    return remaining;
  }
}
