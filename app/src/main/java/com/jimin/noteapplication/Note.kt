package com.jimin.noteapplication

import java.io.Serializable

data class Note(
    val id: Int = 0, // 기본값 0으로 설정
    var title: String,
    var content: String
) : Serializable
