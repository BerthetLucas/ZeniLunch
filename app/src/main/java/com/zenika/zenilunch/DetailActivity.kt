package com.zenika.zenilunch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    companion object {
        fun getStartIntent(context: Context, restaurant: RestaurantUIModel): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("restaurant", restaurant)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent.extras
            ?.getParcelable<RestaurantUIModel>("restaurant")
            ?.let { restaurant -> displayDetails(restaurant) }
            ?: error("This activity requires a restaurant!")
    }

    private fun displayDetails(restaurant: RestaurantUIModel) {
        val name = findViewById<TextView>(R.id.name)
        val type = findViewById<TextView>(R.id.type)
        val price = findViewById<TextView>(R.id.price)
        val option = findViewById<TextView>(R.id.option)
        name.text = restaurant.name
        type.text = restaurant.type
        price.text = restaurant.price

        option.text = when {
            restaurant.vegan -> getString(R.string.option, getString(R.string.vegan))
            restaurant.vegetarian -> getString(R.string.option, getString(R.string.vegetarian))
            else -> getString(R.string.noOption)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
