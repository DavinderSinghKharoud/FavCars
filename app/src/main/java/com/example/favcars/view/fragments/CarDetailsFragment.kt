package com.example.favcars.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.favcars.databinding.FragmentCarDetailsBinding
import java.util.*

class CarDetailsFragment : Fragment() {

    private var mBinding: FragmentCarDetailsBinding? = null
    private var TAG: String = CarDetailsFragment::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCarDetailsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: CarDetailsFragmentArgs by navArgs()
        args.let {
            try {
                Glide.with(requireActivity())
                    .load(it.carDetails.image)
                    .centerCrop()
                    .into(mBinding!!.ivCarImage)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            mBinding!!.tvName.text = it.carDetails.name
            mBinding!!.tvType.text = it.carDetails.type.capitalizeWords()
            mBinding!!.tvEnginePower.text = it.carDetails.enginePow
            mBinding!!.tvPriceRange.text = it.carDetails.priceRange
            mBinding!!.tvDescription.text = it.carDetails.description
            if(mBinding!!.tvReview.text.isNotEmpty()){
                mBinding!!.tvTitleReview.visibility = View.VISIBLE
                mBinding!!.tvReview.text = it.carDetails.review
            }else  mBinding!!.tvTitleReview.visibility = View.GONE

        }
    }

    private fun String.capitalizeWords(): String = split(" ").joinToString(" ") { s ->
        s.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}