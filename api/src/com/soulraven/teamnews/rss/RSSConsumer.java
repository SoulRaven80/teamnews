package com.soulraven.teamnews.rss;

import android.content.Context;
import android.util.Log;
import com.soulraven.teamnews.properties.PropertiesLoader;
import com.soulraven.teamnews.util.http.RemoteResourceFetcher;
import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.rss.parser.RSSParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.*;
import java.net.UnknownHostException;
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

    public void parseXMLAndStoreIt(final XmlPullParser parser, final RSSParser rssParser) {
        this.entries = new ArrayList<RSSEntry>();
        try {
            boolean insideItem = false;
            // Returns the type of current event: START_TAG, END_TAG, etc..
            int eventType = parser.getEventType();
            RSSEntry entry = new RSSEntry();
            while (XmlPullParser.END_DOCUMENT != eventType) {
                String tagName = parser.getName();
                if (XmlPullParser.START_TAG == eventType) {
                    if (RSSEntry.ITEM_TAG.equalsIgnoreCase(tagName)) {
                        insideItem = true;
                    }
                    else if (insideItem) {
                        parseItem(parser, rssParser, entry, tagName);
                    }
                }
                else if (XmlPullParser.END_TAG == eventType
                        && RSSEntry.ITEM_TAG.equalsIgnoreCase(tagName)) {
                    insideItem = false;
                    if (isPertinent(rssParser, entry)) {
                        entries.add(entry);
                    }
                    entry = new RSSEntry();
                }
                eventType = parser.next(); // move to next element
            }
            setParsingComplete(true);
        } catch (Exception e) {
            Log.e(TAG, "Error while parsing the RSS", e);
        }
    }

    private boolean isPertinent(final RSSParser rssParser, final RSSEntry entry) {
        return rssParser.isPertinent(entry);
    }

    private void parseItem(final XmlPullParser parser, final RSSParser rssParser, final RSSEntry entry,
                           final String tagName)
            throws XmlPullParserException, IOException {
        rssParser.parseItem(parser, entry, tagName);
    }

    public void fetchXML(final RSSParser rssParser, final Context context) {
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            final XmlPullParser parser = xmlFactoryObject.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

            try {
                if ("dev".equals(PropertiesLoader.getProperty("env"))) {
                    InputStream stream = context.getAssets().open(rssParser.getClass().getSimpleName() + ".xml");
                    parseStream(stream, parser, rssParser);
                }
                else {
                    new RemoteResourceFetcher<Void>() {
                        @Override
                        protected Void processStream(final InputStream stream) {
                            parseStream(stream, parser, rssParser);
                            return null;
                        }
                    }.fetchResource(urlString);
                }
            } catch (UnknownHostException e) {
                Log.e(TAG, "Error connecting to the RSS host ", e);
                setParsingComplete(true);
            } catch (IOException e) {
                Log.e(TAG, "Error opening dev file streams ", e);
                setParsingComplete(true);
            }
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Error while creating parser ", e);
            setParsingComplete(true);
        }
    }

    private void parseStream(final InputStream stream, final XmlPullParser parser, final RSSParser rssParser) {
        try {
            parser.setInput(stream, null);
            long parseStartTime = System.currentTimeMillis();
            parseXMLAndStoreIt(parser, rssParser);
            long parseEndTime = System.currentTimeMillis();
            Log.d(TAG, "Parsing time: " + (parseEndTime - parseStartTime));
        } catch (XmlPullParserException e) {
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