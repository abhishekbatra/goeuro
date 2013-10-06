package com.goeurotest.travelsearch;

public class LocationData {
	public String __type;
	public int id;
	public String name;
	public String type;
	public double latitude;
	public double longitude;
	
	public LocationData() {
		__type = name = type = null;
		id = 0;
		latitude = longitude = 0;
	}

	@Override
	public String toString() {
		return name;
	}
}
