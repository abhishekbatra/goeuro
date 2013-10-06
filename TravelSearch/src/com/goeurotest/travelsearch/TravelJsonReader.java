package com.goeurotest.travelsearch;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.util.JsonReader;

public class TravelJsonReader {
	public static ArrayList<LocationData> readJSON(InputStream in) throws IOException {
				
		JsonReader reader = new JsonReader(new InputStreamReader(in));
		ArrayList<LocationData> locations = new ArrayList<LocationData>();
		
		reader.beginObject();
		while(reader.hasNext()) {
			String name = reader.nextName();
			if(name.equals("results")) {
				reader.beginArray();
				
				while(reader.hasNext()) {
					locations.add(readLocation(reader));
				}
				reader.endArray();
			}
		}
		reader.endObject();
		
		return locations;
	}
	
	public static ArrayList<LocationData> readJSONSorted(InputStream in, LocationData basedOn) 
			throws IOException {
		ArrayList<LocationData> locations = readJSON(in);
		return sortLocations(locations, basedOn);
	}
	
	public static LocationData readLocation(JsonReader reader) throws IOException {
		LocationData location = new LocationData();
		
		reader.beginObject();
		while(reader.hasNext()) {
			String name = reader.nextName();
			
			if(name.equals("_type")) {
				location.__type = reader.nextString();
			} else if(name.equals("_id")) {
				location.id = reader.nextInt();
			} else if(name.equals("name")) {
				location.name = reader.nextString();
			} else if(name.equals("type")) {
				location.type = reader.nextString();
			} else if(name.equals("geo_position")) {
				reader.beginObject();
				while(reader.hasNext()) {
					String geo_name = reader.nextName();
					if(geo_name.equals("latitude")) {
						location.latitude = reader.nextDouble();
					} else if(geo_name.equals("longitude")) {
						location.longitude = reader.nextDouble();
					}
				}
				reader.endObject();
			}
		}
		reader.endObject();
		
		return location;
	}
	
	public static ArrayList<LocationData> sortLocations(ArrayList<LocationData> locations, 
			LocationData basedOn) {
				return locations;
	}
}
