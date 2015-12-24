package ru.arutyun.agababyanarutyun.ui.display;


import android.support.annotation.StringRes;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.arutyun.agababyanarutyun.db.News;

public interface NewsItemFragmentDisplay extends Display {

    void showContent(@NotNull News news);
    void showError(@StringRes int errRes);
}
