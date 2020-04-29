package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.ops;

import com.google.common.collect.Streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CosSim implements BiFunction<String, String, Double> {
  private Function<String, List<String>> lemmatizer;
  private static final int CACHE_SIZE = 100;
  public CosSim() {
    lemmatizer = new FunctionCacher<>(new Lemmatize(), CACHE_SIZE);
  }

  @Override
  public Double apply(String s1, String s2) {
    List<String> rootLemmas = lemmatizer.apply(s1);
    List<String> leafLemmas = lemmatizer.apply(s2);

    // remove common words and punctuation that overwhelm score
    rootLemmas.removeAll(STOP_WORDS);
    leafLemmas.removeAll(STOP_WORDS);

    // make vector index list
    List<String> ref = Streams.concat(rootLemmas.stream(), leafLemmas.stream())
        .distinct()
        .collect(Collectors.toList());
    Map<String, Integer> indexMap = new HashMap<>(ref.size());
    Streams.forEachPair(ref.stream(), IntStream.range(0, ref.size()).boxed(), indexMap::put);

    int[] rootVect = new int[ref.size()];
    rootLemmas.stream().forEach(str -> {
      rootVect[indexMap.get(str)]++;
    });

    int[] leafVect = new int[ref.size()];
    leafLemmas.stream().forEach(str -> {
      leafVect[indexMap.get(str)]++;
    });

    // compute dot product and normalize
    double dotProd = Streams.zip(Arrays.stream(rootVect).boxed(),
        Arrays.stream(leafVect).boxed(), (r, l) -> r * l)
        .reduce(0, Integer::sum);

    double rootNorm = Math.sqrt((double) Arrays.stream(rootVect).boxed()
        .map(n -> n * n)
        .reduce(0, Integer::sum));

    double leafNorm = Math.sqrt((double) Arrays.stream(leafVect).boxed()
        .map(n -> n * n)
        .reduce(0, Integer::sum));

    return dotProd / (rootNorm * leafNorm);
  }

  private static final Set<String> STOP_WORDS;
  static {
    List<String> words;
    try {
      words = Files.readAllLines(Paths.get("data/cito/english_nltk.txt"));
    } catch (IOException e) {
      System.out.println("Cannot find english_nltk.txt");
      words = Collections.emptyList();
    }
    List<String> puncs = List.of("!", "@", "#", "$", "%", "^", "&", "*", "(", ")", ":",
        "'", "\"", ";", ":", "?", ".", ",", "/", "{", "}", "\\", "[", "]", "|");
    STOP_WORDS = new HashSet<>();
    STOP_WORDS.addAll(words);
    STOP_WORDS.addAll(puncs);
  }
}
