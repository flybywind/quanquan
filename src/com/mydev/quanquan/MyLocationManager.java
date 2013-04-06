package com.mydev.quanquan;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MyLocationManager {
	public MyLocationManager(Context context){
		mLocationManager = (LocationManager) context.getSystemService(
				Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		mBestLocation = null;
		LocationListener listener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				if( isBetterLocation(location, mBestLocation ) )
					mBestLocation = location;
			}
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			
		};
		mLocationManager.requestLocationUpdates(msUpdate_min_time, msUpdate_min_dist, 
				criteria, listener, null);
	}
	
	public LatLng getBestLocation(){
		if( mBestLocation != null )
			return new LatLng(mBestLocation.getLatitude(),
						  	  mBestLocation.getLongitude());
		else
			return new LatLng(0, 0);
	}
//	public static void createMyLocationManager(LocationManager lm){
//		msLocationManager = lm;
//	}
	
	boolean isBetterLocation(Location location, Location currentBestLocation){
		if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}
	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}
	private LocationManager mLocationManager;
	private final static long msUpdate_min_time = 1*60*1000;
	private final static float msUpdate_min_dist = 100;
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	
	Location mBestLocation;
}
