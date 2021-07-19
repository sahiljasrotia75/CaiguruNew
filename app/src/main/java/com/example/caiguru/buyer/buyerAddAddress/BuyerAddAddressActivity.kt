package com.example.caiguru.buyer.buyerAddAddress

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
import com.example.caiguru.databinding.ActivityAddAddressBinding
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.Places.createClient
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_updated.*
import java.util.*

class BuyerAddAddressActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val TAG = "DeliveryZoneActivity"
    }

    private var optionalAddress = DeliveryZoneModel()
    private var addressArrayData = ArrayList<DeliveryZoneModel>()
    private var googleMap: GoogleMap? = null
    lateinit var mbinding: ActivityAddAddressBinding
    private lateinit var text: TextView
    private var longitudesource: Double = 1.0
    private var latitudesource: Double = 1.0
    var radius: Double = 0.5
    internal var markerPoints: ArrayList<LatLng> = ArrayList()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_add_address)

        SettingUpToolbar()
        this.mbinding.map.onCreate(savedInstanceState)
        try {
            MapsInitializer.initialize(this.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mbinding.map.onResume()
        mbinding.map.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mbinding.btndone.setOnClickListener {
            val deliveryZoneModel = DeliveryZoneModel()
            deliveryZoneModel.lat = latitudesource.toString()
            deliveryZoneModel.long = longitudesource.toString()


            val intent = Intent()
            intent.putExtra("deliveryZone", deliveryZoneModel)
            intent.putExtra("lat", latitudesource.toString())
            intent.putExtra("long", longitudesource.toString())
            setResult(Activity.RESULT_OK, intent)//set the result
            finish()
        }

        mbinding.search.setOnClickListener {
            Places.initialize(applicationContext, "AIzaSyCA14lCUfT4tb2gD7gUQ7NKeu9usiErZrM")
            val placesClient = createClient(this)
            val fields =
                listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

// Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields
                )
                .build(this)
            startActivityForResult(intent, 1)
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
                        getUpDatedAddress(latitudesource, longitudesource)
                        googleMap!!.clear()
                        setMap()
                        googleMap!!.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                currentLatLng,
                                12f
                            )
                        )
                        //set the circle
                        if (intent.hasExtra("keyOptionalDeliveryZone")) {
                            drawCircle(
                                LatLng(latitudesource, longitudesource),
                                Constant.SelllerRadiusLevel(this@BuyerAddAddressActivity)
                            )
                        }
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
        if (intent.hasExtra("choose")) {
            text.text = getText(R.string.Choose_a_pickup_address)
        } else if (intent.hasExtra("Select")) {
            text.text = getText(R.string.updated_address)
        } else {
            text.text = getText(R.string.Choose_the_Delivery_Zone)
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

    // step 1
    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        setupGoogleMapScreenSettings(googleMap)
        //googleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
        googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
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
//buyer fragment intent
                if (intent.hasExtra("keyBuyerAddress")) {
                    addressArrayData =
                        intent.getParcelableArrayListExtra<DeliveryZoneModel>("keyBuyerAddress")!!
                    if (addressArrayData.size > 0) {
                        for (item in addressArrayData) {
                            latitudesource = item.lat.toDouble()
                            longitudesource = item.long.toDouble()
                            getUpDatedAddress(latitudesource, longitudesource)
                            googleMap!!.clear()
                            setMap()
                            //set the circle
//                            drawCircle(
//                                LatLng(latitudesource, longitudesource),
//                                Constant.SelllerRadiusLevel(this)
//                            )
                        }
                    } else {
                        if (location != null) {
                            latitudesource = location.latitude
                            longitudesource = location.longitude
                            googleMap!!.clear()
                            setMap()
                            //set the circle
//                            drawCircle(
//                                LatLng(latitudesource, longitudesource),
//                                Constant.SelllerRadiusLevel(this)
//                            )
                        }
                    }
//seller side map open
                } else if (intent.hasExtra("keyOptionalDeliveryZone")) {
                    optionalAddress =
                        intent.getParcelableExtra<DeliveryZoneModel>("keyOptionalDeliveryZone")!!
                    if (optionalAddress.lat.isNotEmpty()) {
                        latitudesource = optionalAddress.lat.toDouble()
                        longitudesource = optionalAddress.long.toDouble()
                        getUpDatedAddress(latitudesource, longitudesource)
                        googleMap!!.clear()
                        setMap()
                        //set the circle
                        drawCircle(
                            LatLng(latitudesource, longitudesource),
                            Constant.SelllerRadiusLevel(this)
                        )
                    } else {
                        latitudesource = location.latitude
                        longitudesource = location.longitude
                        googleMap!!.clear()
                        setMap()
                        //set the circle
                        drawCircle(
                            LatLng(latitudesource, longitudesource),
                            Constant.SelllerRadiusLevel(this)
                        )
                    }
                }
                //**************dashboard side
                else if (intent.hasExtra("keyOptionalDeliveryZone1")) {
                    optionalAddress =
                        intent.getParcelableExtra<DeliveryZoneModel>("keyOptionalDeliveryZone1")!!
                    if (optionalAddress.lat.isNotEmpty()) {
                        latitudesource = optionalAddress.lat.toDouble()
                        longitudesource = optionalAddress.long.toDouble()
                        getUpDatedAddress(latitudesource, longitudesource)
                        googleMap!!.clear()
                        setMap()
                        //set the circle
//                        drawCircle(
//                            LatLng(latitudesource, longitudesource),
//                            Constant.SelllerRadiusLevel(this)
//                        )
                    } else {
                        latitudesource = location.latitude
                        longitudesource = location.longitude
                        googleMap!!.clear()
                        setMap()
                        //set the circle
//                        drawCircle(
//                            LatLng(latitudesource, longitudesource),
//                            Constant.SelllerRadiusLevel(this)
//                        )
                    }
                } else if (intent.hasExtra("keyPrefillAddres")) {
                    addressArrayData =
                        intent.getParcelableArrayListExtra<DeliveryZoneModel>("keyPrefillAddres") as ArrayList<DeliveryZoneModel>
                    if (addressArrayData.size > 0) {
                        for (item in addressArrayData) {
                            latitudesource = item.lat.toDouble()
                            longitudesource = item.long.toDouble()
                            getUpDatedAddress(latitudesource, longitudesource)
                            googleMap!!.clear()
                            setMap()
                            //set the circle
//                            drawCircle(
//                                LatLng(latitudesource, longitudesource),
//                                Constant.SelllerRadiusLevel(this)
//                            )
                        }
                    } else {
                        latitudesource = location.latitude
                        longitudesource = location.longitude
                        getUpDatedAddress(latitudesource, longitudesource)
                        googleMap!!.clear()
                        setMap()
                        //set the circle
//                        drawCircle(
//                            LatLng(latitudesource, longitudesource),
//                            Constant.SelllerRadiusLevel(this)
//                        )
                    }

                } else {
                    latitudesource = location.latitude
                    longitudesource = location.longitude
                    getUpDatedAddress(latitudesource, longitudesource)
                    googleMap!!.clear()
                    setMap()
                    //set the circle
                    drawCircle(
                        LatLng(latitudesource, longitudesource),
                        Constant.SelllerRadiusLevel(this)
                    )
                }

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        Handler().postDelayed({
            googleMap?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        latitudesource, longitudesource
                    ), 11F
                )
            )
            radius = 5000.0

        }, 1000)

        googleMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {

            override fun onMapClick(latlng: LatLng) {
                latitudesource = latlng.latitude
                longitudesource = latlng.longitude
                getUpDatedAddress(latitudesource, longitudesource)
                googleMap!!.clear()
                setMap()
                //set the circle
                if (intent.hasExtra("keyOptionalDeliveryZone")) {
                    drawCircle(
                        LatLng(latitudesource, longitudesource),
                        Constant.SelllerRadiusLevel(this@BuyerAddAddressActivity)
                    )
                }
            }
        })
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
            markerPoints.add(latLng)
            val options = MarkerOptions()
            options.position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.locationmap))
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
                    getUpDatedAddress(latitudesource, longitudesource)
                    googleMap!!.clear()
                    setMap()
                    googleMap!!.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            currentLatLng,
                            12f
                        )
                    )
                    //set the circle
                    drawCircle(
                        LatLng(latitudesource, longitudesource),
                        Constant.SelllerRadiusLevel(this)
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

    fun getUpDatedAddress(lat: Double, long: Double) {
        try {
            val geocoder = Geocoder(this)
            val address: Address?
            if (lat != null) {
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
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    // circle around marker
    fun drawCircle(point: LatLng, radius: Double) {
        // Instantiating CircleOptions to draw a circle around the marker
        val circleOptions = CircleOptions();

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