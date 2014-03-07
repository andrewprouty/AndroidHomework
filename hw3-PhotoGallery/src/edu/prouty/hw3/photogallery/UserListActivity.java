package edu.prouty.hw3.photogallery;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

public class UserListActivity extends SingleFragmentActivity {
	private static final String TAG = "UserListActivity";
	private UserItem mulaUserItem;
	
	protected void setUserItem (UserItem userItem) {
		mulaUserItem = userItem;
		Log.i(TAG, "setUserItem() user: "
				+ mulaUserItem.getUserId() + "-"
				+ mulaUserItem.getUserName());
	}
	
	protected void launchPhotoListActivity() {
		Intent i = new Intent (UserListActivity.this, PhotoListActivity.class);
		i.putExtra("UserId", mulaUserItem.getUserId().toString());
		i.putExtra("UserName", mulaUserItem.getUserName().toString());
		startActivity(i);
	}
	
	public UserItem getUserItem () {
		return mulaUserItem;
	}
	
	@Override
	public Fragment createFragment() {
		//return new PhotoListFragment();
		 return new UserListFragment();
		// return new PhotoGalleryFragment();
	}
}
