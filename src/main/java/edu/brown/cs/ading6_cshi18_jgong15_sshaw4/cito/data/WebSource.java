package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WebSource implements Source {

  private String html;
  private String content;
  private String url;
  private Calendar timestamp;
  private List<String> links;

  /**
   * Creates a new WebSource.
   * @param url source url. http/https prefix is necessary or link-scraping will break.
   * @param html source html
   */
  public WebSource(String url, String html) {
    this.html = html;
    this.url = url;
    this.timestamp = timestamp;

    // extract all links (href attributes in anchor tags)
    Document doc = Jsoup.parse(html, url);

    // extract all text elements (h1, h2, h3, h4, h5, h6, p, li tags)
    Elements h1s = doc.getElementsByTag("h1");
    Elements h2s = doc.getElementsByTag("h2");
    Elements h3s = doc.getElementsByTag("h3");
    Elements h4s = doc.getElementsByTag("h4");
    Elements h5s = doc.getElementsByTag("h5");
    Elements h6s = doc.getElementsByTag("h6");
    Elements ps = doc.getElementsByTag("p");
    Elements li = doc.getElementsByTag("li");

    // flatten into single list, extract text from each, and then join
    // with a single space apart
    this.content = Stream.of(h1s, h2s, h3s, h4s, h5s, h6s, ps, li)
        .flatMap(Collection::stream)
        .map(Element::text)
        .collect(Collectors.joining(" "));

    // extract links from relevant content
    this.links = Stream.concat(ps.stream(), li.stream())
        .flatMap(elt -> elt.getElementsByTag("a").stream())
        .map(elt -> elt.absUrl("href"))
        .filter(str -> !str.equals(""))
        .distinct()
        .collect(Collectors.toList());
  }

  @Override
  public String getHTML() {
    return html;
  }

  @Override
  public String getContent() {
    return content;
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
