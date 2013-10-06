package com.goeurotest.travelsearch;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import org.apache.http.HttpResponse;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

public class LocationListAdapter extends ArrayAdapter<LocationData> {
	
	String mSearchString;
	private final Semaphore buildLock = new Semaphore(1);
	private LocationData mSourceLocation;
	ArrayList<Double> mDistancesFromSource;
	public boolean mHasSourceLocation;
	
	public LocationListAdapter(Context context) {
		super(context, 0);
		mSourceLocation = null;
		mDistancesFromSource = new ArrayList<Double>();
		mHasSourceLocation = false;
	}
	
	public void SetSourceLocation(LocationData sourceLocation) {
		mSourceLocation = sourceLocation;
	}
	
	public LocationData GetSourceLocation() {
		return mSourceLocation;
	}
	
	public void RequestNewData(String searchString) {
		mSearchString = searchString;
		GetLocationSuggestionsTask suggestionsFetchTask = new GetLocationSuggestionsTask();
		suggestionsFetchTask.execute(this);
	}
	
	public void BuildData(HttpResponse response) throws IOException, InterruptedException {
		InputStream content = response.getEntity().getContent();
		if(content == null) {
			return;
		}
		buildLock.acquire();
		if(mHasSourceLocation && (mSourceLocation != null)) {
			for(LocationData location : TravelJsonReader.readJSON(content)) {
				insertSorted(location);
			}
		} else {
			for(LocationData location : TravelJsonReader.readJSON(content)) {
				add(location);
				Log.d("BuildData", String.format("Location added: %s", location.name));
			}
		}
		buildLock.release();
		
		notifyDataSetChanged();
	}
	
	private void insertSorted(LocationData location) {
		double distance = getDistance(location);
		
		if(getCount() != 0) {
			for(int i = 0; i < getCount(); i++) {
				if(distance < mDistancesFromSource.get(i))
				{
					insert(location, i);
					mDistancesFromSource.add(i, distance);
					return;
				}
			}
		}
		
		add(location);
		mDistancesFromSource.add(distance);
	}

	private double getDistance(LocationData location) {
		return distance(location.latitude, location.longitude, mSourceLocation.latitude, mSourceLocation.longitude);
	}

	private double distance(double lat1, double lon1, double lat2, double lon2) {
		  double theta = lon1 - lon2;
		  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		  dist = Math.acos(dist);
		  dist = rad2deg(dist);
		  dist = dist * 60 * 1.1515;
		  dist = dist * 1.609344;
		  return (dist);
		}

	private double deg2rad(double deg) {
	  return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
	  return (rad * 180 / Math.PI);
	}

	public String getCurrentSearchString() {
		return mSearchString;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LocationData data = getItem(position);
		if(convertView == null) {
			convertView = View.inflate(getContext(), R.layout.location_item_layout, null);
		}
		String locationName = changeLocationNameFormat(data.name);
		TextView locationSuggestionView = (TextView) convertView.findViewById(R.id.location_suggestion_view);
		locationSuggestionView.setText(locationName);
		
		return convertView;
	}
		
	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return super.getFilter();
	}

	public String changeLocationNameFormat(String locationName) {
		int commaLocation = locationName.lastIndexOf(',');
		if(commaLocation != -1) {
			String primaryName = locationName.substring(0, commaLocation);
			String countryName = locationName.substring(commaLocation + 2);
			return primaryName.concat(" ").concat("(").concat(countryName).concat(")");
		}
		
		return locationName;
	}
}
