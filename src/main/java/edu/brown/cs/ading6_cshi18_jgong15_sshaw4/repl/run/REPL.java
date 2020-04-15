package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.ArgumentFormException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.InvalidInputException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used for running a command line using commands from any number of Worlds.
 */
public class REPL {
  private final World[] worlds;
  private final PrintWriter pw;

  /**
   * Creates a new REPL that runs commands from certain Worlds.
   * @param pw the PrintWriter to print the results
   * @param worlds the Worlds whose commands can be run
   */
  public REPL(PrintWriter pw, World... worlds) {
    this.worlds = worlds;
    this.pw = pw;
  }

  /**
   * Starts the REPL. Repeatedly accept input, run a command if able, and return an output or
   * error message.
   */
  public void run() {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    Pattern pat = Pattern.compile("^\\s*\\S+\\s*");

    try {
      String command = in.readLine();

      while (command != null) {
        Matcher mat = pat.matcher(command);
        if (mat.find()) {
          int splitIndex = mat.end();
          String commandName = command.substring(0, splitIndex).trim();
          String arguments = command.substring(splitIndex);
          boolean ranCommand = false;

          for (World w : worlds) {
            if (w.isValidCommand(commandName)) {
              try {
                w.runCommand(commandName, arguments, pw);
              } catch (ArgumentFormException | ParseException
                  | InvalidInputException | WorldException e) {
                handleError(e);
              }
              ranCommand = true;
              break;
            }
          }

          if (!ranCommand) {
            handleError("\"" + commandName + "\" is not a valid command.");
          }
        } else {
          handleError("Please enter a command.");
        }

        command = in.readLine();
      }

      for (World w : worlds) {
        try {
          w.close();
        } catch (WorldException e) {
          handleError(e);
        }
      }
      pw.close();
    } catch (IOException e) {
      handleError("An I/O exception occurred.");
    }
  }

  /**
   * Indicates an error by printing "ERROR:" followed by the error message.
   *
   * @param message the error message
   */
  public final void handleError(String message) {
    pw.println("ERROR: " + message);
    pw.flush();
  }

  /**
   * Indicates an error by printing "ERROR:" followed by the error message of
   * the input Exception.
   *
   * @param e the exception whose error message will be printed
   */
  public final void handleError(Exception e) {
    handleError(e.getMessage());
  }
}
