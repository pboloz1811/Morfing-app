package com.example.patrykbolozmarcincisek.morfingapp

import android.support.v4.app.Fragment
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import com.shared.logger.Logger
import java.io.File


class SwipeFragment: Fragment() {

    private val MAX_POINTS = 3



    private var imageView: ImageView? = null
    private var imageUrl = ""



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.swipe_fragment, container, false)
        imageView = view.findViewById(R.id.gestureImageView) as ImageView
        var file = File(imageUrl)
        if(file.exists()) {
            imageView?.setImageURI(Uri.fromFile(file))
        }
        imageView?.setOnTouchListener({ v: View, m: MotionEvent ->
            if (m.action == MotionEvent.ACTION_DOWN) {
                Logger.log("X: " + m.getX() + " Y: " + m.getY())
            }
            true
        })
        return view
    }


    private fun drawPoint(x: Float, y: Float) {


    }

    companion object {
        fun newInstance(url: String): SwipeFragment {
            var fragment = SwipeFragment()
            fragment.imageUrl = url
            return fragment
        }
    }


}