package com.soulraven.teamnews.rss.parser.postprocess.impl;

import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.properties.PropertiesLoader;
import com.soulraven.teamnews.properties.PropertyKeys;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;

public final class KeywordsRSSFilter implements RSSFilter {

    private static RSSFilter instance;

    private String[] keywords = null;

    public static RSSFilter getInstance() {
        if (instance == null) {
            instance = new KeywordsRSSFilter();
        }
        return instance;
    }

    private KeywordsRSSFilter() {
        String keywordList = PropertiesLoader.getProperty(PropertyKeys.KEYWORDS);
        keywords = keywordList.split(PropertiesLoader.getProperty(PropertyKeys.KEYWORDS_SEPARATOR));
    }

    @Override
    public boolean filter(final RSSEntry entry) {
        for (String key : keywords) {
            String lowerCaseKey = key.toLowerCase();
            if (entry.getTitle().toLowerCase().contains(lowerCaseKey)) {
                return true;
            }
            if (entry.getDescription().toLowerCase().contains(lowerCaseKey)) {
                return true;
            }
        }
        return false;
    }
}
