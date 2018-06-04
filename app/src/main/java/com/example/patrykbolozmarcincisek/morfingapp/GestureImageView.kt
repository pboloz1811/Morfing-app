package com.example.patrykbolozmarcincisek.morfingapp


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager


class GestureImageView : AppCompatActivity() {


    lateinit var mPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture_image_view)
        var url1 = intent.getStringExtra("imageUrl")
        var url2 = intent.getStringExtra("imageUrl2")
        mPager = findViewById<ViewPager>(R.id.pager)
        mPager.adapter = GestureImageViewAdapter(this, url1, url2, supportFragmentManager)

    }



}

