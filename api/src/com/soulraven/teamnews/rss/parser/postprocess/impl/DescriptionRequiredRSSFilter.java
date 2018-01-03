package com.soulraven.teamnews.rss.parser.postprocess.impl;

import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;
import org.json.JSONException;
import org.json.JSONObject;

public class DescriptionRequiredRSSFilter implements RSSFilter {

    @Override
    public boolean filter(final JSONObject entry) throws JSONException {
        String description = entry.getString("description");
        return description != null && !description.trim().isEmpty();
    }
}
