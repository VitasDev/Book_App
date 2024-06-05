package com.books.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.books.app.R
import com.books.app.data.BookSlide
import com.books.app.databinding.ItemDotBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DotsAdapter(
    private val bannerSlideList: List<BookSlide>,
    private val localScope: CoroutineScope
) : RecyclerView.Adapter<DotsAdapter.DotsViewHolder>() {

    data class DotData(val selectedIndex: Int)
    private val selectedDotTime: MutableStateFlow<DotData> = MutableStateFlow(DotData(0))

    fun setSelectedDotTime(index: Int) {
        localScope.launch {
            selectedDotTime.emit(DotData(index))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DotsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dot, parent, false)
        return DotsViewHolder(view)
    }

    override fun onBindViewHolder(holder: DotsViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount(): Int = bannerSlideList.size

    inner class DotsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemDotBinding.bind(view)
        private var localJob: Job? = null

        fun bind(dotIndex: Int) {
            localJob?.cancel()
            localScope.launch {
                selectedDotTime.collect {
                    if (dotIndex == it.selectedIndex) {
                        binding.dot.setImageDrawable(
                            ContextCompat.getDrawable(
                                itemView.context,
                                R.drawable.ic_indicator_selected
                            )
                        )
                    } else {
                        binding.dot.setImageDrawable(
                            ContextCompat.getDrawable(
                                itemView.context,
                                R.drawable.ic_indicator_unselected
                            )
                        )
                    }
                }
            }
        }
    }
}