package bou.amine.apps.isitexpired.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bou.amine.apps.isitexpired.AddFoodActivity
import bou.amine.apps.isitexpired.R
import bou.amine.apps.isitexpired.database.Food
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.food_item.view.*
import java.io.File

class FoodAdapter(var data: List<Food>, val c: Context) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.food_item, parent, false),
            c
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(data[position])

    fun swapData(data: List<Food>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, val c: Context) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Food) = with(itemView) {
            textView.text = item.name

            if (item.image.isNotEmpty()) {
                val photoUri = Uri.fromFile(File(item.image))
                Glide.with(c)
                    .load(photoUri)
                    .apply(RequestOptions.centerCropTransform())
                    .into(imageView)
            }

            setOnClickListener {
                val i = Intent(c, AddFoodActivity::class.java)
                i.putExtra("foodId", item.id)
                c.startActivity(i)
            }
        }
    }
}