package ir.easazade.osmdroidsample

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.button
import kotlinx.android.synthetic.main.activity_main.mapView
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.content.Intent
import android.content.DialogInterface
import android.location.LocationManager
import android.provider.Settings
import android.support.v7.app.AlertDialog

class MainActivity : AppCompatActivity() {

  private var locationApi: LocationApi? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    button.setOnClickListener {
      askToTurnOnLocationIfOff {
        if (locationApi == null)
          locationApi = LocationApi.newInstance(this)
        locationApi!!.getLocation { location ->
          val createMapView = CreateMap(this)
          createMapView.setMapView(mapView, "title", location.latitude, location.longitude, true)
        }
      }
    }
  }

  private fun askToTurnOnLocationIfOff(action: () -> Unit) {
    val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
      // Build the alert dialog
      val builder = AlertDialog.Builder(this)
      builder.setTitle("Location Services Not Active")
      builder.setMessage("Please enable Location Services and GPS")
      builder.setPositiveButton("OK") { dialogInterface, i ->
        // Show location settings when the user acknowledges the alert dialog
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
      }
      val alertDialog = builder.create()
      alertDialog.setCanceledOnTouchOutside(false)
      alertDialog.show()
    } else {
      action()
    }
  }
}
