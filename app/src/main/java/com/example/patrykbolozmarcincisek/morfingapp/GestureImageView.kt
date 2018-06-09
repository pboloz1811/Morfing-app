package com.example.patrykbolozmarcincisek.morfingapp

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog

/* BUTTON 2: CHOOSE POINTS */
class GestureImageView : AppCompatActivity(), onFinishDelegate {
    lateinit var mPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture_image_view)
        var url1 = intent.getStringExtra("imageUrl")
        var url2 = intent.getStringExtra("imageUrl2")
        mPager = findViewById<ViewPager>(R.id.pager)
        mPager.adapter = GestureImageViewAdapter(this, url1, url2, mPager ,supportFragmentManager, this)
        dialogBox()
    }

    /* DIALOG BOX */
    fun dialogBox() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Please choose 3 points on each image.")
        alertDialogBuilder.setPositiveButton("Ok",
                DialogInterface.OnClickListener { arg0, arg1 -> })
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onFinishActivity() {
        this.finish()
    }
}

interface onFinishDelegate {
    fun onFinishActivity()
}

