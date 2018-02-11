package bou.amine.apps.isitexpired

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import bou.amine.apps.isitexpired.database.AppDatabase
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import bou.amine.apps.isitexpired.adapter.FoodAdapter
import bou.amine.apps.isitexpired.database.Food
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var foods: List<Food> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerview.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(baseContext)
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

        val db = AppDatabase.getInstance(applicationContext)

        db?.foodDao()?.getAllFoods()?.observe(this, Observer<List<Food>> { t ->
            foods = t ?: emptyList()
            (recyclerview.adapter as FoodAdapter).swapData(foods)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        AppDatabase.destroyInstance()
    }
}
