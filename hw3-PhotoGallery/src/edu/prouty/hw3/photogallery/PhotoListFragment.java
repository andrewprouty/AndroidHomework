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

public class PhotoListFragment extends Fragment{
	private static final String TAG = "PhotoListFragment";
	private ArrayList<PhotoItem> mPhotoItems;
	private UserItem mUserItem;
	private PhotoItem mPhotoItem;
	
	View view;
	TextView mUserTextView;
	TextView mPhotoTextView;
	ListView mListView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true); // survive across Activity re-create (i.e. orientation)
		new FetchPhotoItemsTask().execute(mUserItem);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
	{       
		view = inflater.inflate(R.layout.fragment_photo_list, container,false);
		mUserTextView = (TextView)view.findViewById(R.id.photo_list_user_name);
		mPhotoTextView = (TextView)view.findViewById(R.id.photo_list_photo_name);
		mListView = (ListView)view.findViewById(R.id.photo_list_view);

		mUserItem=((PhotoListActivity) getActivity()).getUserItem();
		mUserTextView.setText(mUserItem.getUserName());

		setupAdapter();
		mListView.setOnItemClickListener(new OnItemClickListener () {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView textViewItem = ((TextView) view.findViewById(R.id.row_photo_name_textView));
				// get the clicked item name
				String listItemText = textViewItem.getText().toString();
				Log.d(TAG, "().onItemClick() Photo ["+position+"]= "+listItemText);
				returnSelection(position);
			}
		});
		return view;
	}

	private void setupAdapter() {
		if (getActivity() == null || mListView == null) {
			return;
		}
		if (mPhotoItems != null) {
			PhotoListAdapter adapter = new PhotoListAdapter(mPhotoItems);
			mListView.setAdapter(adapter);
		}
		else {
			mListView.setAdapter(null);
		}
	}

	private void returnSelection(int position) {
		mPhotoItem = mPhotoItems.get(position);
		Log.i(TAG, "returnSelection()=["+position+"] "+mPhotoItem.getPhotoId()+": "+mPhotoItem.getPhotoName());
		mPhotoTextView.setText(mPhotoItem.getPhotoName());
		((PhotoListActivity) getActivity()).setPhotoItem(mPhotoItem);
		((PhotoListActivity) getActivity()).launchPhotoDisplayActivity();
	}
	private class FetchPhotoItemsTask extends AsyncTask<UserItem,Void,ArrayList<PhotoItem>> {
		@Override
		protected ArrayList<PhotoItem> doInBackground(UserItem... params) {
			// pass context to know app dir so can write the cache file
			return new PhotoListBismarck().fetchItems(mUserItem, getActivity().getApplicationContext());
		}
		@Override
		protected void onPostExecute(ArrayList<PhotoItem> photoItems) {
			mPhotoItems = photoItems;
			//mUserTextView.setText("I'm back");
			setupAdapter();
            cancel(true); // done !
        	Log.d(TAG, "FetchPhotoTask onPostExecute()-cancel");
		}
        @Override
        protected void onCancelled() {
        	Log.d(TAG, "FetchPhotoTask onCancelled()");
        }

	}
	private class PhotoListAdapter extends ArrayAdapter<PhotoItem> {
		public PhotoListAdapter(ArrayList<PhotoItem> photoItems) {
			super(getActivity(), 0, photoItems);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.photo_list_row, parent, false);
			}

			PhotoItem item = getItem(position);
			TextView photoTextView = (TextView)convertView.findViewById(R.id.row_photo_name_textView);
			//Log.d(TAG, "adapter.getView() item.getUserName(): "+item.getUserName());
			photoTextView.setText(item.getPhotoName());

			return convertView;
		}
	}
}