package com.books.app.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.books.app.utils.Consts.DELAY_AUTO_SCROLL_VIEWPAGER
import com.books.app.adapter.DotsAdapter
import com.books.app.adapter.GenresAdapter
import com.books.app.adapter.ViewPagerBookAdapter
import com.books.app.data.BookDetail
import com.books.app.data.BookSlide
import com.books.app.databinding.MainFragmentBinding
import com.books.app.viewmodel.BookViewModel

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var viewPagerBookAdapter: ViewPagerBookAdapter
    private var bannerSlideList = emptyList<BookSlide>()
    var dotAdapter: DotsAdapter? = null
    private lateinit var adapterGenres: GenresAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterGenres = GenresAdapter()
        viewPagerBookAdapter = ViewPagerBookAdapter()
        binding.apply {
            viewPagerBooks.adapter = viewPagerBookAdapter
            rvGenres.adapter = adapterGenres
            val myLinearLayoutManager = object : LinearLayoutManager(requireContext(), VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            rvGenres.layoutManager = myLinearLayoutManager
        }

        bookViewModel.bannerSlideLiveData.observe(viewLifecycleOwner) {
            bannerSlideList = it.bannerSlides
            initDotsAdapter(bannerSlideList)
            viewPagerBookAdapter.setData(bannerSlideList)
            binding.viewPagerBooks.currentItem = 1
            onInfinitePageChangeCallback(bannerSlideList.size + 2)
            binding.viewPagerBooks.autoScroll(DELAY_AUTO_SCROLL_VIEWPAGER)
        }


        bookViewModel.bookLiveData.observe(viewLifecycleOwner) { books ->
            val bookFilteredByGenres: HashMap<String, MutableList<BookDetail>> = HashMap()

            for (itemBook in books.books) {
                val listBooks: MutableList<BookDetail> =
                    if (!bookFilteredByGenres.containsKey(itemBook.genre)) {
                        emptyList<BookDetail>().toMutableList()
                    } else {
                        bookFilteredByGenres[itemBook.genre]!!
                    }
                listBooks.add(itemBook)
                bookFilteredByGenres[itemBook.genre] = listBooks
            }

            val listGenresName = bookFilteredByGenres.keys.toList()
            adapterGenres.setData(listGenresName, bookFilteredByGenres)
        }
    }

    private fun initDotsAdapter(bannerSlideList: List<BookSlide>) {
        binding.tabIndicators.isVisible = bannerSlideList.size > 1
        binding.tabIndicators.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.tabIndicators.adapter = DotsAdapter(bannerSlideList, lifecycleScope).also {
            dotAdapter = it
        }
    }

    private fun onInfinitePageChangeCallback(listSize: Int) {
        binding.viewPagerBooks.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    when (binding.viewPagerBooks.currentItem) {
                        listSize - 1 -> binding.viewPagerBooks.setCurrentItem(1, false)
                        0 -> binding.viewPagerBooks.setCurrentItem(listSize - 2, false)
                    }

                    if (listSize - 2 == binding.viewPagerBooks.currentItem) {
                        dotAdapter?.setSelectedDotTime(0)
                    } else {
                        dotAdapter?.setSelectedDotTime(binding.viewPagerBooks.currentItem)
                    }
                }
            }
        })
    }

    private fun ViewPager2.autoScroll(interval: Long) {
        val handler = Handler()
        var scrollPosition = 0

        val runnable = object : Runnable {
            override fun run() {
                val count = adapter?.itemCount ?: 0
                setCurrentItem(scrollPosition++ % count, true)
                handler.postDelayed(this, interval)
            }
        }

        registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                scrollPosition = position + 1
            }
        })
        handler.post(runnable)
    }
}