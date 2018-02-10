package bou.amine.apps.isitexpired.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

@Dao
interface FoodDao {

    @Query("select * from food")
    fun getAllFoods(): LiveData<List<Food>>

    @Query("select * from food where food_id = :id")
    fun findFoodById(id: Long): LiveData<Food>

    @Insert
    fun insertFood(food: Food): Long

    @Update(onConflict = REPLACE)
    fun updateFood(food: Food)

    @Delete
    fun deleteFood(food: Food)
}

@Dao
interface FoodDetailDao {

    @Query("select * from food_detail where food_id = :id")
    fun findFoodDetailByFoodId(id: Long): LiveData<List<FoodDetail>>

    @Query("select * from food_detail where detail_id = :id")
    fun findFoodDetailById(id: Long): LiveData<FoodDetail>

    @Insert
    fun insertFoodDetail(food: FoodDetail): Long

    @Update(onConflict = REPLACE)
    fun updateFoodDetail(foodDetail: FoodDetail)

    @Delete
    fun deleteFoodDetail(foodDetail: FoodDetail)
}