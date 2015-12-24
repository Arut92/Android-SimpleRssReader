package ru.arutyun.agababyanarutyun.presenter;


import android.support.annotation.NonNull;

import ru.arutyun.agababyanarutyun.db.News;
import ru.arutyun.agababyanarutyun.data.FilterType;

public interface NewsListFragmentPresenter extends Presenter {

    void loadNewsList(@NonNull FilterType filterType);
    void downloadNews(@NonNull FilterType filterType);
    void searchNews(@NonNull String searchString);
    void readAllNews(@NonNull FilterType filterType);
    void newsClick(News news);

}
