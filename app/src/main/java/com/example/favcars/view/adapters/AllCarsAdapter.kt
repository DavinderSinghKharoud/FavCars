package com.example.favcars.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favcars.databinding.ItemCarLayoutBinding
import com.example.favcars.model.entities.Car
import com.example.favcars.view.fragments.AllCarsFragment
import com.example.favcars.view.fragments.FavoriteCarsFragment

class AllCarsAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<AllCarsAdapter.ViewHolder>() {

    private var cars: List<Car> = listOf()

    class ViewHolder(view: ItemCarLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val mDishImage = view.ivCarImage
        val tvTitle = view.tvCarName

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCarLayoutBinding = ItemCarLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val car = cars[position]
        Glide.with(fragment)
            .load(car.image)
            .into(holder.mDishImage)
        holder.tvTitle.text = car.name
        holder.itemView.setOnClickListener {
            //pass the details to "Car details" fragment
            if (fragment is AllCarsFragment) {
                fragment.navigateCarDetails(car)
            }else if (fragment is FavoriteCarsFragment){
                fragment.navigateCarDetails(car)
            }
        }
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    fun setCars(list: List<Car>) {
        cars = list
        notifyDataSetChanged()
    }
}