package ir.easazade.osmdroidsample

import android.app.Activity
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class CreateMap(private val activity: Activity) {
  private var map: MapView? = null

  init {

    Configuration.getInstance().load(activity, PreferenceManager.getDefaultSharedPreferences(activity))
  }

  fun setMapView(map: MapView, str_title: String, dbl_Lat: Double?, dbl_Long: Double?, isOpenMapActivity: Boolean) {
    this.map = map

    init(str_title, dbl_Lat, dbl_Long, isOpenMapActivity)
  }

  private fun init(str_title: String, dbl_Lat: Double?, dbl_Long: Double?, isOpenMapActivity: Boolean) {
    map!!.setTileSource(TileSourceFactory.MAPNIK)

    //map.setBuiltInZoomControls(true);
    map!!.setMultiTouchControls(true)

    val mapController = map!!.controller
    mapController.setZoom(15.5)
    val startPoint = GeoPoint(dbl_Lat!!, dbl_Long!!)
    mapController.setCenter(startPoint)

    val startMarker = Marker(map!!)
    startMarker.position = startPoint
    startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

    startMarker.icon = ContextCompat.getDrawable(activity,R.mipmap.ic_launcher_round)
    startMarker.title = str_title

    if (isOpenMapActivity) {
      //startMarker.showInfoWindow();
      startMarker.setOnMarkerClickListener { marker, mapView ->
//        onClickMarker(str_title, dbl_Lat, dbl_Long)
        true
      }
    }

    map!!.overlays.add(startMarker)
  }

//  private fun onClickMarker(str_title: String, dbl_Lat: Double?, dbl_Long: Double?) {
//    val intent = Intent(activity, MapActivity::class.java)
//    intent.putExtra(MapActivity.KEY_POST_Title, str_title)
//    intent.putExtra(MapActivity.KEY_POST_LAT, dbl_Lat)
//    intent.putExtra(MapActivity.KEY_POST_LONG, dbl_Long)
//    activity.startActivity(intent)
//  }

  fun onResumeMap() {
    if (map != null)
      map!!.onResume()
  }

  fun onPauseMap() {
    if (map != null)
      map!!.onPause()
  }
}