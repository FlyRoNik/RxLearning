package com.rxlearning.ui.screen.main

import android.content.Context
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rxlearning.FIREBASE_RDB_USERS_KEY
import com.rxlearning.R
import com.rxlearning.RxLearningApp
import com.rxlearning.models.user.UserModel
import com.rxlearning.ui.base.BaseLifecycleActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class MainActivity : BaseLifecycleActivity<MainViewModel>() {
    override val viewModelClass = MainViewModel::class.java
    override val containerId = R.id.container
    override val layoutId = R.layout.activity_main

    companion object {
        fun start(context: Context) {
            with(context) {
                intentFor<MainActivity>()
                        .newTask()
                        .clearTask()
                        .let { startActivity(it) }
            }
        }
    }

    override fun observeLiveData() {
        //Do nothing
    }

    override fun hasProgressBar() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO example for getting
        RxLearningApp.instance.getCurrentUser()?.let{
            FirebaseDatabase.getInstance().reference
                    .child(FIREBASE_RDB_USERS_KEY)
                    .child(it.uid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(data: DataSnapshot) {
                            tvName.text = data.getValue(UserModel::class.java)?.firstName
                        }

                        override fun onCancelled(error: DatabaseError) {
                            //
                        }

                    })

        }

    }
}
