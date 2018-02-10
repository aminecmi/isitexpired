package bou.amine.apps.isitexpired

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import bou.amine.apps.isitexpired.database.AppDatabase
import android.arch.persistence.room.Room
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import bou.amine.apps.isitexpired.adapter.FoodAdapter
import bou.amine.apps.isitexpired.database.Food
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var foods: List<Food> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // use this setting to improve performance if you know that changes
        recyclerview.setHasFixedSize(true)

        val layoutManager = GridLayoutManager(baseContext, 2)
        recyclerview.layoutManager = layoutManager

        val mAdapter = FoodAdapter(foods, baseContext)
        recyclerview.adapter = mAdapter

        floatingActionButton.setOnClickListener {
            val i = Intent(this@MainActivity, AddFoodActivity::class.java)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()

        db.foodDao().getAllFoods().observe(this, Observer<List<Food>> { t ->
            foods = t ?: emptyList()
            (recyclerview.adapter as FoodAdapter).swapData(foods)
        })
    }
}
