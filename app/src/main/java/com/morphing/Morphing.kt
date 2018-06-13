package com.morphing

import android.graphics.Bitmap
import android.graphics.Color
import android.os.AsyncTask
import com.com.helpers.ChPoint
import com.example.patrykbolozmarcincisek.morfingapp.Point
import com.shared.logger.Logger
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt


var list1: ArrayList<Point> = ArrayList<Point>()
var list2: ArrayList<Point> = ArrayList<Point>()

class Morphing: AsyncTask<Void, Void, Bitmap> {
    private var firstImageBitMap: Bitmap
    private var secondImageBitMap: Bitmap
    private var firstImageWidth: Int = 0
    private var firstImageHeight: Int = 0
    var finalImageBitmap: Bitmap
    private var lambda: Double
    private var targetPoints = ArrayList<ChPoint>() // Punkty na obrazie docelowym obliczanie na podstawie punktów charakterystycznych dla każdego obrazka

    private var firstImageCharacteristicPoints = ArrayList<Point>()
    private var secondImageCharacteristicPoints = ArrayList<Point>()

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

    constructor(firstImage: Bitmap, secondImage: Bitmap, lambda: Double) {
        Logger.log("IN MORPHING: ")
        firstImageCharacteristicPoints.add(list1.get(0))
        firstImageCharacteristicPoints.add(list1.get(1))
        firstImageCharacteristicPoints.add(list1.get(2))

        secondImageCharacteristicPoints.add(list2.get(0))
        secondImageCharacteristicPoints.add(list2.get(1))
        secondImageCharacteristicPoints.add(list2.get(2))

        calculateTargetPoints()
        firstImageBitMap = firstImage
        secondImageBitMap = secondImage
        this.lambda = lambda
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
//        val firstChPoint = targetPoints[0]
//        val secondChPoint = targetPoints[1]
//        val thirdChPoint = targetPoints[2]
//
//        printArrayToLog(firstImageCharacteristicPoints, 1)
//        printArrayToLog(secondImageCharacteristicPoints, 2)

        for(y in 0..finalImageBitmap.height - 1) {
            for (x in 0..finalImageBitmap.width - 1) {
//                val firstDiff = sqrt((x - firstChPoint.tx).pow(2) + (y - firstChPoint.ty).pow(2))
//                val secondDiff = sqrt((x - secondChPoint.tx).pow(2) + (y - secondChPoint.ty).pow(2))
//                val thirdDiff = sqrt((x - thirdChPoint.tx).pow(2) + (y - thirdChPoint.ty).pow(2))
//                val px = (((firstChPoint.rpi / firstDiff.pow(2)) + (secondChPoint.rpi / secondDiff.pow(2)) + (thirdChPoint.rpi / thirdDiff.pow(2))) / ((1 / firstDiff.pow(2)) + (1 / secondDiff.pow(2)) + (1 / thirdDiff.pow(2))))
//                val py = (((firstChPoint.rpi / firstDiff.pow(2)) + (secondChPoint.rpi / secondDiff.pow(2)) + (thirdChPoint.rpi / thirdDiff.pow(2))) / ((1 / firstDiff.pow(2)) + (1 / secondDiff.pow(2)) + (1 / thirdDiff.pow(2))))
//                val qx = (((firstChPoint.rqi / firstDiff.pow(2)) + (secondChPoint.rqi / secondDiff.pow(2)) + (thirdChPoint.rqi / thirdDiff.pow(2))) / ((1 / firstDiff.pow(2)) + (1 / secondDiff.pow(2)) + (1 / thirdDiff.pow(2))))
//                val qy = (((firstChPoint.rqi / firstDiff.pow(2)) + (secondChPoint.rqi / secondDiff.pow(2)) + (thirdChPoint.rqi / thirdDiff.pow(2))) / ((1 / firstDiff.pow(2)) + (1 / secondDiff.pow(2)) + (1 / thirdDiff.pow(2))))
//                var pColor = 0
//                var qColor = 0
//                if (px.toInt() < firstImageWidth && py.toInt() < firstImageHeight) {
//                    pColor = firstImageBitMap.getPixel(px.toInt(),py.toInt())
//                }
//                if (qx.toInt() < firstImageWidth && qy.toInt() < firstImageHeight) {
//                    qColor = secondImageBitMap.getPixel(qx.toInt(), qy.toInt())
//                }
//                val red = (1 - lambda) * Color.red(pColor) + (lambda * Color.red(qColor))
//                val green = (1 - lambda) * Color.green(pColor) + (lambda * Color.green(qColor))
//                val blue = (1 - lambda) * Color.green(pColor) + (lambda * Color.green(qColor))
//                val color = Color.rgb(red.toInt(), green.toInt(), blue.toInt())
//                finalImageBitmap.setPixel(x,y,color)
                val point = findCoordinate(x,y)
                var pColor = 0
                var qColor = 0
                if (point.spx.toInt() >= 0 && point.spx.toInt() < firstImageWidth && point.spy.toInt() >= 0 && point.spy.toInt() < firstImageHeight) {
                    pColor = firstImageBitMap.getPixel(point.spx.toInt(),point.spy.toInt())
                }
                if (point.sqx.toInt() >= 0 && point.sqx.toInt() < firstImageWidth && point.sqy.toInt() >=0 && point.sqy.toInt() < firstImageHeight) {
                    qColor = secondImageBitMap.getPixel(point.sqx.toInt(), point.sqy.toInt())
                }
                val red = (1 - lambda) * Color.red(pColor) + (lambda * Color.red(qColor))
                val green = (1 - lambda) * Color.green(pColor) + (lambda * Color.green(qColor))
                val blue = (1 - lambda) * Color.green(pColor) + (lambda * Color.green(qColor))
                val color = Color.rgb(red.toInt(), green.toInt(), blue.toInt())
                finalImageBitmap.setPixel(x,y,color)

            }
        }
    }

