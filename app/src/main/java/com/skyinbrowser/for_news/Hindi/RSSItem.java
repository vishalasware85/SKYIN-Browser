package com.skyinbrowser.for_news.Hindi;

public class RSSItem {

    public String title;
    public String mainTitle;
    public String description;
    public String contentEncoded;
    public String link;
    public String guid;
    public String pubdate;

    public RSSItem(String title, String description, String contentEncoded,
                   String link, String guid,String pubdate,String mainTitle) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.contentEncoded=contentEncoded;
        this.guid = guid;
        this.pubdate = pubdate;
        this.mainTitle=mainTitle;
    }
}