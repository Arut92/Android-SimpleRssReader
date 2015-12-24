package ru.arutyun.agababyanarutyun.ui.display;


import android.support.annotation.StringRes;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.arutyun.agababyanarutyun.db.News;

public interface NewsListFragmentDisplay extends Display {

    void showProgress();
    void hideProgress();
    void showMessage(@StringRes int mesText);
    void showError(@NotNull String errText);
    void showError(@StringRes int errRes);

    void updateListContent(@NotNull List<News> newsList);
    void markAllRead();
    void newsClick(@NotNull News news);
}
