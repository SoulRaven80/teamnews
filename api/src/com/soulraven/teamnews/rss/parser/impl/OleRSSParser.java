package com.soulraven.teamnews.rss.parser.impl;

import android.util.Log;
import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.rss.parser.RSSParser;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;
import com.soulraven.teamnews.rss.parser.postprocess.impl.TrueRSSFilter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;

public class OleRSSParser extends RSSParser {

    private static final String TAG = OleRSSParser.class.getSimpleName();

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
        } else if (RSSEntry.GUID_TAG.equalsIgnoreCase(tagName)) {
            entry.setGuid(parser.nextText());
        } else if (RSSEntry.PUBDATE_TAG.equalsIgnoreCase(tagName)) {
            try {
                String text = parser.nextText();
                entry.setPubDate(RSS_FORMAT.parse(text));
            } catch (ParseException e) {
                Log.e(TAG, "Cannot parse date", e);
            }
        } else if (RSSEntry.ENCLOSURE_TAG.equalsIgnoreCase(tagName)) {
            if (!parser.isEmptyElementTag()) {
                entry.setImageLink(parser.nextText());
            }
            else {
                entry.setImageLink(parser.getAttributeValue(null, "url"));
            }
        }
    }

    @Override
    protected RSSFilter getFilter() {
        return TrueRSSFilter.getInstance();
    }
}
