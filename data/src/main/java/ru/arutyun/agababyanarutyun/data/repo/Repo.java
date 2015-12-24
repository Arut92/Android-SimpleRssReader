package ru.arutyun.agababyanarutyun.data.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDateTime;

import java.util.List;

import ru.arutyun.agababyanarutyun.data.FilterType;
import ru.arutyun.agababyanarutyun.db.News;
import rx.Observable;

public interface Repo {

    @Nullable
    Observable<List<News>> getNewsList(@NonNull FilterType filterType);

    @Nullable
    Observable<List<News>> getNewsList(@NonNull FilterType filterType, int limit);

    @Nullable
    Observable<News> getNews(int newsId);

    Observable<List<News>> searchStrInNews(@NonNull String searchString);

    void createNews(@NonNull List<News> newsList);

    void changeNewsFavoriteState(@NonNull News news);

    @Nullable
    Observable readAllNews();

    @Nullable
    Observable readNews(@NonNull News news);

    boolean isFirstRun();

    @Nullable
    LocalDateTime getLastUpdateTime();

    void setLastUpdateTime (@NonNull LocalDateTime lastUpdateTime);
}
