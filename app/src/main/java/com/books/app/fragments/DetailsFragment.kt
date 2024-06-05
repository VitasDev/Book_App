package com.books.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.books.app.adapter.GenreBookAdapter
import com.books.app.adapter.ViewPagerDetailsAdapter
import com.books.app.data.BookDetail
import com.books.app.databinding.DetailsFragmentBinding
import com.books.app.viewmodel.BookViewModel
import kotlin.math.abs

class DetailsFragment : Fragment() {

    private lateinit var binding: DetailsFragmentBinding
    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var viewPagerDetailsAdapter: ViewPagerDetailsAdapter
    private lateinit var adapterRecommendedBook: GenreBookAdapter
    private var listBooks: List<BookDetail> = emptyList()
    private var selectedSlideBook: Int? = null
    private var selectedBook: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedSlideBook = requireArguments().getInt("book_id")
        selectedBook = requireArguments().getInt("id")

        viewPagerDetailsAdapter = ViewPagerDetailsAdapter()

        binding.apply {
            viewPagerDetails.adapter = viewPagerDetailsAdapter
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            rvRecommendedBook.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL, false
            )

            viewPagerDetails.offscreenPageLimit = 3
            viewPagerDetails.clipToPadding = false
            viewPagerDetails.clipChildren = false
            viewPagerDetails.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        viewPagerTransformer()

        bookViewModel.bookDetailsLiveData.observe(viewLifecycleOwner) { books ->
            listBooks = books.books
            viewPagerDetailsAdapter.setData(listBooks)

            if (selectedSlideBook != null) {
                binding.viewPagerDetails.setCurrentItem(selectedSlideBook!!, true)
                updateBookInfo(listBooks[selectedSlideBook!!])
            }

            if (selectedBook != null) {
                binding.viewPagerDetails.setCurrentItem(selectedBook!!, true)
                updateBookInfo(listBooks[selectedBook!!])
            }

            onInfinitePageChangeCallback(listBooks)
        }

        bookViewModel.recommendedBookLiveData.observe(viewLifecycleOwner) { books ->
            adapterRecommendedBook = GenreBookAdapter(books, false)
            binding.rvRecommendedBook.adapter = adapterRecommendedBook
        }
    }

    private fun onInfinitePageChangeCallback(listBooks: List<BookDetail>) {
        binding.viewPagerDetails.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateBookInfo(listBooks[position])
            }
        })
    }

    private fun updateBookInfo(bookDetail: BookDetail) {
        binding.apply {
            bookName.text = bookDetail.name
            authorName.text = bookDetail.author
            readersValue.text = bookDetail.views
            likesValue.text = bookDetail.likes
            quotesValue.text = bookDetail.quotes
            genreValue.text = bookDetail.genre
            summaryValue.text = bookDetail.summary
        }
    }

    private fun viewPagerTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(20))
        transformer.addTransformer { page, position ->
            val radius = 1 - abs(position)
            page.scaleY = 0.85f + radius * 0.14f
        }
        binding.viewPagerDetails.setPageTransformer(transformer)
    }
}