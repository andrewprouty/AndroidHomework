package edu.prouty.hw3.photogallery;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class UserListActivity extends FragmentActivity {
	private static final String TAG = "UserListActivity";
	private UserItem mUserItem;
	private GalleryDatabaseHelper mHelper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate()");
        setContentView(R.layout.activity_fragment);
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
    
	protected void launchPhotoListActivity() {
		//Toast.makeText(this,"Selected "+mUserItem.getUserId() + "-" + mUserItem.getUserName(),Toast.LENGTH_SHORT).show();
		Intent i = new Intent (UserListActivity.this, PhotoListActivity.class);
		i.putExtra("UserId", mUserItem.getUserId().toString());
		i.putExtra("UserName", mUserItem.getUserName().toString());
		startActivity(i);
	}
	
	public Fragment createFragment() {
		 return new UserListFragment();
	}

	public UserItem getUserItem () {
		return mUserItem;
	}
	protected void setUserItem (UserItem userItem) {
		mUserItem = userItem;
		Log.d(TAG, "setUserItem() user: "
				+ mUserItem.getUserId() + "-"
				+ mUserItem.getUserName());
	}
    protected void insertUserItems(ArrayList<UserItem> users) {
        UserItem user;
        Log.d(TAG, "insertUserItem()");
		Log.d(TAG, "insertUserItem() newer");
		mHelper.deleteUsers();
		Log.d(TAG, "insertUserItem() removed old");
        for (int i=0; i<users.size(); i++) {
    		user=users.get(i);
    		Log.d(TAG, "insertUserItem() user: "
    				+ user.getUserId() + "-"
    				+ user.getUserName());
            mHelper.insertUser(user);
        }
        return;
    }

}
