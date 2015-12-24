package ru.arutyun.agababyanarutyun.di.component;

import dagger.Component;
import ru.arutyun.agababyanarutyun.di.PerActivity;
import ru.arutyun.agababyanarutyun.di.module.ActivityModule;
import ru.arutyun.agababyanarutyun.di.module.UserModule;
import ru.arutyun.agababyanarutyun.ui.fragment.NewsItemFragment;
import ru.arutyun.agababyanarutyun.ui.fragment.NewsListFragment;


@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, UserModule.class})
public interface UserComponent {

    void inject(NewsListFragment listFragment);
    void inject(NewsItemFragment itemFragment);

}
