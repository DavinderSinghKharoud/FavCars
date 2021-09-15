package com.example.favcars.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.favcars.application.FavCarsApplication
import com.example.favcars.databinding.FragmentFavoriteCarsBinding
import com.example.favcars.view.adapters.AllCarsAdapter
import com.example.favcars.view_model.CarsViewModel
import com.example.favcars.view_model.CarsViewModelFactory

class FavoriteCarsFragment : Fragment() {

    private var TAG: String = FavoriteCarsFragment::class.java.name
    private val mFavCarViewModel: CarsViewModel by viewModels {
        CarsViewModelFactory((requireActivity().application as FavCarsApplication).repository)
    }

    private var mBinding: FragmentFavoriteCarsBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentFavoriteCarsBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set up recycler view
        mBinding!!.rvFavCarsList.layoutManager = GridLayoutManager(requireActivity(), 2)
        val adapter = AllCarsAdapter(this)
        mBinding!!.rvFavCarsList.adapter = adapter

        //Observe the fav cars
        mFavCarViewModel.favoriteCars.observe(viewLifecycleOwner) { cars ->
            cars.let {
                if (it.isNotEmpty()) {
                    mBinding!!.rvFavCarsList.visibility = View.VISIBLE
                    mBinding!!.tvNoCars.visibility = View.GONE
                    adapter.setCars(it)
                } else {
                    mBinding!!.rvFavCarsList.visibility = View.GONE
                    mBinding!!.tvNoCars.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}