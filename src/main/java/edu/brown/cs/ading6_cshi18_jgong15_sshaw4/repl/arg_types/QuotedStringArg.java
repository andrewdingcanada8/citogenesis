package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.arg_types;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.RegexArgType;

/**
 * An ArgType representing quoted strings. When converting, removes the quotes and processes escape
 * sequences.
 */
public class QuotedStringArg extends RegexArgType<String> {
  /**
   * Constructs a new QuotedStringArg.
   */
  public QuotedStringArg() {
    super("quoted string", "\"([^\"\\\\]|\\\\.)*\"");
  }

  @Override
  protected String convertChecked(String input) throws ParseException {
    String withoutQuotes = input.substring(1, input.length() - 1);
    String escapedQuotes = withoutQuotes.replace("\\\"", "\"");
    return escapedQuotes.replace("\\\\", "\\");
  }
}
