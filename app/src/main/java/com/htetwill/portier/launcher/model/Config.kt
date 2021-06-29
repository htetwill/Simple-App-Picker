package com.htetwill.portier.launcher.model

data class Config(
    var status: String? = null,
//    var content: List<Article>? = null,
    var serverTime: String? = null,

    var background: String? = null,
    var color: String? = null,
    var hotel: Hotel? = null
)
