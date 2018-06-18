package com.example.patrykbolozmarcincisek.morfingapp
import android.graphics.Matrix
import android.support.v4.app.Fragment
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import com.shared.logger.Logger
import java.io.File
import android.view.MotionEvent
import android.view.View.OnTouchListener
import com.morphing.Morphing
import com.morphing.list1
import com.morphing.list2


class SwipeFragment: Fragment() {
    private val MAX_POINTS_1 = 3
    private val MAX_POINTS_2 = 6
    private var imageView: ImageView? = null
    private var imageUrl = ""
    private var counter = 1
    private var X = 0.0F
    private var Y = 0.0F
    var delegate: SwipeDelegate? = null


    interface SwipeDelegate {
        fun swipeToNext(forceClose: Boolean? = null,
                        listPoint1: ArrayList<Point>? = null,
                        listPoint2: ArrayList<Point>? = null)
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
                        val coords = getPointerCoords(imageView, event)
                        list1?.add(Point(coords[0], coords[1]))
                        counter++
                        if (SwipeFragment.counter == 3) {
                            printArrayToLog(list1, 1)
                            delegate?.swipeToNext()
                        }
                    }

                    else if (SwipeFragment.counter > MAX_POINTS_1 && SwipeFragment.counter <= MAX_POINTS_2) {
                        Logger.log("POINT 2: " + "X: " + X + " Y: " + Y + " counter: " + SwipeFragment.counter)
                        val coords = getPointerCoords(imageView, event)
                        list2?.add(Point(coords[0], coords[1]))
                        if(SwipeFragment.counter == 6) {
                            printArrayToLog(list2, 2)
                            delegate?.swipeToNext(true, list1, list2)
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

    fun getPointerCoords(view: ImageView?, e: MotionEvent): FloatArray {
        val index = e.actionIndex
        val coords = floatArrayOf(e.getX(index), e.getY(index))
        val matrix = Matrix()
        view?.imageMatrix?.invert(matrix)
        matrix.postTranslate(view?.scrollX!!.toFloat(), view?.scrollY.toFloat())
        matrix.mapPoints(coords)
        return coords
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