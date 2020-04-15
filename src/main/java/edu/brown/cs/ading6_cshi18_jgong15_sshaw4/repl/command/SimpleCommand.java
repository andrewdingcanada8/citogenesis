package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ArgType;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.argument.ArgumentRoot;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.argument.ListArgument;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.argument.SimpleArgument;

import java.util.ArrayList;
import java.util.List;

/**
 * A Command that only uses SimpleArguments. Has methods making it easy to add options without
 * having to deal with making a tree yourself.
 */
public abstract class SimpleCommand extends Command {
  private ArgumentRoot<SimpleArgument> ar;
  private List<String> errorMessageOptions;

  /**
   * Creates a SimpleCommand with a given name.
   * @param name the name of the command
   */
  protected SimpleCommand(String name) {
    super(name);
    this.ar = new ArgumentRoot<>(new ArrayList<>());
    errorMessageOptions = new ArrayList<>();
  }

  /**
   * Creates a SimpleCommand with a given name and loads one argument option based on the other
   * parameters.
   * @param name the name of the command
   * @param cr the CommandRunner to run when this command receives the specified arguments
   * @param argTypes the types of arguments this command can receive
   * @param argNames the names of those arguments
   */
  public SimpleCommand(String name, CommandRunner cr, ArgType<?>[] argTypes, String[] argNames) {
    this(name);
    addOption(cr, argTypes, argNames);
  }

  @Override
  protected ArgumentRoot<SimpleArgument> getArgRoot() {
    return ar;
  }

  /**
   * An error message that lists all the allowed argument options.
   * @return the error message
   */
  @Override
  public String getGeneralErrorMessage() {
    return "The input to " + getName() + " must be one of the following:\n" + String.join("\n",
            errorMessageOptions);
  }

  /**
   * Adds an argument option to this command. Matches as many of the arguments as possible to the
   * preexisting tree (doesn't create a new branch identical to an old branch).
   * @param cr the CommandRunner to run when this command receives the specified arguments
   * @param argTypes the types of arguments this command can receive
   * @param argNames the names of those arguments
   */
  public void addOption(CommandRunner cr, ArgType<?>[] argTypes, String[] argNames) {
    if (argTypes.length != argNames.length) {
      throw new IllegalArgumentException("The number of arguments and the number of names must be"
              + " the same.");
    }
    int numArgs = argTypes.length;

    ListArgument<SimpleArgument> currentAddedArg = ar;
    int level = 0;
    while (level < numArgs) {
      List<SimpleArgument> nextAddedArgs = currentAddedArg.getNext();
      SimpleArgument matchingAddedArg = null;
      for (SimpleArgument arg : nextAddedArgs) {
        if (arg.getArgType().getName().equals(argTypes[level].getName())
                && arg.getName().equals(argNames[level])) {
          matchingAddedArg = arg;
          break;
        }
      }
      if (matchingAddedArg == null) {
        break;
      } else {
        currentAddedArg = matchingAddedArg;
      }
      level++;
    }

    SimpleArgument buildingArgs = null;
    for (int i = numArgs - 1; i >= level; i--) {
      ArgType<?> thisArgType = argTypes[i];
      String thisArgName = argNames[i];
      if (buildingArgs == null) {
        buildingArgs = new SimpleArgument(thisArgName, thisArgType, cr);
      } else {
        SimpleArgument oldBuildingArgs = buildingArgs;
        List<SimpleArgument> next = new ArrayList<>();
        next.add(oldBuildingArgs);
        buildingArgs = new SimpleArgument(thisArgName, thisArgType, next);
      }
    }
    currentAddedArg.addArgument(buildingArgs);

    StringBuilder newErrorMessageOption = new StringBuilder("> ");
    for (int i = 0; i < numArgs; i++) {
      newErrorMessageOption.append(argNames[i]);
      newErrorMessageOption.append(" (");
      newErrorMessageOption.append(argTypes[i].getName());
      newErrorMessageOption.append(")");
      if (i < numArgs - 1) {
        newErrorMessageOption.append(", ");
      }
    }
    errorMessageOptions.add(newErrorMessageOption.toString());
  }
}
