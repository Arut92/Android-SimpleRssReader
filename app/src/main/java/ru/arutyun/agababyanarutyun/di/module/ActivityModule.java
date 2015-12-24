package ru.arutyun.agababyanarutyun.di.module;

import android.app.Activity;
import dagger.Module;
import dagger.Provides;
import ru.arutyun.agababyanarutyun.di.PerActivity;


@Module
public class ActivityModule {

    private final Activity mActivity;

    //////////////////////

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    //////////////////////

    @Provides @PerActivity
    Activity activity() {
        return mActivity;
    }

}
