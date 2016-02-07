package com.soulraven.teamnews.rss.parser.postprocess.impl;

import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;

public class TrueRSSFilter implements RSSFilter {

    private static RSSFilter instance;

    public static RSSFilter getInstance() {
        if (instance == null) {
            instance = new TrueRSSFilter();
        }
        return instance;
    }

    private TrueRSSFilter() {}

    @Override
    public boolean filter(final RSSEntry entry) {
        return true;
    }
}
