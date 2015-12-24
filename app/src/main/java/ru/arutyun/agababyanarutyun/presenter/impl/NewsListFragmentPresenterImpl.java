package ru.arutyun.agababyanarutyun.presenter.impl;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import ru.arutyun.agababyanarutyun.R;
import ru.arutyun.agababyanarutyun.RssSource;
import ru.arutyun.agababyanarutyun.bus.event.UnreadNewsCountUpdateEvent;
import ru.arutyun.agababyanarutyun.data.bus.RxBus;
import ru.arutyun.agababyanarutyun.data.net.RestClient;
import ru.arutyun.agababyanarutyun.data.util.NewsUtils;
import ru.arutyun.agababyanarutyun.data.repo.Repo;
import ru.arutyun.agababyanarutyun.db.News;
import ru.arutyun.agababyanarutyun.di.PerActivity;
import ru.arutyun.agababyanarutyun.net.XmlRss;
import ru.arutyun.agababyanarutyun.presenter.NewsListFragmentPresenter;
import ru.arutyun.agababyanarutyun.ui.display.Display;
import ru.arutyun.agababyanarutyun.ui.display.NewsListFragmentDisplay;
import ru.arutyun.agababyanarutyun.data.FilterType;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@PerActivity
public class NewsListFragmentPresenterImpl implements NewsListFragmentPresenter {

    @Inject RestClient restClient;
    @Inject Repo repo;
    @Inject RxBus eventBus;

    private NewsListFragmentDisplay mDisplay;

    private AtomicBoolean mIsLoading = new AtomicBoolean(false);

    //////////////////////

    @Inject
    public NewsListFragmentPresenterImpl() {}

    //////////////////////

    @Override
    public void bindDisplay(Display display) {
        if (display instanceof NewsListFragmentDisplay) {
            mDisplay = (NewsListFragmentDisplay) display;
        } else {
            throw new IllegalArgumentException("display must be instance of NewsListFragmentDisplay");
        }
    }

    @Override
    public void loadNewsList(@NonNull final FilterType filterType) {
        Observable observable = repo.getNewsList(filterType);
        if (observable != null) {
            observable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<News>>() {
                        @Override
                        public void call(List<News> newsList) {
                            if (repo.isFirstRun()) {
                                // first run - download
                                downloadNews(filterType);
                            } else {
                                mDisplay.hideProgress();
                                mDisplay.updateListContent(newsList);
                            }
                        }
                    });
        }
    }

    @Override
    public void downloadNews(@NonNull final FilterType filterType) {
        if (!mIsLoading.get()) {
            mIsLoading.set(true);
            mDisplay.showProgress();
            Observable.from(RssSource.values())
                    // RssSource -> XmlRss
                    .flatMap(new Func1<RssSource, Observable<XmlRss>>() {
                        @Override
                        public Observable<XmlRss> call(RssSource source) {
                            return restClient.getRss(source);
                        }
                    })
                    // XmlRss -> News list
                    .map(new Func1<XmlRss, List<News>>() {
                        @Override
                        public List<News> call(XmlRss xmlRss) {
                            List<News> newsList = NewsUtils.rssToNews(xmlRss);
                            return NewsUtils.filterByDate(newsList, repo.getLastUpdateTime());
                        }
                    })
                    .doOnNext(new Action1<List<News>>() {
                        @Override
                        public void call(List<News> newsList) {
                            repo.createNews(newsList);
                        }
                    })
                    .finallyDo(new Action0() {
                        @Override
                        public void call() {
                            updateUnreadCount();
                            repo.setLastUpdateTime(LocalDateTime.now());
                            mIsLoading.set(false);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<News>>() {
                        @Override
                        public void onCompleted() {
                            if (mDisplay != null) {
                                mDisplay.hideProgress();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mDisplay != null) {
                                mDisplay.showError(e.getMessage());
                                mDisplay.hideProgress();
                            }
                        }

                        @Override
                        public void onNext(List<News> newsList) {
                            mDisplay.updateListContent(newsList);
                        }
                    });
        }
    }

    @Override
    public void readAllNews(@NonNull final FilterType filterType) {
        Observable observable = repo.readAllNews();
        if(observable != null) {
            observable
                    .flatMap(new Func1<Object, Observable<List<News>>>() {
                        @Override
                        public Observable<List<News>> call(Object object) {
                            return repo.getNewsList(filterType);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<News>>() {
                        @Override
                        public void call(List<News> newsList) {
                            eventBus.post(new UnreadNewsCountUpdateEvent(0));
                            if (mDisplay != null) {
                                mDisplay.markAllRead();
                                mDisplay.showMessage(R.string.read_all_news);
                            }
                        }
                    });
        }

    }

    @Override
    public void newsClick(News news) {
        if (!news.isRead()) {
            Observable observable = repo.readNews(news);
            if (observable != null) {
                observable
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            updateUnreadCount();
                        }
                    });
            }

        }

        mDisplay.newsClick(news);
    }

    @Override
    public void searchNews(@NonNull String searchString) {
        repo.searchStrInNews(searchString);
    }

    private void updateUnreadCount() {
        Observable observable = repo.getNewsList(FilterType.UNREAD);
        if (observable != null) {
            observable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<News>>() {
                        @Override
                        public void call(List<News> newsList) {
                            eventBus.post(new UnreadNewsCountUpdateEvent(newsList.size()));
                        }
                    });
        }
    }


}
