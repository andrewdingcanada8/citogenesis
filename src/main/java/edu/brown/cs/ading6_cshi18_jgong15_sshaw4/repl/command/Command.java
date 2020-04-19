package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ArgType;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.argument.Argument;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.argument.ArgumentRoot;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseProgress;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run.WorldException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a REPL command. Can have multiple options for arguments.
 *
 * @author calder
 *
 */
public abstract class Command {
  private String name;

  /**
   * Creates a command with a given name.
   * @param name the name of the command
   */
  protected Command(String name) {
    this.name = name;
  }

  /**
   * Accessor for the name of the command, to allow the World to recognize when this command is run.
   *
   * @return the name of the command
   */
  public String getName() {
    return name;
  };

  /**
   * Gets an error message in the case that it is unclear what the user may have been trying to do.
   * Lists all the options you can enter into this command.
   * @return the error message
   */
  public abstract String getGeneralErrorMessage();

  protected abstract ArgumentRoot<?> getArgRoot();

  /**
   * Parses a string of input, determines if it matches an allowed input to this command, and
   * runs the command if so.
   *
   * @param args the arguments to the command (unparsed)
   * @param pw output writer
   * @throws ArgumentFormException if something about the argument tree is badly-formed
   * @throws ParseException if something went wrong when parsing the input
   * @throws InvalidInputException if the input does not conform to what the
   *                               command expects
   * @throws WorldException if something goes wrong running the command
   */
  public void run(String args, PrintWriter pw) throws ArgumentFormException,
          ParseException, InvalidInputException, WorldException {
//    All the arguments that are guaranteed to be able to parse something from the beginning of
//    the unparsed portion of the input
    List<Argument<?>> argList = new ArrayList<>();
//    Starts out containing only the root argument
    argList.add(getArgRoot());
//     Different branches of the tree might parse the string in different ways, so each argument
//     in argList has its own ParseProgress to parse from
    List<ParseProgress> inputList = new ArrayList<>();
    inputList.add(new ParseProgress(args));

//    Progresses down the argument tree, level by level, until either a command has been run or
//    none of the arguments at this level can parse anything from their ParseProgress
    int level = 1;
    boolean ranCommand = false;
    while (!ranCommand) {
//      Start new versions of argList and inputList to populate for the new level
      List<Argument<?>> newArgList = new ArrayList<>();
      List<ParseProgress> newInputList = new ArrayList<>();
//      An error for every branch we can't proceed down
      List<String> errorList = new ArrayList<>();

//      For each Argument in argList, parses one argument string off of the input, and then either
//      (1) runs the command if possible, (2) passes the remaining unparsed input onto all the child
//      (next) Arguments that can parse it, or (3) adds an error to the list if can't do (1) or (2).
      for (int argIndex = 0; argIndex < argList.size(); argIndex++) {
        Argument<?> arg = argList.get(argIndex);
        ParseProgress input = inputList.get(argIndex);
//        Parse one argument string off of the input (guaranteed to be possible)
        ParseProgress newInput = arg.nextToken(input);

//        Try running the command if there is no input left to parse
        if (newInput.isDone()) {
          ranCommand = arg.run(newInput.getTokens(), pw);
          if (ranCommand) {
            break;
          }
        }

//        All the arguments that can come after this one
        List<? extends Argument<?>> nextArgs = arg.getNext();
//        The remaining unparsed part of the string
        String remaining = newInput.getRemaining();
        boolean thereAreNextArguments = nextArgs.size() != 0;
        boolean thereIsTextRemaining = !newInput.isDone();

        if (thereAreNextArguments) {
          if (thereIsTextRemaining) {
//            See which of the next arguments can parse from the beginning of remaining
            List<Argument<?>> nextArgsThatCanParse = new ArrayList<>();
            for (Argument<?> nextArg : nextArgs) {
              ArgType<?> nextArgType = nextArg.getArgType();
              if (nextArgType.isStartOf(remaining)) {
                nextArgsThatCanParse.add(nextArg);
              }
            }

//            If none of them can, add an error
            if (nextArgsThatCanParse.size() == 0) {
              StringBuilder errorMessage = new StringBuilder();
              if (nextArgs.size() == 1) {
                errorMessage.append(nextArgs.get(0).getName());
              } else {
                errorMessage.append("Argument ");
                errorMessage.append(level);
              }
              errorMessage.append(" must be of type ");
              errorMessage.append(listArgTypeNames(nextArgs));
              errorMessage.append(".");
              errorList.add(errorMessage.toString());
            } else {
//              If at least one of the next arguments can parse from the beginning of remaining,
//              then store that argument in the list for the next iteration
              for (Argument<?> nextArg : nextArgsThatCanParse) {
                newArgList.add(nextArg);
                newInputList.add(newInput);
              }
            }
          } else {
//            If this argument is not the last one, but there is no more input to parse, add an
//            error.
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Not enough arguments. You must add an argument of type ");
            errorMessage.append(listArgTypeNames(nextArgs));
            errorMessage.append(".");
            errorList.add(errorMessage.toString());
          }
        } else {
          if (thereIsTextRemaining) {
//            If this argument is the last one, but there is still input to parse, add an error.
            errorList.add("Too many arguments. You only need " + (level - 1) + ".");
          } else {
//            If this argument is the last one, and there is no input to parse, but there is
//            nothing left to run, then the argument tree is badly-formed — a command that parses
//            completely should always result in an argument being run.
            throw new ArgumentFormException("Nothing to run at a leaf.");
          }
        }
      }

//      If we were not able to run a command or proceed down a level anywhere, throw an error
      if (!ranCommand && newArgList.size() == 0) {
        if (errorList.size() == 0) {
          throw new ArgumentFormException("Reached a state with no command run and no child "
                  + "commands to proceed to, but no error.");
        }

//        If only one argument branch resulted in an error, throw its error message. If multiple
//        argument branches resulted in exactly the same error message, throw that message. If
//        multiple argument branches gave different error messages, then it is unclear which one
//        the user was trying to use, so just name the format of all possible inputs.
        String firstError = errorList.get(0);
        boolean allTheSameError = true;
        for (int i = 1; i < errorList.size(); i++) {
          if (!errorList.get(i).equals(firstError)) {
            allTheSameError = false;
            break;
          }
        }
        if (allTheSameError) {
          throw new InvalidInputException(errorList.get(0));
        } else {
          throw new InvalidInputException(getGeneralErrorMessage());
        }
      }

      level++;
      argList = newArgList;
      inputList = newInputList;
      errorList.clear();
    }
  }

  private String listArgTypeNames(List<? extends Argument<?>> args) {
    List<String> argNames = new ArrayList<>();
    int numNextArgs = 0;
    for (Argument<?> arg : args) {
      String argName = arg.getName();
      if (!argNames.contains(argName)) {
        argNames.add(arg.getArgType().getName());
        numNextArgs++;
      }
    }
    StringBuilder argListText = new StringBuilder();
    for (int nextArgIndex = 0; nextArgIndex < numNextArgs; nextArgIndex++) {
      String argTypeName = argNames.get(nextArgIndex);
      argListText.append(argTypeName);
      if (numNextArgs == 2 && nextArgIndex == 0) {
        argListText.append(" or ");
      } else if (numNextArgs > 2) {
        if (nextArgIndex == numNextArgs - 2) {
          argListText.append(", or ");
        } else if (nextArgIndex < numNextArgs - 2) {
          argListText.append(", ");
        }
      }
    }
    return argListText.toString();
  }
}
