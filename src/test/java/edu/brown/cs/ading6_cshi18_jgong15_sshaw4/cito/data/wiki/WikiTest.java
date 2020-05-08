package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.data.wiki;

import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.builders.CitationBuilder;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries.WikiQuery;
import edu.brown.cs.ading6_cshi18_jgong15_sshaw4.data.exception.QueryException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WikiTest {
  Wiki wikiMurphysLaw;
  Wiki wikiBrownUniversity;
  Wiki wikiReptilianCons;
  Wiki wikiAvatar;

  @Before
  public void setUp() throws QueryException {
    String urlMurphysLaw = "https://en.wikipedia.org/wiki/Murphy%27s_law";
    wikiMurphysLaw = new WikiQuery(30).query(urlMurphysLaw);

    String urlBrownUniversity = "https://en.wikipedia.org/wiki/Brown_University";
    wikiBrownUniversity = new WikiQuery(30).query(urlBrownUniversity);

    String urlRep = "https://en.wikipedia.org/wiki/Reptilian_conspiracy_theory";
    wikiReptilianCons = new WikiQuery(40).query(urlRep);

    String urlAvatar = "https://en.wikipedia.org/wiki/Avatar:_The_Last_Airbender";
    wikiAvatar = new WikiQuery(40).query(urlAvatar);
  }

  @After
  public void tearDown() {
    wikiMurphysLaw = null;
  }

  @Test
  public void testGetCitationIDs() {
    assertEquals(wikiMurphysLaw.getCitationIDs().size(), 27);
    assertEquals(wikiBrownUniversity.getCitationIDs().size(), 151);
    assertEquals(wikiReptilianCons.getCitationIDs().size(), 27);
    assertEquals(wikiAvatar.getCitationIDs().size(), 102);
    System.out.println(wikiReptilianCons.getCitationIDs());
  }

  @Test
  public void testGetCitationFromIDForCitationOne() {
    Citation citationOne = wikiMurphysLaw.getCitationFromID(
        "#cite_note-1", 30, 2, 0.2);
    System.out.println(citationOne);
    Citation citationOneBuilded =
        new CitationBuilder(Citation.NON_HTML_TYPE, "#cite_note-1")
                .setTimeout(30)
                .setDepth(2)
                .setThreshold(0.2)
                .setUrl(new ArrayList<>(
                    List.of("https://web.archive.org/web/20080312052959/http://" +
                        "listserv.linguistlist.org/cgi-bin/wa?A2=ind0710B&" +
                        "L=ADS-L&P=R432&I=-3")))
                .build();
    System.out.println(citationOneBuilded);
    assertEquals(citationOne, citationOneBuilded);
  }

  @Test
  public void testGetCitationFromIDForCitationSeven() {
    Citation citationSeven = wikiMurphysLaw.getCitationFromID(
        "#cite_note-7", 30, 2, 0.2);
    System.out.println(citationSeven);
    Citation citationSevenBuilded =
        new CitationBuilder(Citation.WEB_TYPE, "#cite_note-7")
            .setTimeout(30)
            .setDepth(2)
            .setThreshold(0.2)
            .setUrl(new ArrayList<>(
                List.of("https://www.nytimes.com/1948/06/13/archives/"
                    + "thingness-of-things-resistentialism-it-says-here-is"
                    + "-the-very-latest.html")))
            .build();
    System.out.println("Builded: " + citationSevenBuilded);
    assertEquals(citationSeven, citationSevenBuilded);
  }

  @Test
  public void runGetCitationFromID() {
    wikiReptilianCons.getCitationFromID(
        "#cite_note-book,_synonym-1", 30, 2, 0.2);
    wikiAvatar.getCitationFromID(
        "#cite_note-:0-30", 30, 2, 0.2);
  }
}
