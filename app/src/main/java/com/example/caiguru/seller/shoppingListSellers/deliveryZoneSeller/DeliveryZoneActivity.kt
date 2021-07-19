package com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.caiguru.R
import com.example.caiguru.databinding.ActivityDeliveryZoneBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*

import kotlinx.android.synthetic.main.activity_delivery_zone.*
import java.util.ArrayList

class DeliveryZoneActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val TAG = "DeliveryZoneActivity"
    }

    private var keySellerDeliveryZone = DeliveryZoneModel()
    private var googleMap: GoogleMap? = null
    lateinit var mbinding: ActivityDeliveryZoneBinding
    private lateinit var text: TextView
    private var longitudesource: Double = 1.0
    private var latitudesource: Double = 1.0
    var radius: Double = 0.5
    private  var txtkmStatics: String=""
    private var progressStatics: Int = 0
    internal var markerPoints: ArrayList<LatLng> = ArrayList()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_delivery_zone)

        SettingUpToolbar()
        if (intent.hasExtra("keySellerDeliveryZone")) {
            keySellerDeliveryZone =
                intent.getParcelableExtra<DeliveryZoneModel>("keySellerDeliveryZone")!!
            if (keySellerDeliveryZone.radius.isNotEmpty()) {
                seekBar.progress =
                    (keySellerDeliveryZone.radius.toDouble() + keySellerDeliveryZone.radius.toDouble()).toInt()
                val index = keySellerDeliveryZone.radius
                val index1 = index.toFloat()
                radius = ((index1  * 1000).toDouble())
                progressStatics=(keySellerDeliveryZone.radius.toDouble() + keySellerDeliveryZone.radius.toDouble()).toInt()
                txtKm.text = index + getString(R.string.km)
                txtkmStatics=index + getString(R.string.km)
            } else {
                seekBar.progress = 10
                progressStatics=10
                radius = ((10 / 2 * 1000).toDouble())
                txtKm.text = "5.0km"
                txtkmStatics="5.0km"
            }
        } else {
            seekBar.progress = 10
            progressStatics=10
            radius = ((10 / 2 * 1000).toDouble())
            txtKm.text = "5.0km"
            txtkmStatics="5.0km"
        }

        // Set a SeekBar change listener
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                val index = i.toFloat()
                progressStatics=index.toInt()
                txtkmStatics=(index / 2).toString()
                txtKm.text = (index / 2).toString() + getString(R.string.km)
                seekBar.max = 40
                radius = ((index / 2 * 1000).toDouble())

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                seekBar.clearFocus()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                drawCircle(LatLng(latitudesource, longitudesource), radius)

            }
        })

        this.mbinding.map.onCreate(savedInstanceState)

        try {
            //  mbinding.progressBarMap.visibility = View.VISIBLE
            MapsInitializer.initialize(this.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mbinding.map.onResume()
        mbinding.map.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //  fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

        mbinding.btndone.setOnClickListener {
            val deliveryZoneModel = DeliveryZoneModel()
            deliveryZoneModel.lat = latitudesource.toString()
            deliveryZoneModel.long = longitudesource.toString()
            val seekbar = (seekBar.progress).toFloat()
            deliveryZoneModel.radius = (seekbar / 2).toString()

            val intent = Intent()
            intent.putExtra("deliveryZone", deliveryZoneModel)
            intent.putExtra("lat", latitudesource.toString())
            intent.putExtra("long", longitudesource.toString())
//            Log.e("latitude=")
            setResult(Activity.RESULT_OK, intent)//set the result
            finish()
        }

        mbinding.currentLocation.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    //RequestBusinessFragment2.LOCATION_PERMISSION_REQUEST_CODE
                    100
                )

            } else {
                seekBar.progress =progressStatics
                txtKm.text = txtkmStatics

                fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        latitudesource = location.latitude
                        longitudesource = location.longitude
                        googleMap!!.clear()
                        setMap()
                        googleMap!!.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                currentLatLng,
                                12f
                            )
                        )
                        radius = radius
                        drawCircle(LatLng(latitudesource, longitudesource), radius)
                    }
                }
            }

        }

    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Delivery_Zone_choose)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)

    }

    //..........back button click...........//
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            if (item.itemId == android.R.id.home) {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Map is ready here
    // step 1
    override fun onMapReady(map: GoogleMap?) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
            if (location != null) {
                googleMap = map
                setupGoogleMapScreenSettings(googleMap)
                googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
                if (intent.hasExtra("keySellerDeliveryZone")) {
                    if (keySellerDeliveryZone.lat.isNotEmpty()) {
                        latitudesource = keySellerDeliveryZone.lat.toDouble()
                        longitudesource = keySellerDeliveryZone.long.toDouble()
                        googleMap!!.clear()
                        setMap()
                        Handler().postDelayed({
                            googleMap?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        latitudesource, longitudesource
                                    ), 11F
                                )
                            )
                            radius = keySellerDeliveryZone.radius.toDouble() * 1000
                            drawCircle(LatLng(latitudesource, longitudesource), radius)

                        }, 1000)

                    } else {
                        latitudesource = location.latitude
                        longitudesource = location.longitude
                        googleMap!!.clear()
                        setMap()
                        Handler().postDelayed({
                            googleMap?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        latitudesource, longitudesource
                                    ), 11F
                                )
                            )
                            radius = 5000.0
                            drawCircle(LatLng(latitudesource, longitudesource), radius)

                        }, 1000)
                    }
                } else {
                    latitudesource = location.latitude
                    longitudesource = location.longitude
                    googleMap!!.clear()
                    setMap()
                    Handler().postDelayed({
                        googleMap?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    latitudesource, longitudesource
                                ), 11F
                            )
                        )
                        radius = 5000.0
                        drawCircle(LatLng(latitudesource, longitudesource), radius)

                    }, 1000)
                }

                googleMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
                    override fun onMapClick(latlng: LatLng) {
                        latitudesource = latlng.latitude
                        longitudesource = latlng.longitude
                        googleMap!!.clear()
                        drawCircle(latlng, radius)
                        setMap()
                    }
                })
            }
        }
    }

    // map is set here
    // step 2
    fun setMap() {
        val lat1: Double?
        val longitude1: Double?
        val lat2: Double?
        val longitude2: Double?



        if (latitudesource != 1.0 && longitudesource != 1.0) {
            lat1 = latitudesource
            longitude1 = longitudesource

            val latLng = LatLng(lat1, longitude1)
            // val latLng1 = LatLng(lat2, longitude2)
            markerPoints.add(latLng)

            //   markerPoints.add(latLng1)
            val options = MarkerOptions()
            options.position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_choose_location_seller_icon))
            googleMap?.addMarker(options)

        }

    }


    private fun setupGoogleMapScreenSettings(googleMap: GoogleMap?) {
        googleMap?.isBuildingsEnabled = true
        googleMap?.isIndoorEnabled = true
        googleMap?.isTrafficEnabled = true

        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            googleMap?.isMyLocationEnabled = false
        } catch (e: Exception) {
        }

        val mUiSettings = googleMap?.uiSettings
        mUiSettings?.isCompassEnabled = true
        mUiSettings?.isMyLocationButtonEnabled = true
        mUiSettings?.isMyLocationButtonEnabled = true
        mUiSettings?.isScrollGesturesEnabled = true
        mUiSettings?.isZoomGesturesEnabled = true
        mUiSettings?.isTiltGesturesEnabled = true
        mUiSettings?.isRotateGesturesEnabled = true
        mUiSettings?.isMapToolbarEnabled = true
        mUiSettings?.isMapToolbarEnabled = true
    }


    // circle around marker
    fun drawCircle(point: LatLng, radius: Double) {
        // Instantiating CircleOptions to draw a circle around the marker
        val circleOptions = CircleOptions()

        // Specifying the center of the circle
        circleOptions.center(point)
        // Radius of the circle
        circleOptions.radius(radius)
        circleOptions.zIndex(14f)
        // Border color of the circle
        circleOptions.strokeColor(resources.getColor(R.color.circlestroke))
        // Fill color of the circle
        circleOptions.fillColor(0x30ffbf00)
        // Border width of the circle
        circleOptions.strokeWidth(2F)
        googleMap!!.clear()
        // Adding the circle to the GoogleMap
        googleMap!!.addCircle(circleOptions)
        setMap()

    }


}