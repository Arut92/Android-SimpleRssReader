package ru.arutyun.agababyanarutyun.presenter.impl;

import android.support.annotation.NonNull;


import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.arutyun.agababyanarutyun.data.repo.Repo;
import ru.arutyun.agababyanarutyun.db.News;
import ru.arutyun.agababyanarutyun.presenter.RssActivityPresenter;
import ru.arutyun.agababyanarutyun.ui.display.Display;
import ru.arutyun.agababyanarutyun.ui.display.RssActivityDisplay;
import ru.arutyun.agababyanarutyun.data.FilterType;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

@Singleton
public class RssActivityPresenterImpl implements RssActivityPresenter {

    @Inject Repo repo;

    private RssActivityDisplay mDisplay;

    //////////////////////

    @Inject
    public RssActivityPresenterImpl() {}

    //////////////////////

    @Override
    public void bindDisplay(Display display) {
        if (display instanceof RssActivityDisplay) {
            mDisplay = (RssActivityDisplay) display;
        } else {
            throw new IllegalArgumentException("display must be instance of RssActivityDisplay");
        }
    }

    @Override
    public void openNewsList(@NonNull FilterType filterType) {
        mDisplay.showNewsList(filterType);
    }

    @Override
    public void openNewsItem(int newsId) {
        mDisplay.showNewsItem(newsId);
    }

    @Override
    public void updateFavoriteCount() {
        Observable observable = repo.getNewsList(FilterType.FAVORITES);
        if (observable != null) {
            observable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<News>>() {
                        @Override
                        public void call(List<News> newsList) {
                            if (mDisplay != null) {
                                mDisplay.updateFavoriteCount(newsList.size());
                            }
                        }
                    });
        }
    }

    @Override
    public void updateUnreadCount() {
        Observable observable = repo.getNewsList(FilterType.UNREAD);
        if (observable != null) {
            observable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<News>>() {
                        @Override
                        public void call(List<News> newsList) {
                            if (mDisplay != null) {
                                mDisplay.updateUnreadCount(newsList.size());
                            }
                        }
                    });
        }
    }

}
