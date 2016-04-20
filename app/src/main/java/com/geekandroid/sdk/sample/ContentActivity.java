package com.geekandroid.sdk.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * date        :  2016-04-20  13:46
 * author      :  Mickaecle gizthon
 * description :
 */
public class ContentActivity extends AppCompatActivity {
    public static Fragment showFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        if (showFragment != null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, showFragment).commit();
        }
    }

    Fragment current;

    public void switchContent(Fragment from, Fragment to) {
        if (current != to) {
            current = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()/*.setCustomAnimations(
                    android.R.anim.fade_in, R.anim.fade_out)*/;
            if (!to.isAdded()) {
                transaction.hide(from).add(R.id.container, to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
    }
}
