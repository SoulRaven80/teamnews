package com.soulraven.teamnews.rss;

import android.util.Log;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;
import com.soulraven.teamnews.util.http.RemoteResourceFetcher;
import com.soulraven.teamnews.model.RSSEntry;

import java.io.*;
import java.net.UnknownHostException;
import java.util.List;

public class RSSConsumer {

    private static final String TAG = RSSConsumer.class.getSimpleName();

    private volatile ParsingStatus parsingStatus;
    private String urlString = null;
    private List<RSSEntry> entries;
    private JSONReader jsonReader = null;

    public RSSConsumer(final String url) {
        this.urlString = url;
        this.parsingStatus = new ParsingStatus();
        this.jsonReader = new JSONReader();
    }

    public void storeEntries(List<RSSEntry> entries) {
        this.entries = entries;
        setParsingComplete(true);
    }

    public void fetchJSON(final RSSFilter filter) {
        final ParsingStatus status = parsingStatus;
        try {
            new RemoteResourceFetcher<Void>() {
                @Override
                protected Void processStream(final InputStream stream) {
                    parseStream(stream, filter, status);
                    return null;
                }
            }.fetchResource(urlString);
        } catch (UnknownHostException e) {
            Log.e(TAG, "Error connecting to the RSS host ", e);
        }
        setParsingComplete(true);
    }

    private void parseStream(final InputStream stream, final RSSFilter filter, ParsingStatus status) {
        try {
            long parseStartTime = System.currentTimeMillis();
            List<RSSEntry> rssEntries = jsonReader.readJsonStream(stream, filter);
            storeEntries(rssEntries);
            long parseEndTime = System.currentTimeMillis();
            Log.d(TAG, "Parsing time: " + (parseEndTime - parseStartTime) + " ms.");
        } catch (Exception e) {
            Log.e(TAG, "Error while parsing ", e);
            status.setParsingComplete(true);
        }
    }

    public synchronized boolean isParsingComplete() {
        return parsingStatus.isParsingComplete();
    }

    public synchronized void setParsingComplete(final boolean parsingComplete) {
        parsingStatus.setParsingComplete(parsingComplete);
    }

    public boolean hasEntries() {
        return entries != null && !entries.isEmpty();
    }

    public List<RSSEntry> getEntries() {
        return entries;
    }

    public static class ParsingStatus {
        private volatile boolean parsingComplete = false;
        public synchronized boolean isParsingComplete() {
            return parsingComplete;
        }

        public synchronized void setParsingComplete(final boolean parsingComplete) {
            this.parsingComplete = parsingComplete;
        }

    }
}