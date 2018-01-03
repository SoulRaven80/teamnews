package com.soulraven.teamnews.rss.parser.postprocess.impl;

import com.soulraven.teamnews.properties.PropertiesLoader;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;

import org.json.JSONArray;
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
        String description = entry.getString("description");
        for (String key : keywords) {
            String lowerCaseKey = key.toLowerCase();
            if (title.toLowerCase().contains(lowerCaseKey) || description.toLowerCase().contains(lowerCaseKey)) {
                return true;
            }
        }
        if (entry.has("category")) {
            JSONArray categories = entry.getJSONArray("category");
            for (String key : keywords) {
                String lowerCaseKey = key.toLowerCase();
                for (int i = 0; i < categories.length(); i++) {
                    if (categories.getString(i).toLowerCase().contains(lowerCaseKey)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
