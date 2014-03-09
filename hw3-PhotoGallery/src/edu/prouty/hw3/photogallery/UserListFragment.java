package edu.prouty.hw3.photogallery;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UserListFragment extends Fragment{
	private static final String TAG = "UserListFragment";
	private ArrayList<UserItem> mUserItems;
	private UserItem mUserItem;
	FetchUserItemsTask mFetchUserItemsTask = new FetchUserItemsTask();

	View view;
	TextView mUserTextView;
	ListView mListView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setRetainInstance(true); // survive across Activity re-create (i.e. orientation)
        mFetchUserItemsTask.execute();
    }
	
    @Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
	{       
		Log.d(TAG, "onCreateView()");
		view = inflater.inflate(R.layout.fragment_user_list, container,false);
        mUserTextView = (TextView)view.findViewById(R.id.user_list_textView);
		mListView = (ListView)view.findViewById(R.id.user_list_view);
		setupAdapter();
		mListView.setOnItemClickListener(new OnItemClickListener () {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView textViewItem = ((TextView) view.findViewById(R.id.row_user_name_textView));
				// get the clicked item name
				String listItemText = textViewItem.getText().toString();
				Log.d(TAG, "().onItemClick() User ["+position+"]= "+listItemText);
				returnSelection(position);
			}
		});
		return view;
	}
	
    private void setupAdapter() {
    	if (getActivity() == null || mListView == null) {
    		return;
    	}
		if (mUserItems != null) {
			UserListAdapter adapter = new UserListAdapter(mUserItems);
			mListView.setAdapter(adapter);
		}
		else {
			mListView.setAdapter(null);
		}
    }
	
    private void returnSelection(int position) {
		mFetchUserItemsTask.cancel(true);
    	mUserItem = mUserItems.get(position);
    	Log.i(TAG, "returnSelection()=["+position+"] "+mUserItem.getUserId()+": "+mUserItem.getUserName());
		mUserTextView.setText(mUserItem.getUserName());
		((UserListActivity) getActivity()).setUserItem(mUserItem);
		((UserListActivity) getActivity()).launchPhotoListActivity();
    }
    private class FetchUserItemsTask extends AsyncTask<Void,Void,ArrayList<UserItem>> {
        @Override
        protected ArrayList<UserItem> doInBackground(Void... params) {
        	Log.d(TAG, "FetchUserTask doInBackground()");
    		ArrayList<UserItem> items = null;
    		try {
    			// pass context for app dir to cache file
        		items = new UserListBismarck().fetchItems(getActivity().getApplicationContext());
    		} catch (Exception e) {
    			Log.e(TAG, "doInBackground() Exception.", e);
    		}
        	return items;
        	//return new UserListBismarck().fetchItems(getActivity().getApplicationContext());
        }
        @Override
        protected void onPostExecute(ArrayList<UserItem> userItems) {
            mUserItems = userItems;
        	Log.d(TAG, "FetchUserTask onPostExecute");
            //mUserTextView.setText("I'm back");
            setupAdapter();
            cancel(true); // done !
        	Log.d(TAG, "FetchUserTask onPostExecute()-cancel");
        }
        @Override
        protected void onCancelled() {
        	Log.d(TAG, "FetchUserTask onCancelled()");
        }
    }
    private class UserListAdapter extends ArrayAdapter<UserItem> {
        public UserListAdapter(ArrayList<UserItem> userItems) {
            super(getActivity(), 0, userItems);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.user_list_row, parent, false);
            }
            
            UserItem item = getItem(position);
            TextView userTextView = (TextView)convertView.findViewById(R.id.row_user_name_textView);
			//Log.d(TAG, "adapter.getView() item.getUserName(): "+item.getUserName());
            userTextView.setText(item.getUserName());
            
            return convertView;
        }
    }
}