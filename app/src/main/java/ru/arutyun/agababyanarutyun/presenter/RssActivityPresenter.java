package ru.arutyun.agababyanarutyun.presenter;


import android.support.annotation.NonNull;

import ru.arutyun.agababyanarutyun.data.FilterType;

public interface RssActivityPresenter extends Presenter {

    void openNewsList(@NonNull FilterType filterType);
    void openNewsItem(int newsId);

    void updateFavoriteCount();
    void updateUnreadCount();

}
