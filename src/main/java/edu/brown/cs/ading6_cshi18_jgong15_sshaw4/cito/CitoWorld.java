package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.commands.CitationSearchCommand;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run.World;

public class CitoWorld extends World {

  public CitoWorld() {
    super();
    addCommands(new CitationSearchCommand());
  }

}
