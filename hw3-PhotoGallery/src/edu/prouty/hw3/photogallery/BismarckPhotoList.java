package edu.prouty.hw3.photogallery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

public class BismarckPhotoList {
	public static final String TAG = "BismarckPhotoList";
	// http://bismarck.sdsu.edu/photoserver/userphotos/2
	private static final String ENDPOINT = "http://bismarck.sdsu.edu/photoserver/userphotos/";
	private static final String USER_KEY = "2";

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

	public ArrayList<GalleryItem> fetchItems() {
		ArrayList<GalleryItem> items = new ArrayList<GalleryItem>();

		try {
			String url = Uri.parse(ENDPOINT).toString() + USER_KEY;
			String jsonString = getUrl(url);
			Log.i(TAG, "fetchItems() URL: " + url);
			Log.i(TAG, "fetchItems() Received json: " + jsonString);

			parsePhotoList(items, jsonString);
		} catch (IOException ioe) {
			Log.e(TAG, "fetchItems() Failed to fetch items", ioe);
		}
		return items;
	}

	void parsePhotoList(ArrayList<GalleryItem> items, String stringPhotoList) {
		try {
			JSONArray jsonPhotoList = new JSONArray (stringPhotoList);  
			// {"name":"dog","id":"23"},...
			Log.i(TAG, "parsePhotoList() count of photos: "+jsonPhotoList.length());
		    for (int i = 0; i < jsonPhotoList.length(); i++) {
		        JSONObject jsonNode = jsonPhotoList.getJSONObject(i);
		        String photo_name   = jsonNode.optString("name").toString();
		        String photo_id     = jsonNode.optString("id").toString();
		        Log.i(TAG, "parsePhotoList(): "+ i + ": "+photo_id+"-"+photo_name);
		        
				GalleryItem item = new GalleryItem();
				item.setUserId(USER_KEY); // Andy USER ID=2 for now
				item.setPhotoName(photo_name);
				item.setPhotoId(photo_id);
				items.add(item);
		    }
		    
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

/*	void parseItems(ArrayList<GalleryItem> items, XmlPullParser parser) throws XmlPullParserException, IOException {
		int eventType = parser.next();

		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG &&
					XML_PHOTO.equals(parser.getName())) {
				String id = parser.getAttributeValue(null, "id");
				String caption = parser.getAttributeValue(null, "title");
				String smallUrl = parser.getAttributeValue(null, EXTRA_SMALL_URL);

				GalleryItem item = new GalleryItem();
				item.setId(id);
				item.setCaption(caption);
				item.setUrl(smallUrl);
				items.add(item);
			}

			eventType = parser.next();
		}
	}*/
}