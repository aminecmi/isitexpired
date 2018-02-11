package bou.amine.apps.isitexpired

import android.arch.lifecycle.Observer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import bou.amine.apps.isitexpired.database.AppDatabase
import bou.amine.apps.isitexpired.database.Food
import bou.amine.apps.isitexpired.database.FoodDetail
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_add_food.*
import java.io.File
import android.view.Menu
import android.view.MenuItem
import kotlinx.coroutines.experimental.async

class AddFoodActivity : AppCompatActivity() {

    private var food: Pair<Food, List<FoodDetail>>? = null
    private var db: AppDatabase? = null
    private var foodId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        foodId = intent.getLongExtra("foodId", -1)

        db = AppDatabase.getInstance(applicationContext)

        if (foodId != (-1).toLong()) {
            db?.foodDao()?.findFoodById(foodId)?.observe(this, Observer<Food> { f ->
              db?.foodDetailDao()?.findFoodDetailByFoodId(foodId)?.observe(this, Observer<List<FoodDetail>> { d ->
                  if (f != null) {
                      food = Pair(f, d.orEmpty())
                      invalidateOptionsMenu()
                      editText.setText(f.name)

                      if (f.image.isNotEmpty()) {
                          val photoUri = Uri.fromFile(File(f.image))
                          Glide.with(baseContext)
                              .load(photoUri)
                              .apply(RequestOptions.centerCropTransform())
                              .into(imageView)
                      }
                  }
              })
            })
        } else {
            editText.setText("")

            Glide.with(baseContext)
                .load(R.drawable.ic_launcher_foreground)
                .apply(RequestOptions.centerCropTransform())
                .into(imageView)
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                food = food ?: Pair(Food(), emptyList())
                food = food!!.copy(first = food!!.first.copy(name = s.toString()))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    override fun onResume() {
        super.onResume()

        food = null
        editText.setText("")
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menu != null && foodId != (-1).toLong()) {
            menu.findItem(R.id.delete).isVisible = true
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.food_detail_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.save -> {
                if (food != null) {
                    async {
                        if (foodId != (-1).toLong()) {
                            db?.foodDao()?.updateFood(food!!.first)
                        } else {
                            db?.foodDao()?.insertFood(food!!.first)
                        }
                        finish()
                    }
                }
                return true
            }
            R.id.delete -> {
                async {
                    db?.foodDao()?.deleteFood(food!!.first)
                }
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppDatabase.destroyInstance()
    }
}
