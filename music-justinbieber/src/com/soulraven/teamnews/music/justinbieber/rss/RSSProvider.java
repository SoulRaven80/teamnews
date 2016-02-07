package com.soulraven.teamnews.music.justinbieber.rss;

import java.util.*;

import com.soulraven.teamnews.music.justinbieber.properties.PropertyKeys;
import com.soulraven.teamnews.music.justinbieber.rss.parser.impl.*;
import com.soulraven.teamnews.rss.parser.RSSParser;

public final class RSSProvider extends com.soulraven.teamnews.rss.RSSProvider {

    private static RSSProvider instance = null;

    private RSSProvider() {}

    public static synchronized RSSProvider getInstance() {
        if (instance == null) {
            instance = new RSSProvider();
        }
        return instance;
    }

    @Override
    protected Map<String, RSSParser> getParsers() {
        Map<String, RSSParser> parserMap = new HashMap<String, RSSParser>();
        parserMap.put(PropertyKeys.RSS_URL_MUSICFEEDS, new MusicfeedsRSSParser());
        parserMap.put(PropertyKeys.RSS_URL_NYTIMES, new NYTimesRSSParser());
//        parserMap.put(PropertyKeys.RSS_URL_POSH24, new Posh24RSSParser());
        parserMap.put(PropertyKeys.RSS_URL_STARPULSE, new StarpulseRSSParser());
        parserMap.put(PropertyKeys.RSS_URL_TMZ, new TmzRSSParser());
        return parserMap;
    }
}
