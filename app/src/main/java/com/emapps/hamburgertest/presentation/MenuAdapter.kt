package com.emapps.hamburgertest.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emapps.hamburgertest.R
import com.emapps.hamburgertest.domain.model.Meal
import com.emapps.hamburgertest.databinding.ViewHolderMenuItemBinding
import java.text.NumberFormat

class MenuAdapter : ListAdapter<Meal, MenuAdapter.MealViewHolder>(
    object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(
            oldItem: Meal,
            newItem: Meal
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Meal,
            newItem: Meal
        ) = oldItem.id == newItem.id &&
                oldItem.type == newItem.type &&
                oldItem.name == newItem.name &&
                oldItem.price == newItem.price &&
                oldItem.description == newItem.description &&
                oldItem.isVegan == newItem.isVegan &&
                oldItem.isHot == newItem.isHot &&
                oldItem.rating == newItem.rating &&
                oldItem.image == newItem.image &&
                oldItem.isAvailable == newItem.isAvailable
    }
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MealViewHolder = MealViewHolder(
        ViewHolderMenuItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: MealViewHolder,
        position: Int
    ) {
        val meal = getItem(position)
        holder.bindMeal(meal)
    }

//    fun addMeals(meals: List<Meal>) {
//        submitList(currentList + meals)
//    }

    class MealViewHolder(
        private val itemBinding: ViewHolderMenuItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bindMeal(meal: Meal) {
            itemBinding.name.text = meal.name
            Glide.with(itemBinding.image.context)
                .load(R.drawable.menu_item_placeholder)
                .placeholder(R.drawable.menu_item_placeholder)
                .into(itemBinding.image)
            itemBinding.description.text = meal.description
            itemBinding.price.text = NumberFormat.getCurrencyInstance().format(meal.price)
            if (meal.isVegan) {
                itemBinding.notVegan.visibility = View.INVISIBLE
                itemBinding.labelVegan.setTextColor(ContextCompat.getColor(
                    itemBinding.labelVegan.context,
                    R.color.black
                ))
            } else {
                itemBinding.notVegan.visibility = View.VISIBLE
                itemBinding.labelVegan.setTextColor(ContextCompat.getColor(
                    itemBinding.labelVegan.context,
                    R.color.grey
                ))
            }
            if (meal.isHot) {
                itemBinding.notHot.visibility = View.INVISIBLE
                itemBinding.labelHot.setTextColor(ContextCompat.getColor(
                    itemBinding.labelHot.context,
                    R.color.black
                ))
            } else {
                itemBinding.notHot.visibility = View.VISIBLE
                itemBinding.labelHot.setTextColor(ContextCompat.getColor(
                    itemBinding.labelHot.context,
                    R.color.grey
                ))
            }
            itemBinding.rating.text = meal.rating.toString()
            if (meal.isAvailable) {
                itemBinding.availability.text = itemBinding.availability.context.getString(R.string.available)
                itemBinding.availability.setTextColor(ContextCompat.getColor(
                    itemBinding.labelVegan.context,
                    R.color.black
                ))
            } else {
                itemBinding.availability.text = itemBinding.availability.context.getString(R.string.not_available)
                itemBinding.availability.setTextColor(ContextCompat.getColor(
                    itemBinding.labelVegan.context,
                    R.color.red
                ))
            }
        }
    }
}