package com.example.patrykbolozmarcincisek.morfingapp

import java.util.logging.Logger

class Vector {
    lateinit var point: Point
    lateinit var normal: Vector

    constructor(x: Float, y: Float) {
        point = Point(x, y)
    }

    constructor(pp: Point, q: Point) {
        point = Point(q.getX() - pp.getX(), q.getY() - pp.getY())
    }

    fun getX(): Float {
        return point.getX()
    }

    fun getY(): Float {
        return point.getY()
    }

    fun getNormalVector(): Vector {
        val invY = point.getY() * -1
        val tempX = point.getX()
        normal = Vector(invY, tempX)
        return normal
    }

}