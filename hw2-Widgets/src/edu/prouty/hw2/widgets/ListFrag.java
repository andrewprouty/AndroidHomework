package edu.prouty.hw2.widgets;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListFrag extends ListFragment{

	private static final String TAG = "hw2-L_Frag";
	String[] mAndroidOS = new String[] { "1.5 Cupcake", "1.6 Donut", "2.0 Eclaire", "2.1 Eclaire", "2.2 Froyo",
			"2.3 Gingerbread","3 Honeycomb", "4.0 Ice Cream Sandwich", "4.1 Jelly Bean",
			"4.2 Jelly Bean", "4.3 Jelly Bean",	"4.4 KitKat"};
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(getActivity(), " " + mAndroidOS[position], Toast.LENGTH_SHORT).show();

		((ListActivity)getActivity()).setOS(mAndroidOS[position]);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView() called");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1,	mAndroidOS);
		setListAdapter(adapter);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}