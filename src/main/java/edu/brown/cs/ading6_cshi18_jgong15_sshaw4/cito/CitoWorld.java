package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.commands.CitationSearchCommand;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.commands.WikiLoadCommand;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki.Wiki;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run.World;

public final class CitoWorld extends World {
  private static CitoWorld citoWorld;
  private Wiki wiki;

  public static CitoWorld getInstance() {
    if (citoWorld == null) {
      citoWorld = new CitoWorld();
    }
    return citoWorld;
  }

  private CitoWorld() {
    super();
    addCommands(new CitationSearchCommand(), new WikiLoadCommand());
  }

  public void setWiki(Wiki wiki) {
    this.wiki = wiki;
  }
}
