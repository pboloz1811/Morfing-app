package com.example.patrykbolozmarcincisek.morfingapp

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater

import android.view.View



class GestureImageViewAdapter: FragmentStatePagerAdapter, SwipeDelegate {

    var context: Context
    var imageUrls = ArrayList<String>()
    val pager: ViewPager

    lateinit var inflater: LayoutInflater

    constructor(context: Context, imageUrl1: String, imageUrl2: String, pager: ViewPager, manager: FragmentManager): super(manager) {
        this.context = context
        imageUrls.add(imageUrl1)
        imageUrls.add(imageUrl2)
        this.pager = pager
    }

    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun getItem(position: Int): Fragment {
        val fragment = SwipeFragment.newInstance(imageUrls[position])
        fragment.delegate = this
        return fragment
    }


    override fun swipeToNext() {
        pager.setCurrentItem(1,true)
    }

}