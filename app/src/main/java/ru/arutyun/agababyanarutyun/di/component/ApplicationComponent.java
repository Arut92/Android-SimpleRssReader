package ru.arutyun.agababyanarutyun.di.component;

import android.content.Context;


import javax.inject.Singleton;

import dagger.Component;
import ru.arutyun.agababyanarutyun.data.bus.RxBus;
import ru.arutyun.agababyanarutyun.data.db.DbHelper;
import ru.arutyun.agababyanarutyun.data.net.RestClient;
import ru.arutyun.agababyanarutyun.data.repo.Repo;
import ru.arutyun.agababyanarutyun.di.module.ApplicationModule;
import ru.arutyun.agababyanarutyun.ui.activity.RssActivity;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(RssActivity mainActivity);

    Context context();
    RestClient restClient();
    Repo repo();
    RxBus rxBus();
}
