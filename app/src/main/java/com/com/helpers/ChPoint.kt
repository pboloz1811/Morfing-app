package com.com.helpers

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
    var rp: Point // odl względna między punktami P oraz punktami docelowymi T
    var rq: Point // odl względna między punktami Q oraz punktami docelowymi T




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

        this.rp = Point(rpx - tx, rpy - ty)
        this.rq = Point(rqx - tx, rqy - ty)

    }


}