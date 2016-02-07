package com.soulraven.teamnews.rss.parser;

import android.text.Html;
import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;
import com.soulraven.teamnews.util.presentation.RSSRenderer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RSSParser {

    protected static final DateFormat RSS_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);

    public abstract void parseItem(final XmlPullParser parser, final RSSEntry entry, final String tagName)
            throws XmlPullParserException, IOException;

    protected abstract RSSFilter getFilter();

    public boolean isPertinent(final RSSEntry entry) {
        return getFilter().filter(entry);
    }

    protected String stripHtml(final String text) {
        String html = RSSRenderer.stripHTML(text);
        String spanned = Html.fromHtml(Html.fromHtml(html).toString()).toString();
        return spanned.trim();
    }

    protected String getImg(final String text) {
        String ret = null;
        Pattern pattern = Pattern.compile("<img\\s+src\\s*=\\s*([\"'][^\"']+[\"']+)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            ret = matcher.group(1).replaceAll("'", "");
        }
        return ret;
    }
}
