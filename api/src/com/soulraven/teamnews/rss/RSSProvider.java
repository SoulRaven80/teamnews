package com.soulraven.teamnews.rss;

import android.content.Context;
import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.properties.PropertiesLoader;
import com.soulraven.teamnews.rss.parser.RSSParser;

import java.util.*;

public abstract class RSSProvider {

    protected List<RSSEntry> data = null;

    protected RSSProvider() { }

    public boolean loadData(final Context context) {
        data = new ArrayList<RSSEntry>();

        Map<String, RSSParser> parsers = getParsers();
        Set<String> keySet = parsers.keySet();
        for (String key : keySet) {
            addEntries(PropertiesLoader.getProperty(key), data, parsers.get(key), context);
        }

        Collections.sort(data, new Comparator<RSSEntry>() {
            @Override
            public int compare(RSSEntry entry, RSSEntry entry2) {
                if (entry.getPubDate() != null && entry2.getPubDate() != null) {
                    return entry2.getPubDate().compareTo(entry.getPubDate());
                }
                return 0;
            }
        });
        return !data.isEmpty();
    }

    protected void addEntries(final String page, final List<RSSEntry> entries, final RSSParser rssParser,
                                   final Context context) {
        RSSConsumer handler = new RSSConsumer(page);
        handler.fetchXML(rssParser, context);
        while (!handler.isParsingComplete());
        if (handler.hasEntries()) {
            entries.addAll(handler.getEntries());
        }
    }

    public List<RSSEntry> getData() {
        return data;
    }

    protected abstract Map<String, RSSParser> getParsers();
}
