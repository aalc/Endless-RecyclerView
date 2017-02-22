package com.github.ybq.endless_recyclerview;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.ybq.endless.Endless;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Integer> data;
    private Endless endless;
    private TextAdapter textAdapter;
    private AsyncTask asyncTask;

    SwipeRefreshLayout pullToRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        pullToRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_profile);

        pullToRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        pullToRefreshLayout.canChildScrollUp();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        View loadingView = View.inflate(this, R.layout.layout_loading, null);
        loadingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        endless = new Endless.Builder(recyclerView, loadingView)
                .pullToRefreshLayout(pullToRefreshLayout)
                .build();

        endless.setAdapter(
                textAdapter = new TextAdapter(),
                new Endless.LoadMoreListener() {
                    @Override
                    public void onRefresh() {
                        textAdapter.clearData();
                        loadData(0);
                    }

                    @Override
                    public void onLoadMore(int page) {
                        loadData(page);
                    }
                });

        loadData(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
    }

    private void loadData(final int page) {
        asyncTask = new AsyncTask<Integer, Integer, List<Integer>>() {
            @Override
            protected List<Integer> doInBackground(Integer[] integers) {
                if (page != 0) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return DataProvider.request(integers[0], 28);
            }

            @Override
            protected void onPostExecute(List<Integer> integers) {
                data = integers;
                if (page == 0) {
                    textAdapter.setData(data);
                    endless.refreshComplete();
                } else {
                    textAdapter.addData(data);
                    endless.loadMoreComplete();

                }
                super.onPostExecute(integers);
            }
        }.execute(textAdapter.getLastId());
    }
}
