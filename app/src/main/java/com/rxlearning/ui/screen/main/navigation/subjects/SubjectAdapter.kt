package com.rxlearning.ui.screen.main.navigation.subjects

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rxlearning.R
import com.rxlearning.extensions.setClickListeners
import com.rxlearning.models.subject.Subject
import com.rxlearning.ui.base.pagination.Adapter
import com.rxlearning.ui.base.pagination.BaseRecyclerViewAdapter
import org.jetbrains.anko.find
import java.lang.ref.WeakReference

interface SubjectAdapterCallback {
    fun onClickItem(subject: Subject)
}

interface AdapterCallback {
    fun onClickItem(position: Int)
}

class SubjectAdapter(context: Context, callback: SubjectAdapterCallback) :
        BaseRecyclerViewAdapter<Subject, SubjectAdapter.SubjectViewHolder>(context), Adapter<Subject>, AdapterCallback {

    private val weakRefCallback = WeakReference(callback)

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) = getItem(position).let { holder.bind(it) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SubjectViewHolder.newInstance(inflater, parent, this)

    override fun onClickItem(position: Int) {
        getItem(position).let { weakRefCallback.get()?.onClickItem(it) }
    }

    class SubjectViewHolder(itemView: View, private val callback: AdapterCallback?) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {

        private val tvSubjectName = itemView.find<TextView>(R.id.tvSubjectName)
        private val rlRootContainer = itemView.find<View>(R.id.rlRootContainer)

        companion object {
            fun newInstance(inflater: LayoutInflater, parent: ViewGroup?, callback: AdapterCallback?) =
                    SubjectViewHolder(inflater.inflate(R.layout.subject_item, parent, false), callback)
        }

        override fun onClick(v: View) {
            when (v.id) {
                R.id.rlRootContainer -> callback?.onClickItem(adapterPosition)
            }
        }

        fun bind(subject: Subject) {
            setClickListeners(rlRootContainer)
            tvSubjectName.text = subject.name
        }
    }

}