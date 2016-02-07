package com.soulraven.teamnews.rss.parser.postprocess.impl;

import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;

public class DescriptionRequiredRSSFilter implements RSSFilter {

    private static RSSFilter instance;

    public static RSSFilter getInstance() {
        if (instance == null) {
            instance = new DescriptionRequiredRSSFilter();
        }
        return instance;
    }

    private DescriptionRequiredRSSFilter() {}

    @Override
    public boolean filter(final RSSEntry entry) {
        return entry.getDescription() != null && !entry.getDescription().trim().isEmpty();
    }
}
