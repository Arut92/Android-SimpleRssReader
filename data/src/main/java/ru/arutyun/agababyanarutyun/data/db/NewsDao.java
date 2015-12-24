package ru.arutyun.agababyanarutyun.data.db;

import android.support.annotation.NonNull;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import ru.arutyun.agababyanarutyun.data.util.RxUtils;
import ru.arutyun.agababyanarutyun.db.News;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class NewsDao extends BaseDaoImpl<News, Integer> {

    ///////////////

    public NewsDao(@NonNull final ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, News.class);
    }

    ///////////////

    public void saveNews(@NonNull final List<News> newsList) throws SQLException {
        final Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                callBatchTasks(new Callable<News>() {
                    @Override
                    public News call() throws SQLException {
                        for (News news : newsList) {
                                create(news);
                        }
                        return null;
                    }
                });
                return null;
            }
        };
        RxUtils.makeObservable(callable)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1() {
                    @Override
                    public void call(Object o) {}
                });
    }

    @NotNull
    public Observable<List<News>> selectAllNews() {
        final Callable<List<News>> callable = new Callable<List<News>>() {
            @Override
            public List<News> call() throws SQLException {
                return queryBuilder().orderBy(News.FIELD_DATE, false)
                        .query();
            }
        };
        return RxUtils.makeObservable(callable)
                .subscribeOn(Schedulers.newThread());
    }

    @NotNull
    public Observable<List<News>> selectFavoritesNews() {
        final Callable<List<News>> callable = new Callable<List<News>>() {
            @Override
            public List<News> call() throws SQLException {
                return queryBuilder().orderBy(News.FIELD_DATE, false)
                        .where()
                        .eq(News.FIELD_FAVORITE, true)
                        .query();
            }
        };
        return RxUtils.makeObservable(callable)
                .subscribeOn(Schedulers.newThread());
    }

    @NotNull
    public Observable<List<News>> selectUnreadNews() {
        final Callable<List<News>> callable = new Callable<List<News>>() {
            @Override
            public List<News> call() throws SQLException {
                return queryBuilder().orderBy(News.FIELD_DATE, false)
                        .where()
                        .eq(News.FIELD_READ, false)
                        .query();
            }
        };
        return RxUtils.makeObservable(callable)
                .subscribeOn(Schedulers.newThread());
    }

    public void changeFavoriteState(final News news) throws SQLException {
        final Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                UpdateBuilder<News, Integer> updateBuilder = updateBuilder();
                updateBuilder.where().idEq(news.getId());
                updateBuilder.updateColumnValue(News.FIELD_FAVORITE, !news.isFavorite());
                updateBuilder.update();
                return null;
            }
        };
        RxUtils.makeObservable(callable)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1() {
                    @Override
                    public void call(Object o) {}
                });
    }

    @NotNull
    public Observable markAsRead(final News news) throws SQLException {
        final Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                UpdateBuilder<News, Integer> updateBuilder = updateBuilder();
                updateBuilder.where().idEq(news.getId());
                updateBuilder.updateColumnValue(News.FIELD_READ, true);
                updateBuilder.update();
                return null;
            }
        };

        return RxUtils.makeObservable(callable)
                .subscribeOn(Schedulers.newThread());

    }

    @NotNull
    public Observable markAllAsRead() throws SQLException {
        final Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                UpdateBuilder<News, Integer> updateBuilder = updateBuilder();
                updateBuilder.updateColumnValue(News.FIELD_READ, true);
                updateBuilder.update();
                return null;
            }
        };

        return RxUtils.makeObservable(callable)
                .subscribeOn(Schedulers.newThread());
    }

    @NotNull
    public Observable<News> selectNewsById(final int newsId) throws SQLException {
        final Callable<News> callable = new Callable<News>() {
            @Override
            public News call() throws SQLException {
                return queryForId(newsId);
            }
        };
        return RxUtils.makeObservable(callable)
                .subscribeOn(Schedulers.newThread());
    }

    @NotNull
    public Observable<List<News>> selectNewsByStr(final String searchStr) throws SQLException {
        final Callable<List<News>> callable = new Callable<List<News>>() {
            @Override
            public List<News> call() throws SQLException {
                return queryBuilder()
                        .where().like(News.FIELD_TITLE, searchStr)
                        .query();
            }
        };
        return RxUtils.makeObservable(callable)
                .subscribeOn(Schedulers.newThread());
    }

}
