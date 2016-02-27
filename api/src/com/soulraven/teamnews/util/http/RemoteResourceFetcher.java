package com.soulraven.teamnews.util.http;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;

public abstract class RemoteResourceFetcher<T> {

    private static final String TAG = RemoteResourceFetcher.class.getSimpleName();

    private T result = null;
    private boolean processFinished = false;

    public void fetchResource(final String urlString) throws UnknownHostException {
        InputStream stream = null;
        long startTime = System.currentTimeMillis();

        try {
            URL url = new URL(urlString);
            long openConnectionStartTime = System.currentTimeMillis();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            long openConnectionEndTime = System.currentTimeMillis();
            Log.v(TAG, "Open connection to URL time: " + (openConnectionEndTime - openConnectionStartTime));
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            long connectionStartTime = System.currentTimeMillis();
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                long connectionEndTime = System.currentTimeMillis();
                Log.v(TAG, "Connection to remote file time: " + (connectionEndTime - connectionStartTime));
                long streamStartTime = System.currentTimeMillis();
                stream = conn.getInputStream();
                long streamEndTime = System.currentTimeMillis();
                Log.v(TAG, "Fetching remote stream time: " + (streamEndTime - streamStartTime));

                result = processStream(stream);
            }
        } catch (UnknownHostException e) {
            Log.e(TAG, "Unknown host while getting RSS remote file", e);
            throw e;
        } catch (Exception e) {
            Log.e(TAG, "Error while getting RSS remote file", e);
        }
        finally {
            long closeStreamStartTime = System.currentTimeMillis();
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    Log.v(TAG, "Error closing the stream", e);
                }
            }
            long closeStreamEndTime = System.currentTimeMillis();
            Log.v(TAG, "Close stream time: " + (closeStreamEndTime - closeStreamStartTime));
            setProcessFinished(true);
        }
        Log.v(TAG, "Overall process time: " + (System.currentTimeMillis() - startTime));
    }

    protected abstract T processStream(final InputStream stream);

    private synchronized boolean isProcessFinished() {
        return processFinished;
    }

    private synchronized void setProcessFinished(final boolean processFinished) {
        this.processFinished = processFinished;
    }

    public T get() {
        while (!isProcessFinished());
        return result;
    }
}
