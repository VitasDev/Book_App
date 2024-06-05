package com.books.app.data

import com.google.gson.annotations.SerializedName

data class BookSlide(
    @SerializedName("id")
    val id: Int,
    @SerializedName("book_id")
    var bookId: Int,
    @SerializedName("cover")
    var coverUrl: String
)