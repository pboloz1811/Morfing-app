package com.com.helpers

import kotlin.math.pow
import kotlin.math.sqrt

class ChPoint {

    var px: Float
    var py: Float
    var qx: Float
    var qy: Float
    var tx: Float
    var ty: Float
    var rpx: Float
    var rpy: Float
    var rqx: Float
    var rqy: Float
    var PTDiff: Float // odl względna między punktami P oraz punktami docelowymi T
    var QTDiff: Float // odl względna między punktami Q oraz punktami docelowymi T




    constructor(px: Float, py: Float, qx: Float, qy: Float, tx: Float, ty: Float, rpx: Float, rpy: Float, rqx: Float, rqy: Float) {
        this.px = px
        this.py = py
        this.qx = qx
        this.qy = qy
        this.tx = tx
        this.ty = ty
        this.rpx = rpx
        this.rpy = rpy
        this.rqx = rqx
        this.rqy = rqy
        PTDiff = sqrt((rpx - tx).pow(2) + (rpy - ty).pow(2))
        QTDiff = sqrt((rqx - tx).pow(2) + (rqy - ty).pow(2))
    }


}