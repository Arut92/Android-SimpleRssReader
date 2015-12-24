package ru.arutyun.agababyanarutyun.presenter;


import android.support.annotation.NonNull;

import ru.arutyun.agababyanarutyun.db.News;

public interface NewsItemFragmentPresenter extends Presenter {

    void loadNews(int newsId);
    void handleFavoriteClick(@NonNull News news);

}
