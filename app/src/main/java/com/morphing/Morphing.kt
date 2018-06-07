package com.morphing
import android.graphics.Bitmap
import com.com.helpers.ChPoint
import com.com.helpers.Point
import com.shared.logger.Logger


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
        for(i in 0..finalImageBitmap.width) {
            for (j in 0..finalImageBitmap.height) {




            }
        }

    }


    fun getFirstImagePoint() {

    }



}