package com.soulraven.teamnews.rss.parser;

import android.text.Html;
import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;
import com.soulraven.teamnews.util.presentation.RSSRenderer;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RSSParser {

    public static final DateFormat RSS_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);

    public abstract void parseItem(final XmlPullParser xml, final RSSEntry entry, final String tagName)
            throws XmlPullParserException, IOException;

    protected abstract RSSFilter getFilter();


}
