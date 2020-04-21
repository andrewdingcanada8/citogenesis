package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WebSource extends Source {

  private String html;
  private String url;
  private Calendar timestamp;
  private List<String> links;

  /**
   * Creates a new WebSource.
   * @param url source url. http/https prefix is necessary or link-scraping will break.
   * @param html source html
   * @param timestamp publication timestamp
   */
  public WebSource(String url, String html, Calendar timestamp) {
    this.html = html;
    this.url = url;
    this.timestamp = timestamp;

    // extract all links
    Document doc = Jsoup.parse(html, url);
    Elements anchors = doc.getElementsByTag("a");
    this.links = anchors.stream()
        .map(e -> e.absUrl("href"))
        .filter(str -> !str.equals(""))
        .collect(Collectors.toList());
  }

  @Override
  public String getHTML() {
    return html;
  }

  @Override
  public String getURL() {
    return url;
  }

  @Override
  public List<String> getLinks() {
    return links;
  }

  @Override
  public Calendar getTimestamp() {
    return timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WebSource webSource = (WebSource) o;
    return Objects.equals(url, webSource.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url);
  }

  @Override
  public String toString() {
    return "WebSource{"
        + "url='" + url + '\''
        + '}';
  }
}
