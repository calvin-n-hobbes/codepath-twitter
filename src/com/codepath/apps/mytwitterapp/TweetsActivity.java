package com.codepath.apps.mytwitterapp;

import com.codepath.apps.mytwitterapp.models.Tweet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class TweetsActivity extends FragmentActivity {
    public static final int POST_TWEET_REQUEST = 1648;

    TweetsListFragment fragmentTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);

        /*
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }
        */

        fragmentTweets = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentHomeTweets);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tweets, menu);
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
                fragmentTweets.getAdapter().insert(tw, 0);
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
