package ru.arutyun.agababyanarutyun.ui.display;


import android.support.annotation.StringRes;

import org.jetbrains.annotations.NotNull;

import ru.arutyun.agababyanarutyun.data.FilterType;

public interface RssActivityDisplay extends Display {

    void updateUnreadCount(int count);
    void updateFavoriteCount(int count);
    void showNewsList(@NotNull FilterType filter);
    void showNewsItem(int newsId);
    void showError(@StringRes int errRes);

}
