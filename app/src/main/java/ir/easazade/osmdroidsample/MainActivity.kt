package ir.easazade.osmdroidsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.mapView

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val createMapView = CreateMap(this)
    createMapView.setMapView(mapView,"title",37.276658,49.580497,true)
  }
}
