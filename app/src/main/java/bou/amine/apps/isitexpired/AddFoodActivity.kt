package bou.amine.apps.isitexpired

import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import bou.amine.apps.isitexpired.database.AppDatabase
import bou.amine.apps.isitexpired.database.Food
import bou.amine.apps.isitexpired.database.FoodDetail
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_add_food.*
import kotlinx.coroutines.experimental.async

class AddFoodActivity : AppCompatActivity() {

    private var food: Pair<Food, List<FoodDetail>>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        editText.setText("")

        val id: Long = intent.getLongExtra("foodId", -1)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()

        if (id != (-1).toLong()) {
            db.foodDao().findFoodById(id).observe(this, Observer<Food> { f ->
              db.foodDetailDao().findFoodDetailByFoodId(id).observe(this, Observer<List<FoodDetail>> { d ->
                  if (f != null) {
                      food = Pair(f, d.orEmpty())
                      editText.setText(f.name)

                      Glide.with(baseContext)
                          .load(f.image)
                          .apply(RequestOptions.centerCropTransform())
                          .into(imageView)
                  }
              })
            })
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                food = food ?: Pair(Food("", ""), emptyList())
                food = food!!.copy(first = if (food!!.first != null) {
                    food!!.first.copy(name = s.toString())
                } else {
                    Food(s.toString(), "")
                })
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        floatingActionButton.setOnClickListener {
            if (food != null && food!!.first != null) {
                async {
                    if (food!!.first.id != (-1).toLong()) {
                        db.foodDao().updateFood(food!!.first)
                    } else {
                        db.foodDao().insertFood(food!!.first)
                    }
                    finish()
                }
            }
        }
    }
}
