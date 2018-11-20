package com.rxlearning.models.group

import android.os.Parcel
import com.rxlearning.models.BaseParcelable
import com.rxlearning.models.read
import com.rxlearning.models.subject.Subject
import com.rxlearning.models.write

interface Group : BaseParcelable {
    var id: String?
    var ownerId: String?
    var name: String?
    var subjects: List<Subject>?
}

data class GroupModel(override var id: String? = null,
                      override var ownerId: String?,
                      override var name: String?,
                      override var subjects: List<Subject>?) : Group {

    companion object {
        @JvmField
        val CREATOR = BaseParcelable.generateCreator {
            GroupModel(it.read(), it.read(), it.read(), it.read())
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) = dest.write(id, ownerId, name, subjects)

}