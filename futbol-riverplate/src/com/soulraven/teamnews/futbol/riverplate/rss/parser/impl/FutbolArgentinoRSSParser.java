package com.soulraven.teamnews.futbol.riverplate.rss.parser.impl;

import android.util.Log;
import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.rss.parser.RSSParser;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;
import com.soulraven.teamnews.rss.parser.postprocess.impl.CompositeRSSFilter;
import com.soulraven.teamnews.rss.parser.postprocess.impl.DescriptionRequiredRSSFilter;
import com.soulraven.teamnews.rss.parser.postprocess.impl.KeywordsRSSFilter;
import com.soulraven.teamnews.rss.parser.postprocess.impl.TrueRSSFilter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class FutbolArgentinoRSSParser extends RSSParser {

    private static final String TAG = FutbolArgentinoRSSParser.class.getSimpleName();
    protected static final DateFormat FUTAR_RSS_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public void parseItem(final XmlPullParser parser, final RSSEntry entry, final String tagName)
            throws XmlPullParserException, IOException {
        if (RSSEntry.TITLE_TAG.equalsIgnoreCase(tagName)) {
            String text = stripHtml(parser.nextText()).toString();
            entry.setTitle(text);
            entry.setOriginalTitle(text);
        } else if (RSSEntry.LINK_TAG.equalsIgnoreCase(tagName)) {
            entry.setLink(parser.nextText());
        } else if (RSSEntry.DESCRIPTION_TAG.equalsIgnoreCase(tagName)) {
            String text = stripHtml(parser.nextText()).toString();
            entry.setDescription(text);
            entry.setOriginalDescription(text);
        } else if (RSSEntry.GUID_TAG.equalsIgnoreCase(tagName)) {
            entry.setGuid(parser.nextText());
        } else if (RSSEntry.PUBDATE_TAG.equalsIgnoreCase(tagName)) {
            try {
                String text = stripHtml(parser.nextText());
                entry.setPubDate(FUTAR_RSS_FORMAT.parse(text));
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
        return CompositeRSSFilter.getInstance(KeywordsRSSFilter.getInstance(),
                DescriptionRequiredRSSFilter.getInstance());
    }
}
