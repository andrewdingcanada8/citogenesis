package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.arg_types;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.RegexArgType;

/**
 * An ArgType representing integers.
 */
public class IntArg extends RegexArgType<Integer> {
  /**
   * Constructs a new IntArg.
   */
  public IntArg() {
    super("integer", "-?\\d+");
  }

  @Override
  protected Integer convertChecked(String input) throws ParseException {
    try {
      return Integer.valueOf(input);
    } catch (NumberFormatException e) {
      throw new ParseException("Could not interpret \"" + input + "\" as an integer.");
    }
  }
}
