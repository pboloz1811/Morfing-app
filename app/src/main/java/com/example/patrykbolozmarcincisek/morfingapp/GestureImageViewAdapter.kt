package com.example.patrykbolozmarcincisek.morfingapp

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.app.Fragment
import android.view.LayoutInflater

import android.view.View



class GestureImageViewAdapter: FragmentStatePagerAdapter {

    var context: Context
    var imageUrls = ArrayList<String>()

    lateinit var inflater: LayoutInflater

    constructor(context: Context, imageUrl1: String, imageUrl2: String, manager: FragmentManager): super(manager) {
        this.context = context
        imageUrls.add(imageUrl1)
        imageUrls.add(imageUrl2)
    }

    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun getItem(position: Int): Fragment {
        return SwipeFragment.newInstance(imageUrls[position])
    }


}