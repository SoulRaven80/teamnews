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
import com.soulraven.teamnews.rss.parser.postprocess.impl.CompositeRSSFilter;
import com.soulraven.teamnews.rss.parser.postprocess.impl.DescriptionRequiredRSSFilter;
import com.soulraven.teamnews.rss.parser.postprocess.impl.KeywordsRSSFilter;

public class ProbasketballtalkRSSParser extends RSSParser {

    protected static final DateFormat PBT_RSS_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss +0000", Locale.ENGLISH);
    private static final String TAG = ProbasketballtalkRSSParser.class.getSimpleName();

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
            String text = parser.nextText();
            entry.setDescription(stripHtml(text).toString());
            entry.setOriginalDescription(text);
        } else if (RSSEntry.MEDIA_CONTENT_TAG.equalsIgnoreCase(tagName)) {
            try {
                String url = parser.getAttributeValue(null, "url");
                entry.setImageLink(url);
            }
            catch (Exception e) {
                Log.e(TAG, "Cannot obtain image link", e);
            }
        } else if (RSSEntry.PUBDATE_TAG.equalsIgnoreCase(tagName)) {
            try {
                String text = parser.nextText();
                entry.setPubDate(PBT_RSS_FORMAT.parse(text));
            } catch (ParseException e) {
                Log.e(TAG, "Cannot parse date", e);
            }
        }
    }

    @Override
    protected RSSFilter getFilter() {
        return CompositeRSSFilter.getInstance(DescriptionRequiredRSSFilter.getInstance(),
                KeywordsRSSFilter.getInstance());
    }
}
