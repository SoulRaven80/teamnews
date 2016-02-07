package com.soulraven.teamnews.music.justinbieber.rss.parser.impl;

import android.util.Log;
import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.rss.parser.RSSParser;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;
import com.soulraven.teamnews.rss.parser.postprocess.impl.CompositeRSSFilter;
import com.soulraven.teamnews.rss.parser.postprocess.impl.DescriptionRequiredRSSFilter;
import com.soulraven.teamnews.rss.parser.postprocess.impl.KeywordsRSSFilter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TmzRSSParser extends RSSParser {

    protected static final DateFormat TMZ_RSS_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
    private static final String TAG = TmzRSSParser.class.getSimpleName();
    private static final String DC_DATE_TAG = "DC:DATE";

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
        } else {
            if (DC_DATE_TAG.equalsIgnoreCase(tagName)) {
                try {
                    String text = parser.nextText();
                    entry.setPubDate(TMZ_RSS_FORMAT.parse(text));
                } catch (ParseException e) {
                    Log.e(TAG, "Cannot parse date", e);
                }
            }
        }
    }

    @Override
    protected RSSFilter getFilter() {
        return CompositeRSSFilter.getInstance(DescriptionRequiredRSSFilter.getInstance(),
                KeywordsRSSFilter.getInstance());
    }
}
