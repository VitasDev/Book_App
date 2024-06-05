package com.books.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.books.app.R
import com.books.app.data.BookDetail
import com.books.app.databinding.ItemBookBinding

class GenreBookAdapter(private val bookList: List<BookDetail>, private val colorLight: Boolean = true): RecyclerView.Adapter<GenreBookAdapter.BookViewHolder>() {

    class BookViewHolder(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemBinding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentItem = bookList[position]

        with(holder) {
            binding.apply {
                imgBook.load(currentItem.coverUrl) {
                    placeholder(R.drawable.empty_image)
                    transformations(RoundedCornersTransformation(16f))
                }

                imgBook.setOnClickListener {
                    val bundle = bundleOf("id" to currentItem.id)
                    findNavController(it).navigate(R.id.detailsFragment, bundle)
                }

                if (colorLight) {
                    bookName.setTextColor(binding.root.context.resources.getColor(R.color.white_70))
                } else {
                    bookName.setTextColor(binding.root.context.resources.getColor(R.color.dark_value))
                }

                bookName.text = currentItem.name
            }
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}