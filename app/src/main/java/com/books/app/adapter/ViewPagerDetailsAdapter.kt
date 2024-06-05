package com.books.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.books.app.R
import com.books.app.data.BookDetail
import com.books.app.databinding.ItemViewpagerDetailsBinding

class ViewPagerDetailsAdapter : RecyclerView.Adapter<ViewPagerDetailsAdapter.ViewHolder>() {

    private var bookSlideList = emptyList<BookDetail>()

    class ViewHolder(val binding: ItemViewpagerDetailsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemViewpagerDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = bookSlideList[position]

        with(holder) {
            binding.apply {
                imgBook.load(currentItem.coverUrl) {
                    placeholder(R.drawable.empty_image)
                    transformations(RoundedCornersTransformation(16f))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return bookSlideList.size
    }

    fun setData(bookSlides: List<BookDetail>) {
        this.bookSlideList = bookSlides
        notifyDataSetChanged()
    }
}