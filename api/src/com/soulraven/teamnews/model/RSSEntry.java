package com.soulraven.teamnews.model;

import java.io.Serializable;
import java.util.Date;

public class RSSEntry implements Serializable {

	private static final long serialVersionUID = -7483985354574135318L;

	public static final String EXTRA_KEY = "com.soulraven.teamnews.extra.entry";

    public static final String ITEM_TAG = "ITEM";
    public static final String TITLE_TAG = "TITLE";
    public static final String LINK_TAG = "LINK";
    public static final String DESCRIPTION_TAG = "DESCRIPTION";
    public static final String GUID_TAG = "GUID";
    public static final String MEDIA_CONTENT_TAG = "MEDIA:CONTENT";
    public static final String PUBDATE_TAG = "PUBDATE";
    public static final String ENCLOSURE_TAG = "ENCLOSURE";
    public static final String CONTENT_ENCODED_TAG = "CONTENT:ENCODED";

    private String title = "";
    private String originalTitle = "";
    private String link = "";
    private String description = "";
    private String originalDescription = "";
    private String guid = "";
    private Date pubDate = null;
    private String imageLink = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(final String guid) {
        this.guid = guid;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(final Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(final String imageLink) {
        this.imageLink = imageLink;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(final String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalDescription() {
        return originalDescription;
    }

    public void setOriginalDescription(final String originalDescription) {
        this.originalDescription = originalDescription;
    }
}
