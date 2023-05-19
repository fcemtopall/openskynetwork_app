package com.fcemtopall.openskynetwork.domain.entity

data class States(
    val icao24: String?,
    val callsign : String?,
    val origin_country : String?,
    val time_position : Int?,
    val last_contact : Int?,
    val longitude : Float?,
    val latitude : Float?,
    val baro_altitude :Float?,
    val on_ground : Boolean?,
    val velocity : Float?,
    val true_track : Float?,
    val vertical_rate : Float?,
    val sensors : Array<Int>?,
    val geo_altitude : Float?,
    val squawk : String?,
    val spi : Boolean?,
    val position_source : Int?,
    val category : Int?
    )
