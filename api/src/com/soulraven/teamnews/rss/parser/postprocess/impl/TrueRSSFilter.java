package com.soulraven.teamnews.rss.parser.postprocess.impl;

import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;
import org.json.JSONObject;

public class TrueRSSFilter implements RSSFilter {

    @Override
    public boolean filter(final JSONObject entry) {
        return true;
    }
}
