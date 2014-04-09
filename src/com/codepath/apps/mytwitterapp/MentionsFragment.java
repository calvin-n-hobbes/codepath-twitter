package com.codepath.apps.mytwitterapp;

public class MentionsFragment extends TweetsListFragment {

    /**
     * Asynchronously returns tweets mentioning the user.
     * This is called for the first time as well as subsequent refreshes, but not for scrolling/pagination updates.
     */
    @Override
    protected void fetchTweets() {
        MyTwitterApp.getRestClient().getMentions(this.getRefreshHandler());
    }

    /**
     * Returns next page of tweets.
     * Because Twitter returns results in reverse chronological order, the
     * last tweet in the ArrayAdapter has the lowest ID, which is upper
     * bound for the next page's results.
     */
    @Override
    protected void getNextOldestTweets() {
        TweetsAdapter adapter = this.getAdapter();

        // avoid ArrayOutOfBoundsException
        if ( adapter.isEmpty() ) return;

        long lowestId = adapter.getItem(adapter.getCount() - 1).getTweetId();
        MyTwitterApp.getRestClient().getMentionsWithMaxId(this.getScrollHandler(), lowestId - 1);
    }
}
