package ru.arutyun.agababyanarutyun.ui.activity;

import android.support.v7.app.AppCompatActivity;

import ru.arutyun.agababyanarutyun.di.component.ApplicationComponent;
import ru.arutyun.agababyanarutyun.di.module.ActivityModule;
import ru.arutyun.agababyanarutyun.App;


public abstract class BaseActivity extends AppCompatActivity {

    protected ApplicationComponent getApplicationComponent() {
        return ((App) getApplication()).getApplicationComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

}
