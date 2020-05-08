package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.commands;

import com.google.common.base.Charsets;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.CitoWorld;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.graph.GraphSaver;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki.Citation;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki.Wiki;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.WikiQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.RootedSourcedMemGraph;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.arg_types.QuotedStringArg;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.CommandRunner;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.InvalidInputException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.command.SimpleCommand;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ArgType;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.ParseException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.parse.TypedString;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.repl.run.WorldException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.List;
import java.util.Set;

public class DownloadWikiCommand extends SimpleCommand {
  public DownloadWikiCommand() {
    super("download");
    addOption(new DownloadRunner(),
        new ArgType[]{new QuotedStringArg(), new QuotedStringArg()},
        new String[]{"wiki url", "dir name"});
  }

  private class DownloadRunner extends CommandRunner {
    @Override
    public void run(List<TypedString> arguments, PrintWriter pw)
        throws ParseException, InvalidInputException, WorldException {
      String wikiUrl = new QuotedStringArg().convert(arguments.get(0));
      String dirName = new QuotedStringArg().convert(arguments.get(1)) + "/";
      int citeCount = 0;

      File dir = new File("dummies/" + dirName);
      if (!dir.mkdir()) {
        throw new InvalidInputException("directory " + dirName + " already exists.");
      }

      Wiki wiki;
      String html;
      Document doc;
      try {
        wiki = new WikiQuery(CitoWorld.getInstance().getTimeout()).query(wikiUrl);
        html = wiki.getContentHTML();
        doc = Jsoup.parse(html);
      } catch (QueryException e) {
        throw new WorldException("could not obtain wiki: " + e.getMessage());
      }

      Set<String> citationIds = wiki.getCitationIDs();
      for (String id : citationIds) {
        Citation citation = wiki.getCitationFromID(id,
            CitoWorld.getInstance().getTimeout(),
            CitoWorld.getInstance().getDepth(),
            CitoWorld.getInstance().getThresh());
        // if citation is a web type, find graph and save it, and override
        // corresponding links in the original html
        if (citation.getType().equals(Citation.WEB_TYPE)) {
          // obtain and save graph, dangerous casting here
          RootedSourcedMemGraph<Source, String> graph =
              (RootedSourcedMemGraph<Source, String>) citation.getGraph();
          String citeDirName = dirName + "cite" + citeCount++ + "/";
          File citeDir = new File("dummies/" + citeDirName);
          if (!citeDir.mkdir()) {
            throw new WorldException("somehow " + citeDirName + "already exists.");
          }
          GraphSaver saver = new GraphSaver("dummies/" + citeDirName);
          saver.saveGraph(graph);

          // override corresponding links in wikipedia html
          Elements links = doc.select(id).select(".external");
          links.forEach(l -> l.attr("href", citeDirName + "source0.html"));

        } else {
          // if citation isn't, don't save any graph, and link to an irrelevant deadend
          // page (like singleton)
          Elements links = doc.select(id).select(".external");
          links.forEach(l -> l.attr("href", "singleton.html"));
        }
      }

      // acquire modified html and save in the dummies directory
      String moddedWikiHtml = doc.html();
      String wikiFileName = wikiUrl.replace("https://en.wikipedia.org/wiki/", "");
      File fileDir = new File("dummies/" + wikiFileName + ".html");
      try {
        fileDir.createNewFile();
        Writer out = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(fileDir), Charsets.UTF_8));
        out.write(moddedWikiHtml);
        out.close();
      } catch (IOException e) {
        System.err.println("error writing wiki file: " + e.getMessage());
      }
      pw.write("Downloaded wiki: " + wikiFileName + "." + System.lineSeparator());
      pw.flush();
    }
  }
}
