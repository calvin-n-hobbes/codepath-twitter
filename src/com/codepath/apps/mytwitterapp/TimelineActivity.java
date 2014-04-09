package com.codepath.apps.mytwitterapp;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.codepath.apps.mytwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends Activity {
    public static final int POST_TWEET_REQUEST = 1648;

    private ArrayAdapter<Tweet> adapter = null;
    private PullToRefreshListView lvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        adapter = new TweetsAdapter(getBaseContext());
        lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
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
                Log.d("kuoj", "Refreshing timeline");
                fetchTimelineAsync();
            }
        });

        // call endpoint to retrieve the timeline for the first time
        fetchTimelineAsync();
    }

    /**
     * Returns next page of tweets.
     * Because Twitter returns results in reverse chronological order, the
     * last tweet in the ArrayAdapter has the lowest ID, which is upper
     * bound for the next page's results.
     */
    private void getNextOldestTweets() {
        // avoid ArrayOutOfBoundsException
        if ( adapter.isEmpty() ) return;

        long lowestId = adapter.getItem(adapter.getCount() - 1).getTweetId();
        MyTwitterApp.getRestClient().getHomeTimelineWithMaxId(scrollHandler, lowestId - 1);
    }

    /**
     * Asynchronously returns tweets on the home timeline.
     * This is called for the first time as well as subsequent refreshes, but not for scrolling/pagination updates.
     */
    private void fetchTimelineAsync() {
        MyTwitterApp.getRestClient().getHomeTimeline(refreshHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    public void onCompose(MenuItem item) {
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, POST_TWEET_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( POST_TWEET_REQUEST == requestCode ) {
            // show the new tweet in the timeline immediately after posting
            if ( RESULT_OK == resultCode ) {
                // Option 1: refresh the timeline
                /*
                Log.i("kuoj", "Refreshing the timeline");
                MyTwitterApp.getRestClient().getHomeTimeLine(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONArray jsonTweets) {
                        ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
                        adapter.clear();
                        adapter.addAll(tweets);
                    }
                });
                */

                // Option 2: inject the new tweet into the list adapter
                Log.i("kuoj", "Added new tweet to timeline list adapter");
                Tweet tw = (Tweet) data.getExtras().getSerializable("newTweet");
                adapter.insert(tw, 0);
            }
            else if ( RESULT_CANCELED == resultCode ) {
                Log.i("kuoj", "Compose canceled");
            }
        }
    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/

    // JSON response handler for refresh case (clear adapter before enqueuing tweets)
    private JsonHttpResponseHandler refreshHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(JSONArray jsonTweets) {
            clearAndAddTweets(jsonTweets);
            lvTweets.onRefreshComplete();
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
        adapter.clear();
        addTweets(jsonTweets);
    }

    private void addTweets(JSONArray jsonTweets) {
        Log.d("kuoj", jsonTweets.toString());
        ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
        adapter.addAll(tweets);        
    }
}
