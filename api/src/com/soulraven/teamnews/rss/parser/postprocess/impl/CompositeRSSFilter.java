package com.soulraven.teamnews.rss.parser.postprocess.impl;

import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;
import org.json.JSONException;
import org.json.JSONObject;

public class CompositeRSSFilter implements RSSFilter {

    private static RSSFilter instance;
    private RSSFilter[] filters;

    public static RSSFilter getInstance(final RSSFilter... filters) {
        instance = new CompositeRSSFilter(filters);
        return instance;
    }

    private CompositeRSSFilter(final RSSFilter... filters) {
        this.filters = filters;
    }

    @Override
    public boolean filter(final JSONObject entry) throws JSONException {
        boolean ret = true;
        for (RSSFilter filter : filters) {
            ret = (ret && filter.filter(entry));
        }
        return ret;
    }
}
