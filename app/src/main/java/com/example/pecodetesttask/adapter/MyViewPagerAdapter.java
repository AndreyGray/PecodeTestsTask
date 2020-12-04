package com.example.pecodetesttask.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.pecodetesttask.fragments.BlankFragment;

import java.util.List;

public class MyViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<BlankFragment> mFragmentList;

    public MyViewPagerAdapter(@NonNull FragmentManager fm, int behavior, List<BlankFragment> mFragmentList) {
        super(fm, behavior);
        this.mFragmentList = mFragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
