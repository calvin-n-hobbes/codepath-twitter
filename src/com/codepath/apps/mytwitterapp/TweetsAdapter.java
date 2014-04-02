package com.codepath.apps.mytwitterapp;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mytwitterapp.models.Tweet;
import com.codepath.apps.mytwitterapp.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsAdapter extends ArrayAdapter<Tweet> {
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Tweet tweet = getItem(position);
        assert tweet != null;
        User user = tweet.getUser();
        assert user != null;

        // Check if an existing view is being reused, otherwise inflate the view
        if ( null == convertView ) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.tweet_item, null);
        }

        // Lookup views within item layout
        ImageView ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);

        // Populate the data into the template view using the data object
        ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfile);
        String formattedName = "<b>" + user.getName() + "</b> <small><font color=\"#777777\">@" + user.getHandle() + "</font></small>"; 
        tvName.setText(Html.fromHtml(formattedName));
        String ago = DateUtils.getRelativeTimeSpanString(tweet.getTimestamp().getTime(),
                Calendar.getInstance().getTimeInMillis(),
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        String formattedAgo = "<small><font color=\"#777777\">" + ago + "</font></small>";
        tvRelativeTime.setText(Html.fromHtml(formattedAgo));
        tvBody.setText(tweet.getText());

        return convertView;
    }
}
