package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.ops;


import edu.stanford.nlp.simple.Document;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Lemmatize implements Function<String, List<String>> {

  @Override
  public List<String> apply(String doc) {
    Document rootDoc = new Document(doc.toLowerCase());
    return rootDoc.sentences().stream()
        .flatMap(s -> s.lemmas().stream())
        .collect(Collectors.toList());  }
}