    private fun findCoordinate(tx: Int, ty: Int): Coordinate {
        var invdp = 0.0f
        var invdq = 0.0f
        var spx = 0.0f
        var spy = 0.0f
        var sqx = 0.0f
        var sqy = 0.0f
        var fp = 0.0f
        var fq = 0.0f
        for (i in 0..targetPoints.size - 1) {
            val point = targetPoints[i]
            if (tx.toFloat() == point.tx && ty.toFloat() == point.ty) {
                return Coordinate(point.pPoint.getX(), point.pPoint.getY(), point.qPoint.getX(), point.qPoint.getY())
            }
            invdp = 1.0f / sqrt((tx - point.tx).pow(2) + (ty - point.ty).pow(2)).pow(2)
            invdq = 1.0f / sqrt((tx - point.tx).pow(2) + (ty - point.ty).pow(2)).pow(2)
            spx = spx + point.rpx * invdp
            spy = spy + point.rpy * invdp
            sqx = sqx + point.rqx * invdp
            sqy = sqy + point.rqy * invdp
            fp = fp + invdp
            fq = fq + invdp
        }
        spx = round(spx/fp+tx)
        spy = round(spy/fp+ty)
        sqx = round(sqx/fq+tx)
        sqy = round(sqy/fq+ty)
        return Coordinate(spx,spy,sqx,sqy)
    }

    internal class Coordinate {
        var spx = 0.0f
        var spy = 0.0f
        var sqx = 0.0f
        var sqy = 0.0f

        constructor(spx: Float, spy: Float, sqx: Float, sqy: Float) {
            this.spx = spx
            this.spy = spy
            this.sqx = sqx
            this.sqy = sqy
        }
    }

    // async
    override fun doInBackground(vararg params: Void?): Bitmap {
        Logger.log("Started background task")
        Logger.log("Current thread: " + Thread.currentThread().toString())
        getNewImage()
        Logger.log("Finished do in background task")
        return finalImageBitmap
    }

}