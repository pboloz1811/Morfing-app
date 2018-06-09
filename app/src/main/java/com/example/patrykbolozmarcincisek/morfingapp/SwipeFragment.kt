package com.example.patrykbolozmarcincisek.morfingapp

import android.support.v4.app.Fragment
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import com.shared.logger.Logger
import java.io.File
import android.view.MotionEvent
import android.view.View.OnTouchListener


class SwipeFragment: Fragment() {
    private val MAX_POINTS_1 = 3
    private val MAX_POINTS_2 = 6
    private var imageView: ImageView? = null
    private var imageUrl = ""
    private var counter = 1;
    private var X = 0.0.toFloat()
    private var Y = 0.0.toFloat()
    val point1: Point? = null
    val point2: Point? = null



    var delegate: SwipeDelegate? = null

    interface SwipeDelegate {
        fun swipeToNext(forceClose: Boolean? = null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.swipe_fragment, container, false)
        imageView = view.findViewById(R.id.gestureImageView) as ImageView
        var file = File(imageUrl)

        /* DODAWANIE PUNKTÓW */
        if(file.exists()) {
            imageView?.setImageURI(Uri.fromFile(file))
        }

        /* VERSION WITH LAMBDA */
//        imageView?.setOnTouchListener({ v: View, m: MotionEvent ->
//            if (m.action == MotionEvent.ACTION_DOWN) {
//                Logger.log("X: " + m.getX() + " Y: " + m.getY())
//                counter ++;
//
//            }
//            true
//        })

        /* VERSION WITHOUT LAMBDA */
        imageView?.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {

                if (event.action == MotionEvent.ACTION_DOWN) {
                    X = event.getX().toFloat()
                    Y = event.getY().toFloat()
                    if (SwipeFragment.counter <= MAX_POINTS_1) {
                        Logger.log("POINT 1: " + "X: " + X + " Y: " + Y + " counter: " + SwipeFragment.counter)
                        if (SwipeFragment.counter == 3) { delegate?.swipeToNext() }
                    } else if (SwipeFragment.counter > MAX_POINTS_1 && SwipeFragment.counter <= MAX_POINTS_2) {
                        Logger.log("POINT 2: " + "X: " + X + " Y: " + Y + " counter: " + SwipeFragment.counter)
                        if(SwipeFragment.counter == 6) { delegate?.swipeToNext(true) }
                    } else {
                        Logger.log("Foce close")
                    }
                    val point1 = Point(X as Float, Y as Float)
                    SwipeFragment.counter++
                }

                return true

                /* PIERWSZY OBRAZ */
//                if (counter <= MAX_POINTS_1) {
//                    return if (event.action == MotionEvent.ACTION_DOWN) {
//                        X = event.getX().toFloat()
//                        Y = event.getY().toFloat()
//                        Logger.log("POINT 1: " + "X: " + X + " Y: " + Y + " counter: " + counter)
////                         first point
//                        val point1 = Point(X as Float, Y as Float)
//                        counter++
//                        return true
//                    } else false
//                }
//                /* DRUGI OBRAZ */
//                if (counter > MAX_POINTS_1 && counter <= MAX_POINTS_2) {
//                    if (counter == MAX_POINTS_1 + 1) delegate?.swipeToNext()
//                    return if (event.action == MotionEvent.ACTION_DOWN) {
//                        X = event.getX().toFloat()
//                        Y = event.getY().toFloat()
//                        Logger.log("POINT 2: " + "X: " + X + " Y: " + Y + " counter: " + counter)
////                        second point
//                        val point2 = Point(X as Float, Y as Float)
//                        counter++
//                        true
//                    } else false
//                }
//                /* POWYZEJ 6 - WRÓC DO MAIN MENU */
//                else {
//                    if (counter == MAX_POINTS_2 + 1) delegate?.swipeToNext(true)
//                    Logger.log("counter bigger than 3. Get back to main menu.")
//
//                }
//                return true
            }
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
        var counter = 1
    }


}

interface SwipeDelegate {
    fun swipeToNext()
}