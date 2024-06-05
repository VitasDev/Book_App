package com.books.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.books.app.data.Book
import com.books.app.data.BookBannerSlide
import com.books.app.data.BookDetail
import com.books.app.data.RecommendedBook
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson

class BookViewModel : ViewModel() {

    private var remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    private val _bannerSlideLiveData = MutableLiveData<BookBannerSlide>()
    val bannerSlideLiveData: LiveData<BookBannerSlide> = _bannerSlideLiveData

    private val _bookLiveData = MutableLiveData<Book>()
    val bookLiveData: LiveData<Book> = _bookLiveData

    private val _bookDetailsLiveData = MutableLiveData<Book>()
    val bookDetailsLiveData: LiveData<Book> = _bookDetailsLiveData

    private val _recommendedBookLiveData = MutableLiveData<List<BookDetail>>()
    val recommendedBookLiveData: LiveData<List<BookDetail>> = _recommendedBookLiveData

    init {
        remoteConfig.fetchAndActivate()
            .addOnSuccessListener {
                val bookJson = remoteConfig.getString("details_carousel")
                if (bookJson.isNotEmpty()) {
                    getDetailsBookData(bookJson)
                }
            }

        remoteConfig.fetchAndActivate()
            .addOnSuccessListener {
                val bookJson = remoteConfig.getString("json_data")
                if (bookJson.isNotEmpty()) {
                    getBookBannerSlidesData(bookJson)
                    getBookData(bookJson)
                    getRecommendedBookData(bookJson)
                }
            }
    }

    private fun getBookBannerSlidesData(bookJson: String) {
        val bannerSlideJsonModel = Gson().fromJson(bookJson, BookBannerSlide::class.java)
        _bannerSlideLiveData.value = BookBannerSlide(
            bannerSlides = bannerSlideJsonModel.bannerSlides
        )
    }

    private fun getBookData(bookJson: String) {
        val bookJsonModel = Gson().fromJson(bookJson, Book::class.java)
        _bookLiveData.value = Book(
            books = bookJsonModel.books
        )
    }

    private fun getDetailsBookData(bookJson: String) {
        val bookJsonModel = Gson().fromJson(bookJson, Book::class.java)
        _bookDetailsLiveData.value = Book(
            books = bookJsonModel.books
        )
    }

    private fun getRecommendedBookData(bookJson: String) {
        val bookJsonModelRecommended = Gson().fromJson(bookJson, RecommendedBook::class.java)
        val bookJsonModel = Gson().fromJson(bookJson, Book::class.java)
        val recommendedList: MutableList<BookDetail> = emptyList<BookDetail>().toMutableList()

        bookJsonModel.books.forEach { book ->
            if (bookJsonModelRecommended.recommendedBook.contains(book.id)) {
                recommendedList.add(book)
            }
        }
        _recommendedBookLiveData.value = recommendedList
    }
}