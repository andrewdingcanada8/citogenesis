package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.ArgumentFormException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.Command;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.InvalidInputException;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores a set of commands and runs them when given the correct input. Can also store
 * information that the commands can act upon, and can also have methods that allow other objects
 * to interact with the data without using a command line.
 */
public abstract class World {
  private final Map<String, Command> commandMap;

  protected World(Command... commands) {
    commandMap = new HashMap<>();
    addCommands(commands);
  }

  protected void addCommands(Command... commands) {
    for (Command command : commands) {
      commandMap.put(command.getName(), command);
    }
  }

  /**
   * Passes on the user input to the correct command.
   * @param commandName the name of the command to run
   * @param arguments the arguments to pass to the command (as a String)
   * @param pw output writer
   * @throws ArgumentFormException if something about an argument tree is badly-formed
   * @throws ParseException if something goes wrong when trying to parse the command
   * @throws InvalidInputException if a command with this name does not exist, or if the
   * arguments to the command do not fit the required format
   * @throws WorldException if something goes wrong when running the command
   */
  public final void runCommand(String commandName, String arguments, PrintWriter pw)
      throws ParseException,
          InvalidInputException, WorldException, ArgumentFormException {
    if (!commandMap.containsKey(commandName)) {
      throw new InvalidInputException("\"" + commandName + "\" is not a valid command.");
    }
    Command thisCommand = commandMap.get(commandName);
    thisCommand.run(arguments, pw);
  }

  /**
   * Returns whether this World has a command with a certain name.
   * @param commandName the name of the command
   * @return whether this World has a command with this name
   */
  public boolean isValidCommand(String commandName) {
    return commandMap.containsKey(commandName);
  }

  /**
   * Does everything that needs to be done when the user is finished with the program. By
   * default, does nothing.
   * @throws WorldException if something goes wrong while doing these things
   */
  public void close() throws WorldException {
  }
}
