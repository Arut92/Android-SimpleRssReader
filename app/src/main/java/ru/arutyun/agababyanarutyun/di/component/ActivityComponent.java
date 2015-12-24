package ru.arutyun.agababyanarutyun.di.component;

import android.app.Activity;

import dagger.Component;
import ru.arutyun.agababyanarutyun.di.PerActivity;
import ru.arutyun.agababyanarutyun.di.module.ActivityModule;


@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    // Exposed to subgraphs
    Activity activity();

}
