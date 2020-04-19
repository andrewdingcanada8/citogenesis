package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.arg_types;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.RegexArgType;

/**
 * An ArgType representing natural numbers.
 */
public class NaturalNumberArg extends RegexArgType<Integer> {
  /**
   * Constructs a new NaturalNumberArg.
   */
  public NaturalNumberArg() {
    super("natural number", "\\d+");
  }

  @Override
  protected Integer convertChecked(String input) throws ParseException {
    try {
      return Integer.valueOf(input);
    } catch (NumberFormatException e) {
      throw new ParseException("Could not interpret \"" + input + "\" as a natural number.");
    }
  }
}
