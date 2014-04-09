package com.codepath.apps.mytwitterapp;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.mytwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public abstract class TweetsListFragment extends Fragment {
    private TweetsAdapter adapter = null;
    private PullToRefreshListView lvTweets = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchTweets();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        // initialize ListView
        // NOTE: the adapter is not available the very first time until onActivityCreated because it requires a context
        Log.d("kuoj-tweets-list", "Initializing ListView");
        lvTweets = (PullToRefreshListView) v.findViewById(R.id.lvTweets);
        if ( null != adapter )
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
                Log.d("kuoj-tweets-list", "Refreshing list of tweets");
                fetchTweets();
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // initialize adapter
        if ( null == adapter ) {
            Log.d("kuoj-tweets-list", "Initializing new adapter");
            adapter = new TweetsAdapter(getActivity());
            // set adapter on the tweets list view, created in onCreateView
            if ( null != lvTweets )
                lvTweets.setAdapter(adapter);
        }
    }

    /* getters */
    public TweetsAdapter getAdapter() { return adapter; }
    public ListView getListView() { return lvTweets; }
    public JsonHttpResponseHandler getRefreshHandler() { return refreshHandler; }
    public JsonHttpResponseHandler getScrollHandler() { return scrollHandler; }

    /* abstract methods */
    protected abstract void fetchTweets(); // for initial loading and pull-to-refresh
    protected abstract void getNextOldestTweets(); // for endless scrolling

    // JSON response handler for refresh case (clear adapter before enqueuing tweets)
    private JsonHttpResponseHandler refreshHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(JSONArray jsonTweets) {
            clearAndAddTweets(jsonTweets);
            lvTweets.onRefreshComplete();
        }

        @Override
        public void onFailure(Throwable e) {
            Log.e("kuoj-tweets-list", "Error in JSON refresh handler");
            e.printStackTrace();
        }
    };

    // JSON response handler for endless scroll case (enqueue tweets without clearing the adapter)
    private JsonHttpResponseHandler scrollHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(JSONArray jsonTweets) {
            addTweets(jsonTweets);
        }        

        @Override
        public void onFailure(Throwable e) {
            Log.e("kuoj-tweets-list", "Error in JSON scroll handler");
            e.printStackTrace();
        }
    };

    private void clearAndAddTweets(JSONArray jsonTweets) {
        getAdapter().clear();
        addTweets(jsonTweets);
    }

    private void addTweets(JSONArray jsonTweets) {
        Log.v("kuoj-tweets-list", jsonTweets.toString());
        ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
        adapter.addAll(tweets);        
    }
}
