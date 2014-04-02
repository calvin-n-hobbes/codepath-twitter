package com.codepath.apps.mytwitterapp;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mytwitterapp.models.Tweet;
import com.codepath.apps.mytwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeActivity extends Activity {
    private EditText etTweet;
    private TextView tvLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        // unpack current user
        User me = MyTwitterApp.getCurrentUser();
        assert me != null;

        ImageView ivProfile = (ImageView) findViewById(R.id.ivCurrentUserProfile);
        TextView tvName = (TextView) findViewById(R.id.tvCurrentUserName);
        TextView tvHandle = (TextView) findViewById(R.id.tvCurrentUserHandle);
        tvLength = (TextView) findViewById(R.id.tvTweetLength);
        etTweet = (EditText) findViewById(R.id.etTweet);

        ImageLoader.getInstance().displayImage(me.getProfileImageUrl(), ivProfile);
        tvName.setText(me.getName());
        tvHandle.setText(me.getHandle());

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int remaining = 140 - s.length();
                String remainStr = String.format(getResources().getString(R.string.tweet_chars_remaining), remaining);
                tvLength.setText(remainStr);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });

        String initialRemainStr = String.format(getResources().getString(R.string.tweet_chars_remaining), 140);
        tvLength.setText(initialRemainStr);
    }

    /**
     * Posts tweet and finishes the activity when the user clicks the "Tweet" button
     */
    public void onPost(MenuItem item) {
        Log.i("kuoj", "Posting tweet");
        MyTwitterApp.getRestClient().postTweet(etTweet.getText().toString(), new JsonHttpResponseHandler() {
            public void onSuccess(JSONObject json) {
                Log.i("kuoj", "Posted tweet");
                // create new Tweet object out of response and pass it back to calling activity
                Tweet newTweet = new Tweet(json);
                Intent data = new Intent();
                data.putExtra("newTweet", newTweet);
                ComposeActivity.this.setResult(RESULT_OK, data);
                ComposeActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose, menu);
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
