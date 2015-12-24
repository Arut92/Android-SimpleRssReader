package ru.arutyun.agababyanarutyun.di.module;

import android.content.Context;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.arutyun.agababyanarutyun.data.bus.RxBus;
import ru.arutyun.agababyanarutyun.data.db.DbHelper;
import ru.arutyun.agababyanarutyun.data.net.RestClient;
import ru.arutyun.agababyanarutyun.App;
import ru.arutyun.agababyanarutyun.data.repo.Repo;
import ru.arutyun.agababyanarutyun.data.repo.RepoImpl;


@Module
public class ApplicationModule {

    private final App mApp;

    /////////////////////////

    public ApplicationModule(App app) {
        mApp = app;
    }

    /////////////////////////

    @Provides @Singleton Context provideApplicationContext() {
        return mApp;
    }

    @Provides @Singleton RestClient provideServerApi() {
        return new RestClient();
    }

    @Provides @Singleton Repo provideRepo() {
        return new RepoImpl(mApp);
    }

    @Provides @Singleton RxBus provideRxBus() {
        return new RxBus();
    }

}
