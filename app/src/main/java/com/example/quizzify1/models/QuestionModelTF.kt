package com.example.quizzify1.models

data class QuestionModelTF (

    var questionId: String? = null,
    var ques: String? = null,
    val ans: String? = null,
    val quesImg : String? = null,
    var userAns: String? = null
)