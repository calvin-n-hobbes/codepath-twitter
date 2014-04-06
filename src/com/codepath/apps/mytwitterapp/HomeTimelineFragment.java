package com.codepath.apps.mytwitterapp;

import java.util.ArrayList;

import org.json.JSONArray;

import com.codepath.apps.mytwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.util.Log;
import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class HomeTimelineFragment extends TweetsListFragment {
    /**
     * Asynchronously returns tweets on the home timeline.
     * This is called for the first time as well as subsequent refreshes, but not for scrolling/pagination updates.
     */
    @Override
    protected void fetchTweets() {
        MyTwitterApp.getRestClient().getHomeTimeLine(refreshHandler);
    }

    /**
     * Returns next page of tweets.
     * Because Twitter returns results in reverse chronological order, the
     * last tweet in the ArrayAdapter has the lowest ID, which is upper
     * bound for the next page's results.
     */
    @Override
    protected void getNextOldestTweets() {
        // avoid ArrayOutOfBoundsException
        if ( getAdapter().isEmpty() ) return;

        long lowestId = getAdapter().getItem(getAdapter().getCount() - 1).getTweetId();
        MyTwitterApp.getRestClient().getHomeTimeLineWithMaxId(scrollHandler, lowestId - 1);
    }

    // JSON response handler for refresh case (clear adapter before enqueuing tweets)
    private JsonHttpResponseHandler refreshHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(JSONArray jsonTweets) {
            clearAndAddTweets(jsonTweets);
            ((PullToRefreshListView) getListView()).onRefreshComplete();
        }

        @Override
        public void onFailure(Throwable e) {
            Log.e("kuoj", "Error in JSON refresh handler");
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
            Log.e("kuoj", "Error in JSON scroll handler");
            e.printStackTrace();
        }
    };

    private void clearAndAddTweets(JSONArray jsonTweets) {
        getAdapter().clear();
        addTweets(jsonTweets);
    }

    private void addTweets(JSONArray jsonTweets) {
        Log.d("kuoj", jsonTweets.toString());
        ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
        getAdapter().addAll(tweets);        
    }
}
