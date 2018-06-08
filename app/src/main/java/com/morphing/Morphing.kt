package com.morphing
import android.graphics.Bitmap
import com.example.patrykbolozmarcincisek.morfingapp.ChPoint
import com.example.patrykbolozmarcincisek.morfingapp.Point

class Morphing {
    private var firstImageBitMap: Bitmap
    private var secondImageBitMap: Bitmap
    private val width: Int
    private val height: Int
    private val lambda = 0.5
    private var targetPoints = ArrayList<ChPoint>()
    private var Pp = Point(2.0f, 3.0f)
    private var Qq = Point(2.0f, 3.0f)

    constructor(firstImage: Bitmap, secondImage: Bitmap) {
        firstImageBitMap = firstImage
        secondImageBitMap = secondImage
        width = if(firstImageBitMap.width > secondImageBitMap.width) firstImageBitMap.width else secondImageBitMap.width
        height = if(firstImageBitMap.height > secondImageBitMap.height) firstImageBitMap.height else secondImageBitMap.height
    }

    fun cloneBitmap(): Bitmap {
        var bitmap = Bitmap.createBitmap(firstImageBitMap.width, firstImageBitMap.height, firstImageBitMap.config)
        return bitmap
    }

    fun computeTargetPoints() {
        for (line in 0..height) {
            for (col in 0..width) {
                val point = ChPoint()
                point.tx = (1-lambda) * Pp.getX() + (lambda * Qq.getX())
                point.ty = (1-lambda) * Pp.getY() + (lambda * Qq.getY())
                point.rpx = Pp.getX() - point.tx
                point.rpy = Pp.getY() - point.ty
                point.rqx = Qq.getX() - point.tx
                point.rqy = Qq.getY() - point.ty
                targetPoints.add(point)
            }
        }

    }

    fun findCoordinates(tx: Int, ty: Int, px: Int, pz: Int, qx: Int, qz: Int) {
    }

    fun calculateNewImage() {
        // punkty na obrazie docelowym przy użyciu punktów charakterystycznych
        var Tx = (1-lambda) * Pp.getX() + (lambda * Qq.getX())
        var Ty = (1-lambda) * Pp.getY() + (lambda * Qq.getY())
        var newImage = cloneBitmap()
        for (line in 0..firstImageBitMap.height) {
            for (col in 0..firstImageBitMap.width) {
            }
        }
    }
}