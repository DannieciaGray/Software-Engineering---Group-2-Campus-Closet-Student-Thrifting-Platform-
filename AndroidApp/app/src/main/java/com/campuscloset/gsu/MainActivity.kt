package com.campuscloset.gsu

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.campuscloset.gsu.ui.browse.BrowseFragment
import com.campuscloset.gsu.ui.cart.CartFragment
import com.campuscloset.gsu.ui.favorites.FavoritesFragment
import com.campuscloset.gsu.ui.profile.ProfileFragment
import com.campuscloset.gsu.viewmodel.CartViewModel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private val cartViewModel: CartViewModel by viewModels()

    private lateinit var bottomNav: BottomNavigationView
    private var cartBadge: BadgeDrawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, BrowseFragment())
                .commit()
        }

        cartBadge = bottomNav.getOrCreateBadge(R.id.navCart)
        cartBadge?.isVisible = false

        cartViewModel.cartCount.observe(this) { count ->
            if (count > 0) {
                cartBadge?.isVisible = true
                cartBadge?.number = count
            } else {
                cartBadge?.isVisible = false
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navBrowse -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, BrowseFragment())
                    .commit()
                true
            }
            R.id.navFavorites -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, FavoritesFragment())
                    .commit()
                true
            }
            R.id.navCart -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, CartFragment())
                    .commit()
                true
            }
            R.id.navProfile -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, ProfileFragment())
                    .commit()
                true
            }
            else -> false
        }
    }
}