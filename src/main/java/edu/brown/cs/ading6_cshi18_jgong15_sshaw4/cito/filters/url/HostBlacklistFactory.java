package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.filters.url;

import java.util.Set;

public final class HostBlacklistFactory {

  private HostBlacklistFactory() { }

  public static Set<URLRule> getDefault() {
    return Set.of(new HostBlocker("google"),
        new HostBlocker("bing"),
        new HostBlocker("ask"),
        new HostBlocker("amazon"),
        new HostBlocker("baidu"),
        new HostBlocker("slack"),
        new HostBlocker("yandex"),
        new HostBlocker("twitter"),
        new HostBlocker("facebook"));
  }
}
