package com.example.mychat

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class util {
    companion object {
        @SuppressLint("SimpleDateFormat")
        fun gettime(long: Long): String {
            val simpleDateFormat = SimpleDateFormat("hh:mm")
            val date = Date(long)
            return simpleDateFormat.format(date)
        }
    }
}