package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.argument;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ArgType;

import java.util.List;

/**
 * A non-lazy Argument that stores all of the child Arguments in a List.
 *
 * @param <N> the type of any next argument
 */
public abstract class ListArgument<N extends Argument<?>> extends Argument<N> {
  private List<N> next;

  /**
   * Creates a ListArgument that can be followed by any of the arguments in the given list.
   *
   * @param argType the type of input this Argument represents
   * @param next    all the arguments that can follow this one
   */
  protected ListArgument(ArgType<?> argType, List<N> next) {
    super(argType);
    this.next = next;
  }

  /**
   * Return next argument in the list-branch.
   *
   * @return next argument in the branch
   */
  public List<N> getNext() {
    return next;
  }

  /**
   * Adds a new argument to the allowed argument list.
   *
   * @param arg the argument to add.
   */
  public void addArgument(N arg) {
    next.add(arg);
  }
}
