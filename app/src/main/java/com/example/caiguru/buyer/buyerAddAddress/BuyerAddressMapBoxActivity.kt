package com.example.caiguru.buyer.buyerAddAddress

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.caiguru.R
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.seller.sellerSetting.sellerProfile.sellerEditProfile.SellerEditProfileViewModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineCallback
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.location.LocationEngineResult
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.geojson.*
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener
import com.mapbox.mapboxsdk.location.OnLocationClickListener
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.mapbox.turf.TurfConstants
import com.mapbox.turf.TurfMeta
import com.mapbox.turf.TurfTransformation
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_buyer_address_map_box.*
import kotlinx.android.synthetic.main.edit_map_address_bottom_sheet.*
import kotlinx.android.synthetic.main.requested_list_custom_dialog.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class BuyerAddressMapBoxActivity : AppCompatActivity(), OnMapReadyCallback,
    OnLocationClickListener, MapboxMap.OnMapClickListener, PermissionsListener,
    OnCameraTrackingChangedListener, LocationEngineCallback<LocationEngineResult> {
    private lateinit var mvmodel: SellerEditProfileViewModel
    private var optionalAddress = DeliveryZoneModel()
    private var addressArrayData = ArrayList<DeliveryZoneModel>()
    private lateinit var text: TextView
    private var longitudesource: Double = 1.0
    private var latitudesource: Double = 1.0
    private var lastClickPoint: Point? = null
    private lateinit var locationEngine: LocationEngine
    private val geojsonSourceLayerId = "geojsonSourceLayerId"
    private var permissionsManager: PermissionsManager? = null
    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null
    private var circleRadius = 0.5
    private var isInTrackingMode = false
    private var circleUnit: String = TurfConstants.UNIT_METERS
    private var locationComponent: LocationComponent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mapbox access token is configured here. This needs to be called either in your application
        Mapbox.getInstance(this, getString(R.string.Key))
        setContentView(R.layout.activity_buyer_address_map_box)
        mvmodel = ViewModelProvider(this).get(SellerEditProfileViewModel::class.java)

        SettingUpToolbar()
        mapView = findViewById(R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
        MapTypeBottomSheetVisible()
        setButtonClick()
        bottomSheetAddress.filters = arrayOf<InputFilter>(HideEmoji(this))
        setObserVer()
    }

    private fun setObserVer() {
        mvmodel.mSucessFulClearCart().observe(this, androidx.lifecycle.Observer {
            btndones.text = getString(R.string.done)
            btndones.isClickable = true
            val deliveryZoneModel = DeliveryZoneModel()
            deliveryZoneModel.lat = latitudesource.toString()
            deliveryZoneModel.long = longitudesource.toString()
            deliveryZoneModel.address = searchs.text.toString()

            val intent = Intent()
            intent.putExtra("deliveryZone", deliveryZoneModel)
            intent.putExtra("lat", latitudesource.toString())
            intent.putExtra("long", longitudesource.toString())
            setResult(Activity.RESULT_OK, intent)//set the result
            finish()

        })

        mvmodel.mErrorClearCart().observe(this, androidx.lifecycle.Observer {
            btndones.text = getString(R.string.done)
            btndones.isClickable = true

        })

    }

    private fun setButtonClick() {
        //********done button click
        btndones.setOnClickListener {
            if (intent.hasExtra("keyOptionalDeliveryZone1")) {
                optionalAddress =
                    intent.getParcelableExtra<DeliveryZoneModel>("keyOptionalDeliveryZone1")!!
                if (optionalAddress.lat == latitudesource.toString() && optionalAddress.long == longitudesource.toString()) {
                    val deliveryZoneModel = DeliveryZoneModel()
                    deliveryZoneModel.lat = latitudesource.toString()
                    deliveryZoneModel.long = longitudesource.toString()
                    deliveryZoneModel.address = searchs.text.toString()

                    val intent = Intent()
                    intent.putExtra("deliveryZone", deliveryZoneModel)
                    intent.putExtra("lat", latitudesource.toString())
                    intent.putExtra("long", longitudesource.toString())
                    setResult(Activity.RESULT_OK, intent)//set the result
                    finish()
                } else {
                    if (Constant.getPrefs(application).getString(Constant.is_has_cart, "")=="1"
                    ) {
                        setAlertDialog()
                    }else{
                        val deliveryZoneModel = DeliveryZoneModel()
                        deliveryZoneModel.lat = latitudesource.toString()
                        deliveryZoneModel.long = longitudesource.toString()
                        deliveryZoneModel.address = searchs.text.toString()

                        val intent = Intent()
                        intent.putExtra("deliveryZone", deliveryZoneModel)
                        intent.putExtra("lat", latitudesource.toString())
                        intent.putExtra("long", longitudesource.toString())
                        setResult(Activity.RESULT_OK, intent)//set the result
                        finish()
                    }


                }

            }
            //buyer profile check
            else {
                val deliveryZoneModel = DeliveryZoneModel()
                deliveryZoneModel.lat = latitudesource.toString()
                deliveryZoneModel.long = longitudesource.toString()
                deliveryZoneModel.address = searchs.text.toString()

                val intent = Intent()
                intent.putExtra("deliveryZone", deliveryZoneModel)
                intent.putExtra("lat", latitudesource.toString())
                intent.putExtra("long", longitudesource.toString())
                setResult(Activity.RESULT_OK, intent)//set the result
                finish()
            }

        }

        //********done button click on toolbar
        done.setOnClickListener {

            if (bottomSheetAddress.text.isEmpty()) {
                Constant.showToast(getString(R.string.please_enter_your_address), this)
                return@setOnClickListener
            } else if (intent.hasExtra("keyProfileSellerLat")) {
                val lat = intent.getStringExtra("keyProfileSellerLat")
                val long = intent.getStringExtra("keyProfileSellerLong")
                if (lat == latitudesource.toString() && long == longitudesource.toString()) {
                    val deliveryZoneModel = DeliveryZoneModel()
                    deliveryZoneModel.lat = latitudesource.toString()
                    deliveryZoneModel.long = longitudesource.toString()
                    deliveryZoneModel.address = searchs.text.toString()

                    val intent = Intent()
                    intent.putExtra("deliveryZone", deliveryZoneModel)
                    intent.putExtra("lat", latitudesource.toString())
                    intent.putExtra("long", longitudesource.toString())
                    setResult(Activity.RESULT_OK, intent)//set the result
                    finish()
                } else {
                    if (Constant.getPrefs(application).getString(Constant.is_has_cart, "")=="1"
                    ) {
                        setAlertDialog()
                    }else{
                        val deliveryZoneModel = DeliveryZoneModel()
                        deliveryZoneModel.lat = latitudesource.toString()
                        deliveryZoneModel.long = longitudesource.toString()
                        deliveryZoneModel.address = searchs.text.toString()

                        val intent = Intent()
                        intent.putExtra("deliveryZone", deliveryZoneModel)
                        intent.putExtra("lat", latitudesource.toString())
                        intent.putExtra("long", longitudesource.toString())
                        setResult(Activity.RESULT_OK, intent)//set the result
                        finish()
                    }


                }

            }
            //buyer profile check
            else if (intent.hasExtra("keyProfileBuyerLat")) {
                val lat = intent.getStringExtra("keyProfileBuyerLat")
                val long = intent.getStringExtra("keyProfileBuyerLat")
                if (lat == latitudesource.toString() && long == longitudesource.toString()) {
                    val deliveryZoneModel = DeliveryZoneModel()
                    deliveryZoneModel.lat = latitudesource.toString()
                    deliveryZoneModel.long = longitudesource.toString()
                    deliveryZoneModel.address = searchs.text.toString()

                    val intent = Intent()
                    intent.putExtra("deliveryZone", deliveryZoneModel)
                    intent.putExtra("lat", latitudesource.toString())
                    intent.putExtra("long", longitudesource.toString())
                    setResult(Activity.RESULT_OK, intent)//set the result
                    finish()
                } else {
                    if (Constant.getPrefs(application).getString(Constant.is_has_cart, "")=="1"
                    ) {
                        setAlertDialog()
                    }else{
                        val deliveryZoneModel = DeliveryZoneModel()
                        deliveryZoneModel.lat = latitudesource.toString()
                        deliveryZoneModel.long = longitudesource.toString()
                        deliveryZoneModel.address = searchs.text.toString()

                        val intent = Intent()
                        intent.putExtra("deliveryZone", deliveryZoneModel)
                        intent.putExtra("lat", latitudesource.toString())
                        intent.putExtra("long", longitudesource.toString())
                        setResult(Activity.RESULT_OK, intent)//set the result
                        finish()
                    }


                }

            } else {
                val deliveryZoneModel = DeliveryZoneModel()
                deliveryZoneModel.lat = latitudesource.toString()
                deliveryZoneModel.long = longitudesource.toString()
                deliveryZoneModel.address = searchs.text.toString()

                val intent = Intent()
                intent.putExtra("deliveryZone", deliveryZoneModel)
                intent.putExtra("lat", latitudesource.toString())
                intent.putExtra("long", longitudesource.toString())
                setResult(Activity.RESULT_OK, intent)//set the result
                finish()
            }


        }
    }

    private fun MapTypeBottomSheetVisible() {
        if (intent.hasExtra("MapType")) {
            btndones.visibility = View.GONE
            done.visibility = View.VISIBLE
            editAddressLayout.visibility = View.VISIBLE
        } else {
            btndones.visibility = View.VISIBLE
            done.visibility = View.GONE
            editAddressLayout.visibility = View.INVISIBLE
        }
    }

    //************1st step on map ready call
    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(Style.MAPBOX_STREETS) { style -> enableLocationComponent(style) }
        mapboxMap.addOnMapClickListener(this)
        initSearchFab()
    }

    private fun enableLocationComponent(loadedMapStyle: Style) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            locationComponent = mapboxMap!!.locationComponent
            val locationComponent = mapboxMap!!.locationComponent

            // Set the LocationComponent activation options
            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(this, loadedMapStyle)
                    .useDefaultLocationEngine(false)
                    .build()

            // Activate with the LocationComponentActivationOptions object
            locationComponent.activateLocationComponent(locationComponentActivationOptions)
            // Enable to make component visible
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

            locationComponent.isLocationComponentEnabled = true
            // Set the component's camera mode
            locationComponent.cameraMode = CameraMode.TRACKING
            val mUiSettings = mapboxMap?.uiSettings
            mapboxMap?.getUiSettings()?.isLogoEnabled = false
            mapboxMap?.getUiSettings()?.setAttributionEnabled(false)
            mUiSettings?.isCompassEnabled = false
            // Set the component's render mode
            // Add the location icon click listener
            locationComponent.addOnLocationClickListener(this)
            // Add the camera tracking listener. Fires if the map camera is manually moved.
            locationComponent.addOnCameraTrackingChangedListener(this)
            //set the click to find the current location
            findViewById<View>(R.id.currentLocations).setOnClickListener {
                if (intent.hasExtra("keyOptionalDeliveryZone")) {
                    circleRadius = Constant.SelllerRadiusLevel(this)
                    initLocationEngine()
                } else {
                    initLocationEngine()
                }

            }
            //**********************prefilling case******************//
            try {
//buyer fragment intent
                if (intent.hasExtra("keyBuyerAddress")) {
                    addressArrayData =
                        intent.getParcelableArrayListExtra<DeliveryZoneModel>("keyBuyerAddress") as ArrayList<DeliveryZoneModel>
                    if (addressArrayData.size > 0) {
                        for (item in addressArrayData) {
                            latitudesource = item.lat.toDouble()
                            longitudesource = item.long.toDouble()
                            getUpDatedAddress(latitudesource, longitudesource)
                            lastClickPoint =
                                Point.fromLngLat(
                                    longitudesource,
                                    latitudesource
                                )//this is for circle
                            setMap()
                            Handler().postDelayed({
                                showMapMarkerFirstTimeAndMapAsync()//show thw marker first time in a map
                            }, 1000)
                        }
                    } else {
                        initLocationEngine()//get the current location

                    }
//seller side map open
                }
                //circle radius set  in one
                else if (intent.hasExtra("keyOptionalDeliveryZone")) {
                    optionalAddress =
                        intent.getParcelableExtra<DeliveryZoneModel>("keyOptionalDeliveryZone")!!
                    if (optionalAddress.lat.isNotEmpty()) {
                        latitudesource = optionalAddress.lat.toDouble()
                        longitudesource = optionalAddress.long.toDouble()
                        circleRadius = Constant.SelllerRadiusLevel(this)
                        getUpDatedAddress(latitudesource, longitudesource)
                        lastClickPoint =
                            Point.fromLngLat(longitudesource, latitudesource)//this is for circle
                        setMap()
                        Handler().postDelayed({
                            showMapMarkerFirstTimeAndMapAsync()//show thw marker first time in a map
                        }, 1000)


                    } else {
                        initLocationEngine()//get the current location
                        //set the circle radius
                        circleRadius = Constant.SelllerRadiusLevel(this)

                    }
                }
                //**************dashboard side
                else if (intent.hasExtra("keyOptionalDeliveryZone1")) {
                    optionalAddress =
                        intent.getParcelableExtra<DeliveryZoneModel>("keyOptionalDeliveryZone1")!!
                    if (optionalAddress.lat.isNotEmpty()) {
                        latitudesource = optionalAddress.lat.toDouble()
                        longitudesource = optionalAddress.long.toDouble()
                        //  getUpDatedAddress(latitudesource, longitudesource)
                        setAddressCustom(optionalAddress.address)
                        lastClickPoint =
                            Point.fromLngLat(longitudesource, latitudesource)//this is for circle
                        setMap()
                        Handler().postDelayed({
                            showMapMarkerFirstTimeAndMapAsync()//show thw marker first time in a map
                        }, 1000)

                    } else {
                        initLocationEngine()//get the current location

                    }
                } else if (intent.hasExtra("keyPrefillAddres")) {
                    addressArrayData =
                        intent.getParcelableArrayListExtra<DeliveryZoneModel>("keyPrefillAddres")!!
                    if (addressArrayData.size > 0) {
                        for (item in addressArrayData) {
                            latitudesource = item.lat.toDouble()
                            longitudesource = item.long.toDouble()

                            getUpDatedAddress(latitudesource, longitudesource)
                            lastClickPoint =
                                Point.fromLngLat(
                                    longitudesource,
                                    latitudesource
                                )//this is for circle
                            setMap()
                            Handler().postDelayed({
                                showMapMarkerFirstTimeAndMapAsync()//show thw marker first time in a map
                            }, 1000)
                        }
                    } else {
                        initLocationEngine()//get the current location
                    }

                }                //2nd activity data
                else if (intent.hasExtra("keyProfileBuyerLat")) {
                    val lat = intent.getStringExtra("keyProfileBuyerLat")
                    val long = intent.getStringExtra("keyProfileBuyerLong")
                    if (lat!!.isNotEmpty()) {
                        latitudesource = lat.toDouble()
                        longitudesource = long!!.toDouble()
                        if (intent.hasExtra("MapType")) {
                            val address = intent.getStringExtra("MapType")
                            address?.let { setAddressCustom(it) }
                        } else {
                            getUpDatedAddress(latitudesource, longitudesource)
                        }
                        // getUpDatedAddress(latitudesource, longitudesource)
                        lastClickPoint =
                            Point.fromLngLat(
                                longitudesource,
                                latitudesource
                            )//this is for circle
                        setMap()
                        Handler().postDelayed({
                            showMapMarkerFirstTimeAndMapAsync()//show thw marker first time in a map
                        }, 1000)
                    } else {
                        initLocationEngine()//get the current location

                    }
                    //buyer profile address fillup
                } else if (intent.hasExtra("keyProfileSellerLat")) {
                    val lat = intent.getStringExtra("keyProfileSellerLat")
                    val long = intent.getStringExtra("keyProfileSellerLong")
                    if (lat!!.isNotEmpty()) {
                        latitudesource = lat.toDouble()
                        longitudesource = long!!.toDouble()
                        if (intent.hasExtra("MapType")) {
                            val address = intent.getStringExtra("MapType")
                            address?.let { setAddressCustom(it) }
                        } else {
                            getUpDatedAddress(latitudesource, longitudesource)
                        }
                        lastClickPoint =
                            Point.fromLngLat(
                                longitudesource,
                                latitudesource
                            )//this is for circle
                        setMap()
                        Handler().postDelayed({
                            showMapMarkerFirstTimeAndMapAsync()//show thw marker first time in a map
                        }, 1000)

                    } else {

                        initLocationEngine()//get the current location
                    }
                }
                //choose seller prefill map
                else if (intent.hasExtra("keyChooseSellerLat")) {
                    val lat = intent.getStringExtra("keyChooseSellerLat")
                    val long = intent.getStringExtra("keyChooseSellerLong")
                    if (lat!!.isNotEmpty()) {
                        latitudesource = lat.toDouble()
                        longitudesource = long!!.toDouble()
                        if (intent.hasExtra("MapType")) {
                            val address = intent.getStringExtra("MapType")
                            address?.let { setAddressCustom(it) }
                        } else {
                            getUpDatedAddress(latitudesource, longitudesource)
                        }


                        lastClickPoint =
                            Point.fromLngLat(
                                longitudesource,
                                latitudesource
                            )//this is for circle
                        setMap()
                        Handler().postDelayed({
                            showMapMarkerFirstTimeAndMapAsync()//show thw marker first time in a map
                        }, 1000)

                    } else {
                        initLocationEngine()//get the current location
                    }
                } else {
                    initLocationEngine()//get the current location
                }

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }


        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager!!.requestLocationPermissions(this)
        }
    }


    //************1st step on map ready call

    //get the current location

    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this)
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
        locationEngine.getLastLocation(this)
    }

    //marker click
    override fun onMapClick(point: LatLng): Boolean {
        if (point.latitude != null) {
            latitudesource = point.latitude
            longitudesource = point.longitude
            setMap()
            lastClickPoint = Point.fromLngLat(
                point.longitude,
                point.latitude
            )
            getUpDatedAddress(latitudesource, longitudesource)//get the adress
            //set the circle
            if (intent.hasExtra("keyOptionalDeliveryZone")) {
                circleRadius = Constant.SelllerRadiusLevel(this)
                moveCircleCenterMarker(lastClickPoint!!)// marker set
                drawPolygonCircle(lastClickPoint!!)//circle set  with radius
            } else {
                moveCircleCenterMarker(lastClickPoint!!)//only marker set
            }
        }

        return true
    }

    //***********************first time map current location get****************//
    override fun onSuccess(result: LocationEngineResult?) {
        try {
            if (result != null) {
                latitudesource = result.lastLocation!!.latitude
                longitudesource = result.lastLocation!!.longitude
                lastClickPoint = Point.fromLngLat(longitudesource, latitudesource)
                getUpDatedAddress(latitudesource, longitudesource)
                setMap()
                showMapMarkerFirstTimeAndMapAsync()//show thw marker first time in a map
            }
        } catch (e: Exception) {
            e.printStackTrace()
            initLocationEngine()//get the current location
        }

    }

    //***********location failure
    override fun onFailure(exception: Exception) {
        Constant.showToast(getString(R.string.network_error), this)
        // Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
    }

    //*************show the marker on map
    private fun showMapMarkerFirstTimeAndMapAsync() {
        mapView?.getMapAsync(OnMapReadyCallback { mapboxMap ->
            mapboxMap.setStyle(
                Style.Builder()
                    .fromUri(Style.MAPBOX_STREETS)
                    .withImage(
                        CIRCLE_CENTER_ICON_ID,
                        BitmapUtils.getBitmapFromDrawable(
                            resources.getDrawable(R.mipmap.locationmap)
                        )!!
                    )
                    .withSource(
                        GeoJsonSource(
                            CIRCLE_CENTER_SOURCE_ID,
                            Feature.fromGeometry(lastClickPoint)
                        )
                    )
                    .withSource(GeoJsonSource(TURF_CALCULATION_FILL_LAYER_GEOJSON_SOURCE_ID))
                    .withLayer(
                        SymbolLayer(
                            CIRCLE_CENTER_LAYER_ID,
                            CIRCLE_CENTER_SOURCE_ID
                        ).withProperties(
                            PropertyFactory.iconImage(
                                CIRCLE_CENTER_ICON_ID
                            ),
                            PropertyFactory.iconIgnorePlacement(
                                true
                            ),
                            PropertyFactory.iconAllowOverlap(
                                true
                            ),
                            PropertyFactory.iconOffset(
                                arrayOf(
                                    0f,
                                    -4f
                                )
                            )
                        )
                    )
            ) {
                //set the circle
                if (intent.hasExtra("keyOptionalDeliveryZone")) {
                    initPolygonCircleFillLayer()//set the polygon circle style
                    drawPolygonCircle(lastClickPoint!!)
                }

            }
        })
    }

    //****************************set the map circle code*****************//
