package com.example.caiguru.buyer.updatedAddress

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.caiguru.R
import com.example.caiguru.databinding.ActivityUpdatedBinding
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.activity_updated.*

class UpdatedAddressActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val TAG = "UpdatedAddressActivity"
    }

    private var addressLatLong = DeliveryZoneModel()
    private var googleMap: GoogleMap? = null
    lateinit var mbinding: ActivityUpdatedBinding
    private lateinit var text: TextView
    private var longitudesource: Double = 1.0
    private var latitudesource: Double = 1.0


    internal var markerPoints: ArrayList<LatLng> = ArrayList()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_updated)
        SettingUpToolbar()

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
            //deliveryZoneModel.radius = seekBar.progress.toString()


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

                        //  radius = 5000.0
                        //     drawCircle(LatLng(latitudesource, longitudesource), radius)

                    }
                }
            }

        }
        mbinding.search.setOnClickListener {
            Places.initialize(applicationContext, "AIzaSyCA14lCUfT4tb2gD7gUQ7NKeu9usiErZrM")
            val placesClient = Places.createClient(this)
            val fields =
                listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

// Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            )
                .build(this)
            startActivityForResult(intent, 1)
        }

//        mbinding.txtcancle.setOnClickListener {
//            Constant.hideSoftKeyboard(it, this)
//            mbinding.search.clearFocus()
//        }


    }
    fun getUpDatedAddress(lat: Double, long: Double) {
        val geocoder = Geocoder(this)
        val address: Address?
        if (this.latitudesource != null) {
            val latitudeResult = lat
            val longitudeResult = long
            // 2
            val addresses =
                geocoder.getFromLocation(
                    latitudeResult,
                    longitudeResult,
                    1
                )
            address = addresses[0]
            Log.e("Map Fragment ", address.toString())
            val builder = StringBuilder()
            builder.append(address?.getAddressLine(0)).append(",\t")
            val resultDestination = builder.toString()
            search.text = resultDestination
        }
    }

    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        if (intent.hasExtra("Select")) {
            text.text = getText(R.string.select_address)
        } else {
            text.text = getText(R.string.updated_address)
        }

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
            try {
                if (intent.hasExtra("keyProfileBuyerLat")) {
                    var lat = intent.getStringExtra("keyProfileBuyerLat")
                    var long = intent.getStringExtra("keyProfileBuyerLong")
                    if (lat!!.isNotEmpty()) {
                        googleMap = map
                        setupGoogleMapScreenSettings(googleMap)
                        //googleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
                        latitudesource = lat.toDouble()
                        longitudesource = long!!.toDouble()
                        getUpDatedAddress(latitudesource,longitudesource)
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
                        }, 1000)


                    } else {

                        googleMap = map
                        setupGoogleMapScreenSettings(googleMap)
                        //googleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
                        latitudesource = location.latitude
                        longitudesource = location.longitude
                        getUpDatedAddress(latitudesource,longitudesource)
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
                        }, 1000)
                    }
                    //buyer profile address fillup
                } else if (intent.hasExtra("keyProfileSellerLat")) {
                    val lat = intent.getStringExtra("keyProfileSellerLat")
                    val long = intent.getStringExtra("keyProfileSellerLong")
                    if (lat!!.isNotEmpty()) {
                        googleMap = map
                        setupGoogleMapScreenSettings(googleMap)
                        //googleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
                        latitudesource = lat.toDouble()
                        longitudesource = long!!.toDouble()
                        getUpDatedAddress(latitudesource,longitudesource)
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
                        }, 1000)


                    } else {

                        googleMap = map
                        setupGoogleMapScreenSettings(googleMap)
                        //googleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
                        latitudesource = location.latitude
                        longitudesource = location.longitude
                        getUpDatedAddress(latitudesource,longitudesource)
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
                        }, 1000)
                    }
                }
                //choose seller prefill map
                else if (intent.hasExtra("keyChooseSellerLat")) {
                    var lat = intent.getStringExtra("keyChooseSellerLat")
                    var long = intent.getStringExtra("keyChooseSellerLong")
                    if (lat!!.isNotEmpty()) {
                        googleMap = map
                        setupGoogleMapScreenSettings(googleMap)
                        //googleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
                        latitudesource = lat.toDouble()
                        longitudesource = long!!.toDouble()
                        getUpDatedAddress(latitudesource,longitudesource)
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
                        }, 1000)

                    } else {
                        googleMap = map
                        setupGoogleMapScreenSettings(googleMap)
                        //googleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
                        latitudesource = location.latitude
                        longitudesource = location.longitude
                        getUpDatedAddress(latitudesource,longitudesource)
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
                        }, 1000)
                    }
                } else {

                    googleMap = map
                    setupGoogleMapScreenSettings(googleMap)
                    //googleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
                    googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
                    latitudesource = location.latitude
                    longitudesource = location.longitude
                    getUpDatedAddress(latitudesource,longitudesource)
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
                    }, 1000)
                }

