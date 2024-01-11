package com.example.quizzify1.models

 data class QuestionModel (

     var questionId: String? = null,
     var ques: String? = null,
     var opt1: String? = null,
     var opt2: String? = null,
     var opt3: String? = null,
     val opt4: String? = null,
     val ans: String? = null,
     val quesImg : String? = null,
     var userAns: String? = null

 )