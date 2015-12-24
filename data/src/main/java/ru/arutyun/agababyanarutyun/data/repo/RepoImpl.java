package ru.arutyun.agababyanarutyun.data.repo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


import org.joda.time.LocalDateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import ru.arutyun.agababyanarutyun.data.FilterType;
import ru.arutyun.agababyanarutyun.data.db.DbHelper;
import ru.arutyun.agababyanarutyun.data.setting.Settings;
import ru.arutyun.agababyanarutyun.data.util.RxUtils;
import ru.arutyun.agababyanarutyun.db.News;
import rx.Observable;
import rx.functions.Action1;


public class RepoImpl implements Repo {

    private static final String TAG = RepoImpl.class.getCanonicalName();

    private List<News> mCachedAllNews = new ArrayList<>();
    private List<News> mCachedUnreadNews = new ArrayList<>();
    private List<News> mCachedFavoriteNews = new ArrayList<>();

    DbHelper mDbHelper;
    Settings mSettings;

    //////////////////////

    @Inject
    public RepoImpl(@NonNull Context context) {
        mDbHelper = new DbHelper(context);
        mSettings = new Settings(context);
    }

    @Override
    public Observable<List<News>> getNewsList(@NonNull FilterType filterType) {
        switch (filterType) {
            case ALL_NEWS:
                return getAllNews();
            case UNREAD:
                return getUnreadNews();
            case FAVORITES:
                return getFavoriteNews();
        }
        return null;
    }

    @Override
    public Observable<List<News>> getNewsList(@NonNull FilterType filterType, int limit) {
        return null;
    }

    @NonNull
    private Observable<List<News>> getAllNews() {
        if (mCachedAllNews.isEmpty()) {
            // get from DB
            try {
                return mDbHelper.getNewsDao().selectAllNews()
                        .doOnNext(new Action1<List<News>>() {
                            @Override
                            public void call(List<News> newsList) {
                                mCachedAllNews = new ArrayList<>(newsList);
                            }
                        });
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return RxUtils.makeObservable(new Callable<List<News>>() {
            @Override
            public List<News> call() throws Exception {
                return new ArrayList<>(mCachedAllNews);
            }
        });
    }

    @NonNull
    private Observable<List<News>> getFavoriteNews() {
        if (mCachedFavoriteNews.isEmpty()) {
            // get from DB
            try {
                return mDbHelper.getNewsDao().selectFavoritesNews()
                        .doOnNext(new Action1<List<News>>() {
                            @Override
                            public void call(List<News> newsList) {
                                mCachedFavoriteNews = new ArrayList<>(newsList);
                            }
                        });
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return RxUtils.makeObservable(new Callable<List<News>>() {
            @Override
            public List<News> call() throws Exception {
                return new ArrayList<>(mCachedFavoriteNews);
            }
        });
    }

    @NonNull
    private Observable<List<News>> getUnreadNews() {
        if (mCachedUnreadNews.isEmpty()) {
            // get from DB
            try {
                return mDbHelper.getNewsDao().selectUnreadNews()
                        .doOnNext(new Action1<List<News>>() {
                            @Override
                            public void call(List<News> newsList) {
                                mCachedUnreadNews = new ArrayList<>(newsList);
                            }
                        });
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return RxUtils.makeObservable(new Callable<List<News>>() {
            @Override
            public List<News> call() throws Exception {
                return new ArrayList<>(mCachedUnreadNews);
            }
        });
    }

    @Override
    public void createNews(@NonNull List<News> newsList) {
        clearCache();
        try {
            mDbHelper.getNewsDao().saveNews(newsList);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void changeNewsFavoriteState(@NonNull News news) {
        mCachedFavoriteNews = Collections.EMPTY_LIST;
        try {
            mDbHelper.getNewsDao().changeFavoriteState(news);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Nullable
    @Override
    public  Observable<News> getNews(int newsId) {
        News news = getNewsFromCache(newsId);
        if (news == null) {
            try {
                return mDbHelper.getNewsDao().selectNewsById(newsId);
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return null;
    }

    @Nullable
    private News getNewsFromCache(long newsId) {
        for(News news: mCachedUnreadNews) {
            if (news.getId() == newsId) {
                return news;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public  Observable<List<News>> searchStrInNews(@NonNull String searchString) {
        try {
            mDbHelper.getNewsDao().selectNewsByStr(searchString);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Nullable
    @Override
    public Observable readAllNews() {
        clearCache();
        try {
            return mDbHelper.getNewsDao().markAllAsRead();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    @Nullable
    public Observable readNews(@NonNull News news) {
        clearCache();
        try {
            return mDbHelper.getNewsDao().markAsRead(news);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    @Nullable
    public LocalDateTime getLastUpdateTime() {
        return mSettings.getLastUpdateTime();
    }

    @Override
    public void setLastUpdateTime (@NonNull LocalDateTime lastUpdateTime) {
        mSettings.setLastUpdateTime(lastUpdateTime);
    }

    @Override
    public boolean isFirstRun() {
        return getLastUpdateTime() == null;
    }

    private void clearCache() {
        mCachedAllNews.clear();
        mCachedFavoriteNews.clear();
        mCachedUnreadNews.clear();
    }

}
