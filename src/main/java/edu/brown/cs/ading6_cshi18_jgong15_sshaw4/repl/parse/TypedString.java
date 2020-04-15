package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse;

/**
 * A string that represents a given type of information.
 */
public class TypedString {
  private String string;
  private ArgType<?> type;

  /**
   * Creates a new TypedString with a given string and a given type.
   * @param string the string
   * @param type an ArgType indicating what type of information the string represents
   */
  public TypedString(String string, ArgType<?> type) {
    this.string = string;
    this.type = type;
  }

  /**
   * Returns the string.
   * @return the string
   */
  public String getString() {
    return string;
  }

  /**
   * Returns the type of the string.
   * @return an ArgType indicating what type of information the string represents
   */
  public ArgType<?> getType() {
    return type;
  }
}
