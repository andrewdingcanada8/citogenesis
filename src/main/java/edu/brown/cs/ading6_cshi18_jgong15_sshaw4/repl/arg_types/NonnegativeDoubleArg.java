package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.arg_types;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.RegexArgType;

/**
 * An ArgType representing floating point numbers.
 */
public class NonnegativeDoubleArg extends RegexArgType<Double> {
  /**
   * Constructs a new NonnegativeDoubleArg.
   */
  public NonnegativeDoubleArg() {
    super("nonnegative real number", "\\d+(\\.\\d+)?");
  }

  @Override
  protected Double convertChecked(String input) throws ParseException {
    try {
      return Double.valueOf(input);
    } catch (NumberFormatException e) {
      throw new ParseException("Could not interpret \"" + input + "\" as a nonnegative number.");
    }
  }
}
