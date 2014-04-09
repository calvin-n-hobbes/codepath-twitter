package com.codepath.apps.mytwitterapp;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class FragmentTabListener<T extends Fragment> implements TabListener {
    private Fragment mFragment;
    private final FragmentActivity mActivity;
    private final String mTag;
    private final Class<T> mClass;
    private final int mfragmentContainerId;
    private final Bundle mArgs;

    // Replaces the entire activity content area
    // Usage: new FragmentTabListener<SomeFragment>(this, "first", SomeFragment.class, new Bundle())
    public FragmentTabListener(FragmentActivity activity, String tag, Class<T> clazz, Bundle args) {
        mActivity = activity;
        mTag = tag;
        mClass = clazz;
        mArgs = args;
        mfragmentContainerId = android.R.id.content;
    }

    public FragmentTabListener(FragmentActivity activity, String tag, Class<T> clazz) {
        this(activity, tag, clazz, (Bundle) null);
    }

    // Replaces the specified container with fragment content
    // Usage: new FragmentTabListener<SomeFragment>(R.id.flContent, this, "first", SomeFragment.class)
    public FragmentTabListener(int fragmentContainerId, FragmentActivity activity, String tag, Class<T> clazz, Bundle args) {
        mActivity = activity;
        mTag = tag;
        mClass = clazz;
        mArgs = args;
        mfragmentContainerId = fragmentContainerId;
    }

    public FragmentTabListener(int fragmentContainerId, FragmentActivity activity, String tag, Class<T> clazz) {
        this(fragmentContainerId, activity, tag, clazz, (Bundle) null);
    }

    /* ActionBar.TabListener callbacks */

    public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
        FragmentTransaction sft = mActivity.getSupportFragmentManager().beginTransaction();
        // Check if the fragment is already initialized
        if (mFragment == null) {
            // If not, instantiate and add it to the activity
            mFragment = Fragment.instantiate(mActivity, mClass.getName());
            sft.add(mfragmentContainerId, mFragment, mTag);
        }
        else {
            // If it exists, simply attach it in order to show it
            sft.attach(mFragment);
        }
        sft.commit();
    }

    public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
        FragmentTransaction sft = mActivity.getSupportFragmentManager().beginTransaction();
        if ( null != mFragment ) {
            // Detach the fragment, because another one is being attached
            sft.detach(mFragment);
        }
        sft.commit();
    }

    public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
        // User selected the already selected tab. Usually do nothing.
    }
}
