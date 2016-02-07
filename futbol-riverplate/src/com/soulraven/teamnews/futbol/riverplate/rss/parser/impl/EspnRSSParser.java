package com.soulraven.teamnews.futbol.riverplate.rss.parser.impl;

import android.util.Log;
import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.rss.parser.RSSParser;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;
import com.soulraven.teamnews.rss.parser.postprocess.impl.KeywordsRSSFilter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class EspnRSSParser extends RSSParser {

    private static final String TAG = EspnRSSParser.class.getSimpleName();
    protected static final DateFormat ESPN_RSS_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz",
            new Locale("es", "AR"));

    @Override
    public void parseItem(final XmlPullParser parser, final RSSEntry entry, final String tagName)
            throws XmlPullParserException, IOException {
        if (RSSEntry.TITLE_TAG.equalsIgnoreCase(tagName)) {
            String text = stripHtml(parser.nextText());
            entry.setTitle(text);
            entry.setOriginalTitle(text);
        } else if (RSSEntry.LINK_TAG.equalsIgnoreCase(tagName)) {
            entry.setLink(parser.nextText());
        } else if (RSSEntry.DESCRIPTION_TAG.equalsIgnoreCase(tagName)) {
            String text = stripHtml(parser.nextText());
            entry.setDescription(text);
            entry.setOriginalDescription(text);
        } else if (RSSEntry.MEDIA_CONTENT_TAG.equalsIgnoreCase(tagName)) {
            try {
                entry.setImageLink(parser.getAttributeValue(null, "url"));
            }
            catch (Exception e) {
                Log.e(EspnRSSParser.class.getSimpleName(), "Cannot obtain image link");
            }
        } else if (RSSEntry.PUBDATE_TAG.equalsIgnoreCase(tagName)) {
            try {
                String text = parser.nextText();
                entry.setPubDate(ESPN_RSS_FORMAT.parse(text));
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
