package com.soulraven.teamnews.music.justinbieber.rss.parser.impl;

import java.io.IOException;
import java.text.ParseException;

import com.soulraven.teamnews.rss.parser.postprocess.impl.CompositeRSSFilter;
import com.soulraven.teamnews.rss.parser.postprocess.impl.KeywordsRSSFilter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.rss.parser.RSSParser;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;
import com.soulraven.teamnews.rss.parser.postprocess.impl.DescriptionRequiredRSSFilter;

public class MusicfeedsRSSParser extends RSSParser {

    private static final String TAG = MusicfeedsRSSParser.class.getSimpleName();

    @Override
    public void parseItem(final XmlPullParser parser, final RSSEntry entry, final String tagName)
            throws XmlPullParserException, IOException {
        if (RSSEntry.TITLE_TAG.equalsIgnoreCase(tagName)) {
            String text = stripHtml(parser.nextText()).toString();
            entry.setTitle(text);
            entry.setOriginalTitle(text);
        } else if (RSSEntry.LINK_TAG.equalsIgnoreCase(tagName)) {
            entry.setLink(parser.nextText());
//        } else if (RSSEntry.DESCRIPTION_TAG.equalsIgnoreCase(tagName)) {
        } else if (RSSEntry.CONTENT_ENCODED_TAG.equalsIgnoreCase(tagName)) {
            String text = parser.nextText();
            entry.setDescription(stripHtml(text).toString());
            entry.setOriginalDescription(text);
        } else if (RSSEntry.GUID_TAG.equalsIgnoreCase(tagName)) {
            entry.setGuid(parser.nextText());
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
        return CompositeRSSFilter.getInstance(KeywordsRSSFilter.getInstance(),
                DescriptionRequiredRSSFilter.getInstance()
        );
    }
}