//            if (location != null) {
//                googleMap = map
//                setupGoogleMapScreenSettings(googleMap)
//                //googleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
//                googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
//                latitudesource = location.latitude
//                longitudesource = location.longitude
//                setMap()
//                Handler().postDelayed({
//                    googleMap?.moveCamera(
//                        CameraUpdateFactory.newLatLngZoom(
//                            LatLng(
//                                latitudesource, longitudesource
//                            ), 11F
//                        )
//                    )
//                }, 1000)
                googleMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
                    override fun onMapClick(latlng: LatLng) {

                        latitudesource = latlng.latitude
                        longitudesource = latlng.longitude
                        getUpDatedAddress(latitudesource,longitudesource)
                        googleMap!!.clear()
                        setMap()


                    }

                })

            } catch (e: Exception) {
                e.printStackTrace()
//                showAlertLocationEnabled()
//                Toast.makeText(this, "please open Your location", Toast.LENGTH_SHORT).show()
            }
        }


    }

//    private fun showAlertLocationEnabled() {
//        val mDialog = AlertDialog.Builder(this)
//            .setTitle(getString(R.string.alert))
//            .setMessage(getString(R.string.User_location_are_required))
//            .setCancelable(false)
//
//        mDialog.setPositiveButton(
//            android.R.string.ok,
//            DialogInterface.OnClickListener { dialog, which ->
//                startActivityForResult(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0)

//                val i = Intent()
//                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                i.addCategory(Intent.CATEGORY_DEFAULT)
//                i.setData(Uri.parse("package:" + this.getPackageName()))
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
//                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
//                this!!.startActivity(i)

    //})
//        mDialog.setNegativeButton(
//            android.R.string.no,
//            DialogInterface.OnClickListener { dialog, which ->
//                ActivityCompat.requestPermissions(
//                    this!!, arrayOf(
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                    ), 112
//                )
//
//                dialog.cancel()
//
//            })
//        mDialog.show()
//    }

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

//            lat2 = data2.latitude.toDouble()
//            longitude2 = data2.longitude.toDouble()

            val latLng = LatLng(lat1, longitude1)
            // val latLng1 = LatLng(lat2, longitude2)

            markerPoints.add(latLng)

            //   markerPoints.add(latLng1)
            val options = MarkerOptions()
            options.position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.locationmap))
            // options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.fromResource(R.mipmap.location)))
            // options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.l))
            /*if (markerPoints.size == 1) {
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
    } else if (markerPoints.size == 2) {
        //options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
    }*/
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

                return
            }
            googleMap?.isMyLocationEnabled = false
        } catch (e: Exception) {

        }


        val mUiSettings = googleMap?.uiSettings
        //mUiSettings?.isZoomControlsEnabled = true
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                // mbinding.search.text=place.address
                if (place.latLng!!.latitude != null) {
                    val currentLatLng = LatLng(place.latLng!!.latitude, place.latLng!!.longitude)
                    latitudesource = place.latLng!!.latitude
                    longitudesource = place.latLng!!.longitude
                    getUpDatedAddress(latitudesource,longitudesource)
                    googleMap!!.clear()
                    setMap()
                    googleMap!!.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            currentLatLng,
                            12f
                        )
                    )
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status = Autocomplete.getStatusFromIntent(data!!)
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}