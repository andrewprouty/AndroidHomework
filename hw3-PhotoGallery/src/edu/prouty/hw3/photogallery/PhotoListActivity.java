package edu.prouty.hw3.photogallery;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import edu.prouty.hw3.photogallery.GalleryDatabaseHelper.PhotoCursor;

public class PhotoListActivity extends FragmentActivity {
	private static final String TAG = "PhotoListActivity";
	private UserItem mUserItem = new UserItem();
	//private ArrayList<PhotoItem> mPhotoItems;
	//private PhotoItem mPhotoItem;
	private GalleryDatabaseHelper mHelper;

	protected void launchPhotoDisplayActivity(PhotoItem photo, int position) {
		//mPhotoItem = photoItem;
		/* TODO remove Toast.makeText(this,mPhotoItem.getUserId() + "-"
						  + mPhotoItem.getUserName() + "; "
						  + mPhotoItem.getPhotoId() + "-"
						  + mPhotoItem.getPhotoName()
				,Toast.LENGTH_SHORT).show();*/
		Intent i = new Intent (PhotoListActivity.this, ImagePagerActivity.class);
		i.putExtra("UserId",   photo.getUserId().toString());
		i.putExtra("UserName", photo.getUserName().toString());
		//i.putExtra("PhotoId",  photo.getPhotoId().toString());
		//i.putExtra("PhotoName",photo.getPhotoName().toString());
		i.putExtra("position", position);
		//i.putExtra("PhotosCount",mPhotoItems.size());
		Log.d(TAG, "launchPhotoDisplayActivity() photo ["+position+"]: "
				+ photo.getUserId() + "-"
				+ photo.getUserName() + "; "
				+ photo.getPhotoId() + "-"
				+ photo.getPhotoName());
		startActivity(i);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
		Log.d(TAG, "onCreate()");

		String id = getIntent().getStringExtra("UserId");
		String name = getIntent().getStringExtra("UserName");
		initUserItem(id, name);

		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

		if (fragment == null) {
			fragment = createFragment();
			manager.beginTransaction()
			.add(R.id.fragmentContainer, fragment)
			.commit();
		}
        mHelper = new GalleryDatabaseHelper(getApplicationContext());
	}

	public Fragment createFragment() {
		return new PhotoListFragment();
	}

	public void initUserItem (String id, String name) {
		mUserItem.setUserId(id);
		mUserItem.setUserName(name);
		Log.d(TAG, "initUserItem() user: "
				+ mUserItem.getUserId() + "-"
				+ mUserItem.getUserName());
	}
	public UserItem getUserItem () {
		return mUserItem;
	}
/*	public void setPhotoItem (PhotoItem photoItem) {
		mPhotoItem = photoItem;
		Log.d(TAG, "setPhotoItem() photo: "
				+ mPhotoItem.getUserId() + "-"
				+ mPhotoItem.getUserName() + "; "
				+ mPhotoItem.getPhotoId() + "-"
				+ mPhotoItem.getPhotoName());
	}*/
//	public PhotoItem getPhotoItem () {
	//return mPhotoItem;
//	}
//	public void setPhotoItems (ArrayList<PhotoItem> items) {
	//	mPhotoItems = items;
//	}

	protected void insertPhotoItems(ArrayList<PhotoItem> items, UserItem user) {
		PhotoItem item;
		Log.d(TAG, "insertPhotoItems() user:"+user.getUserId()+"-"+user.getUserName());
		mHelper.deletePhotosforUserId(user.getUserId());
		// TODO try fetching - see what is remaining
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
	
	protected ArrayList<PhotoItem> fetchPhotoItemsforUser(UserItem user) {
		PhotoCursor cursor;
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