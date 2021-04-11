package fr.homingpigeon.myapplication.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import fr.homingpigeon.myapplication.ConversationFragment;
import fr.homingpigeon.myapplication.FriendFragment;
import fr.homingpigeon.myapplication.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;
    private Bundle bundle;

    public SectionsPagerAdapter(Context context, FragmentManager fm,String token,String username) {
        super(fm);
        mContext = context;
        bundle = new Bundle();
        bundle.putString("token",token);
        bundle.putString("username",username);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = position == 0 ? new FriendFragment() : new ConversationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}