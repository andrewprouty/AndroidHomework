package edu.prouty.hw3.photogallery;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

public class BismarckUserList {
	public static final String TAG = "BismarckUserList";
	// http://bismarck.sdsu.edu/photoserver/userlist
	private static final String ENDPOINT = "http://bismarck.sdsu.edu/photoserver/userlist/";

	public byte[] getUrlBytes(String urlSpec) throws IOException {
		URL url = new URL(urlSpec);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();

		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			InputStream in = connection.getInputStream();

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return null;
			}

			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead = in.read(buffer)) > 0) {
				out.write(buffer, 0, bytesRead);
			}
			out.close();
			return out.toByteArray();
		} finally {
			connection.disconnect();
		}
	}

	String getUrl(String urlSpec) throws IOException {
		return new String(getUrlBytes(urlSpec));
	}

	public ArrayList<UserItem> fetchItems(Context appContext) {
		ArrayList<UserItem> items = new ArrayList<UserItem>();

		String jsonString = GETUserList();
		if (jsonString == null || jsonString.length() == 0) {
			jsonString = readUserList(appContext); // exists in cache?
		}
		else {
			cacheUserList(appContext, jsonString);
		}

		if (jsonString == null || jsonString.length() == 0) {
			Log.i(TAG, "fetchItems() Failed to fetch items");
		}
		else {
			parseUserList(items, jsonString);
		}
		return items;
	}
	private String GETUserList() {
		String jsonString = "";
		try {
			String url = Uri.parse(ENDPOINT).toString();
			jsonString = getUrl(url);
			Log.i(TAG, "GETUserList() URL: " + url);
			Log.i(TAG, "GETUserList() Received json: " + jsonString);
		} catch (IOException ioe) {
			Log.e(TAG, "GETUserList() Failed to fetch items", ioe);
		}
		return jsonString;
	}

	private void cacheUserList(Context appContext, String jsonString) {
		String fName = "UserList";
		FileOutputStream outFile;
		try {
			outFile = appContext.openFileOutput(fName, Context.MODE_PRIVATE);
			outFile.write(jsonString.getBytes());
			outFile.close();
		} catch (Exception e) {
			Log.e(TAG, "cacheUserList() Error writing to file", e);
		}
		Log.i(TAG, "cacheUserList() end " +appContext.getFileStreamPath(fName));
	}

	private String readUserList(Context appContext) {
		String fName = "UserList";
		String fileContents;
		FileInputStream inFile;
		try {
			inFile = appContext.openFileInput(fName);
			byte[] data = new byte[inFile.available()];
			inFile.read(data, 0, inFile.available());
			fileContents = new String (data);
			inFile.close();
		} catch (Exception e) {
			Log.e(TAG, "readUserList() Error reading file", e);
			fileContents = "";
		}
		Log.i(TAG, "readUserList() end " +appContext.getFileStreamPath(fName));
		return fileContents;
	}
	private void parseUserList(ArrayList<UserItem> items, String stringUserList) {
		try {
			JSONArray jsonUserList = new JSONArray (stringUserList);  
			// {"name":"Roger Whitney","id":"1"},...
			
			Log.i(TAG, "parseUserList() count of photos: "+jsonUserList.length());
			for (int i = 0; i < jsonUserList.length(); i++) {
				JSONObject jsonNode = jsonUserList.getJSONObject(i);
				String user_name   = jsonNode.optString("name").toString();
				String user_id     = jsonNode.optString("id").toString();
				Log.i(TAG, "parseUserList(): "+ i + ": "+user_name+"-"+user_id);

				UserItem item = new UserItem();
				item.setUserName(user_name);
				item.setUserId(user_id);
				items.add(item);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}