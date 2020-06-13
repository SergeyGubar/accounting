package io.github.gubarsergey.accounting.ui.prediction

data class PredictionsDto(
    val coef: Double,
    val formula: String,
    val points: List<List<Double>>,
    val predictions: List<Prediction>
)