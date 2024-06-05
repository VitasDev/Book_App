package com.books.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.books.app.data.BookDetail
import com.books.app.databinding.ItemBookGenreBinding

class GenresAdapter: RecyclerView.Adapter<GenresAdapter.GenresViewHolder>() {

    private var genresMap = emptyMap<String, MutableList<BookDetail>>()
    private var listGenres = emptyList<String>()
    private lateinit var adapterBook: GenreBookAdapter

    class GenresViewHolder(val binding: ItemBookGenreBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        val itemBinding = ItemBookGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenresViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        val currentItem = listGenres[position]

        with(holder) {
            binding.apply {
                genreName.text = currentItem
                adapterBook = GenreBookAdapter(genresMap[currentItem]!!.toList())
                rvBook.adapter = adapterBook
                rvBook.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    fun setData(listGenres: List<String>, bookFilteredByGenres: HashMap<String, MutableList<BookDetail>>) {
        this.genresMap = bookFilteredByGenres
        this.listGenres = listGenres
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listGenres.size
    }
}