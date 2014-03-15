package edu.prouty.hw3.photogallery;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import edu.prouty.hw3.photogallery.GalleryDatabaseHelper.UserCursor;

public class MainUserListActivity extends FragmentActivity {
	private static final String TAG = "MainUserListActivity";
	//private UserItem mUserItem;
	private int mPosition;
	private GalleryDatabaseHelper mHelper;
	
	protected void launchPhotoListActivity(UserItem user) {
		Intent i = new Intent (MainUserListActivity.this, PhotoListActivity.class);
		i.putExtra("UserId", user.getUserId().toString());
		i.putExtra("UserName", user.getUserName().toString());
		Log.d(TAG, "launchPhotoListActivity() user: "
				+ user.getUserId() + "-"
				+ user.getUserName());
		startActivity(i);
	}

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

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_main_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_upload:
	    		Log.d(TAG, "onOptionsItemSelected() calling UploadFileActivity");
	    		Intent i = new Intent (MainUserListActivity.this, UploadFileActivity.class);
	    		startActivity(i);
	            return true;
	        default:
	    		Log.d(TAG, "onOptionsItemSelected() Id: "+item.getItemId());
	            return super.onOptionsItemSelected(item);
	    }
	}
	
    public Fragment createFragment() {
		 return new UserListFragment();
	}

	public int getPosition () {
		return mPosition;
	}

//	public UserItem getUserItem () {
	//	return mUserItem;
	//}
    /*
	protected void setUserItem (UserItem userItem) {
		mUserItem = userItem;
		Log.d(TAG, "setUserItem() user: "
				+ mUserItem.getUserId() + "-"
				+ mUserItem.getUserName());
	}*/
    protected void insertUserItems(ArrayList<UserItem> items) {
        UserItem item;
        Log.d(TAG, "insertUserItems()");
		mHelper.deleteUsers();
        for (int i=0; i<items.size(); i++) {
    		item=items.get(i);
    		Log.d(TAG, "insertUserItems() user: "
    				+ item.getUserId() + "-"
    				+ item.getUserName());
            mHelper.insertUser(item);
            mHelper.close();
        }
        return;
    }
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
    	cursor.close();
        mHelper.close();
    	return items;
    }
    private UserItem cursorToUserItem(UserCursor cursor) {
    	UserItem item = new UserItem();
    	item.setUserId(cursor.getString(0));   // TODO cursor.getInt
    	item.setUserName(cursor.getString(1));
		return item;
    }
}
