package ru.arutyun.agababyanarutyun.data.util;


import android.support.annotation.NonNull;

import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.arutyun.agababyanarutyun.RssSource;
import ru.arutyun.agababyanarutyun.db.News;
import ru.arutyun.agababyanarutyun.net.XmlNews;
import ru.arutyun.agababyanarutyun.net.XmlRss;

public final class NewsUtils {

    private NewsUtils() {}

    @NonNull
    public static final List<News> rssToNews(@Nullable XmlRss rss) {
        final List<News> newsList = new ArrayList<>();
        if (rss != null && rss.getChannel() != null) {
            final String sourceTitle = rss.getChannel().getTitle().toLowerCase();
            RssSource source = null;
            if (sourceTitle.startsWith(RssSource.GAZETA.getName())) {
                source = RssSource.GAZETA;
            } else if (sourceTitle.startsWith(RssSource.LENTA.getName())) {
                source = RssSource.LENTA;
            }

            if (source != null) {
                final List<XmlNews> xmlNewsList = rss.getChannel().getItemList();
                for (XmlNews xmlNews : xmlNewsList) {
                    newsList.add(new News(source, xmlNews));
                }
            }
        }
        return newsList;
    }

    @NonNull
    public static List<News> filterByDate(@NonNull List<News> newsList, @Nullable LocalDateTime filterDate) {
        if (filterDate != null) {
            final Iterator<News> iterator = newsList.iterator();
            News candidate;
            while (iterator.hasNext()) {
                candidate = iterator.next();
                if (candidate.getPubDate().isBefore(filterDate)) {
                    iterator.remove();
                }
            }
        }
        return newsList;
    }

}
