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

    private var counter = 0
    private var imageView: ImageView? = null
    private var imageUrl = ""

    var delegate: SwipeDelegate? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.swipe_fragment, container, false)
        imageView = view.findViewById(R.id.gestureImageView) as ImageView
        var file = File(imageUrl)
        if(file.exists()) {
            imageView?.setImageURI(Uri.fromFile(file))
        }
        imageView?.setOnTouchListener({ v: View, m: MotionEvent ->
            if (m.action == MotionEvent.ACTION_DOWN) {
                counter++
                if (counter == MAX_POINTS) {
                    delegate?.swipeToNext()
                }
            }
            true
        })
        return view
    }

    companion object {
        fun newInstance(url: String): SwipeFragment {
            var fragment = SwipeFragment()
            fragment.imageUrl = url
            return fragment
        }
    }


}

interface SwipeDelegate {
    fun swipeToNext()
}