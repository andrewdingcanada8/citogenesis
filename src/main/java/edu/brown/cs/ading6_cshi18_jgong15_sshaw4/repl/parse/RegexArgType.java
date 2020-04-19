package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An ArgType that uses regular to identify and parse strings.
 * @param <T> the type that this argument represents
 */
public abstract class RegexArgType<T> extends ArgType<T> {
  private String regex;
  private String startRegex;

  protected RegexArgType(String name, String regex) {
    super(name);
    this.regex = regex;
    startRegex = "^" + regex + "(\\s+|$)";
  }

  /**
   * Uses the regular expression to see if the input string is of type T.
   * @param input the input String
   * @return whether it is of type T
   */
  public boolean matches(String input) {
    return input.matches(regex);
  }

  /**
   * Uses the regular expression to see if the input string starts with something of type T.
   * @param input the input String
   * @return whether it starts with something of type T
   */
  public boolean isStartOf(String input) {
    Pattern pat = Pattern.compile(startRegex);
    Matcher mat = pat.matcher(input);
    return mat.find();
  }

  /**
   * Splits a string representing something of type T off of the input String.
   * @param input the input String
   * @return a SplitString containing the part that was split off (representing something of type
   * T) and tzhe rest of the string
   * @throws ParseException if the string does not start with something of type T
   */
  protected SplitString splitChecked(String input) throws ParseException {
    Pattern pat = Pattern.compile(startRegex);
    Matcher mat = pat.matcher(input);
    if (mat.find()) {
      int splitIndex = mat.end();
      return new SplitString(input, splitIndex);
    } else {
      throw new ParseException("Tried to split a string that did not start with " + getName());
    }
  }
}
