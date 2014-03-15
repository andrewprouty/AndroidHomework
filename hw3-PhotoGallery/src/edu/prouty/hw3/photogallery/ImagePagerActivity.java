package edu.prouty.hw3.photogallery;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import edu.prouty.hw3.photogallery.GalleryDatabaseHelper.PhotoCursor;

public class ImagePagerActivity extends FragmentActivity {
	private static final String TAG = "ImagePagerActivity";
	private static ArrayList<PhotoItem> mFetchPhotos;
	private static ArrayList<PhotoItem> mDisplayItems = new ArrayList<PhotoItem>();
	//private static ArrayList<PhotoItem> mDisplayItem;
	private static PhotoItem mFetchItem = new PhotoItem();
	private static String mAsyncLoad;
	private UserItem mUserItem = new UserItem();
	private MyAdapter mAdapter;
	private ViewPager mViewPager;

	private GalleryDatabaseHelper mHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");

		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);

		String uId   = getIntent().getStringExtra("UserId");
		String uName = getIntent().getStringExtra("UserName");
		int position = getIntent().getIntExtra ("position",0);
		Log.d(TAG, "onCreate().position: "+position);

		initUserItem(uId, uName);
		mHelper = new GalleryDatabaseHelper(getApplicationContext());
		mFetchPhotos=fetchPhotoItemsforUserId(mUserItem);
  
		mAsyncLoad=position+":"; 
		mFetchItem=mFetchPhotos.get(position);
		Log.d(TAG, "onCreate() mFetchItem: "
				+ mFetchItem.getUserId() + "-"
				+ mFetchItem.getUserName() + "; "
				+ mFetchItem.getPhotoId() + "-"
				+ mFetchItem.getPhotoName());

		mAdapter = new MyAdapter(getSupportFragmentManager());

		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(position);

		//FragmentManager fm = getSupportFragmentManager();
		/*
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return mFetchItems.size();
			}
			@Override
			public Fragment getItem(int pos) {
				mFetchItem=mFetchItems.get(pos);
				Log.d(TAG, "setAdapter().getItem ["+pos+"] "
						+ mFetchItem.getPhotoId() + "-"
						+ mFetchItem.getPhotoName());
				return new ImageFragment();
			}
		});*/

		/*
		mViewPager.setOnPageChangeListener(new OnPageChangeListener () {
			  @Override
		        public void onPageSelected(int arg0) {
					Log.d(TAG, "onChangeListener().Selected mFetchItem: "
									+ mFetchItem.getUserId() + "-"
									+ mFetchItem.getUserName() + "; "
									+ mFetchItem.getPhotoId() + "-"
									+ mFetchItem.getPhotoName());
		        }
		        @Override
		        public void onPageScrolled(int arg0, float arg1, int arg2) {
					Log.d(TAG, "onChangeListener().Scrolled mFetchItem: "
							+ mFetchItem.getUserId() + "-"
							+ mFetchItem.getUserName() + "; "
							+ mFetchItem.getPhotoId() + "-"
							+ mFetchItem.getPhotoName());
		        }

		        @Override
		        public void onPageScrollStateChanged(int arg0) {
					Log.d(TAG, "onChangeListener().State mFetchItem: "
							+ mFetchItem.getUserId() + "-"
							+ mFetchItem.getUserName() + "; "
							+ mFetchItem.getPhotoId() + "-"
							+ mFetchItem.getPhotoName());
		        }
		});*/
		//duplicate mHelper = new GalleryDatabaseHelper(getApplicationContext());
	}

	public static class MyAdapter extends FragmentStatePagerAdapter {
		public MyAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return mFetchPhotos.size();
		}

		@Override
		public Fragment getItem(int pos) {
			mFetchItem=mFetchPhotos.get(pos);
			setDisplayItem(pos, mFetchPhotos.get(pos));
			Log.i(TAG, "setAdapter().getItem ["+pos+"] "
					+ "("+mAsyncLoad+")"
					+ mFetchItem.getPhotoId() + "-"
					+ mFetchItem.getPhotoName());
			//return new ImageFragment().init(pos);
			return new ImageFragment();
		}
	}

	public void initPhotoItem (String uId, String uName, String pId, String pName) {
		mFetchItem.setUserId(uId);
		mFetchItem.setUserName(uName);
		mFetchItem.setPhotoId(pId);
		mFetchItem.setPhotoName(pName);
		Log.d(TAG, "initPhotoItem(): "
				+ mFetchItem.getUserId() + "-"
				+ mFetchItem.getUserName() + "; "
				+ mFetchItem.getPhotoId() + "-"
				+ mFetchItem.getPhotoName());
	}
	public void initUserItem (String uId, String uName) {
		mUserItem.setUserId(uId);
		mUserItem.setUserName(uName);
		Log.d(TAG, "initUserItem(): "
				+ mUserItem.getUserId() + "-"
				+ mUserItem.getUserName());
	}

	public PhotoItem popDisplayItem() {
		Log.d(TAG, "popDisplayItem() queue depth: "+mDisplayItems.size());
		PhotoItem item = mDisplayItems.get(0);
		mDisplayItems.remove(0);
		return item;
	}
	public static void setDisplayItem(int pos, PhotoItem item) {
		mAsyncLoad=mAsyncLoad+","+pos;
		Log.d(TAG, "setDisplayItem() Pos["+pos+"]Photo: "+mAsyncLoad);
		//PhotoItem photo = new PhotoItem();
		//photo=item;
		mDisplayItems.add(item);
        for (int i=0; i<mDisplayItems.size(); i++) {
    		item=mDisplayItems.get(i);
    		Log.d(TAG, "setDisplayItem() Pos["+pos+"]Photo ["+i+"]:"
					+ item.getUserId() + "-"
					+ item.getUserName() + "; "
					+ item.getPhotoId() + "-"
					+ item.getPhotoName());
        }
		Log.d(TAG, "setDisplayItem() Pos["+pos+"]Size: "+mDisplayItems.size());
		return;
	}
	protected ArrayList<PhotoItem> fetchPhotoItemsforUserId(UserItem user) {
		PhotoCursor cursor;
		Log.d(TAG, "fetchPhotoItemsforUser() UserId: "+user.getUserId());
		ArrayList<PhotoItem> items = new ArrayList<PhotoItem>();
		cursor = mHelper.queryPhotosForUserId(user.getUserId());
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			PhotoItem item = cursorToPhotoItem(cursor);
			items.add(item);
			cursor.moveToNext();
			Log.d(TAG, "fetchPhotoItemsforUser() user: "
					+ item.getUserId() + "-"
					+ item.getUserName() + "; "
					+ item.getPhotoId() + "-"
					+ item.getPhotoName());
		}
		cursor.close();
        mHelper.close();
		return items;
	}
	private PhotoItem cursorToPhotoItem(PhotoCursor cursor) {
		PhotoItem item = new PhotoItem();
		item.setPhotoId(cursor.getString(0));
		item.setPhotoName(cursor.getString(1));
		item.setUserId(cursor.getString(2));
		item.setUserName(cursor.getString(3));
		return item;
	}
}
