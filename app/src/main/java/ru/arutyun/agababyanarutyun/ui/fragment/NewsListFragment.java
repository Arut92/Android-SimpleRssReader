package ru.arutyun.agababyanarutyun.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.pnikosis.materialishprogress.ProgressWheel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ru.arutyun.agababyanarutyun.R;
import ru.arutyun.agababyanarutyun.db.News;
import ru.arutyun.agababyanarutyun.di.component.UserComponent;
import ru.arutyun.agababyanarutyun.presenter.impl.NewsListFragmentPresenterImpl;
import ru.arutyun.agababyanarutyun.adapter.NewsRecyclerViewAdapter;
import ru.arutyun.agababyanarutyun.ui.display.NewsListFragmentDisplay;
import ru.arutyun.agababyanarutyun.data.FilterType;
import ru.arutyun.agababyanarutyun.ui.view.NestedScrollableSwipeRefreshLayout;


public class NewsListFragment  extends BaseFragment implements NewsListFragmentDisplay,
        NewsRecyclerViewAdapter.OnNewsClickListener {

    private static final String ARG_FILTER_TYPE = "arg_filter_type";

    private static final String STATE_FILTER_TYPE = "state_filter_type";

    @InjectView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @InjectView(R.id.contentView)       NestedScrollableSwipeRefreshLayout refreshLayout;
    @InjectView(R.id.news_list)         RecyclerView rvNewsList;
    @InjectView(R.id.pb_wheel)          ProgressWheel pbNewsLoading;
    @InjectView(R.id.fab_read_all)      FloatingActionButton fabReadAll;

    private Callbacks mCallback;

    private FilterType mFilterType;

    private NewsRecyclerViewAdapter mAdapter;

    @Inject NewsListFragmentPresenterImpl presenter;

    public static NewsListFragment newInstance(@NonNull FilterType filterType) {
        final NewsListFragment fragment = new NewsListFragment();
        final Bundle args = new Bundle();
        args.putSerializable(ARG_FILTER_TYPE, filterType);
        fragment.setArguments(args);
        return fragment;
    }

    public NewsListFragment() {
        // Required empty public constructor
    }

    //////////////////////

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof Callbacks) {
            mCallback = (Callbacks) context;
        } else {
            throw new ClassCastException(
                    context.getClass().getSimpleName() + " must implement NewsListFragment.Callback"
            );
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // prevent memory leaks
        mCallback = null;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(UserComponent.class).inject(this);
        presenter.bindDisplay(this);
        if (savedInstanceState == null) {
            final Bundle args = getArguments();
            if (args != null) {
                mFilterType = (FilterType) args.get(ARG_FILTER_TYPE);
            }
        } else {
            mFilterType = (FilterType) savedInstanceState.get(STATE_FILTER_TYPE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_FILTER_TYPE, mFilterType);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        ButterKnife.inject(this, view);

        // Init refreshLayout
        refreshLayout.setCanChildScrollUpCallback(new NestedScrollableSwipeRefreshLayout.CanChildScrollUpCallback() {
            @Override
            public boolean canSwipeRefreshChildScrollUp() {
                return rvNewsList.computeVerticalScrollOffset() > 0;
            }
        });
        refreshLayout.setOnRefreshListener(new NestedScrollableSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.downloadNews(mFilterType);
            }
        });

        presenter.loadNewsList(mFilterType);

        // Init RecyclerView
        mAdapter = new NewsRecyclerViewAdapter(getActivity());
        mAdapter.setOnNewsClickListener(this);
        rvNewsList.setHasFixedSize(true);
        rvNewsList.setAdapter(mAdapter);

        fabReadAll.setVisibility(mFilterType == FilterType.FAVORITES ? View.GONE : View.VISIBLE);
        return view;
    }

    @OnClick(R.id.fab_read_all)
    public void readAll() {
        presenter.readAllNews(mFilterType);
    }

    @Override
    public void onNewsClick(News news) {
        presenter.newsClick(news);
    }

    /*
     Display interface
     */
    @Override
    public void showProgress() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        refreshLayout.setRefreshing(false);
        pbNewsLoading.setVisibility(View.GONE);
        rvNewsList.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(@StringRes int mesText) {
        Snackbar.make(coordinatorLayout, mesText, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showError(@NotNull String errText) {
        Snackbar.make(coordinatorLayout, errText, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showError(@StringRes int errRes) {
        Snackbar.make(coordinatorLayout, getString(errRes), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void updateListContent(@NotNull List<News> newsList) {
        mAdapter.addData(newsList);
    }

    @Override
    public void markAllRead() {
        mAdapter.markAllRead();
    }

    @Override
    public void newsClick(@NotNull News news) {
        mCallback.newsClick(news.getId());
    }

    /*
     Interface for attached Activity
     */
    public interface Callbacks {

        void newsClick(int newsId);

    }

}
