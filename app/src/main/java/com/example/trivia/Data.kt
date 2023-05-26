package com.example.trivia

data class TriviaResponseArt(val results: List<TriviaQuestionArt>)

data class TriviaQuestionArt(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val correctAnswer: String?,
    val incorrectAnswer: List<String>?,
    var selectedOption: String? = null
)