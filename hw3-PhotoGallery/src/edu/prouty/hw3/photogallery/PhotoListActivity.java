package edu.prouty.hw3.photogallery;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import edu.prouty.hw3.photogallery.GalleryDatabaseHelper.PhotoCursor;

public class PhotoListActivity extends FragmentActivity
	implements ImageFragment.Callbacks {
	private static final String TAG = "PhotoListActivity";
	private UserItem mUserItem = new UserItem();
	private static ArrayList<PhotoItem> mPhotoItems;
	//private boolean mTwoPane;

	private GalleryDatabaseHelper mHelper;

	protected void launchPhotoDisplayActivity(PhotoItem photo, int position) {
		if (findViewById(R.id.imageFragmentContainer) == null) {
			Log.d(TAG, "launchPhotoDisplayActivity() ["+position+"] via Activity (phone)");
			Intent i = new Intent (PhotoListActivity.this, ImagePagerActivity.class);
			i.putExtra("UserId",   photo.getUserId().toString());
			i.putExtra("UserName", photo.getUserName().toString());
			i.putExtra("position", position);
			Log.d(TAG, "launchPhotoDisplayActivity(): "
					+ photo.getUserId() + "-"
					+ photo.getUserName() + "; "
					+ photo.getPhotoId() + "-"
					+ photo.getPhotoName());
			startActivity(i);
		}
		else {
			Log.d(TAG, "launchPhotoDisplayActivity() ["+position+"] via Fragment (tablet)");
    		FragmentManager fm = getSupportFragmentManager();
    			Log.d(TAG, "launchPhotoDisplayActivity() 3");
    		FragmentTransaction ft = fm.beginTransaction();
    			Log.d(TAG, "launchPhotoDisplayActivity() 4");
            //Fragment fragment = fm.findFragmentById(R.id.fragmentImage);
    		Fragment fragment = new ImageFragment().init(position);
				Log.d(TAG, "launchPhotoDisplayActivity() about to...");
            ft.add(R.id.imageFragmentContainer, fragment);
				Log.d(TAG, "launchPhotoDisplayActivity() added");
            ft.commit();
				Log.d(TAG, "launchPhotoDisplayActivity() committed");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_masterdetail);
		Log.d(TAG, "onCreate()");

		String id = getIntent().getStringExtra("UserId");
		String name = getIntent().getStringExtra("UserName");
		initUserItem(id, name);

		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

		if (fragment == null) {
			fragment = new PhotoListFragment();
			fm.beginTransaction()
			.add(R.id.fragmentContainer, fragment)
			.commit();
		}
        mHelper = new GalleryDatabaseHelper(getApplicationContext());
	}

	private void initUserItem (String id, String name) {
		mUserItem.setUserId(id);
		mUserItem.setUserName(name);
		Log.d(TAG, "initUserItem() user: "
				+ mUserItem.getUserId() + "-"
				+ mUserItem.getUserName());
	}
	public UserItem getUserItem () {
		return mUserItem;
	}
	
	public PhotoItem getPhotoItem(int pos) {
		Log.d(TAG, "getDisplayItem() ["+pos+"] size:"+mPhotoItems.size());
		PhotoItem item = mPhotoItems.get(pos);
		return item;
	}
	
	public void setPhotoItems(ArrayList<PhotoItem> items) {
		mPhotoItems = items;
		Log.d(TAG, "setPhotoItems() in:"+items.size()+" set:"+mPhotoItems.size());
		return;
	}
	
	protected void insertPhotoItems(ArrayList<PhotoItem> items, UserItem user) {
		PhotoItem item;
		Log.d(TAG, "insertPhotoItems() user:"+user.getUserId()+"-"+user.getUserName());
		mHelper.deletePhotosforUserId(user.getUserId());
		for (int i=0; i<items.size(); i++) {
			item=items.get(i);
			Log.d(TAG, "insertPhotoItems() user: "
					+ item.getUserId() + "-"
					+ item.getUserName() + "; "
					+ item.getPhotoId() + "-"
					+ item.getPhotoName());
			mHelper.insertPhoto(item);
		}
		mHelper.close();
		return;
	    }	
	
	protected ArrayList<PhotoItem> queryPhotoItemsforUserId(UserItem user) {
		PhotoCursor cursor;
		ArrayList<PhotoItem> items = new ArrayList<PhotoItem>();
		cursor = mHelper.queryPhotosForUserId(user.getUserId());
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			PhotoItem item = cursor.getPhotoItem();
			items.add(item);
			cursor.moveToNext();
			Log.d(TAG, "queryPhotoItemsforUserId(): "
					+ item.getUserId() + "-"
					+ item.getUserName() + "; "
					+ item.getPhotoId() + "-"
					+ item.getPhotoName());
		}
    	cursor.close();
        mHelper.close();
		return items;
	}
}