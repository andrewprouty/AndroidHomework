package edu.prouty.hw3.photogallery;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class ImagePagerActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private ArrayList<PhotoItem> mPhotoItems;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		mPhotoItems = Album.get(this).getPhotoItems();
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return mPhotoItems.size();
			}
			@Override
			public Fragment getItem(int pos) {
				PhotoItem photoItem = mPhotoItems.get(pos);
				return ImageFragment.newInstance(photoItem.getPhotoId());
			}
		}); 

	}
}
