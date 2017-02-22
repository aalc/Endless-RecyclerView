package com.github.ybq.endless;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 */
public class Endless {
    private final EndlessScrollListener listener;

    private boolean loadMoreAvailable = true;
    private LoadMoreListener loadMoreListener;
    private RecyclerView recyclerView;
    private EndlessAdapter mAdapter;
    private static ArrayList<WeakReference<Endless>> mLoadMoreEntries;
    private View loadMoreView;

    private SwipeRefreshLayout swipeRefreshLayout;

    public static void remove(RecyclerView recyclerView) {
        if (mLoadMoreEntries != null) {
            for (int i = 0; i < mLoadMoreEntries.size(); i++) {
                WeakReference<Endless> weakReference = mLoadMoreEntries.get(i);
                Endless endless = weakReference.get();
                if (endless == null || endless.getRecyclerView() == null || endless.getRecyclerView().equals(recyclerView)) {
                    mLoadMoreEntries.remove(i);
                    i--;
                }
            }
        }
    }


    public static class Builder {
        RecyclerView recyclerView;
        View loadMoreView;
        SwipeRefreshLayout swipeRefreshLayout;

        public Builder(RecyclerView recyclerView, View loadMoreView) {
            this.recyclerView = recyclerView;
            this.loadMoreView = loadMoreView;
        }

        public Builder pullToRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
            this.swipeRefreshLayout = swipeRefreshLayout;
            return this;
        }


        public Endless build() {
            return applyTo(recyclerView, loadMoreView, swipeRefreshLayout);
        }
    }


    private static Endless applyTo(RecyclerView recyclerView,
                                   View loadMoreView,
                                   SwipeRefreshLayout swipeRefreshLayout
    ) {
        Endless endless;
        if (mLoadMoreEntries == null) {
            mLoadMoreEntries = new ArrayList<>();
        } else {
            for (int i = 0; i < mLoadMoreEntries.size(); i++) {
                WeakReference<Endless> weakReference = mLoadMoreEntries.get(i);
                endless = weakReference.get();
                if (endless == null || endless.getRecyclerView() == null) {
                    mLoadMoreEntries.remove(i);
                    i--;
                } else if (endless.getRecyclerView().equals(recyclerView)) {
                    return endless;
                }
            }
        }
        endless = new Endless(recyclerView, loadMoreView, swipeRefreshLayout);
        mLoadMoreEntries.add(new WeakReference<>(endless));
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null) {
            endless.setAdapter(adapter);
        }
        return endless;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private Endless(final RecyclerView recyclerView,
                    View loadMoreView,
                    SwipeRefreshLayout swipeRefreshLayout
    ) {

        this.recyclerView = recyclerView;
        this.loadMoreView = loadMoreView;
        this.swipeRefreshLayout = swipeRefreshLayout;
        final RecyclerView.Adapter adapter = recyclerView.getAdapter();

        if (!(adapter instanceof EndlessAdapter)) {
            setAdapter(adapter);
        }
        recyclerView.addOnScrollListener(listener = new EndlessScrollListener() {
            @Override
            public void onLoadMore(final int currentPage) {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (loadMoreAvailable && loadMoreListener != null && !mAdapter.isLoading()) {
                            mAdapter.setLoading(true);
                            loadMoreListener.onLoadMore(currentPage);
                        }
                    }
                });

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listener.clearPage();
                loadMoreListener.onRefresh();
            }
        });

    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }


    public void loadMoreComplete() {
        mAdapter.setLoading(false);
        listener.setLoading(false);
    }

    public boolean isLoadMoreAvailable() {
        return loadMoreAvailable;
    }

    public void setLoadMoreAvailable(boolean loadMoreAvailable) {
        this.loadMoreAvailable = loadMoreAvailable;
    }


    public void setAdapter(RecyclerView.Adapter adapter, LoadMoreListener loadMoreListener) {
        setAdapter(adapter);
        setLoadMoreListener(loadMoreListener);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null) {
            return;
        }
        if (adapter instanceof EndlessAdapter) {
            recyclerView.setAdapter(adapter);
        }
        recyclerView.setAdapter(EndlessAdapter.wrap(adapter, loadMoreView));
        mAdapter = (EndlessAdapter) recyclerView.getAdapter();
    }

    public void refreshComplete() {
        mAdapter.setLoading(false);
        listener.setLoading(false);
        swipeRefreshLayout.setRefreshing(false);
    }

    public interface LoadMoreListener {
        void onRefresh();

        void onLoadMore(int page);
    }
}
