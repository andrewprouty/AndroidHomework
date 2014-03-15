package edu.prouty.hw3.photogallery;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class FileUpload {
	//Here is the Java code. You have to add a path to a photo and your username and password.
	//	public static void main(String[] ags) {
	//		upload();
	//	}
	private static final String TAG = "FileUpload";
	public static String upload(String pathToOurFile, String fname) {
		Log.d(TAG,"upload() start");
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		String urlServer = "http://bismarck.sdsu.edu/photoserver/postphoto/Andrew/1161/"+fname;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		String returnMsg;
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(
					pathToOurFile));
			URL url = new URL(urlServer);
			Log.d(TAG,"upload() url: "+url);
			connection = (HttpURLConnection) url.openConnection();
			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			// Enable POST method
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
					+ pathToOurFile + "\"" + lineEnd);
			outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
			outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
			outputStream.writeBytes(lineEnd);
			Log.d(TAG,"upload() header done");
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				Log.d(TAG,"upload() writing photo");
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens
					+ lineEnd);
			Log.d(TAG,"upload() get response");
			int serverResponseCode = connection.getResponseCode();
			String serverResponseMessage = connection.getResponseMessage();
			returnMsg = serverResponseMessage + " " + serverResponseCode;
			Log.d(TAG,"upload() response: "+returnMsg);
			fileInputStream.close();
			outputStream.flush();
			outputStream.close();
		} catch (Exception ex) {
			Log.d(TAG,"upload() Exc: "+ ex.getMessage()+ex);
			returnMsg = "Exception: " + ex.getMessage();
		}
		return returnMsg;
	}
}