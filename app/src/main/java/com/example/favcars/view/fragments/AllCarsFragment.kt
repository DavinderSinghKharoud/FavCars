package com.example.favcars.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.favcars.R
import com.example.favcars.application.FavCarsApplication
import com.example.favcars.databinding.FragmentAllCarsBinding
import com.example.favcars.view.activities.AddUpdateCarsActivity
import com.example.favcars.view.activities.MainActivity
import com.example.favcars.view.adapters.AllCarsAdapter
import com.example.favcars.view_model.CarsViewModel
import com.example.favcars.view_model.CarsViewModelFactory

class AllCarsFragment : Fragment() {

    private lateinit var mBinding: FragmentAllCarsBinding

    private val mCarsViewModel: CarsViewModel by viewModels {
        CarsViewModelFactory((requireActivity().application as FavCarsApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAllCarsBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set up recycler view
        mBinding.rvCarsList.layoutManager = GridLayoutManager(requireActivity(), 2)
        val carsAdapter = AllCarsAdapter(this@AllCarsFragment)
        mBinding.rvCarsList.adapter = carsAdapter

        //observe the dishes
        mCarsViewModel.allCarsList.observe(viewLifecycleOwner) { dishes ->
            dishes?.let {
                if (it.isNotEmpty()) {
                    mBinding.rvCarsList.visibility = View.VISIBLE
                    mBinding.tvNoCars.visibility = View.GONE

                    carsAdapter.setDishes(it)
                } else {
                    mBinding.rvCarsList.visibility = View.GONE
                    mBinding.tvNoCars.visibility = View.VISIBLE
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    //Set up Menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_car -> {
                startActivity(Intent(requireActivity(), AddUpdateCarsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_cars, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun navigateDishDetails() {
        findNavController().navigate(AllCarsFragmentDirections.actionNavigationAllCarsToNavigationCarDetails())
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.hideBottomNavigationView()
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.showBottomNavigationView()
        }
    }
}