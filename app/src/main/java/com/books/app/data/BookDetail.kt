package com.books.app.data

import com.google.gson.annotations.SerializedName

data class BookDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("author")
    var author: String,
    @SerializedName("summary")
    var summary: String,
    @SerializedName("genre")
    var genre: String,
    @SerializedName("cover_url")
    var coverUrl: String,
    @SerializedName("views")
    var views: String,
    @SerializedName("likes")
    var likes: String,
    @SerializedName("quotes")
    var quotes: String
)