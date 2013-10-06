package com.goeurotest.travelsearch;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements TextWatcher, OnItemClickListener, OnClickListener {
	
	private AutoCompleteTextView mSourceLocation;
	private AutoCompleteTextView mDestinationLocation;
	private Button mSearchButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSourceLocation = (AutoCompleteTextView)findViewById(R.id.source_location_list);
		mSourceLocation.setAdapter(new LocationListAdapter(this));
		mSourceLocation.addTextChangedListener(this);
		mSourceLocation.setOnItemClickListener(this);
		
		mDestinationLocation = (AutoCompleteTextView)findViewById(R.id.destination_location_list);
		mDestinationLocation.setAdapter(new LocationListAdapter(this));
		((LocationListAdapter)mDestinationLocation.getAdapter()).mHasSourceLocation = true;
		mDestinationLocation.addTextChangedListener(this);
		
		mSearchButton = (Button)findViewById(R.id.search_button);
		mSearchButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(isNetworkConnect()) {
			if(mSourceLocation.isFocused()) {
				LocationListAdapter sourceAdapter = (LocationListAdapter)mSourceLocation.getAdapter();
				if(s.length() > 0) {
					sourceAdapter.RequestNewData(s.toString());
				}
			} else {
				LocationListAdapter destinationAdapter = (LocationListAdapter)mDestinationLocation.getAdapter();
				if(s.length() > 0) {
					destinationAdapter.RequestNewData(s.toString());
				}
			}
		}
	}

	private boolean isNetworkConnect() {
		boolean status = false;
		
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getNetworkInfo(0);
        if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
            status= true;
        }else {
            netInfo = cm.getNetworkInfo(1);
            if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                status= true;
        }
		return status;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		LocationListAdapter destinationAdapter = (LocationListAdapter)mDestinationLocation.getAdapter();
		LocationListAdapter sourceAdapter = (LocationListAdapter)mSourceLocation.getAdapter();
		destinationAdapter.SetSourceLocation(sourceAdapter.getItem(position));
	}

	@Override
	public void onClick(View v) {
		CharSequence text = "Search is not yet implemented";
		int duration = Toast.LENGTH_SHORT;
		
		Toast toast = Toast.makeText(this, text, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
