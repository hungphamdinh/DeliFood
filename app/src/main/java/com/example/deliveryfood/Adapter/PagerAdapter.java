package com.example.deliveryfood.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PagerAdapter extends FragmentPagerAdapter {
  private int mNoOfTabs;
  private Context context;

  public PagerAdapter(FragmentManager fm, int NumberOfTabs, Context context)
  {
    super(fm);
    this.mNoOfTabs = NumberOfTabs;
    this.context=context;
  }


  @Override
  public Fragment getItem(int position) {

    Fragment fragment = null;
    switch(position)
    {

      case 0:
       // fragment  = new OrderFragment(context);

        break;
      //case 1:
        //fragment = new GeneratorQRFragment(context);
       // break;
      default:
        return null;
    }
    return fragment;
  }
  @Override
  public int getCount() {
    return mNoOfTabs;
  }

}
