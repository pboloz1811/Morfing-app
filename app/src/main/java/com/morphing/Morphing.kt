package com.morphing

import android.graphics.Bitmap
import android.graphics.Color
import com.com.helpers.ChPoint
import com.example.patrykbolozmarcincisek.morfingapp.Point
import com.shared.logger.Logger
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt


var list1: ArrayList<Point> = ArrayList<Point>()
var list2: ArrayList<Point> = ArrayList<Point>()

class Morphing: Thread {
    private var firstImageBitMap: Bitmap
    private var secondImageBitMap: Bitmap
    private var firstImageWidth: Int = 0
    private var firstImageHeight: Int = 0
    private var secondImageWidth: Int = 0
    private var secondImageHeight: Int = 0
    private var lambda: Double
    private var targetPoints = ArrayList<ChPoint>() // Punkty na obrazie docelowym obliczanie na podstawie punktów charakterystycznych dla każdego obrazka
    private var firstImageCharacteristicPoints = ArrayList<Point>()
    private var secondImageCharacteristicPoints = ArrayList<Point>()
    var finalImageBitmap: Bitmap



    constructor(firstImage: Bitmap, secondImage: Bitmap, lambda: Double) {
        firstImageCharacteristicPoints.add(list1.get(0))
        firstImageCharacteristicPoints.add(list1.get(1))
        firstImageCharacteristicPoints.add(list1.get(2))
        secondImageCharacteristicPoints.add(list2.get(0))
        secondImageCharacteristicPoints.add(list2.get(1))
        secondImageCharacteristicPoints.add(list2.get(2))
        this.firstImageBitMap = firstImage
        this.secondImageBitMap = secondImage
        this.lambda = lambda

        firstImageWidth = firstImageBitMap.width
        firstImageHeight = firstImageBitMap.height

        secondImageWidth = secondImageBitMap.width
        secondImageHeight = secondImageBitMap.height

        calculateTargetPoints()
        finalImageBitmap = cloneBitmap()

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

    private fun cloneBitmap(): Bitmap {
        var bitmap = Bitmap.createBitmap(firstImageWidth, firstImageHeight, firstImageBitMap.config)
        return bitmap
    }

    private fun getNewImage() {
        for(y in 0..finalImageBitmap.height - 1) {
            for (x in 0..finalImageBitmap.width - 1) {
                val point = findCoordinate(x,y)
                var pColor = 0
                var qColor = 0
                if (point.spx.toInt() >= 0 && point.spx.toInt() < firstImageWidth && point.spy.toInt() >= 0 && point.spy.toInt() < firstImageHeight) {
                    pColor = firstImageBitMap.getPixel(point.spx.toInt(),point.spy.toInt())
                }
                if (point.sqx.toInt() >= 0 && point.sqx.toInt() < secondImageWidth && point.sqy.toInt() >=0 && point.sqy.toInt() < secondImageHeight) {
                    qColor = secondImageBitMap.getPixel(point.sqx.toInt(), point.sqy.toInt())
                }
                val red = (1 - lambda) * Color.red(pColor) + (lambda * Color.red(qColor))
                val green = (1 - lambda) * Color.green(pColor) + (lambda * Color.green(qColor))
                val blue = (1 - lambda) * Color.blue(pColor) + (lambda * Color.blue(qColor))
                val color = Color.rgb(red.toInt(), green.toInt(), blue.toInt())
                finalImageBitmap.setPixel(x,y,color)
            }
        }

    }

    private fun findCoordinate(tx: Int, ty: Int): Coordinate {
        var invdp = 0.0f
        var spx = 0.0f
        var spy = 0.0f
        var sqx = 0.0f
        var sqy = 0.0f
        var f = 0.0f
        for (i in 0..targetPoints.size - 1) {
            val point = targetPoints[i]
            if (tx == point.tx.toInt() && ty == point.ty.toInt()) {
                return Coordinate(point.pPoint.getX(), point.pPoint.getY(), point.qPoint.getX(), point.qPoint.getY())
            }
            invdp = 1.0f / ((tx - point.tx).pow(2) + (ty - point.ty).pow(2))
            spx += point.rpx * invdp
            spy += point.rpy * invdp
            sqx += point.rqx * invdp
            sqy += point.rqy * invdp
            f += invdp
        }
        val px = round(spx/f+tx)
        val py = round(spy/f+ty)
        val qx = round(sqx/f+tx)
        val qy = round(sqy/f+ty)
        return Coordinate(px,py,qx,qy)
    }


    override fun run() {
        Logger.log("Started on running thread: " + Thread.currentThread().id)
        getNewImage()
        Logger.log("finished on thread: " + Thread.currentThread().id)
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


}