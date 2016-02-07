package com.soulraven.teamnews.rss.parser.postprocess;

import com.soulraven.teamnews.model.RSSEntry;

public interface RSSFilter {

    boolean filter(final RSSEntry entry);
}
