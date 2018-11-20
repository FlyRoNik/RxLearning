package com.rxlearning.ui.base.pagination

interface Adapter<TData> {

    fun dataLoad(newData: List<TData>)

    fun dataRangeLoad(newData: List<TData>)

    fun getItems(): List<TData>?

}