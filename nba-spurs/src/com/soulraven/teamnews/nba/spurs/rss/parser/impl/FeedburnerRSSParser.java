package com.soulraven.teamnews.nba.spurs.rss.parser.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.rss.parser.RSSParser;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;
import com.soulraven.teamnews.rss.parser.postprocess.impl.DescriptionRequiredRSSFilter;

public class FeedburnerRSSParser extends RSSParser {

    private static final DateFormat FB_RSS_FORMAT = new SimpleDateFormat("MMM dd, yyyy:", Locale.ENGLISH);
    private static final String TAG = FeedburnerRSSParser.class.getSimpleName();

    @Override
    public void parseItem(final XmlPullParser parser, final RSSEntry entry, final String tagName)
            throws XmlPullParserException, IOException {
        if (RSSEntry.TITLE_TAG.equalsIgnoreCase(tagName)) {
            String text = parser.nextText();
            entry.setTitle(stripHtml(text).toString());
            entry.setOriginalTitle(text);
        } else if (RSSEntry.LINK_TAG.equalsIgnoreCase(tagName)) {
            entry.setLink(parser.nextText());
        } else if (RSSEntry.DESCRIPTION_TAG.equalsIgnoreCase(tagName)) {
            String description = parser.nextText();
            description = description.substring(description.indexOf("http://feeds.feedburner.com"));
            entry.setImageLink(description.substring(0, description.indexOf("\"")));
            entry.setOriginalDescription(entry.getImageLink());
        } else if (RSSEntry.PUBDATE_TAG.equalsIgnoreCase(tagName)) {
            try {
                String text = parser.nextText();
                entry.setPubDate(FB_RSS_FORMAT.parse(text));
            } catch (ParseException e) {
                Log.e(TAG, "Cannot parse date", e);
            }
        }
    }

    @Override
    protected RSSFilter getFilter() {
        return DescriptionRequiredRSSFilter.getInstance();
    }
}
