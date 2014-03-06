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
	public static final String TAG = "UserListFragment";
	ArrayList<UserItem> mUserItems;
	UserItem mUserItem;
	ListView mListView;
	TextView mUserTextView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRetainInstance(true); // survive across Activity re-create (i.e. orientation)
        new FetchUserItemsTask().execute();
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
	{       
		View view = inflater.inflate(R.layout.fragment_user_list, container,false);
        mUserTextView = (TextView)view.findViewById(R.id.user_list_textView);
		mListView = (ListView)view.findViewById(R.id.user_list_view);
		setupAdapter();
		mListView.setOnItemClickListener(new OnItemClickListener () {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView textViewItem = ((TextView) view.findViewById(R.id.user_name_textView));
				// get the clicked item name
				String listItemText = textViewItem.getText().toString();
				Log.i(TAG, "().onItemClick() User ["+position+"]= "+listItemText);
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
		Log.i(TAG, "returnSelection() position= ["+position+"]");
		
		mUserItem = mUserItems.get(position);
		((PhotoGalleryActivity) getActivity()).setUserItem(mUserItem);
    }
    private class FetchUserItemsTask extends AsyncTask<Void,Void,ArrayList<UserItem>> {
        @Override
        protected ArrayList<UserItem> doInBackground(Void... params) {
        	return new BismarckUserList().fetchItems(getActivity().getApplicationContext());
        }

        @Override
        protected void onPostExecute(ArrayList<UserItem> userItems) {
            mUserItems = userItems;
            //mUserTextView.setText("I'm back");
            setupAdapter();
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
            TextView userTextView = (TextView)convertView.findViewById(R.id.user_name_textView);
			//Log.i(TAG, "adapter.getView() item.getUserName(): "+item.getUserName());
            userTextView.setText(item.getUserName());
            
            return convertView;
        }
    }
}