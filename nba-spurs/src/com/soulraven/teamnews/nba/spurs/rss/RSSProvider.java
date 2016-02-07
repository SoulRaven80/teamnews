package com.soulraven.teamnews.nba.spurs.rss;

import java.util.*;

import com.soulraven.teamnews.nba.spurs.properties.PropertyKeys;
import com.soulraven.teamnews.nba.spurs.rss.parser.impl.EspnRSSParser;
import com.soulraven.teamnews.nba.spurs.rss.parser.impl.FeedburnerRSSParser;
import com.soulraven.teamnews.nba.spurs.rss.parser.impl.NbaRSSParser;
import com.soulraven.teamnews.nba.spurs.rss.parser.impl.ProbasketballtalkRSSParser;
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
        parserMap.put(PropertyKeys.RSS_URL_ESPN, new EspnRSSParser());
//        parserMap.put(PropertyKeys.RSS_URL_FEEDBURNER, new FeedburnerRSSParser());
        parserMap.put(PropertyKeys.RSS_URL_NBA, new NbaRSSParser());
        parserMap.put(PropertyKeys.RSS_URL_PROBASKETBALLTALK, new ProbasketballtalkRSSParser());
        return parserMap;
    }
}
