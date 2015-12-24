package ru.arutyun.agababyanarutyun.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.LinearLayout;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ru.arutyun.agababyanarutyun.bus.event.FavoriteNewsCountUpdateEvent;
import ru.arutyun.agababyanarutyun.bus.event.UnreadNewsCountUpdateEvent;
import ru.arutyun.agababyanarutyun.data.bus.RxBus;
import ru.arutyun.agababyanarutyun.data.FilterType;
import ru.arutyun.agababyanarutyun.R;
import ru.arutyun.agababyanarutyun.di.HasComponent;
import ru.arutyun.agababyanarutyun.di.component.DaggerUserComponent;
import ru.arutyun.agababyanarutyun.di.component.UserComponent;
import ru.arutyun.agababyanarutyun.presenter.impl.RssActivityPresenterImpl;
import ru.arutyun.agababyanarutyun.ui.display.RssActivityDisplay;
import ru.arutyun.agababyanarutyun.ui.fragment.NewsItemFragment;
import ru.arutyun.agababyanarutyun.ui.fragment.NewsListFragment;
import ru.arutyun.agababyanarutyun.ui.view.DrawerItemFactory;
import rx.Subscription;
import rx.functions.Action1;

public class RssActivity extends BaseActivity implements HasComponent<UserComponent>,
        RssActivityDisplay,
        NewsListFragment.Callbacks {

    private static final String STATE_TWO_PANE = "state_two_pane";
    private static final String STATE_FILTER_TYPE = "state_filter_type";

    @InjectView(R.id.toolbar)  Toolbar toolbar;
    @InjectView(R.id.lv_root)  LinearLayout lvRoot;

    @Inject RxBus rxBus;
    @Inject RssActivityPresenterImpl presenter;

    private Subscription mUpdateUnreadCount;
    private Subscription mUpdateFavoriteCount;

    private UserComponent mUserComponent;

    private Drawer mDrawer;
    private boolean mTwoPane = false;
    private FilterType mCurrentFilterType = FilterType.ALL_NEWS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
        setContentView(R.layout.activity_rss);
        ButterKnife.inject(this);
        initializeInjector();
        registerBusEvents();
        presenter.bindDisplay(this);
        if (savedInstanceState == null) {
            presenter.openNewsList(mCurrentFilterType);
            if (findViewById(R.id.news_item_container) != null) {
                mTwoPane = true;
            }
        } else {
            mTwoPane = savedInstanceState.getBoolean(STATE_TWO_PANE);
            mCurrentFilterType = (FilterType) savedInstanceState.get(STATE_FILTER_TYPE);
        }
        initActionBar();
        initDrawer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_TWO_PANE, mTwoPane);
        outState.putSerializable(STATE_FILTER_TYPE, mCurrentFilterType);
    }

    private void initializeInjector() {
        mUserComponent = DaggerUserComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    private void registerBusEvents() {
        mUpdateUnreadCount = rxBus.register(UnreadNewsCountUpdateEvent.class, new Action1<UnreadNewsCountUpdateEvent>() {
            @Override
            public void call(UnreadNewsCountUpdateEvent event) {
                updateUnreadCount(event.getCount());
            }
        });

        mUpdateFavoriteCount = rxBus.register(FavoriteNewsCountUpdateEvent.class, new Action1<FavoriteNewsCountUpdateEvent>() {
            @Override
            public void call(FavoriteNewsCountUpdateEvent event) {
                updateFavoriteCount(event.getCount());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void unregisterBusEvents() {
        if (mUpdateFavoriteCount != null) {
            mUpdateUnreadCount.unsubscribe();
        }
        if (mUpdateFavoriteCount != null) {
            mUpdateFavoriteCount.unsubscribe();
        }
    }

    @Override
    public UserComponent getComponent() {
        return mUserComponent;
    }

    private void initActionBar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBusEvents();
        ButterKnife.reset(this);
    }

    private void initDrawer() {
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withHeader(R.layout.nav_header)
                .withToolbar(toolbar)
                .addDrawerItems(
                        DrawerItemFactory.getDrawerItem(FilterType.ALL_NEWS),
                        DrawerItemFactory.getDrawerItem(FilterType.UNREAD),
                        DrawerItemFactory.getDrawerItem(FilterType.FAVORITES)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        final FilterType filterType = (FilterType) drawerItem.getTag();
                        if (filterType != mCurrentFilterType) {
                            mCurrentFilterType = filterType;
                            presenter.openNewsList(filterType);
                        }
                        return false;

                    }
                })
                .build();

        switch (mCurrentFilterType) {
            case ALL_NEWS:
                mDrawer.setSelection(0);
                break;
            case UNREAD:
                mDrawer.setSelection(1);
                break;
            case FAVORITES:
                mDrawer.setSelection(2);
                break;
        }
        presenter.updateFavoriteCount();
        presenter.updateUnreadCount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_rss_toolbar, menu);
        return true;
    }

    /*
     Display interface
     */
    @Override
    public void showNewsList(@NonNull FilterType filter) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction()
                .replace(R.id.news_list_container, NewsListFragment.newInstance(filter))
                .commit();
        if(mTwoPane) {
            Fragment fragment = fragmentManager.findFragmentById(R.id.news_item_container);
            if (fragment != null) {
                fragmentManager.beginTransaction()
                        .remove(fragment)
                        .commit();
            }
        }
    }

    @Override
    public void showNewsItem(int newsId) {
        if (mTwoPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.news_item_container, NewsItemFragment.newInstance(newsId))
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.news_list_container, NewsItemFragment.newInstance(newsId))
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void showError(@StringRes int errRes) {
        Snackbar.make(lvRoot, errRes, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void updateUnreadCount(int count) {
        final StringHolder badgeCount = count == 0 ? null :
                new StringHolder(String.valueOf(count));

        mDrawer.updateBadge(FilterType.UNREAD.ordinal(), badgeCount);
    }

    @Override
    public void updateFavoriteCount(int count) {
        final StringHolder badgeCount = count == 0 ? null :
                new StringHolder(String.valueOf(count));
        mDrawer.updateBadge(FilterType.FAVORITES.ordinal(), badgeCount);
    }

    /*
     NewsListFragment.Callback
     */
    @Override
    public void newsClick(int newsId) {
        presenter.openNewsItem(newsId);
    }

}
