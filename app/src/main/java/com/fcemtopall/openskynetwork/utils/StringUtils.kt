package com.fcemtopall.openskynetwork.utils

class StringUtils {

    companion object {

        fun getValueFromLocalProperties(value : String) : String {
            return System.getProperties().getProperty(value)
        }
    }
}