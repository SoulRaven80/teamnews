package com.soulraven.teamnews.rss.parser.impl;

import android.util.Log;
import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.rss.parser.RSSParser;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;
import com.soulraven.teamnews.rss.parser.postprocess.impl.KeywordsRSSFilter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;

public class YahooRSSParser extends RSSParser {

    private static final String TAG = YahooRSSParser.class.getSimpleName();

    @Override
    public void parseItem(final XmlPullParser parser, final RSSEntry entry, final String tagName)
            throws XmlPullParserException, IOException {
        if (RSSEntry.TITLE_TAG.equalsIgnoreCase(tagName)) {
            // TODO move display logic away from the parser
            entry.setTitle(stripHtml(parser.nextText()).toString());
        } else if (RSSEntry.LINK_TAG.equalsIgnoreCase(tagName)) {
            entry.setLink(parser.nextText());
        } else if (RSSEntry.DESCRIPTION_TAG.equalsIgnoreCase(tagName)) {
            // TODO move display logic away from the parser
            entry.setDescription(stripHtml(parser.nextText()).toString());
        } else if (RSSEntry.MEDIA_CONTENT_TAG.equalsIgnoreCase(tagName)) {
            try {
                entry.setImageLink(parser.getAttributeValue(null, "url"));
            }
            catch (Exception e) {
                Log.e(YahooRSSParser.class.getSimpleName(), "Cannot obtain image link");
            }
        } else if (RSSEntry.PUBDATE_TAG.equalsIgnoreCase(tagName)) {
            try {
                String text = parser.nextText();
                entry.setPubDate(RSS_FORMAT.parse(text));
            } catch (ParseException e) {
                Log.e(TAG, "Cannot parse date", e);
            }
        }
    }

    @Override
    protected RSSFilter getFilter() {
        return KeywordsRSSFilter.getInstance();
    }
}
