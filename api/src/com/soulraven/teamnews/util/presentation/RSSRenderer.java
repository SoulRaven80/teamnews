package com.soulraven.teamnews.util.presentation;

public class RSSRenderer {

    public static String stripHTML(final String html) {
        return html.replaceAll("<[^>]*>", "");
    }
}
