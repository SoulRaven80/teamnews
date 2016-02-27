package com.soulraven.teamnews.rss.parser.postprocess;

import org.json.JSONException;
import org.json.JSONObject;

public interface RSSFilter {

    boolean filter(final JSONObject entry) throws JSONException;
}
