package com.codepath.apps.mytwitterapp;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.mytwitterapp.models.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public abstract class TweetsListFragment extends Fragment {
    private TweetsAdapter adapter = null;
    private PullToRefreshListView lvTweets;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchTweets();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tweets_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        adapter = new TweetsAdapter(getActivity(), tweets);
        lvTweets = (PullToRefreshListView) getActivity().findViewById(R.id.lvTweets);
        lvTweets.setAdapter(adapter);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getNextOldestTweets();
            }
        });
        lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("kuoj", "Refreshing list of tweets");
                fetchTweets();
            }
        });
    }

    public TweetsAdapter getAdapter() {
        return adapter;
    }

    public ListView getListView() {
        return lvTweets;
    }

    // for endless scrolling
    protected abstract void getNextOldestTweets();

    // for pull-to-refresh
    protected abstract void fetchTweets();
}
