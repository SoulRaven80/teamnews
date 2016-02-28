package com.soulraven.teamnews.rss.parser.postprocess.impl;

import com.soulraven.teamnews.properties.PropertiesLoader;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;
import org.json.JSONException;
import org.json.JSONObject;

public final class KeywordsRSSFilter implements RSSFilter {

    private String[] keywords = null;

    public KeywordsRSSFilter() {
        String keywordList = PropertiesLoader.getProperty(PropertiesLoader.KEYWORDS);
        keywords = keywordList.split(PropertiesLoader.getProperty(PropertiesLoader.KEYWORDS_SEPARATOR));
    }

    @Override
    public boolean filter(final JSONObject entry) throws JSONException {
        String title = entry.getString("title");
        String description = entry.getString("content");
        for (String key : keywords) {
            String lowerCaseKey = key.toLowerCase();
            if (title.toLowerCase().contains(lowerCaseKey)) {
                return true;
            }
            if (description.toLowerCase().contains(lowerCaseKey)) {
                return true;
            }
        }
        return false;
    }
}
