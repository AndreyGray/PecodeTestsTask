package com.example.pecodetesttask;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;


import com.example.pecodetesttask.Interface.onEventListener;
import com.example.pecodetesttask.adapter.MyViewPagerAdapter;
import com.example.pecodetesttask.fragments.BlankFragment;
import com.fxn.stash.Stash;

import java.util.ArrayList;


import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.example.pecodetesttask.fragments.BlankFragment.ARGUMENT_PAGE_NUMBER;

public class MainActivity extends FragmentActivity implements onEventListener {

    private static final String TAG = "MainActivity";

    private static ArrayList<BlankFragment> mFragmentList = new ArrayList<>();
    public static final String TAG_FOR_PAGES = "TAG_FOR_PAGES";
    public static final String NUM_PAGE = "NumPage";

    ViewPager mPager;
    MyViewPagerAdapter mAdapter;
    int page = 0;
    int notiPage = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPager = findViewById(R.id.view_pager);
    }


    @Override
    protected void onResume() {
        super.onResume();

        notiPage = getIntent().getIntExtra(NUM_PAGE, -1);

        if (notiPage != -1) {
            Log.d(TAG, "onResume: Bundle == null");
            mFragmentList = new ArrayList<>();
            mFragmentList.addAll(createNotificationList());
            page = getIndex(notiPage);//notiPage;
        } else {
            Log.d(TAG, "onResume: Bundle not null");
            mFragmentList.add(BlankFragment.newInstance(0));
        }

        updateUI();

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveDb(mFragmentList);
    }

    private ArrayList<BlankFragment> createNotificationList() {
        ArrayList<Integer> pageNums = Stash.getArrayList(TAG_FOR_PAGES, Integer.class);
        ArrayList<BlankFragment> result = new ArrayList<>();
        for (Integer page : pageNums) {
            result.add(BlankFragment.newInstance(page));
        }
        return result;
    }

    private void saveDb(ArrayList<BlankFragment> mFragmentList) {
        ArrayList<Integer> pageNums = new ArrayList<>();
        for (BlankFragment fragment : mFragmentList) {
            pageNums.add(fragment.getPageNumber());
        }
        Stash.put(TAG_FOR_PAGES, pageNums);
    }

    private int getIndex(int page) {
        int index = 0;
        for (int i = 0; i < mFragmentList.size(); i++) {
            if (mFragmentList.get(i).getArguments().getInt(ARGUMENT_PAGE_NUMBER) == page) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void onEvent(int pos, String buttonName) {
        if (buttonName.equals("plus")) {
            mFragmentList.add(BlankFragment.newInstance(getMaxIndex()));
            mAdapter.notifyDataSetChanged();
            mPager.setCurrentItem(mAdapter.getCount() - 1);
        } else {
            int currentItemMinusOne = mPager.getCurrentItem() - 1;
            mFragmentList.remove(mPager.getCurrentItem());
            mAdapter.notifyDataSetChanged();
            mPager.setAdapter(mAdapter);
            mPager.setCurrentItem(currentItemMinusOne);
        }
    }

    private int getMaxIndex() {
        int maxIndex = mFragmentList.get(0).getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        for (int i = 1; i < mFragmentList.size(); i++) {
            if (mFragmentList.get(i).getArguments().getInt(ARGUMENT_PAGE_NUMBER) > maxIndex) {
                maxIndex = mFragmentList.get(i).getArguments().getInt(ARGUMENT_PAGE_NUMBER);
            }
        }
        return maxIndex + 1;
    }

    public void updateUI() {
        mAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mFragmentList);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(page);
        mPager.setOffscreenPageLimit(0);
    }

}