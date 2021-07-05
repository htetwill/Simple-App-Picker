package com.htetwill.portier.launcher.model

data class Config(
    var background: String? = null,
    var color: String? = null,
    var hotel: Hotel? = null,
    var apps: List<String>? = null
)
