package com.books.app.data

import com.google.gson.annotations.SerializedName

data class RecommendedBook(
    @SerializedName("you_will_like_section")
    val recommendedBook: List<Int>
)
