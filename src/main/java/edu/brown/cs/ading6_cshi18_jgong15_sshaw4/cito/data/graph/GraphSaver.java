package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.graph;

import com.google.common.base.Charsets;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.Source;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Edge;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.Vertex;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.exception.GraphException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.graph.sourced.remembering.RootedSourcedMemGraph;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class GraphSaver {
  private int count;
  private String dir;
  public GraphSaver(String dir) {
    count = 0;
    this.dir = dir;
  }

  /**
   * Utility function to save graph data in dummies directory for local browsing.
   *
   * @param graph graph to save
   * @param <T>   any graph in Cito.data
   */
  public <T extends RootedSourcedMemGraph<Source, String>> void saveGraph(T graph) {
    Vertex<Source, String> root = graph.getHead();
    Map<Vertex<Source, String>, String> visited = new HashMap<>();
    saveAsHtml(root, visited);
  }
  
  private String saveAsHtml(Vertex<Source, String> vert,
                            Map<Vertex<Source, String>, String> visited) {
    // if visited, return the existing filename
    if (visited.containsKey(vert)) {
      return visited.get(vert);
    }
    // acquire source, confirm it is a websource
    Source src = vert.getVal();
    if (!src.isValid()) {  // if a dead source, don't save it
      return "";
    }
    // decide on filename, enter into the map
    String name = "source" + count++ + ".html";
    visited.put(vert, name);
    // base case - if we are at a leaf node
    try {
      Set<Edge<Source, String>> edges = vert.getEdges()
          .stream()
          .filter(e -> e.getDest().getVal().isValid())
          .collect(Collectors.toSet());
      String newHtml;
      Document doc = Jsoup.parse(src.getHTML(), src.getURL());
      Elements ps = doc.getElementsByTag("p");
      Elements lis = doc.getElementsByTag("li");
      if (edges.isEmpty()) {
        // clean out all urls
        Stream.concat(ps.stream(), lis.stream())
            .flatMap(e -> e.getElementsByTag("a").stream())
            .filter(a -> a.hasAttr("href"))
            .forEach(a -> a.attr("href", ""));
      } else { // recur case - if there are child sources
        // obtain filenames of sources
        List<String> childPaths = edges.stream()
            .map(Edge::getDest)
            .map(v -> saveAsHtml(v, visited))
            .filter(str -> !str.equals(""))
            .collect(Collectors.toList());

        // next, clean out links, and replace them with new ones (and leave the rest empty)
        List<Element> as = Stream.concat(ps.stream(), lis.stream())
            .flatMap(e -> e.getElementsByTag("a").stream())
            .filter(a -> a.hasAttr("href"))
            .map(a -> a.attr("href", ""))
            .collect(Collectors.toList());
        for (int i = 0; i < Math.min(childPaths.size(), as.size()); i++) {
          as.get(i).attr("href", childPaths.get(i));
        }
      }
      // append time source
      Calendar cal = src.getTimestamp();
      if (cal == null) {
        cal = new GregorianCalendar();
      }
      String timeStr = ((GregorianCalendar) cal).toZonedDateTime()
          .format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
      doc.select("body").first()
          .children()
          .first()
          .before("<p data-timestamp=\"" + timeStr + "\">timestring: " + timeStr + "</p>");
      newHtml = doc.html();
      // write result to a file
      File fileDir = new File(dir + name);
      try {
        fileDir.createNewFile();
        Writer out = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(fileDir), Charsets.UTF_8));
        out.write(newHtml);
        out.close();
      } catch (IOException e) {
        System.err.println("error writing file: " + e.getMessage());
        return "";
      }


      return name;
    } catch (GraphException e) {
      throw new IllegalStateException("something is seriously wrong: " + e.getMessage());
    }
  }

}
