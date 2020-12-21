package com.mapbox.mapboxsdk.testapp.activity.fragment

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMapOptions
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.maps.SupportMapFragment
import com.mapbox.mapboxsdk.testapp.R
import kotlinx.android.synthetic.main.activity_viewpager.*

/**
 * Test activity showcasing using the Android SDK ViewPager API to show MapFragments.
 */
class ViewPagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpager)
        viewPager.adapter = MapFragmentAdapter(this, supportFragmentManager)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val currentPosition = viewPager.currentItem
        val offscreenLimit = viewPager.offscreenPageLimit
        for (i in currentPosition - offscreenLimit..currentPosition + offscreenLimit) {
            if (i < 0 || i > viewPager.adapter?.count ?: 0) {
                continue
            }
            val mapFragment = viewPager.adapter?.instantiateItem(viewPager, i) as SupportMapFragment
            mapFragment.getMapAsync(i)
        }
    }

    internal class MapFragmentAdapter(private val context: Context, fragmentManager: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentStatePagerAdapter(fragmentManager) {

        override fun getCount(): Int {
            return NUM_ITEMS
        }

        override fun getItem(position: Int): androidx.fragment.app.Fragment? {
            val options = MapboxMapOptions.createFromAttributes(context)
            options.textureMode(true)
            options.camera(
                CameraPosition.Builder()
                    .zoom(3.0)
                    .target(
                        when (position) {
                            0 -> {
                                LatLng(34.920526, 102.634774)
                            }
                            1 -> {
                                LatLng(62.326440, 92.764913)
                            }
                            2 -> {
                                LatLng(-25.007786, 133.623852)
                            }
                            3 -> {
                                LatLng(62.326440, 92.764913)
                            }
                            else -> {
                                LatLng(34.920526, 102.634774)
                            }
                        }
                    )
                    .build()
            )

            val fragment = SupportMapFragment.newInstance(options)
            fragment.getMapAsync(position)
            return fragment
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return "Page $position"
        }

        companion object {
            private const val NUM_ITEMS = 5
        }
    }
}

fun SupportMapFragment.getMapAsync(index: Int) {
    this.getMapAsync {
        it.setStyle(
            when (index) {
                0 -> Style.MAPBOX_STREETS
                1 -> Style.DARK
                2 -> Style.SATELLITE
                3 -> Style.LIGHT
                4 -> Style.TRAFFIC_NIGHT
                else -> Style.MAPBOX_STREETS
            }
        )
    }
}
