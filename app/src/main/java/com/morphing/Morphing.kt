package com.morphing
import android.graphics.Bitmap
import com.com.helpers.ChPoint
import com.example.patrykbolozmarcincisek.morfingapp.Point
import com.shared.logger.Logger
import kotlin.math.pow
import kotlin.math.sqrt


class Morphing {
    private var firstImageBitMap: Bitmap
    private var secondImageBitMap: Bitmap
    private var finalImageBitmap: Bitmap
    private val width: Int
    private val height: Int
    private val lambda = 0.5
    private var targetPoints = ArrayList<ChPoint>() // Punkty na obrazie docelowym obliczanie na podstawie punktów charakterystycznych dla każdego obrazka

    private var firstImageCharacteristicPoints = ArrayList<Point>()
    private var secondImageCharacteristicPoints = ArrayList<Point>()


    constructor(firstImage: Bitmap, secondImage: Bitmap) {

        // Temporary hardcoded
        firstImageCharacteristicPoints.add(Point(10.0f,12.0f))
        firstImageCharacteristicPoints.add(Point(2.0f,5.0f))
        firstImageCharacteristicPoints.add(Point(4.0f,8.0f))

        secondImageCharacteristicPoints.add(Point(8.0f,11.0f))
        secondImageCharacteristicPoints.add(Point(8.0f,12.0f))
        secondImageCharacteristicPoints.add(Point(2.0f,1.0f))
        //////////////////////

        calculateTargetPoints()
        firstImageBitMap = firstImage
        secondImageBitMap = secondImage
        width = if(firstImageBitMap.width > secondImageBitMap.width) firstImageBitMap.width else secondImageBitMap.width
        height = if(firstImageBitMap.height > secondImageBitMap.height) firstImageBitMap.height else secondImageBitMap.height

        finalImageBitmap = cloneBitmap()

        Logger.log("First Image width: " + firstImageBitMap.width.toString() + " height: " + firstImageBitMap.height.toString())
        Logger.log("Second Image width: " + secondImageBitMap.width.toString() + " height: " + secondImageBitMap.height.toString())

        getNewImage()

    }

    private fun calculateTargetPoints() {
        for (i in 0..2) {
            val p = firstImageCharacteristicPoints[i]
            val q = secondImageCharacteristicPoints[i]
            val tx = (1 - lambda) * p.getX() + (lambda) * q.getX()
            val ty = (1 - lambda) * p.getY() + (lambda) * q.getY()
            val rpx = p.getX() - tx
            val rpy = p.getY() - ty
            val rqx = q.getX() - tx
            val rqy = q.getY() - ty
            targetPoints.add(ChPoint(p.getX(), p.getY(), q.getX(), q.getY(), tx.toFloat(), ty.toFloat(), rpx.toFloat(), rpy.toFloat(), rqx.toFloat(), rqy.toFloat()))
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
        for(x in 0..finalImageBitmap.width - 1) {
            for (y in 0..finalImageBitmap.height - 1) {
                val firstDiff = sqrt((x - firstChPoint.tx).pow(2) + (y - firstChPoint.ty).pow(2))
                val secondDiff = sqrt((x - secondChPoint.tx).pow(2) + (y - secondChPoint.ty).pow(2))
                val thirdDiff = sqrt((x - thirdChPoint.tx).pow(2) + (y - thirdChPoint.ty).pow(2))
                val px = (((firstChPoint.PTDiff / firstDiff.pow(2)) + (secondChPoint.PTDiff / secondDiff.pow(2)) + (thirdChPoint.PTDiff / thirdDiff.pow(2))) / ((1 / firstDiff.pow(2)) + (1 / secondDiff.pow(2)) + (1 / thirdDiff.pow(2)))) + x
                val py = (((firstChPoint.PTDiff / firstDiff.pow(2)) + (secondChPoint.PTDiff / secondDiff.pow(2)) + (thirdChPoint.PTDiff / thirdDiff.pow(2))) / ((1 / firstDiff.pow(2)) + (1 / secondDiff.pow(2)) + (1 / thirdDiff.pow(2)))) + y
                val pixel = firstImageBitMap.getPixel(px.toInt(), py.toInt())
                val qx = (((firstChPoint.QTDiff / firstDiff.pow(2)) + (secondChPoint.QTDiff / secondDiff.pow(2)) + (thirdChPoint.QTDiff / thirdDiff.pow(2))) / ((1 / firstDiff.pow(2)) + (1 / secondDiff.pow(2)) + (1 / thirdDiff.pow(2)))) + x
//                Logger.log("[x]: " + px.toString() + " [y]: " + py.toString())
            }
        }

    }
}