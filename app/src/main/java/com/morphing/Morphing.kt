package com.morphing
import android.graphics.Bitmap
import android.graphics.Color
import android.os.AsyncTask
import com.com.helpers.ChPoint
import com.example.patrykbolozmarcincisek.morfingapp.Point
import com.shared.logger.Logger
import kotlin.math.pow
import kotlin.math.sqrt


class Morphing: AsyncTask<Void, Void, Bitmap> {
    private var firstImageBitMap: Bitmap
    private var secondImageBitMap: Bitmap
    private var firstImageWidth: Int = 0
    private var firstImageHeight: Int = 0
    var finalImageBitmap: Bitmap
    private val lambda = 0.5
    private var targetPoints = ArrayList<ChPoint>() // Punkty na obrazie docelowym obliczanie na podstawie punktów charakterystycznych dla każdego obrazka

    private var firstImageCharacteristicPoints = ArrayList<Point>()
    private var secondImageCharacteristicPoints = ArrayList<Point>()


    constructor(firstImage: Bitmap, secondImage: Bitmap) {

        // Temporary hardcoded
        firstImageCharacteristicPoints.add(Point(615.0f,366.0f))
        firstImageCharacteristicPoints.add(Point(616.0f,398.0f))
        firstImageCharacteristicPoints.add(Point(565.0f,367.0f))

        secondImageCharacteristicPoints.add(Point(518.0f,366.0f))
        secondImageCharacteristicPoints.add(Point(514.0f,410.0f))
        secondImageCharacteristicPoints.add(Point(469.0f,385.0f))
        //////////////////////

        calculateTargetPoints()
        firstImageBitMap = firstImage
        secondImageBitMap = secondImage


        finalImageBitmap = cloneBitmap()

        firstImageWidth = firstImageBitMap.width
        firstImageHeight = firstImageBitMap.height

        Logger.log("First Image width: " + firstImageBitMap.width.toString() + " height: " + firstImageBitMap.height.toString())
        Logger.log("Second Image width: " + secondImageBitMap.width.toString() + " height: " + secondImageBitMap.height.toString())

    }

    private fun calculateTargetPoints() {
        for (i in 0..2) {
            val p = firstImageCharacteristicPoints[i]
            val q = secondImageCharacteristicPoints[i]
            val tx = (1 - lambda) * p.getX() + (lambda) * q.getX()
            val ty = (1 - lambda) * p.getY() + (lambda) * q.getY()
            targetPoints.add(ChPoint(tx.toFloat(), ty.toFloat(), p, q))
        }

    }

    fun cloneBitmap(): Bitmap {
        var bitmap = Bitmap.createBitmap(firstImageBitMap.width, firstImageBitMap.height, firstImageBitMap.config)
        return bitmap
    }


    fun getNewImage() {
        val firstChPoint = targetPoints[0]
        val secondChPoint = targetPoints[1]
        val thirdChPoint = targetPoints[2]
        for(y in 0..finalImageBitmap.height - 1) {
            for (x in 0..finalImageBitmap.width - 1) {
                val firstDiff = sqrt((x - firstChPoint.tx).pow(2) + (y - firstChPoint.ty).pow(2))
                val secondDiff = sqrt((x - secondChPoint.tx).pow(2) + (y - secondChPoint.ty).pow(2))
                val thirdDiff = sqrt((x - thirdChPoint.tx).pow(2) + (y - thirdChPoint.ty).pow(2))
                val px = (((firstChPoint.rpi / firstDiff.pow(2)) + (secondChPoint.rpi / secondDiff.pow(2)) + (thirdChPoint.rpi / thirdDiff.pow(2))) / ((1 / firstDiff.pow(2)) + (1 / secondDiff.pow(2)) + (1 / thirdDiff.pow(2)))) + x
                val py = (((firstChPoint.rpi / firstDiff.pow(2)) + (secondChPoint.rpi / secondDiff.pow(2)) + (thirdChPoint.rpi / thirdDiff.pow(2))) / ((1 / firstDiff.pow(2)) + (1 / secondDiff.pow(2)) + (1 / thirdDiff.pow(2)))) + y
                val qx = (((firstChPoint.rqi / firstDiff.pow(2)) + (secondChPoint.rqi / secondDiff.pow(2)) + (thirdChPoint.rqi / thirdDiff.pow(2))) / ((1 / firstDiff.pow(2)) + (1 / secondDiff.pow(2)) + (1 / thirdDiff.pow(2)))) + x
                val qy = (((firstChPoint.rqi / firstDiff.pow(2)) + (secondChPoint.rqi / secondDiff.pow(2)) + (thirdChPoint.rqi / thirdDiff.pow(2))) / ((1 / firstDiff.pow(2)) + (1 / secondDiff.pow(2)) + (1 / thirdDiff.pow(2)))) + y
                var pColor = 0
                var qColor = 0
                if (px.toInt() < firstImageWidth && py.toInt() < firstImageHeight) {
                    pColor = firstImageBitMap.getPixel(px.toInt(),py.toInt())
                }
                if (qx.toInt() < firstImageWidth && qy.toInt() < firstImageHeight) {
                    qColor = secondImageBitMap.getPixel(qx.toInt(), qy.toInt())
                }
                val red = (1 - lambda) * Color.red(pColor) + (lambda * Color.red(qColor))
                val green = (1 - lambda) * Color.green(pColor) + (lambda * Color.green(qColor))
                val blue = (1 - lambda) * Color.green(pColor) + (lambda * Color.green(qColor))
                val color = Color.rgb(red.toInt(), green.toInt(), blue.toInt())
                finalImageBitmap.setPixel(x,y,color)
            }
        }

    }

    // async
    override fun doInBackground(vararg params: Void?): Bitmap {
        Logger.log("Started background task")
        getNewImage()
        Logger.log("Finished do in background task")
        return finalImageBitmap
    }


}