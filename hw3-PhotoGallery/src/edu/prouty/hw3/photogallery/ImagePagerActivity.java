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
	private PhotoItem mPhotoItem = new PhotoItem();
	private UserItem mUserItem = new UserItem();
	private ArrayList<PhotoItem> mPhotoItems;

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

		String uId   = getIntent().getStringExtra("UserId");
		String uName = getIntent().getStringExtra("UserName");
		//String pId   = getIntent().getStringExtra("PhotoId");
		//String pName = getIntent().getStringExtra("PhotoName");
		int position = getIntent().getIntExtra ("position",0);

		initUserItem(uId, uName);
        mHelper = new GalleryDatabaseHelper(getApplicationContext());
		mPhotoItems=fetchPhotoItemsforUserId(mUserItem);

		Log.d(TAG, "onCreate().position: "+position);
		mPhotoItem=mPhotoItems.get(position);
		Log.d(TAG, "onCreate() mPhotoItem: "
				+ mPhotoItem.getUserId() + "-"
				+ mPhotoItem.getUserName() + "; "
				+ mPhotoItem.getPhotoId() + "-"
				+ mPhotoItem.getPhotoName());


		//final int mPhotosCount = getIntent().getIntExtra("PhotosCount",0);
		//TODO try dynamic set mPhotoItems.size();

		//initPhotoItem(uId, uName, pId, pName);

		//mPhotoItems = Album.get(this).getPhotoItems();

		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return mPhotoItems.size();
			}
			@Override
			public Fragment getItem(int pos) {
				Log.d(TAG, "setAdapter().getItem: "+pos);
				mPhotoItem=mPhotoItems.get(pos);
				//mPhotoItem.setPhotoName(mPhotoItem.getPhotoName()+pos);
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
	public void initUserItem (String uId, String uName) {
		mUserItem.setUserId(uId);
		mUserItem.setUserName(uName);
		Log.d(TAG, "initUserItem(): "
				+ mUserItem.getUserId() + "-"
				+ mUserItem.getUserName());
	}

	public PhotoItem getPhotoItem () {
		return mPhotoItem;
	}
	protected ArrayList<PhotoItem> fetchPhotoItemsforUserId(UserItem user) {
		PhotoCursor cursor;
		Log.d(TAG, "fetchPhotoItemsforUser() a");
		ArrayList<PhotoItem> items = new ArrayList<PhotoItem>();
		Log.d(TAG, "fetchPhotoItemsforUser() b userId: "+user.getUserId());
		cursor = mHelper.queryPhotosForUserId(user.getUserId());
		Log.d(TAG, "fetchPhotoItemsforUser() c");
		cursor.moveToFirst();
		Log.d(TAG, "fetchPhotoItemsforUser() d");
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
		Log.d(TAG, "fetchPhotoItemsforUser().");
		return items;
	}
	private PhotoItem cursorToPhotoItem(PhotoCursor cursor) {
		Log.d(TAG, "cursorToPhotoItem()");
		PhotoItem item = new PhotoItem();
		item.setPhotoId(cursor.getString(0));
		item.setPhotoName(cursor.getString(1));
		item.setUserId(cursor.getString(2));
		item.setUserName(cursor.getString(3));
		Log.d(TAG, "cursorToPhotoItem().");
		return item;
	}
	////  SWITCH TO PHOTO LIST !! TODO remove
/*    protected ArrayList<UserItem> fetchUserItems() {
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
    	item.setUserId(cursor.getString(0));
    	item.setUserName(cursor.getString(1));
		return item;
    }
*/
}
