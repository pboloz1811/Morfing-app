package com.com.helpers
import com.example.patrykbolozmarcincisek.morfingapp.Point
import kotlin.math.pow
import kotlin.math.sqrt

class ChPoint {

    var tx: Float
    var ty: Float
    var pPoint: Point
    var qPoint: Point
    var rpi: Float = 0.0f // odl względna między punktami P oraz punktami docelowymi T
    var rqi: Float = 0.0f // odl względna między punktami Q oraz punktami docelowymi T



    constructor(tx: Float, ty: Float, pPoint: Point, qPoint: Point) {
        this.tx = tx
        this.ty = ty
        this.pPoint = pPoint
        this.qPoint = qPoint
        // obliczenie odległosci względne
        this.rpi = sqrt((pPoint.getX() - tx).pow(2) + (pPoint.getY() - ty).pow(2))
        this.rqi = sqrt((qPoint.getX() - tx).pow(2) + (qPoint.getY() - ty).pow(2))
    }


}