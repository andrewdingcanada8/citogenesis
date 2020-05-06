package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.parsers;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.WebTestUtils;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.sync.TimeStampQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.Query;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.http.sync.HTMLQuery;
import org.jsoup.Jsoup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

public class WikiHTMLParserTest {
  private WikiHTMLParser parserMurphy;
  private WikiHTMLParser parserCastel;
  private Query<String, String> htmlQuery = new HTMLQuery(30);
  private Query<String, Calendar> timeQuery = new TimeStampQuery(30);

  @Before
  public void setUp() throws QueryException {
    String urlOne = "https://en.wikipedia.org/wiki/Murphy%27s_law";
    String htmlOne = htmlQuery.query(urlOne);
    Calendar timestampOne = timeQuery.query(urlOne);
    parserMurphy = new WikiHTMLParser(urlOne, htmlOne, timestampOne);

    String urlTwo = "https://en.wikipedia.org/wiki/Castel_Menardo";
    String htmlTwo = htmlQuery.query(urlTwo);
    Calendar timestampTwo = timeQuery.query(urlTwo);
    parserCastel = new WikiHTMLParser(urlTwo, htmlTwo, timestampTwo);
  }

  @After
  public void tearDown() {
    parserMurphy = null;
    parserCastel = null;
    htmlQuery = null;
    timeQuery = null;
  }

  @Test
  public void testParseForTitle() throws QueryException {
    assertEquals(parserMurphy.parseForTitle(), "Murphy's Law - Wikipedia");
    assertEquals(parserCastel.parseForTitle(), "Castel Menardo - Wikipedia");
  }

  @Test
  public void testParseForContentHTML() throws IOException {
    String html = parserCastel.getHtml();
    String url = "https://en.wikipedia.org/wiki/Castel_Menardo";
    assertEquals(parserCastel.parseForContentHTML(),
        Jsoup.parse(html, url).select("#content").outerHtml());
  }

  @Test
  public void testParseForContent() {
    String castelMenardoContent =
    "Castel Menardo From Wikipedia, the free encyclopedia Jump to navigation " +
        "Jump to search This article may be expanded with text translated from " +
        "the corresponding article in Italian. (May 2016) Click [show] for " +
        "important translation instructions. View a machine-translated version " +
        "of the Italian article. Machine translation like DeepL or Google " +
        "Translate is a useful starting point for translations, but translators " +
        "must revise errors as necessary and confirm that the translation is " +
        "accurate, rather than simply copy-pasting machine-translated text into " +
        "the English Wikipedia. Do not translate text that appears unreliable or " +
        "low-quality. If possible, verify the text with references provided in " +
        "the foreign-language article. You must provide copyright attribution in " +
        "the edit summary accompanying your translation by providing an interlanguage " +
        "link to the source of your translation. A model attribution edit summary" +
        " Content in this edit is translated from the existing Italian Wikipedia" +
        " article at [[:it:Castel Menardo]]; see its history for attribution." +
        " You should also add the template {{Translated|it|Castel Menardo}}" +
        " to the talk page. For more guidance, see Wikipedia:Translation." +
        " Menardo Castle Castel Menardo Serramonacesca Castle in Serramonacesca" +
        " Menardo Castle Type Castle Site history Built 12th century Castel Menardo" +
        " (Italian for Menardo Castle) is a medieval castle in Serramonacesca," +
        " Province of Pescara, Abruzzo, southern Italy.[1] References[edit] ^ Latini," +
        " Marialuce (2000). \"Serramonacesca, Castel Menardo (PE)\". Guida ai" +
        " Castelli d'Abruzzo (in Italian). Pescara: Carsa Edizioni. p. 117." +
        " ISBN 88-85854-87-7. External links[edit] Wikimedia Commons has media" +
        " related to Castel Menardo. \"Castel Menardo\" (in Italian). Regione" +
        " Abruzzo. Archived from the original on 18 April 2011. Retrieved 8" +
        " May 2016. Coordinates: 42°14′25″N 14°05′10″E\uFEFF / \uFEFF42.2402°N" +
        " 14.0862°E\uFEFF / 42.2402; 14.0862 This article about a castle in" +
        " Italy is a stub. You can help Wikipedia by expanding it." +
        " v t e Retrieved from " +
        "\"https://en.wikipedia.org/w/index.php?title=Castel_Menardo&oldid=918940382\"" +
        " Categories: Castles in Abruzzo Serramonacesca Italian castle stubs Hidden" +
        " categories: CS1 Italian-language sources (it) Articles to be expanded" +
        " from May 2016 All articles to be expanded Building and structure articles" +
        " needing translation from Italian Wikipedia Commons category link is on" +
        " Wikidata Coordinates on Wikidata All stub articles";
    assertEquals(parserCastel.parseForContent(), castelMenardoContent);
  }
}
