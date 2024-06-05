package com.books.app.data

import com.google.gson.annotations.SerializedName

data class Book(
    @SerializedName("books")
    val books: List<BookDetail>
)
