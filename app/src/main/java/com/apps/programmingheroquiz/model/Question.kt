package com.apps.programmingheroquiz.model

data class Question(
    val answers: Map<String, String>,
    val correctAnswer: String,
    val question: String,
    val questionImageUrl: String? = null,
    val score: Int
)