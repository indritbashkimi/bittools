package com.ibashkimi.providerstools

data class DisplayParams(
    var minValue: Int = 0,
    var maxValue: Int = 0,
    var bigSegments: Int = 0,
    var smallSegments: Int = 0,
    var decimalFormat: String = "#",
    var measurementUnit: String = ""
)
