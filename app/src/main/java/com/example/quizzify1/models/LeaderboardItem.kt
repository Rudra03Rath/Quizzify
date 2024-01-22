package com.example.quizzify1.models

data class LeaderboardItem(
    val userName: String,
    val quizTitle: String,
    val score: Int,
    val type: Int // 0 for user, 1 for quiz title, 2 for space
)
