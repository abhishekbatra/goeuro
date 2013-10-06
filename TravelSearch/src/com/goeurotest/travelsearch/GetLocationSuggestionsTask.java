package com.goeurotest.travelsearch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class GetLocationSuggestionsTask extends AsyncTask <LocationListAdapter, Void, HttpResponse>{

	public static String apiUrl = "http://pre.dev.goeuro.de:12345/api/v1/suggest/position/en/name/";
	LocationListAdapter mDataAdapter;
	
	@Override
	protected HttpResponse doInBackground(LocationListAdapter... dataAdapters) {
		
		mDataAdapter = dataAdapters[0];
		String searchString = mDataAdapter.getCurrentSearchString();
		searchString = searchString.replaceAll(",", "");
		
		try {
			searchString = URLEncoder.encode(searchString, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		String searchUrl = apiUrl.concat(searchString);
				
		DefaultHttpClient mHttpClient = new DefaultHttpClient();
		
		HttpGet httpGet = new HttpGet(searchUrl);
		HttpResponse response = null;
		try {
			response = mHttpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}

	@Override
	protected void onPostExecute(HttpResponse result) {
		
		super.onPostExecute(result);
				
		try {
			mDataAdapter.BuildData(result);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
