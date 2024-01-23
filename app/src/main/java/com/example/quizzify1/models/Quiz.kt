package com.example.quizzify1.models
data class Quiz(
    var id : String = "",
    var title : String = "",
    var questions: MutableMap<String, QuestionModel> = mutableMapOf()
)