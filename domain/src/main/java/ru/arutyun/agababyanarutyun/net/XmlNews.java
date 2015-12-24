package ru.arutyun.agababyanarutyun.net;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(name = "item", strict = false)
public class XmlNews {

    @Element(name = "title",       required = true)  String mTitle;
    @Element(name = "link",        required = true)  String mLink;
    @Element(name = "description", required = true)  String mDescription;
    @Element(name = "author",      required = false) String mAuthor;
    @Element(name = "category",    required = false) String mCategory;
    @Element(name = "enclosure",   required = false) XmlEnclosure mEnclosure;
    @Element(name = "guid",        required = false) String mGuid;
    @Element(name = "pubDate",     required = false) String mPubDate;

    public String getTitle() {
        return mTitle;
    }

    public String getLink() {
        return mLink;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getCategory() {
        return mCategory;
    }

    public XmlEnclosure getEnclosure() {
        return mEnclosure;
    }

    public String getGuid() {
        return mGuid;
    }

    public String getPubDate() {
        return mPubDate;
    }

    @Override
    public String toString() {
        return "Item{" +
                "title='" + mTitle + '\'' +
                ", link='" + mLink + '\'' +
                ", description='" + mDescription + '\'' +
                ", author='" + mAuthor + '\'' +
                ", category='" + mCategory + '\'' +
                ", enclosure='" + mEnclosure + '\'' +
                ", guid='" + mGuid + '\'' +
                ", pubDate='" + mPubDate + '\'' +
                '}';
    }
}
