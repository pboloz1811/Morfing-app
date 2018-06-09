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
    private var counter = 1
    private var X = 0.0F
    private var Y = 0.0F
    var delegate: SwipeDelegate? = null
    /* PRZEKAZAĆ OBIE LISTY DO Morphing.kt */
    var listPoints1: ArrayList<Point>? = ArrayList<Point>()
    var listPoints2: ArrayList<Point>? = ArrayList<Point>()

    fun getListA(): ArrayList<Point>? { return listPoints1; }
    fun getListB(): ArrayList<Point>? { return listPoints2; }


    interface SwipeDelegate {
        fun swipeToNext(forceClose: Boolean? = null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.swipe_fragment, container, false)
        imageView = view.findViewById(R.id.gestureImageView) as ImageView
        val file = File(imageUrl)

        if(file.exists()) {
            imageView?.setImageURI(Uri.fromFile(file))
        }

        /* DODAWANIE PUNKTÓW */
        imageView?.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {

                if (event.action == MotionEvent.ACTION_DOWN) {
                    X = event.getX()
                    Y = event.getY()

                    if (SwipeFragment.counter <= MAX_POINTS_1) {
                        Logger.log("POINT 1: " + "X: " + X + " Y: " + Y + " counter: " + SwipeFragment.counter)
                        listPoints1?.add(Point(X, Y))
                        counter++
                        if (SwipeFragment.counter == 3) {
                            printArrayToLog(listPoints1, 1)
                            delegate?.swipeToNext()
                        }
                    }

                    else if (SwipeFragment.counter > MAX_POINTS_1 && SwipeFragment.counter <= MAX_POINTS_2) {
                        Logger.log("POINT 2: " + "X: " + X + " Y: " + Y + " counter: " + SwipeFragment.counter)
                        listPoints2?.add(Point(X, Y))
                        if(SwipeFragment.counter == 6) {
                            printArrayToLog(listPoints2, 2)
                            delegate?.swipeToNext(true)
                        }
                    }
                    else {
                        Logger.log("Foce close")
                    }
                    SwipeFragment.counter++
                }
                return true
            }
        })
        return view
    }

    private fun printArrayToLog(listPoints: ArrayList<Point>?, arrayNo: Int) {
        var counter = 1
        Logger.log("Array no. " + arrayNo)
        if (listPoints != null) {
            for(point in listPoints){
                Logger.log("\nPoint " + counter + ": ("
                        + point.getX() + ", "
                        + point.getY() + ")" )
                counter++
            }
        }
        else {
            Logger.log("Empty array.")
        }
    }

    companion object {
        fun newInstance(url: String): SwipeFragment {
            val fragment = SwipeFragment()
            fragment.imageUrl = url
            return fragment
        }
        var counter = 1
    }


}

interface SwipeDelegate {
    fun swipeToNext()
}