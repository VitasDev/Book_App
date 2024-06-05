package com.books.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.books.app.R
import com.books.app.data.BookSlide
import com.books.app.databinding.ItemViewpagerBookBinding

class ViewPagerBookAdapter : RecyclerView.Adapter<ViewPagerBookAdapter.ViewHolder>() {

    private var bookSlideList = emptyList<BookSlide>()

    class ViewHolder(val binding: ItemViewpagerBookBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemViewpagerBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = bookSlideList[position]

        with(holder) {
            binding.apply {
                imgBook.load(currentItem.coverUrl) {
                    placeholder(R.drawable.empty_image)
                    transformations(RoundedCornersTransformation(16f))
                }
                imgBook.setOnClickListener {
                    val bundle = bundleOf("book_id" to currentItem.bookId)
                    findNavController(it).navigate(R.id.detailsFragment, bundle)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return bookSlideList.size
    }

    fun setData(bookSlides: List<BookSlide>) {
        this.bookSlideList = listOf(bookSlides.last()) + bookSlides + listOf(bookSlides.first())
        notifyDataSetChanged()
    }
}