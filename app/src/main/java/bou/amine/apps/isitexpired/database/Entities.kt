package bou.amine.apps.isitexpired.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import java.util.*

@Entity(
    tableName = "food_detail", foreignKeys = arrayOf(
        ForeignKey(
            entity = Food::class,
            parentColumns = arrayOf("food_id"),
            childColumns = arrayOf("food_id"),
            onDelete = ForeignKey.CASCADE
        )
    ),
    indices = arrayOf(Index(value = "detail_id", name = "detail_id"), Index(value = "food_id", name = "for_food_id"))
)
data class FoodDetail(
    @ColumnInfo(name = "food_id") val foodId: Long,
    @ColumnInfo(name = "date") val expirationDate: Date,
    @ColumnInfo(name = "quantity") val quantity: Double
) {
    @ColumnInfo(name = "detail_id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}

@Entity(tableName = "food", indices = arrayOf(Index(value = "food_id", name = "food_id")))
data class Food(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image") val image: String
)  {
    @ColumnInfo(name = "food_id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}