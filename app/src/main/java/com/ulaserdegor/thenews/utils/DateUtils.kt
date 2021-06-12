package com.ulaserdegor.thenews.utils

import java.text.SimpleDateFormat


val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

fun dateIsSame(d1: String, d2: String): Boolean {
    return simpleDateFormat.parse(d1) == simpleDateFormat.parse(d2)
}
