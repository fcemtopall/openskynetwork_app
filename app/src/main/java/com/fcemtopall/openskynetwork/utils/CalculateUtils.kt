package com.fcemtopall.openskynetwork.utils

fun Double.calculateKnotToKMh(knot : Double) : Double {
    return knot * 1.852
}

fun Double.calculateKMhToKnot(kmh : Double) : Double {
    return kmh/0.54
}