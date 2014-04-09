package com.codepath.apps.mytwitterapp;

import android.util.Log;

public class UserTimelineFragment extends TweetsListFragment {
    public static final String USER_ID_KEY = "userId";

    private long userId = -1;

    @Override
    protected void fetchTweets() {
        if ( userId == -1 ) getUserId();

        Log.d("kuoj-user-timeline", "Fetching tweets for user ID " + userId);
        MyTwitterApp.getRestClient().getUserTimeline(userId, this.getRefreshHandler());
    }

    @Override
    protected void getNextOldestTweets() {
        TweetsAdapter adapter = this.getAdapter();

        // avoid ArrayOutOfBoundsException
        if ( adapter.isEmpty() ) return;

        if ( userId == -1 ) getUserId();
        long lowestId = adapter.getItem(adapter.getCount() - 1).getTweetId();        
        MyTwitterApp.getRestClient().getUserTimelineWithMaxId(this.userId, this.getScrollHandler(), lowestId - 1);
    }

    private void getUserId() {
        if ( null != getArguments() ) {
            this.userId = ((Long) getArguments().getLong(USER_ID_KEY)).longValue();
        }        
        else {
            Log.e("kuoj-user-timeline-fragment", "No argument bundle was passed in to UserTimelineFragment");
        }
    }
}
