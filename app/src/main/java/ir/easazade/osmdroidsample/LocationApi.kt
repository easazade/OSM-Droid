package ir.easazade.osmdroidsample

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import io.reactivex.subjects.PublishSubject

class LocationApi private constructor(private val activity: Activity) {

  private val fusedLocationProviderClient: FusedLocationProviderClient = FusedLocationProviderClient(activity)
  private val requestPermission = 789

  private var updatingUserLocationPeriodicallyInProgress: Boolean = false

  private var mAction: ((Location) -> Unit)? = null

  private var callback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult?) {
      if (locationResult != null) {
        val location = locationResult.lastLocation
        if (location != null) {
          fusedLocationProviderClient.removeLocationUpdates(this)
          mAction?.invoke(location)
        }
      }
    }
  }

  //############################################# private methods ##################################

  private val locationRequest: LocationRequest
    get() {
      val lr = LocationRequest()
      lr.interval = 10000
      lr.fastestInterval = 10000
      lr.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
      return lr
    }

  companion object {

    private var instance: LocationApi? = null

    fun newInstance(context: Activity): LocationApi {
      if (instance == null) {
        instance = LocationApi(context)
      }
      return instance!!
    }
  }

  private fun checkAndRequestPermission(activity: Activity): Boolean {
    var hasPermission = true
    if (ActivityCompat.checkSelfPermission(
        this.activity,
        Manifest.permission.ACCESS_FINE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        this.activity,
        Manifest.permission.ACCESS_COARSE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      hasPermission = false
      ActivityCompat.requestPermissions(
        activity,
        arrayOf(
          Manifest.permission.ACCESS_COARSE_LOCATION,
          Manifest.permission.ACCESS_FINE_LOCATION,
          Manifest.permission.INTERNET,
          Manifest.permission.ACCESS_NETWORK_STATE
        ),
        requestPermission
      )
    }
    return hasPermission
  }

  fun getLocation(action: (Location) -> Unit) {
    mAction = action
    checkAndRequestPermission(activity)
    fusedLocationProviderClient.requestLocationUpdates(locationRequest, callback, null)
  }

//  fun getLastKnownLocation(userData: UserData): Observable<UserData> {
//    Log.i("tag", "updating location for the first time")
//    checkAndRequestPermission(activity)
//    return Observable.create({ emitter ->
//      checkAndRequestPermission(activity)
//      fusedLocationProviderClient.getLastLocation().addOnSuccessListener({ location ->
//        if (location != null) {
//          userData.setLocation(doubleArrayOf(location!!.getLatitude(), location!!.getLongitude()))
//          emitter.onNext(userData)
//        } else {
//          userData.setLocation(doubleArrayOf(0.0, 0.0))
//          emitter.onNext(userData)
//        }
//      })
//    })
//  }

}