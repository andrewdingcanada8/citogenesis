package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse;

/**
 * Represents a type of argument to a command. Allows for checking if a string of arguments
 * begins with something of this type, and can convert a string to this type.
 * @param <T> the type that this argument represents
 */
public abstract class ArgType<T> {
  private String name;

  protected ArgType(String name) {
    this.name = name;
  }

  /**
   * Gets the name of this type (e.g. integer, string, etc). Must be unique.
   * @return the name
   */
  public String getName() {
    return name;
  };

  /**
   * Determines whether a String represents something of type T.
   * @param input the input String
   * @return whether the input String represents something of this type
   */
  public abstract boolean matches(String input);

  /**
   * Determines whether a String starts with something of type T.
   * @param input the input String
   * @return whether the input String starts with something of this type
   */
  public abstract boolean isStartOf(String input);

  /**
   * If a String starts with something of type T, returns a SplitString of the part of type T and
   * the remaining part (without whitespace).
   * @param input the input String
   * @return the SplitString from splitting off the part of type T from the front
   * @throws ParseException if the String does not start with something of type T
   */
  public SplitString split(String input) throws ParseException {
    if (!isStartOf(input)) {
      throw new ParseException(input + " does not start with type " + getName());
    } else {
      return splitChecked(input);
    }
  }

  /**
   * Assuming a String starts with something of type T, returns a SplitString of the part of type T
   * and the remaining part (without whitespace).
   * @param input the input String
   * @return the SplitString from splitting off the part of type T from the front
   * @throws ParseException if the String does not start with something of type T
   */
  protected abstract SplitString splitChecked(String input) throws ParseException;

  /**
   * Converts an input string to type T. (Checks that the string can be converted to type T first.)
   * @param input the input string
   * @return the input interpreted as type T
   * @throws ParseException if the input could not be converted to type T
   */
  public T convert(String input) throws ParseException {
    if (!matches(input)) {
      throw new ParseException(input + " cannot be interpreted as type " + getName());
    } else {
      return convertChecked(input);
    }
  }

  /**
   * Converts an input string to type T, assuming that the string is able to be converted to type T.
   * @param input the input string
   * @return the input interpreted as type T
   * @throws ParseException if the input could not be converted to type T
   */
  protected abstract T convertChecked(String input) throws ParseException;

  /**
   * Converts an input TypedString to type T.
   * @param input the input TypedString
   * @return the input interpreted as type T
   * @throws ParseException if the input could not be converted to type T
   */
  public T convert(TypedString input) throws ParseException {
    return convert(input.getString());
  }
}
