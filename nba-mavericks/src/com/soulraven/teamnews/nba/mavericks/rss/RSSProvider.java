package com.soulraven.teamnews.nba.mavericks.rss;

import java.util.*;

import com.soulraven.teamnews.nba.mavericks.properties.PropertyKeys;
import com.soulraven.teamnews.nba.mavericks.rss.parser.impl.EspnRSSParser;
import com.soulraven.teamnews.nba.mavericks.rss.parser.impl.MavsRSSParser;
import com.soulraven.teamnews.nba.mavericks.rss.parser.impl.NbaRSSParser;
import com.soulraven.teamnews.nba.mavericks.rss.parser.impl.ProbasketballtalkRSSParser;
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
        parserMap.put(PropertyKeys.RSS_URL_MAVS, new MavsRSSParser());
        parserMap.put(PropertyKeys.RSS_URL_NBA, new NbaRSSParser());
        parserMap.put(PropertyKeys.RSS_URL_PROBASKETBALLTALK, new ProbasketballtalkRSSParser());
        return parserMap;
    }
}
