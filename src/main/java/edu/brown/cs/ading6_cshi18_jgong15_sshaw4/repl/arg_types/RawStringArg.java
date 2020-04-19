package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.arg_types;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.RegexArgType;

/**
 * An ArgType representing unquoted strings (which cannot contain any spaces).
 */
public class RawStringArg extends RegexArgType<String> {
  /**
   * Constructs a new RawStringArg.
   */
  public RawStringArg() {
    super("raw string", "\\S+");
  }

  @Override
  protected String convertChecked(String input) throws ParseException {
    return input;
  }
}