//Add a [FillLayer] to display a [Polygon] in a the shape of a circle.
    private fun initPolygonCircleFillLayer() {
        mapboxMap!!.getStyle { style -> // Create and style a FillLayer based on information that will come from the Turf calculation
            val fillLayer = FillLayer(
                TURF_CALCULATION_FILL_LAYER_ID,
                TURF_CALCULATION_FILL_LAYER_GEOJSON_SOURCE_ID
            )
            fillLayer.setProperties(
                PropertyFactory.fillColor(
                    resources.getColor(R.color.circlestroke)
                ),
                PropertyFactory.fillOpacity(.5f)
                // ,PropertyFactory.circleStrokeWidth(2F)
            )

            style.addLayerBelow(
                fillLayer,
                CIRCLE_CENTER_LAYER_ID
            )
        }
    }


    //set the polygon circle
    private fun drawPolygonCircle(circleCenter: Point) {
        try {

            mapboxMap!!.getStyle { style -> // Use Turf to calculate the Polygon's coordinates
                val polygonArea =
                    getTurfPolygon(circleCenter, circleRadius, circleUnit)
                val polygonCircleSource =
                    style.getSourceAs<GeoJsonSource>(TURF_CALCULATION_FILL_LAYER_GEOJSON_SOURCE_ID)
                polygonCircleSource?.setGeoJson(
                    Polygon.fromOuterInner(
                        LineString.fromLngLats(TurfMeta.coordAll(polygonArea, false))
                    )
                )
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun getTurfPolygon(
        centerPoint: Point, radius: Double, units: String
    ): Polygon {
        return TurfTransformation.circle(centerPoint, radius, units)
    }

    //**************move the circle center on click*************//
    private fun moveCircleCenterMarker(circleCenter: Point) {
        mapboxMap!!.getStyle { style -> // Use Turf to calculate the Polygon's coordinates
            val markerSource =
                style.getSourceAs<GeoJsonSource>(CIRCLE_CENTER_SOURCE_ID)
            markerSource?.setGeoJson(circleCenter)
        }
    }

    //*************setup the map everytime
    fun setMap() {
        val lat1: Double?
        val longitude1: Double?
        try {
            if (latitudesource != 1.0 && longitudesource != 1.0) {
                lat1 = latitudesource
                longitude1 = longitudesource
                mapboxMap?.clear()
                val latLng = LatLng(lat1, longitude1)
                mapboxMap!!.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder()
                            .target(
                                LatLng(
                                    latitudesource, longitudesource
                                )
                            )
                            .zoom(12.0)
                            .build()
                    ), 3000

                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //**************************search the places*********************//
    private fun initSearchFab() {
        findViewById<View>(R.id.searchs).setOnClickListener {
            val intent: Intent = PlaceAutocomplete.IntentBuilder()
                .accessToken(
                    (if (Mapbox.getAccessToken() != null) Mapbox.getAccessToken() else getString(R.string.Key)).toString()
                )
                .placeOptions(
                    PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .build(PlaceOptions.MODE_CARDS)
                )
                .build(this)
            startActivityForResult(
                intent,
                REQUEST_CODE_AUTOCOMPLETE
            )
        }
    }

    //*****************get the address
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
                searchs.text = resultDestination
                bottomSheetAddress.setText(resultDestination) //bottom sheet text
//                val fragment = MenuFragment(resultDestination)
//                fragment.isCancelable = true
//                fragment.show(supportFragmentManager, fragment.tag)
            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    //******************************get the seacrh places result*******************************//
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            // Retrieve selected location's CarmenFeature
            val selectedCarmenFeature: CarmenFeature = PlaceAutocomplete.getPlace(data)
            // Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
            // Then retrieve and update the source designated for showing a selected location's symbol layer icon
            if (mapboxMap != null) {
                val style = mapboxMap!!.style
                if (style != null) {
                    val source =
                        style.getSourceAs<GeoJsonSource>(geojsonSourceLayerId)
                    source?.setGeoJson(
                        FeatureCollection.fromFeatures(
                            arrayOf(
                                Feature.fromJson(selectedCarmenFeature.toJson())
                            )
                        )
                    )
                    latitudesource = (selectedCarmenFeature.geometry() as Point).latitude()
                    longitudesource = (selectedCarmenFeature.geometry() as Point).longitude()
                    setMap()
                    getUpDatedAddress(latitudesource, longitudesource)//get the adress
                    lastClickPoint = Point.fromLngLat(longitudesource, latitudesource)
                    if (intent.hasExtra("keyOptionalDeliveryZone")) {
                        moveCircleCenterMarker(lastClickPoint!!)
                        drawPolygonCircle(lastClickPoint!!)
                    } else {
                        moveCircleCenterMarker(lastClickPoint!!)
                    }


                }
            }
        }
    }

    //*********************mapbox permission***************//
    override fun onCameraTrackingDismissed() {
        isInTrackingMode = false
    }

    override fun onCameraTrackingChanged(currentMode: Int) {
        // Empty on purpose
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
//        Toast.makeText(this, R.string.please_accept_permission, Toast.LENGTH_LONG)
//            .show()
        Constant.showToast(getString(R.string.please_accept_permission), this)
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            mapboxMap!!.getStyle { style -> enableLocationComponent(style) }
        } else {
//            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG)
//                .show()
            Constant.showToast(getString(R.string.user_location_permission_not_granted), this)
            finish()
        }
    }

    override fun onLocationComponentClick() {
    }

    //*********************************by default activity method*********************************//
    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
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
        } else if (intent.hasExtra("SelectAddressHome")) {
            text.text = getText(R.string.updated_address)
        } else if (intent.hasExtra("Select")) {
            text.text = getText(R.string.updated_address)
        } else if (intent.hasExtra("keyChooseSellerLat")) {
            text.text = getText(R.string.updated_address)
        } else if (intent.hasExtra("keyProfileBuyerLat")) {
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

    //*****************************companion object data*******************//
    companion object {
        const val REQUEST_CODE_AUTOCOMPLETE = 1
        private const val TURF_CALCULATION_FILL_LAYER_GEOJSON_SOURCE_ID =
            "TURF_CALCULATION_FILL_LAYER_GEOJSON_SOURCE_ID"
        private const val TURF_CALCULATION_FILL_LAYER_ID = "TURF_CALCULATION_FILL_LAYER_ID"
        private const val CIRCLE_CENTER_SOURCE_ID = "CIRCLE_CENTER_SOURCE_ID"
        private const val CIRCLE_CENTER_ICON_ID = "CIRCLE_CENTER_ICON_ID"
        private const val CIRCLE_CENTER_LAYER_ID = "CIRCLE_CENTER_LAYER_ID"
    }

    //set the address custom when they manually enter the address
    private fun setAddressCustom(address: String) {
        searchs.text = address
        bottomSheetAddress.setText(address) //bottom sheet text
    }

    fun setAlertDialog() {
        val mdialog = Dialog(this)
        mdialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mdialog.setContentView(R.layout.requested_list_custom_dialog)
        mdialog.show()
        mdialog.alertHeader.text = getString(R.string.alert)
        mdialog.alertBody.text = getString(R.string.alert_clear_cart)
        //yes click
        //*****************set the api of requsted buying list*******************//
        mdialog.yes.setOnClickListener {
            mdialog.dismiss()
            mvmodel.clearCart()
            btndones.text = getString(R.string.pleasewait)
            btndones.isClickable = false

        }
        mdialog.no.setOnClickListener {
            val deliveryZoneModel = DeliveryZoneModel()
            deliveryZoneModel.lat = latitudesource.toString()
            deliveryZoneModel.long = longitudesource.toString()
            deliveryZoneModel.address = searchs.text.toString()

            val intent = Intent()
            intent.putExtra("deliveryZone", deliveryZoneModel)
            intent.putExtra("lat", latitudesource.toString())
            intent.putExtra("long", longitudesource.toString())
            setResult(Activity.RESULT_OK, intent)//set the result
            finish()
            mdialog.dismiss()

        }
    }
}

//**************map bottom sheet dialog
//class MenuFragment() : RoundedBottomSheetDialogFragment() {
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        val view = inflater.inflate(R.layout.edit_map_address_bottom_sheet, container, false)
//
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
//    }
//
//
//}
