package com.codepath.apps.mytwitterapp;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.mytwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {
    public static final int POST_TWEET_REQUEST = 1648;

    private TweetsAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        MyTwitterApp.getRestClient().getHomeTimeLine(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray jsonTweets) {
                Log.d("kuoj", jsonTweets.toString());
                ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
                adapter = new TweetsAdapter(getBaseContext(), tweets);
                ListView lvTweets = (ListView) findViewById(R.id.lvTweets);
                lvTweets.setAdapter(adapter);
                lvTweets.setOnScrollListener(new EndlessScrollListener() {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        getNextOldestTweets();
                    }
                });
            }
        });
    }

    /**
     * Returns next page of tweets.
     * Because Twitter returns results in reverse chronological order, the
     * last tweet in the ArrayAdapter has the lowest ID, which is upper
     * bound for the next page's results. 
     */
    private void getNextOldestTweets() {
        long lowestId = adapter.getItem(adapter.getCount() - 1).getTweetId();
        MyTwitterApp.getRestClient().getHomeTimeLineWithMaxId(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray jsonTweets) {
                Log.d("kuoj", jsonTweets.toString());
                ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
                adapter.addAll(tweets);
            }
        }, lowestId - 1);
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
}
