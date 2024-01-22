package com.example.quizzify1.models

data class QuestionModel (
    var questionId: String? = null,
    var ques: String? = null,
    var options: List<String?>? = null,
    val ans: String? = null,
    val quesImg : String? = null,
    var userAns: String? = null
)