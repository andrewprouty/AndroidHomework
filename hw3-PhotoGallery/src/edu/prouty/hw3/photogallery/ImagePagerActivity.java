package edu.prouty.hw3.photogallery;

import java.util.ArrayList;

import edu.prouty.hw3.photogallery.GalleryDatabaseHelper.UserCursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class ImagePagerActivity extends FragmentActivity {

	private static final String TAG = "ImagePagerActivity";
	private PhotoItem mPhotoItem = new PhotoItem();
	private ViewPager mViewPager;

	private GalleryDatabaseHelper mHelper;

	//	private ArrayList<PhotoItem> mPhotoItems;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");

		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);

		String uId = getIntent().getStringExtra("UserId");
		String uName = getIntent().getStringExtra("UserName");
		String pId = getIntent().getStringExtra("PhotoId");
		String pName = getIntent().getStringExtra("PhotoName");
		final int mPhotosCount = getIntent().getIntExtra("PhotosCount",0);

		initPhotoItem(uId, uName, pId, pName);

		//mPhotoItems = Album.get(this).getPhotoItems();

		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return mPhotosCount;
			}
			@Override
			public Fragment getItem(int pos) {
				Log.d(TAG, "setAdapter().getItem: "+pos);
				mPhotoItem.setPhotoName(mPhotoItem.getPhotoName()+pos);
				//PhotoItem photoItem = mPhotoItems.get(pos);
				//return ImageFragment.newInstance(photoItem.getPhotoId());
				return new ImageFragment();
			}
		});
        mHelper = new GalleryDatabaseHelper(getApplicationContext());
	}
	public void initPhotoItem (String uId, String uName, String pId, String pName) {
		mPhotoItem.setUserId(uId);
		mPhotoItem.setUserName(uName);
		mPhotoItem.setPhotoId(pId);
		mPhotoItem.setPhotoName(pName);
		Log.d(TAG, "initPhotoItem(): "
				+ mPhotoItem.getUserId() + "-"
				+ mPhotoItem.getUserName() + "; "
				+ mPhotoItem.getPhotoId() + "-"
				+ mPhotoItem.getPhotoName());
	}

	public PhotoItem getPhotoItem () {
		return mPhotoItem;
	}
	////  SWITCH TO PHOTO LIST !!
    protected ArrayList<UserItem> fetchUserItems() {
    	UserCursor cursor;
    	ArrayList<UserItem> items = new ArrayList<UserItem>();
    	cursor = mHelper.queryUsers();
    	cursor.moveToFirst();
    	while(!cursor.isAfterLast()) {
    		UserItem item = cursorToUserItem(cursor);
    		items.add(item);
    		cursor.moveToNext();
    		Log.d(TAG, "fetchUserItem() user: "
    				+ item.getUserId() + "-"
    				+ item.getUserName());
    	}
    	return items;
    }
    private UserItem cursorToUserItem(UserCursor cursor) {
    	UserItem item = new UserItem();
    	item.setUserId(cursor.getString(0));   // TODO cursor.getInt
    	item.setUserName(cursor.getString(1));
		return item;
    }

}
