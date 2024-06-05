package com.books.app.data

import com.google.gson.annotations.SerializedName

data class BookBannerSlide(
    @SerializedName("top_banner_slides")
    val bannerSlides: List<BookSlide>
)