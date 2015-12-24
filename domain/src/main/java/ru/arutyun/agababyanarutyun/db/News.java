package ru.arutyun.agababyanarutyun.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDateTime;

import java.io.Serializable;

import ru.arutyun.agababyanarutyun.RssSource;
import ru.arutyun.agababyanarutyun.db.persister.LocalDateTimePersister;
import ru.arutyun.agababyanarutyun.net.XmlEnclosure;
import ru.arutyun.agababyanarutyun.net.XmlNews;
import ru.arutyun.agababyanarutyun.util.DateUtils;

/**
 * Rss news db model
 */
@DatabaseTable(tableName = "news")
public class News implements Serializable {

    public static final String FIELD_DATE = "date";
    public static final String FIELD_READ = "read";
    public static final String FIELD_FAVORITE = "favorite";
    public static final String FIELD_TITLE = "title";

    @DatabaseField(columnName = "news_id", id = true)
    private int mId;

    @DatabaseField(columnName = "source")
    private RssSource mSource;

    @DatabaseField(columnName = FIELD_TITLE)
    private String mTitle;

    @DatabaseField(columnName = "link")
    private String mLink;

    @DatabaseField(columnName = "description")
    private String mDescription;

    @DatabaseField(columnName = "author")
    private String mAuthor;

    @DatabaseField(columnName = "category")
    private String mCategory;

    @DatabaseField(columnName = "image_url")
    private String mImageUrl;

    @DatabaseField(columnName = FIELD_DATE, persisterClass = LocalDateTimePersister.class)
    private LocalDateTime mPubDate;

    @DatabaseField(columnName = FIELD_READ)
    private boolean mRead = false;

    @DatabaseField(columnName = FIELD_FAVORITE)
    private boolean mFavorite = false;

    ////////////////

    public News() {}

    public News(@NotNull RssSource source, @NotNull XmlNews xmlNews) {
        mId = xmlNews.getGuid().hashCode();
        mSource = source;
        mTitle = xmlNews.getTitle();
        mLink = xmlNews.getLink();
        mDescription = xmlNews.getDescription();
        mAuthor = xmlNews.getAuthor();
        mCategory = xmlNews.getCategory();
        XmlEnclosure enclosure = xmlNews.getEnclosure();
        if (enclosure != null) {
            mImageUrl = xmlNews.getEnclosure().getUrl().replaceAll("^/+", "");
        }
        mPubDate = DateUtils.parseStringToDateTime(xmlNews.getPubDate());
    }

    ////////////////
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public RssSource getSource() {
        return mSource;
    }

    public void setSource(RssSource mSource) {
        this.mSource = mSource;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public LocalDateTime getPubDate() {
        return mPubDate;
    }

    public void setPubDate(LocalDateTime pubDate) {
        this.mPubDate = pubDate;
    }

    public boolean isRead() {
        return mRead;
    }

    public void setRead(boolean read) {
        mRead = read;
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        News news = (News) o;

        return mId == news.mId;

    }

    @Override
    public int hashCode() {
        return mId;
    }
}
