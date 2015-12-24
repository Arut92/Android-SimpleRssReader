package ru.arutyun.agababyanarutyun.presenter.impl;


import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import ru.arutyun.agababyanarutyun.R;
import ru.arutyun.agababyanarutyun.bus.event.FavoriteNewsCountUpdateEvent;
import ru.arutyun.agababyanarutyun.data.FilterType;
import ru.arutyun.agababyanarutyun.data.bus.RxBus;
import ru.arutyun.agababyanarutyun.data.net.RestClient;
import ru.arutyun.agababyanarutyun.data.repo.Repo;
import ru.arutyun.agababyanarutyun.db.News;
import ru.arutyun.agababyanarutyun.di.PerActivity;
import ru.arutyun.agababyanarutyun.presenter.NewsItemFragmentPresenter;
import ru.arutyun.agababyanarutyun.ui.display.Display;
import ru.arutyun.agababyanarutyun.ui.display.NewsItemFragmentDisplay;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

@PerActivity
public class NewsItemFragmentPresenterImpl implements NewsItemFragmentPresenter {

    @Inject RestClient restClient;
    @Inject Repo repo;
    @Inject RxBus eventBus;

    private NewsItemFragmentDisplay mDisplay;

    //////////////////////

    @Inject
    public NewsItemFragmentPresenterImpl() {}

    //////////////////////

    @Override
    public void bindDisplay(Display display) {
        if (display instanceof NewsItemFragmentDisplay) {
            mDisplay = (NewsItemFragmentDisplay) display;
        } else {
            throw new IllegalArgumentException("display must be instance of NewsItemFragmentDisplay");
        }
    }

    @Override
    public void loadNews(int newsId) {
        final Observable<News> observable = repo.getNews(newsId);
        if (observable != null) {
            observable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<News>() {
                        @Override
                        public void call(News news) {
                            if (mDisplay != null) {
                                mDisplay.showContent(news);
                            }
                        }
                    });
        } else {
            mDisplay.showError(R.string.error_news_not_found);
        }
    }

    @Override
    public void handleFavoriteClick(@NonNull News news) {
        repo.changeNewsFavoriteState(news);
        Observable observable = repo.getNewsList(FilterType.FAVORITES);
        if (observable != null) {
            observable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<News>>() {
                        @Override
                        public void call(List<News> newsList) {
                            eventBus.post(new FavoriteNewsCountUpdateEvent(newsList.size()));
                        }
                    });
        }
    }

}
