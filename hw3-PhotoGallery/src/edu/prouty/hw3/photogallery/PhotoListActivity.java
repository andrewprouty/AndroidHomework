package edu.prouty.hw3.photogallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

public class PhotoListActivity extends FragmentActivity {
	private static final String TAG = "PhotoListActivity";
	//UserItem mUsrItem;
	private UserItem mUserItem = new UserItem();
	// TODO rename back to mUsrItem.  get rid of new
	private PhotoItem mPhotoItem;

	protected void launchPhotoDisplayActivity() {
		Toast.makeText(this,mPhotoItem.getUserId() + "-"
						  + mPhotoItem.getPhotoId() + "-"
						  + mPhotoItem.getPhotoName()
				,Toast.LENGTH_SHORT).show();
		//Intent i = new Intent (PhotoListActivity.this, DisplayActivity.class);
		//i.putExtra("UserId", mUserItem.getUserId().toString());
		//i.putExtra("UserName", mUserItem.getUserName().toString());
		//startActivity(i);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
		Log.d(TAG, "onCreate()");
		mUserItem.setUserId(getIntent().getStringExtra("UserId"));
		mUserItem.setUserName(getIntent().getStringExtra("UserName"));
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

		if (fragment == null) {
			fragment = createFragment();
			manager.beginTransaction()
			.add(R.id.fragmentContainer, fragment)
			.commit();
		}
	}

	public Fragment createFragment() {
		return new PhotoListFragment();
	}

	public UserItem getUserItem () {
		return mUserItem;
	}
	public void setUserItem (UserItem userItem) {
		mUserItem = userItem;
		Log.d(TAG, "setUserItem() user: "
				+ mUserItem.getUserId() + "-"
				+ mUserItem.getUserName());
	}

	public void setPhotoItem (PhotoItem photoItem) {
		mPhotoItem = photoItem;
		Log.d(TAG, "setPhotoItem() photo: "
				+ mPhotoItem.getUserId() + "-"
				+ mPhotoItem.getPhotoId() + "-"
				+ mPhotoItem.getPhotoName());
	}
	public PhotoItem getPhotoItem () {
		return mPhotoItem;
	}
}