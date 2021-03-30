package store.pengu.mobile.views.maps

import androidx.fragment.app.FragmentManager
import store.pengu.mobile.R

fun MapScreen(supportFragmentManager: FragmentManager) {
    val fragment = MapFragment()

    supportFragmentManager
        .beginTransaction()
        .replace(R.id.google_map, fragment)
        .commit()
}