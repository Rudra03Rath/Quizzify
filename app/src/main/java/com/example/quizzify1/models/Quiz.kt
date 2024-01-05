package com.example.quizzify1.models

class Quiz(id: String, title: String) {
    var id : String = ""
    var title : String = ""
    var question: MutableMap<String, QuestionModel> = mutableMapOf()
}