package com.soulraven.teamnews.futbol.riverplate.rss;

import com.soulraven.teamnews.futbol.riverplate.properties.PropertyKeys;
import com.soulraven.teamnews.futbol.riverplate.rss.parser.impl.*;
import com.soulraven.teamnews.rss.parser.RSSParser;

import java.util.*;

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
        parserMap.put(PropertyKeys.RSS_URL_OLE, new OleRSSParser());
        parserMap.put(PropertyKeys.RSS_URL_YAHOO, new YahooRSSParser());
        parserMap.put(PropertyKeys.RSS_URL_LPM, new LpmRSSParser());
        parserMap.put(PropertyKeys.RSS_URL_FUTARG, new FutbolArgentinoRSSParser());
        parserMap.put(PropertyKeys.RSS_URL_ESPN, new EspnRSSParser());
        return parserMap;
    }
}
