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

public class PhotoListBismarck {
	public static final String TAG = "PhotoListBismarck";
	// http://bismarck.sdsu.edu/photoserver/userphotos/2
	private static final String ENDPOINT = "http://bismarck.sdsu.edu/photoserver/userphotos/";
//	private static final String USER_KEY = "2";
	private UserItem mUserItem;

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

	public ArrayList<PhotoItem> fetchItems(UserItem userItem, Context appContext) {
		ArrayList<PhotoItem> items = new ArrayList<PhotoItem>();
		mUserItem = userItem;
		String jsonString = GETPhotoList();
		if (jsonString == null || jsonString.length() == 0) {
			jsonString = readPhotoList(appContext); // exists in cache?
		}
		else {
			cachePhotoList(appContext, jsonString);
		}

		if (jsonString == null || jsonString.length() == 0) {
			Log.i(TAG, "fetchItems() Failed to fetch items");
		}
		else {
			parsePhotoList(items, jsonString);
		}
		return items;
	}
	private String GETPhotoList() {
		String jsonString = "";
		try {
			String url = Uri.parse(ENDPOINT).toString() + mUserItem.getUserId();
			jsonString = getUrl(url);
			Log.i(TAG, "GETPhotoList() URL: " + url);
			Log.i(TAG, "GETPhotoList() Received json: " + jsonString);
		} catch (IOException ioe) {
			Log.e(TAG, "GETPhotoList() Failed to fetch items", ioe);
		}
		return jsonString;
	}

	private void cachePhotoList(Context appContext, String jsonString) {
		String fName = "PhotoList-"+mUserItem.getUserId();
		FileOutputStream outFile;
		try {
			outFile = appContext.openFileOutput(fName, Context.MODE_PRIVATE);
			outFile.write(jsonString.getBytes());
			outFile.close();
		} catch (Exception e) {
			Log.e(TAG, "cachePhotoList() Error writing to file", e);
		}
		Log.i(TAG, "cachePhotoList() " +appContext.getFileStreamPath(fName));
	}

	private String readPhotoList(Context appContext) {
		String fName = "PhotoList-"+mUserItem.getUserId();
		String fileContents;
		FileInputStream inFile;
		try {
			inFile = appContext.openFileInput(fName);
			byte[] data = new byte[inFile.available()];
			inFile.read(data, 0, inFile.available());
			fileContents = new String (data);
			inFile.close();
		} catch (Exception e) {
			Log.e(TAG, "readPhotoList() Error reading file", e);
			fileContents = "";
		}
		Log.i(TAG, "readPhotoList() " +appContext.getFileStreamPath(fName));
		return fileContents;
	}
	private void parsePhotoList(ArrayList<PhotoItem> items, String stringPhotoList) {
		try {
			JSONArray jsonPhotoList = new JSONArray (stringPhotoList);  
			// {"name":"dog","id":"23"},...
			Log.i(TAG, "parsePhotoList() count of photos: "+jsonPhotoList.length());
			for (int i = 0; i < jsonPhotoList.length(); i++) {
				JSONObject jsonNode = jsonPhotoList.getJSONObject(i);
				String photo_name   = jsonNode.optString("name").toString();
				String photo_id     = jsonNode.optString("id").toString();
				Log.i(TAG, "parsePhotoList(): "+ i + ": "+photo_id+"-"+photo_name);

				PhotoItem item = new PhotoItem();
				item.setUserId(mUserItem.getUserId());
				item.setPhotoName(photo_name);
				item.setPhotoId(photo_id);
				items.add(item);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}