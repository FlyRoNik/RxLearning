package com.rxlearning.models.subject

import android.os.Parcel
import com.rxlearning.models.BaseParcelable
import com.rxlearning.models.Model
import com.rxlearning.models.read
import com.rxlearning.models.write

interface Subject : Model<String> {
    var name: String?
}

data class SubjectModel(override var id: String? = null,
                        override var name: String?) : Subject {

    companion object {
        @JvmField
        val CREATOR = BaseParcelable.generateCreator {
            SubjectModel(it.read(), it.read())
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) = dest.write(id, name)

}