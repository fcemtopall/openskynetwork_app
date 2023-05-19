package com.fcemtopall.openskynetwork.domain.entity

import com.google.gson.annotations.SerializedName


data class States (
    @SerializedName("icao24")
    val icao24: String?,
    @SerializedName("callsign")
    val callsign : String?,
    @SerializedName("origin_country")
    val origin_country : String?,
    @SerializedName("time_position")
    val time_position : Int?,
    @SerializedName("last_contact")
    val last_contact : Int?,
    @SerializedName("longitude")
    val longitude : Float?,
    @SerializedName("latitude")
    val latitude : Float?,
    @SerializedName("baro_altitude")
    val baro_altitude :Float?,
    @SerializedName("on_ground")
    val on_ground : Boolean?,
    @SerializedName("velocity")
    val velocity : Float?,
    @SerializedName("true_track")
    val true_track : Float?,
    @SerializedName("vertical_rate")
    val vertical_rate : Float?,
    @SerializedName("sensors")
    val sensors : IntArray,
    @SerializedName("geo_altitude")
    val geo_altitude : Float?,
    @SerializedName("squawk")
    val squawk : String?,
    @SerializedName("spi")
    val spi : Boolean?,
    @SerializedName("position_source")
    val position_source : Int?,
    @SerializedName("category")
    val category : Int?
    )
