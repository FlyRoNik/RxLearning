package com.rxlearning.ui.base.pagination

import android.content.Context
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter

abstract class BaseListAdapter<TData, TViewHolder : RecyclerView.ViewHolder, THeaderViewHolder : RecyclerView.ViewHolder>(context: Context, diffCallback: DiffUtil.ItemCallback<TData>) :
        ListAdapter<TData, TViewHolder>(diffCallback),
        StickyRecyclerHeadersAdapter<THeaderViewHolder> {

    private var list: List<TData>? = null
    protected val inflater: LayoutInflater = LayoutInflater.from(context)


    override fun submitList(list: List<TData>?) {
        this.list = list
        super.submitList(list)
    }

    fun getItems(): List<TData>? {
        return list
    }
}