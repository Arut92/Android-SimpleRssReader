package ru.arutyun.agababyanarutyun;

import android.app.Application;

import ru.arutyun.agababyanarutyun.di.component.ApplicationComponent;
import ru.arutyun.agababyanarutyun.di.component.DaggerApplicationComponent;
import ru.arutyun.agababyanarutyun.di.module.ApplicationModule;

public class App extends Application {

    private ApplicationComponent mApplicationComponent;

    /////////////////////

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
    }

    ///

    private void initializeInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    /////////////////////

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
