package com.example.patrykbolozmarcincisek.morfingapp

class Point {
    private val x: Float
    private val y: Float

    constructor(x: Float, y:Float) {
        this.x = x
        this.y = y
    }

    fun getX(): Float {
        return this.x
    }

    fun getY(): Float {
        return this.y
    }
}