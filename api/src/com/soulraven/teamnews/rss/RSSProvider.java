package com.soulraven.teamnews.rss;

import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.properties.PropertiesLoader;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;
import com.soulraven.teamnews.rss.parser.postprocess.impl.KeywordsRSSFilter;
import com.soulraven.teamnews.rss.parser.postprocess.impl.TrueRSSFilter;

import java.util.*;

public final class RSSProvider {

    private static RSSProvider instance = null;
    protected List<RSSEntry> data = null;
    private static final TrueRSSFilter TRUE_RSS_FILTER = new TrueRSSFilter();
    private static final KeywordsRSSFilter KEYWORDS_RSS_FILTER = new KeywordsRSSFilter();

    private RSSProvider() { }

    public synchronized static RSSProvider getInstance() {
        if (instance == null) {
            instance = new RSSProvider();
        }
        return instance;
    }

    public boolean loadData() {
        data = new ArrayList<RSSEntry>();

        Enumeration<Object> keys = PropertiesLoader.getKeys();
        while (keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            if (key.startsWith(PropertiesLoader.RSS_URL_NOFILTER)) {
                addEntries(key, TRUE_RSS_FILTER);
            }
            else if (key.startsWith(PropertiesLoader.RSS_URL_KEYWORDS)) {
                addEntries(key, KEYWORDS_RSS_FILTER);
            }
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

    private void addEntries(final String key, final RSSFilter filter) {
        String url = PropertiesLoader.getProperty(key);
        addEntries(url, data, filter);
    }

    private void addEntries(final String page, final List<RSSEntry> entries, final RSSFilter filter) {
        RSSConsumer handler = new RSSConsumer(page);
        handler.fetchJSON(filter);
        while (!handler.isParsingComplete());
        if (handler.hasEntries()) {
            entries.addAll(handler.getEntries());
        }
    }
    public List<RSSEntry> getData() {
        return data;
    }
}
