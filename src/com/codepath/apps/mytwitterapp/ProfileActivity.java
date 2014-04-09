package com.codepath.apps.mytwitterapp;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mytwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {
    public static final String SELF_PROFILE_PARAM = "selfProfile";
    public static final String USER_ID_PARAM = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        long userId;
        // populate profile information
        // use cached current user if self profile requested
        if ( getIntent().getBooleanExtra(SELF_PROFILE_PARAM, false) ) {
            userId = MyTwitterApp.getCurrentUser().getUserId();
            populateProfileInfo(MyTwitterApp.getCurrentUser());
        }
        // otherwise query for user profile
        else {
            userId = getIntent().getLongExtra(USER_ID_PARAM, -1);
            if ( -1 == userId )
                Log.e("kuoj", "User ID was not passed in to ProfileActivity");
            MyTwitterApp.getRestClient().getUserInfoById(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject json) {
                    User u = User.fromJson(json);
                    populateProfileInfo(u);
                }
            }, userId);
        }

        // load user timeline fragment
        Bundle args = new Bundle();
        args.putLong(UserTimelineFragment.USER_ID_KEY, Long.valueOf(userId));
        Fragment userTimeline = Fragment.instantiate(this, UserTimelineFragment.class.getName(), args);
        assert userTimeline != null;
        getSupportFragmentManager().beginTransaction().add(R.id.frame_timeline, userTimeline).commit();
    }

    private void populateProfileInfo(User u) {
        if ( null != u ) {
            //ImageView ivProfileBanner = (ImageView) findViewById(R.id.ivProfileBanner);
            ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
            TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
            TextView tvUserTagLine = (TextView) findViewById(R.id.tvUserTagLine);
            TextView tvUserUrl = (TextView) findViewById(R.id.tvUserUrl);
            TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
            TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

            /* TODO: figure out banner stuff
            // load banner image
            if ( !u.getProfileBannerUrl().isEmpty() ) {
                String mobileBannerUrl = u.getProfileBannerUrl().concat("/mobile");
                Log.i("kuoj-profile", "Mobile profile banner URL = " + mobileBannerUrl);
                //ImageLoader.getInstance().displayImage(u.getProfileBannerUrl(), ivProfileBanner);                
            }
            */
            // construct "bigger" size image URL
            String biggerUrl = u.getProfileImageUrl().replace("_normal.png", "_bigger.png");
            Log.i("kuoj-profile", "Bigger profile image URL = " + biggerUrl);
            ImageLoader.getInstance().displayImage(biggerUrl, ivProfileImage);
            tvUserName.setText(u.getName());
            if ( u.hasTagLine() ) {
                tvUserTagLine.setText(u.getTagLine());
                tvUserTagLine.setVisibility(View.VISIBLE);
            }
            if ( u.hasUrl() ) {
                tvUserUrl.setText(u.getUrl());
                tvUserUrl.setVisibility(View.VISIBLE);
            }
            tvFollowers.setText(u.getFollowersCount() + " followers");
            tvFollowing.setText(u.getFriendsCount() + " following");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

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
}
