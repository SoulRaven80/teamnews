package com.soulraven.teamnews.rss;

import android.content.Context;
import android.util.Log;
import com.soulraven.teamnews.properties.PropertiesLoader;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;
import com.soulraven.teamnews.util.http.RemoteResourceFetcher;
import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.rss.parser.RSSParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.*;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class RSSConsumer {

    private static final String TAG = RSSConsumer.class.getSimpleName();

    private volatile boolean parsingComplete = false;
    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    private List<RSSEntry> entries;

    public RSSConsumer(final String url) {
        this.urlString = url;
    }

    public void storeEntries(List<RSSEntry> entries) {
        this.entries = entries;
        setParsingComplete(true);
    }

    public void fetchJSON(final RSSFilter filter) {
        try {
            new RemoteResourceFetcher<Void>() {
                @Override
                protected Void processStream(final InputStream stream) {
                    parseStream(stream, filter);
                    return null;
                }
            }.fetchResource("http://ajax.googleapis.com/ajax/services/feed/load?v=1.0&num=30&q="+ urlString);
        } catch (UnknownHostException e) {
            Log.e(TAG, "Error connecting to the RSS host ", e);
            setParsingComplete(true);
        }
    }

    private void parseStream(final InputStream stream, final RSSFilter filter) {
        try {
            long parseStartTime = System.currentTimeMillis();
            List<RSSEntry> rssEntries = new JSONReader().readJsonStream(stream, filter);
            storeEntries(rssEntries);
            long parseEndTime = System.currentTimeMillis();
            Log.d(TAG, "Parsing time: " + (parseEndTime - parseStartTime));
        } catch (Exception e) {
            Log.e(TAG, "Error while parsing ", e);
        }
    }

    public synchronized boolean isParsingComplete() {
        return parsingComplete;
    }

    public synchronized void setParsingComplete(final boolean parsingComplete) {
        this.parsingComplete = parsingComplete;
    }

    public boolean hasEntries() {
        return entries != null && !entries.isEmpty();
    }

    public List<RSSEntry> getEntries() {
        return entries;
    }
}